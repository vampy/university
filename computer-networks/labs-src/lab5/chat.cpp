#pragma pack(1)
#include "../os.hpp"
#include "../udp.hpp"
#include <thread>
#include <chrono>
#include <mutex>
#include <atomic>
#include <vector>
#include <cstring>
#include <map>
//#include <ncurses.h>

#define HEADER_SIZE 8 + 32 + 32 + 1 + 2
#define BODY_SIZE   1024
#define MAX_MESSAGE HEADER_SIZE + BODY_SIZE

using namespace std;

const uint16_t PORT = 7777;
SafeSocketUDP *socket_send = nullptr;
atomic<int32_t> threads_count;
atomic<bool> exit_program;
string nickname; // which is also the ID of each message
string MCAST;
enum Command
{
    LIST = 0, JOIN, LEAVE, MSG, NICK, QUIT, HELP_MENU
};
enum Operation
{
    ID_SUBSCRIBE = 1, ID_UNSUSCRIBE = 2, MESSAGE = 3 // TODO add 4 and 5
};


static uint8_t* toBuffer(char room_name[32], char id[32], uint8_t oper, uint16_t msg_len, char *msg)
{
    assert(msg_len <= BODY_SIZE);
    size_t room_name_len =  strlen(room_name), id_len = strlen(id);
    assert(room_name_len < 33); assert(id_len < 33);
    uint8_t *buffer = new uint8_t[HEADER_SIZE + msg_len];

    // zero out buffer header
    memset(buffer, 0, HEADER_SIZE);

    // add header
    uint32_t size = 0;
    char version[8] = "CHATv1";
    memcpy(buffer + size, version, strlen(version)); size = 8;
    memcpy(buffer + size, room_name, room_name_len); size = 40;
    memcpy(buffer + size, id, id_len); size = 72;
    buffer[size] = oper; size++;
    packi16(buffer + size, msg_len); size += 2;

    assert(size == HEADER_SIZE);

    // add message
    if (msg != NULL && msg_len) memcpy(buffer + size, msg, msg_len);

    return buffer;
}

static uint8_t *fromBuffer(uint8_t *buffer, size_t buffer_length,
                        char dst_version[8],
                        char dst_room_name[32],
                        char dst_id[32],
                        uint8_t &dst_oper,
                        uint16_t &dst_msg_len)
{
    uint32_t size = 0;

    // buffer must at least be ass big as header
    assert(buffer_length >= HEADER_SIZE);

    // read header
    memcpy(dst_version, buffer + size, 8); size += 8;
    memcpy(dst_room_name, buffer + size, 32); size += 32;
    memcpy(dst_id, buffer + size, 32); size += 32;
    dst_oper = buffer[size]; size++;
    dst_msg_len = unpacku16(buffer + size); size += 2;

    // buffer must be large enough for body
    assert(size == HEADER_SIZE);
    assert((uint16_t)buffer_length >= (dst_msg_len + HEADER_SIZE));
    assert(dst_msg_len <= BODY_SIZE);

    // read body
    uint8_t *dst_msg = new uint8_t[dst_msg_len + 1];
    if (dst_msg_len) memcpy(dst_msg, buffer + size, dst_msg_len);
    dst_msg[dst_msg_len] = 0;

    return dst_msg;
}

class Chat
{
private:
    // current host is 0, 0 in the vector pair
    map<string, vector<pair<uint32_t, uint16_t>>*> rooms;
    mutex mtx;

    bool roomExists(string room)
    {
        mtx.lock();
        bool found = (rooms.find(room) != rooms.end());
        mtx.unlock();

        return found;
    }

    int32_t findRoom(string room, uint32_t ip, uint16_t port)
    {
        mtx.lock();
        if (rooms[room]->empty())
        {
            mtx.unlock();
            return -1;
        }

        int32_t position = 0;
        for (auto it = rooms[room]->begin(); it != rooms[room]->end(); it++, position++)
        {
            if (it->first == ip && it->second == port)
            {
                mtx.unlock();
                return position;
            }
        }

        mtx.unlock();
        return -1;
    }

    int32_t findSubscribed(string room, uint32_t ip, uint16_t port)
    {
        if (!roomExists(room))
        {
            addRoom(room); // add room
            return -1;
        }

        return findRoom(room, ip, port);
    }

    void addRoom(string room)
    {
        mtx.lock();
        rooms[room] = new vector<pair<uint32_t, uint16_t>>();
        mtx.unlock();
    }

public:
    Chat() {}

    ~Chat()
    {
        mtx.lock();
        for (auto it = rooms.begin(); it != rooms.end(); it++)
        {
            delete it->second;
        }
        rooms.clear();
        mtx.unlock();
    }

    bool isSubscribed(string room, uint32_t ip = 0, uint16_t port = 0)
    {
        return findSubscribed(room, ip, port) != -1;
    }

    // get a list of subscribed rooms for ip and port
    vector<string> getSubscribedRooms(uint32_t ip = 0, uint16_t port = 0)
    {
        vector<string> found_rooms;

        for (auto it = rooms.begin(); it != rooms.end(); it++)
        {
            if (isSubscribed(it->first, ip, port))
            {
                found_rooms.push_back(it->first);
            }
        }

        return found_rooms;
    }

    // subscribe to a room in memory
    bool subscribe(string room, uint32_t ip = 0, uint16_t port = 0)
    {
        if (isSubscribed(room, ip, port)) // already subscribed
        {
            return false;
        }

        mtx.lock();
        rooms[room]->push_back(make_pair(ip, port));
        mtx.unlock();

        return true;
    }

    bool unsuscribe(string room, uint32_t ip = 0, uint16_t port = 0)
    {
        int32_t position = findSubscribed(room, ip, port);
        if (position == -1) // not subscribed
        {
            return false;
        }

        mtx.lock();
        rooms[room]->erase(rooms[room]->begin() + position);
        mtx.unlock();

        return true;
    }

    void unsuscribeNetwork(string room)
    {
        // send message to network
        uint8_t *buffer = toBuffer((char *)room.c_str(), (char *)nickname.c_str(), ID_UNSUSCRIBE, 0, NULL);
        uint32_t sent_bytes = socket_send->send<uint8_t*>(buffer, HEADER_SIZE);
        assert(sent_bytes == HEADER_SIZE);
        DELETE_ARRAY(buffer);
    }

    void unsuscribeAll()
    {
        for (auto it = rooms.begin(); it != rooms.end(); it++)
        {
            unsuscribe(it->first);
            unsuscribeNetwork(it->first);
        }
    }
} chat;

void handle_receive(SafeSocketUDP *socket_receive)
{
    threads_count++;

    // TODO set receive with timeout
    timeval timeout = {.tv_sec = 1, .tv_usec = 0};
    uint8_t message[MAX_MESSAGE];
    socket_receive->setOptionReceiveTimeout(timeout); // 1 second timeout
    while (true)
    {
        if (exit_program) break;
        int32_t message_len = 0;

        // get message
        try
        {
            message_len = socket_receive->receive<uint8_t*>(message, MAX_MESSAGE);
        }
        catch (const SafeSocketException &e)
        {
            if (e.isErrorTryAgain()) continue;
        }

        // drop invalid packets
        if (message_len < HEADER_SIZE)
        {
            //cout << "Dropping packet: packet is not large enough" << endl;
            continue;
        }

        // unpack data
        char version[8], room_name[32], id[32];
        uint8_t oper;
        uint16_t msg_len;
        uint8_t *msg = fromBuffer(message, message_len, version, room_name, id, oper, msg_len);

        // drop invalid version
        if (strncmp(version, "CHATv1", 6) != 0)
        {
            cout << "Dropping packet: packet is not right version" << endl;
            DELETE_ARRAY(msg);
            continue;
        }

        string room(room_name);
        uint16_t port = socket_receive->getFromPort();
        uint32_t ip = socket_receive->getFromIP();
        switch (oper)
        {
        case ID_SUBSCRIBE:
            if (!chat.subscribe(room, ip, port))
            {
                //cout << "SUBSCRIBE room = " << room << ", ip = " << socket_receive->getFromIPString() << ", port = " << port << endl;
                //cout << "Error: already subscribed" << endl;
            }
            break;

        case ID_UNSUSCRIBE:
            if (!chat.unsuscribe(room, ip, port))
            {
                //cout << "UNSUBSCRIBE room = " << room << ", ip = " << socket_receive->getFromIPString() << ", port = " << port << endl;
                //cout << "Error: not subscribed" << endl;
            }
            break;

        case MESSAGE:
            if (chat.isSubscribed(room)) // we are subscribed to that room
            {
                cout << "MESSAGE room = " << room << ", ip = " << socket_receive->getFromIPString() << ", port = " << port << endl;
                cout << room << " - " << id << ": " << msg << endl;
            }

            break;

        default:
            printf("Invalid oper = %u\n", oper);
            break;
        }

        DELETE_ARRAY(msg);
    }

    cout << "Exiting handle_receive" << endl;
    threads_count--;
}

void handle_main(SafeSocketUDP *socket_send)
{
    threads_count++;

    string option;
    while (true)
    {
        if (exit_program) break;
        cout << ">> "; getline(cin, option);
        if (exit_program) break;

        if (option.length() < 3)
        {
            cout << "Invalid command. Try again" << endl; continue;
        }

        string prefix = String::toUpperCase(option.substr(0, 3));
        if (prefix == "LIS") // LIST
        {
            cout << "LIST" << endl;
            vector<string> rooms = chat.getSubscribedRooms();

            if (rooms.size() > 1)
            {
                cout << "rooms = " << rooms[0];
                for (auto it = rooms.begin() + 1; it != rooms.end(); it++)
                {
                    cout << ", " << (*it);
                }
                cout << endl;
            }
            else
            {
                cout << "room = " << rooms[0] << endl;
            }

        }
        else if (prefix == "JOI") // JOIN
        {
            if (nickname.empty())
            {
                cout << "Error:  nickname not set. Set it with NICK <nickname>" << endl; continue;
            }
            cout << "JOIN <room_name>" << endl;

            // split
            vector<string> option_split = String::split(option, ' ');
            if (option_split.size() != 2) // validate split
            {
                cout << "Invalid JOIN. Try again."<< endl; continue;
            }
            string room_name = option_split[1];
            cout << "Room name = " << room_name << endl;

            if (!chat.subscribe(room_name))
            {
                cout << "Invalid JOIN. You are already part of that room" << endl; continue;
            }

            // send message to network
            uint8_t *buffer = toBuffer((char *)room_name.c_str(), (char *)nickname.c_str(), ID_SUBSCRIBE, 0, NULL);
            int32_t sent_bytes = socket_send->send<uint8_t*>(buffer, HEADER_SIZE);
            assert(sent_bytes == HEADER_SIZE);

            DELETE_ARRAY(buffer);
        }
        else if (prefix == "LEA") // LEAVE
        {
            if (nickname.empty())
            {
                cout << "Error: nickname not set. Set it with NICK <nickname>" << endl; continue;
            }
            cout << "LEAVE <room_name>" << endl;

            // split
            vector<string> option_split = String::split(option, ' ');
            if (option_split.size() != 2)
            {
                cout << "Invalid LEAVE. Try again."<< endl; continue;
            }
            string room_name = option_split[1];
            cout << "Room name = " << room_name << endl;

            if (!chat.unsuscribe(room_name)) // validate room and remove
            {
                cout << "Invalid LEAVE. You can not leave a room your are not part of." << endl; continue;
            }

            // send message to network
            chat.unsuscribeNetwork(room_name);
        }
        else if (prefix == "MSG") // MSG
        {
            if (nickname.empty())
            {
                cout << "Error: nickname not set. Set it with NICK <nickname>" << endl; continue;
            }
            cout << "MSG <room_name> <msg>" << endl;

            // split
            vector<string> option_split = String::split(option, ' ');
            if (option_split.size() < 3)
            {
                cout << "Invalid MSG. Try again."<< endl; continue;
            }
            string room_name = option_split[1];
            if (!chat.isSubscribed(room_name))
            {
                cout << "Error: not subscribed" << endl; continue;
            }

            string message = option_split[2];
            for (size_t i = 3; i < option_split.size(); i++) // handle messages with spaces
            {
                message += " " + option_split[i];
            }
            cout << "Room name = " << room_name
                 << ", Message = " << message << endl;

            // send message to network
            uint8_t *buffer = toBuffer((char *)room_name.c_str(),
                                       (char *)nickname.c_str(),
                                       MESSAGE,
                                       message.length(),
                                       (char *)message.c_str());
            uint32_t sent_bytes = socket_send->send<uint8_t*>(buffer, HEADER_SIZE + message.length());
            assert(sent_bytes == (HEADER_SIZE + message.length()));

            DELETE_ARRAY(buffer);
        }
        else if (prefix == "NIC") // NICK
        {
            cout << "NICK <nickname>" << endl;
            vector<string> option_split = String::split(option, ' ');
            if (option_split.size() != 2)
            {
                cout << "Invalid NICK. Try again."<< endl; continue;
            }
            nickname = option_split[1];
            cout << "NickName = " << nickname << endl;
        }
        else if (prefix == "QUI") // QUIT, unsubscribe from all rooms, close sockets and exit.
        {
            cout << "QUIT" << endl;
            chat.unsuscribeAll();
            exit_program = true;
        }
        else
        {
            cout << "Invalid command. Try again" << endl; continue;
        }
    }

    cout << "Exiting handle_main" << endl;
    threads_count--;
}

int main(int argc, char *argv[])
{
    threads_count = 0;
    exit_program = false;

    if (argc >= 2) // set from command line
    {
        MCAST = argv[1];
    }
    else // use default
    {
        MCAST = "224.100.101.102";
    }
    cout << "MCAST = " << MCAST << endl;

    socket_send = new SafeSocketUDP();
    socket_send->open()
            ->setOptionReuseAddress()
            ->setAddress(PORT) // listen to local port 7777 on localhost, aka receive here
            ->bind()
            ->setAddress(MCAST.c_str(), PORT) // send to this address
            ->setAddressMulticast(MCAST.c_str())
            ->setOptionMulticastTTL(1)
            ->setOptionMulticastLoop(false)
            ->setOptionMulticastAdd();

//    // test to
//    char room_name[32] = "test_room",
//         id[32] = "test_nick";
//    uint8_t oper = 7;
//    string msg = "Hello world";

//    uint8_t *buffer = toBuffer(room_name, id, oper, msg.length(), (char *)msg.c_str());
//    size_t buffer_length = msg.length() + HEADER_SIZE;

//    for (size_t i = 0; i < buffer_length; i++)
//    {
//        cout << (uint8_t)buffer[i];
//    }
//    cout << endl;

//    // test from
//    char dst_version[8], dst_room_name[32], dst_id[32];
//    uint8_t dst_oper;
//    uint16_t dst_msg_len;
//    uint8_t *dst_msg = fromBuffer(buffer, buffer_length, dst_version, dst_room_name, dst_id, dst_oper, dst_msg_len);
//    cout << "Received: " << endl
//         << "version = '" << dst_version << "'" << endl
//         << "room = '" << dst_room_name << "'" << endl
//         << "id = '" << dst_id << "'" << endl
//         << "oper = " << (uint32_t)dst_oper << endl
//         << "msg_len = " << dst_msg_len << endl
//         << "msg = '" << dst_msg << "'" << endl;


//    DELETE_ARRAY(dst_msg);
//    DELETE_ARRAY(buffer);

    auto thread_receive = thread(handle_receive, socket_send),
         thread_main = thread(handle_main, socket_send);
    thread_receive.join();
    thread_main.join();

    socket_send->setOptionMulticastDrop();
    DELETE(socket_send);
    return 0;
}
