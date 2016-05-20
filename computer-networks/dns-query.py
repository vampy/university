#!/usr/bin/env python3
__version__ = "0.1"
__description__ = "A simple DNS lookup utility, similar to 'dig'"

# Basic usage examples
# iterative: python3 dns-query.py wordpress.com -s 192.228.79.201 -q A -nr
# recursive: python3 dns-query.py wordpress.com -q A -r
import argparse
import random
import socket
import struct
import sys
from enum import Enum, unique
from ipaddress import IPv4Address, IPv6Address


def exit_error(msg):
    print("ERROR: " + msg)
    sys.exit(1)


def is_ip_valid(ip, family=socket.AF_INET):
    try:
        socket.inet_pton(family, ip)
        return True
    except socket.error:
        return False


def bit_get(number, i):
    """ Get the n bit of the number """
    return (number >> i) & 1


# for qtype and type for messages
# https://tools.ietf.org/html/rfc1035#section-3.2.2
@unique
class RecordType(Enum):
    A = 1  # ipv4 address record
    NS = 2  # name server record
    CNAME = 5  # canonical name record
    SOA = 6  # start of zone authority record
    MX = 15  # mail exchange record
    AAAA = 28  # ipv6 address record


@unique  # https://tools.ietf.org/html/rfc1035#section-3.2.4
class ClassType(Enum):
    IN = 1  # the internet
    # the other are not used
    NONE = 254
    ANY = 255


@unique  # for qr
class QueryType(Enum):
    QUESTION = 0
    RESPONSE = 1


@unique  # for opcode
class OperationCode(Enum):
    QUERY = 0  # standard query
    IQUERY = 1  # inverse query, obsolete
    STATUS = 2  # server status request
    # 3 is reserved, not used
    NOTIFY = 4  # used by primary servers to tell secondary servers that a zone has changed
    UPDATE = 5  # used to implement dynamic DNS


@unique  # for rcode
class ResponseCode(Enum):
    NO_ERROR = 0  # no error occurred
    FORMAT_ERROR = 1  # The name server was unable to interpret the query
    SERVER_FAILURE = 2  # The name server was unable to process this query due to a problem with the name server
    NAME_ERROR = 3  # only for authoritative name server, signifies that the domain name referenced in the query does not exist
    NOT_IMPLEMENTED = 4  # The name server does not support the requested kind of query
    REFUSED = 5  # The name server refuses to perform the specified operation for policy reasons.
    # + others not used for this app


class DNSQuestion:
    # See https://tools.ietf.org/html/rfc1035#section-4.1.2
    header_format = struct.Struct("!2H")  # after the QNAME

    def __init__(self, qname="", qtype=RecordType.A, qclass=ClassType.IN):
        self._qtype = None
        self._qclass = None

        self.qname = qname  # variable up to 255 bytes
        self.qtype = qtype  # 2 bytes, see RecordType
        self.qclass = qclass  # 2 bytes

    @property
    def qtype(self):
        return self._qtype

    @qtype.setter
    def qtype(self, value):
        self._qtype = RecordType(value)

    @property
    def qclass(self):
        return self._qclass

    @qclass.setter
    def qclass(self, value):
        self._qclass = ClassType(value)

    def to_bytes(self):
        """
        Pack a question
        :return: bytes
        """
        return DNSMessage.name_to_bytes(
            self.qname) + DNSQuestion.header_format.pack(self.qtype.value, self.qclass.value)

    @staticmethod
    def from_bytes(message_bytes, offset):
        """
        Unpack a question from packed bytes
        :param message_bytes: bytes
        :param offset: int
        :return: tuple(DNSQuestion, int)
        """
        qname, offset = DNSMessage.name_from_bytes(message_bytes, offset)
        qtype, qclass = DNSQuestion.header_format.unpack_from(message_bytes, offset)

        return DNSQuestion(qname, qtype, qclass), offset + DNSQuestion.header_format.size

    def to_str(self):
        return "%s%s%s%s%s" % (self.qname, "\t" * 4, self.qclass.name, "\t", self.qtype.name)

    def __repr__(self):
        return "<DNSQuestion qname=%s, qtype=%s, qclass=%s>" % (self.qname, self.qtype.name, self.qclass.name)


class RDataMX:
    # See https://tools.ietf.org/html/rfc1035#section-3.3.9
    header_format = struct.Struct("!H")

    def __init__(self, preference=0, exchange=""):
        self.preference = preference  # 2 bytes
        self.exchange = exchange  # variable length

    @staticmethod
    def from_bytes(message_bytes, offset):
        """
        Unpack a rdata for a MX type
        :param message_bytes: bytes
        :param offset: int
        :return: tuple(RDataMX, int)
        """
        preference, = RDataMX.header_format.unpack_from(message_bytes, offset)
        offset += RDataMX.header_format.size
        exchange, offset = DNSMessage.name_from_bytes(message_bytes, offset)

        return RDataMX(preference, exchange), offset

    def __repr__(self):
        return "<RDataMX preference=%d, exchange=%s>" % (self.preference, self.exchange)


class RDataSOA:
    # See https://tools.ietf.org/html/rfc1035#section-3.3.13
    header_format = struct.Struct("!LLLLL")

    def __init__(self, mname="", rname="", serial=0, refresh=0, retry=0, expire=0, minimum=0):
        self.mname = mname  # variable length
        self.rname = rname  # variable length
        self.serial = serial  # 4 bytes
        self.refresh = refresh  # 4 bytes
        self.retry = retry  # 4 bytes
        self.expire = expire  # 4 bytes
        self.minimum = minimum  # 4 bytes

    @staticmethod
    def from_bytes(message_bytes, offset):
        """
        Unpack a rdata for a SOA type
        :param message_bytes: bytes
        :param offset: int
        :return: tuple(RDataSOA, int)
        """
        mname, offset = DNSMessage.name_from_bytes(message_bytes, offset)
        rname, offset = DNSMessage.name_from_bytes(message_bytes, offset)
        serial, refresh, retry, expire, minimum = RDataSOA.header_format.unpack_from(message_bytes, offset)

        return RDataSOA(mname, rname, serial, refresh, retry, expire, minimum), offset + RDataSOA.header_format.size

    def __repr__(self):
        return "<RDataSOA mname=%s, rname=%s, serial=%d, refresh=%d, retry=%d, expire=%d, minimum=%d>" % \
               (self.mname, self.rname, self.serial, self.refresh, self.retry, self.expire, self.minimum)


class DNSResponse:
    # See https://tools.ietf.org/html/rfc1035#section-3.2.1
    header_format = struct.Struct("!2HLH")  # after the NAME

    def __init__(self):
        self.name = ""  # variable up to 255 bytes
        self._type = None  # 2 bytes, see RecordType, specifies meaning of the RDATA
        self._classd = None  # 2 bytes
        self.ttl = 0  # 4 bytes
        self.rdlength = 0  # 2 bytes
        self.rdata = None  # variable

    @property
    def type(self):
        return self._type

    @type.setter
    def type(self, value):
        self._type = RecordType(value)

    @property
    def classd(self):
        return self._classd

    @classd.setter
    def classd(self, value):
        self._classd = ClassType(value)

    @staticmethod
    def from_bytes(message_bytes, offset):
        """
        Unpack a response
        :param message_bytes: bytes
        :param offset: int
        :return: tuple(DNSResponse, int)
        """
        response = DNSResponse()
        response.name, offset = DNSMessage.name_from_bytes(message_bytes, offset)
        response.type, response.classd, response.ttl, response.rdlength = \
            DNSResponse.header_format.unpack_from(message_bytes, offset)
        offset += DNSResponse.header_format.size

        if response.type is RecordType.A:  # https://tools.ietf.org/html/rfc1035#section-3.4.1
            if response.rdlength != 4:
                exit_error("Length for record type A is not 4")
            response.rdata = str(IPv4Address(message_bytes[offset:offset + response.rdlength]))
            offset += response.rdlength
        elif response.type is RecordType.AAAA:
            if response.rdlength != 16:
                exit_error("Length for record type AAAA is not 16")
            response.rdata = str(IPv6Address(message_bytes[offset:offset + response.rdlength]))
            offset += response.rdlength
        elif response.type is RecordType.NS or response.type is RecordType.CNAME:
            # https://tools.ietf.org/html/rfc1035#section-3.3.11 or https://tools.ietf.org/html/rfc1035#section-3.3.1
            response.rdata, offset = DNSMessage.name_from_bytes(message_bytes, offset)
        elif response.type is RecordType.MX:
            response.rdata, offset = RDataMX.from_bytes(message_bytes, offset)
        elif response.type is RecordType.SOA:
            response.rdata, offset = RDataSOA.from_bytes(message_bytes, offset)
        else:
            print("TODO: " + response.type.name)

        return response, offset

    def to_str(self):
        base_return = "%s%s%d%s%s%s%s" % (self.name, "\t" * 3, self.ttl, "\t", self.classd.name, "\t", self.type.name)

        if self.type is RecordType.MX:
            base_return += "%s%d%s%s" % ("\t", self.rdata.preference, "\t\t", self.rdata.exchange)
        elif self.type is RecordType.SOA:
            base_return += "\tmname=%s, rname=%s, serial=%d, refresh=%d,\n%s retry=%d, expire=%d, minimum=%d" % \
                           (self.rdata.mname, self.rdata.rname, self.rdata.serial, self.rdata.refresh, "\t" * 7,
                            self.rdata.retry, self.rdata.expire, self.rdata.minimum)
        else:
            base_return += "%s%s" % ("\t", self.rdata)

        return base_return

    def __repr__(self):
        return "<DNSResponse name=%s, type=%s, class=%s, ttl=%d, rdlength=%d, rdata=%s>" % \
               (self.name, self.type.name, self.classd.name, self.ttl, self.rdlength, self.rdata)


class DNSMessage:
    # All bytes are unsigned
    # https://tools.ietf.org/html/rfc1035#section-4.1.1
    header_format = struct.Struct("!6H")

    def __init__(self, identifier=random.getrandbits(16), is_query=True, recursive=True):
        self._qr = None
        self._opcode = None
        self._rcode = None

        # HEADER
        # 2 bytes
        self.identifier = identifier

        # 1 byte
        self.qr = QueryType.QUESTION if is_query else QueryType.RESPONSE  # 1 bit, query or response
        self.opcode = OperationCode.QUERY  # 4 bits, the kind of query message
        self.aa = 0  # 1 bit, authoritative answer
        self.tc = 0  # 1 bit, truncation, message not truncated
        self.rd = 1 if recursive else 0  # 1 bit, recursion desired

        # 1 byte
        self.ra = 0  # 1 bit, recursion available
        self.z = 0  # 1 bit, reserved for future use
        self.ad = 0  # 1 bit, authentication data
        self.cd = 0  # 1 bit, checking disabled
        self.rcode = ResponseCode.NO_ERROR  # 4 bit, response code

        self.question_count = 0  # 2 bytes, question count in the question section
        self.answer_count = 0  # 2 bytes, answer count in the answer section
        self.authority_count = 0  # 2 bytes, names servers count in the authoritative section
        self.additional_count = 0  # 2 bytes, additional count in the  additional section

        # BODY
        self.questions = []
        self.answers = []
        self.authority = []
        self.additional = []

    @property
    def qr(self):
        return self._qr

    @qr.setter
    def qr(self, value):
        self._qr = QueryType(value)

    @property
    def opcode(self):
        return self._opcode

    @opcode.setter
    def opcode(self, value):
        self._opcode = OperationCode(value)

    @property
    def rcode(self):
        return self._rcode

    @rcode.setter
    def rcode(self, value):
        self._rcode = ResponseCode(value)

    def add_question(self, question_name, question_type=RecordType.A, question_class=ClassType.IN):
        self.questions.append(DNSQuestion(question_name, question_type, question_class))
        self.question_count += 1

        return self

    @staticmethod
    def name_to_bytes(name):
        """
        Convert a name to a packed bytes form
        See: https://tools.ietf.org/html/rfc1035#section-3.1 and https://tools.ietf.org/html/rfc1035#section-2.3.4
        :param name: a python string usually representing a web address
        :return: bytes
        """

        name_packed = bytes()
        length_total = 0
        for part in [part.encode("ascii") for part in name.split(".")]:
            length_part = len(part)
            length_total += length_part + 1  # 1 byte for the length
            if length_part > 63:
                exit_error("Labels can be maximum of 63 bytes")
            name_packed += struct.pack("!B%ds" % length_part, length_part, part)
        name_packed += struct.pack("!B", 0)  # terminate name with 0

        if length_total > 255:
            exit_error("Names can not be more than 255 bytes")

        return name_packed

    @staticmethod
    def name_from_bytes(message_bytes, offset):
        """
        Convert from packed bytes to a readable string
        See: https://tools.ietf.org/html/rfc1035#section-4.1.4
        :param message_bytes: bytes
        :param offset: int
        :return: tuple(str, int)
        """
        labels = []
        length_total = len(message_bytes)
        while True:
            length, = struct.unpack_from("!B", message_bytes, offset)

            # is pointer, has 11 prefix
            if (length & 0xC0) == 0xC0:
                # construct pointer, ignore the first 2 bits
                pointer_raw, = struct.unpack_from("!H", message_bytes, offset)
                pointer = pointer_raw & 0x3FFF  # 0b0011111111111111
                offset += 2
                if pointer >= length_total:
                    exit_error("DNS name pointer out of packet range")

                # find pointer, return the offset from this point, we do not care where the pointer is
                prefix = ".".join(labels)
                if prefix:  # does have labels before, so we can add a dot, example ns3<dot here>google.com
                    prefix += "."

                return prefix + DNSMessage.name_from_bytes(message_bytes, pointer)[0], offset

            # normal string
            offset += 1
            if not length:  # found end of string
                return ".".join(labels), offset

            label, = struct.unpack_from("!%ds" % length, message_bytes, offset)
            labels.append(label.decode("ascii"))
            offset += length

    def to_bytes(self):
        """:return: bytes"""
        # set flags, use 0xF which is 1111 as a bit mask
        flags = (
            self.qr.value << 15) | (
            (self.opcode.value & 0xF) << 11) | (
            self.aa << 10) | (
            self.tc << 9) | (
            self.rd << 8) | (
            self.ra << 7) | (
            self.z << 6) | (
            self.ad << 5) | (
            self.cd << 4) | self.rcode.value

        # set header
        data = self.header_format.pack(self.identifier, flags, self.question_count, self.additional_count,
                                       self.authority_count, self.additional_count)

        # set questions
        for q in self.questions:
            data += q.to_bytes()

        return data

    @staticmethod
    def from_bytes(message_bytes):
        """
        :param message_bytes: bytes
        :return: DNSMessage
        """
        if not isinstance(message_bytes, bytes):
            raise Exception("Instance is not of type bytes")

        offset, message = 0, DNSMessage()

        # set header
        message.id, flags, message.question_count, message.answer_count, message.authority_count, message.additional_count = \
            message.header_format.unpack(message_bytes[offset:message.header_format.size])
        message.qr, message.opcode, message.aa = bit_get(flags, 15), (flags & (0xF << 11)) >> 11, bit_get(flags, 10)
        message.tc, message.rd, message.ra = bit_get(flags, 9), bit_get(flags, 8), bit_get(flags, 7)
        message.z, message.ad, message.cd, message.rcode = bit_get(
            flags, 6), bit_get(
            flags, 5), bit_get(
            flags, 4), flags & 0xF
        offset += message.header_format.size

        # set questions
        for _ in range(message.question_count):
            q, offset = DNSQuestion.from_bytes(message_bytes, offset)
            message.questions.append(q)

        # set answers
        for _ in range(message.answer_count):
            response, offset = DNSResponse.from_bytes(message_bytes, offset)
            message.answers.append(response)

        # set authority
        for _ in range(message.authority_count):
            response, offset = DNSResponse.from_bytes(message_bytes, offset)
            message.authority.append(response)

        # set additional
        for _ in range(message.additional_count):
            response, offset = DNSResponse.from_bytes(message_bytes, offset)
            message.additional.append(response)

        return message

    def header_to_str(self):
        return "id = %d\n" \
               "QR = %s, OPCODE = %s, AA = %d, TC = %d, RD = %d, RA = %d, Z = %d, AD = %d, CD = %d, RCODE = %s\n" \
               "questions = %d, answers = %d\n" \
               "authority = %d, additional = %d\n" \
               % (self.identifier, self.qr.name, self.opcode.name, self.aa, self.tc, self.rd, self.ra, self.z, self.ad,
                  self.cd,
                  self.rcode.name, self.question_count, self.answer_count, self.authority_count, self.additional_count)

    def to_str(self):
        str_repr = ["<<< HEADER >>>\n" + self.header_to_str()]

        # add each section to a string
        def add_to_str(count, list_count, section_name, add_header=True):
            if not count:
                return

            assert count == len(list_count)
            str_repr.append("\n<<< %s SECTION >>>" % section_name)
            if add_header:
                base_return = "Host%sTTL" % ("\t" * 4)
                t = list_count[0].type
                if t is RecordType.MX:
                    base_return += "%sPreference%sExchange" % ("\t" * 3, "\t")
                str_repr.append(base_return)
            for e in list_count:
                str_repr.append(e.to_str())

        add_to_str(self.question_count, self.questions, "QUESTION", False)
        add_to_str(self.answer_count, self.answers, "ANSWER")
        add_to_str(self.authority_count, self.authority, "AUTHORITY")
        add_to_str(self.additional_count, self.additional, "ADDITIONAL")

        return "\n".join(str_repr) + "\n"


if __name__ == "__main__":
    supported_questions = RecordType._member_map_
    cmd_parser = argparse.ArgumentParser(description="Simple DNS query app")
    cmd_parser.add_argument("hostname", help="Hostname too look up")
    cmd_parser.add_argument("--server", "-s", default="8.8.8.8", help="DNS server ip")
    cmd_parser.add_argument("--port", "-p", default=53, type=int, help="DNS server port")
    cmd_parser.add_argument(
        "--recursive",
        "-r",
        dest="is_recursive",
        action="store_true",
        help="Use recursive querying, default")
    cmd_parser.add_argument("--no-recursive", "-nr", dest="is_recursive", action="store_false",
                            help="Do not use recursive querying, use iterative")
    cmd_parser.add_argument("--question", "-q", default="A", type=str,
                            help="The type of record returned. Supported are " + ", ".join(supported_questions.keys()))
    cmd_parser.set_defaults(is_recursive=True)
    args = cmd_parser.parse_args()

    # set and validate
    hostname = args.hostname
    if not is_ip_valid(args.server):
        exit_error("DNS server ip is not valid")
    if args.question not in supported_questions:
        exit_error("Question type is not supported. Supported questions are: " + ", ".join(supported_questions.keys()))

    # create udp socket
    S = None
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    except socket.error:
        exit_error("Failed to create socket")

    # find name server ip in additional section
    def find_ns_ip(ns, additional):
        for a in additional:
            if (a.type is RecordType.A or a.type is RecordType.AAAA) and a.name == ns:
                return a.rdata

        return None

    def send_dns_query(server_ip, server_port, question, is_recursive):
        delimiter = "-" * 40
        print("DNS server = %s:%d\nLooking up: %s" % (server_ip, server_port, hostname), end='\n' * 2)
        try:
            # send
            send_message = DNSMessage(recursive=is_recursive).add_question(hostname, supported_questions[question])
            send_data = send_message.to_bytes()
            s.sendto(send_data, (server_ip, server_port))
            print(delimiter + " SEND " + delimiter)
            print(send_message.to_str())

            # receive
            receive_data, receive_address = s.recvfrom(1024)
            receive_message = DNSMessage.from_bytes(receive_data)
            print(delimiter + " RESPONSE " + delimiter)
            print(receive_message.to_str())

            if receive_message.tc:
                exit_error("Value is truncated, use TCP")

            # use iterative because the response is not recursive or we desire it to be iterative
            if (not is_recursive or not receive_message.ra) and not receive_message.answers:
                if not receive_message.authority and not receive_message.additional:
                    exit_error("Can not make iterative querying because there is no data in authority or additional")
                print("\n".join(["-" * 150] * 2))

                # loop over authority section and try to find suitable dns server with response
                for r in receive_message.authority:
                    if r.type is RecordType.NS:
                        found_ip = find_ns_ip(r.rdata, receive_message.additional)
                        if found_ip is not None:
                            if send_dns_query(found_ip, 53, question, False):
                                return True

                return False

            return True
        except socket.error:
            exit_error("Failed to send message socket")

    # first level
    send_dns_query(args.server, args.port, args.question, args.is_recursive)
