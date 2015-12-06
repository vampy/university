#!/usr/bin/python
"""
@author: Daniel Butum, Group 911
"""

import sys

from domain.number import Number, NumberException


def run():
    # test adding
    assert str(Number("1234") + Number("9")) == "1243"
    assert str(Number("0A5F", 16) + Number("96BD", 16)) == "A11C"
    assert str(Number("1110101101", 2) + Number("0110110011", 2)) == "10101100000"
    assert str(Number("43012", 5) + Number("03243", 5)) == "101310"

    # test subtracting
    assert str(Number("A11C", 16) - Number("0A5F", 16)) == "96BD"
    assert str(Number("20053", 6) - Number("04444", 6)) == "11205"
    assert str(Number("110001011000", 2) - Number("001110110011", 2)) == "100010100101"

    # test multiplication by one digit
    assert str(Number("2031", 4) * Number("3", 4)) == "12213"
    assert str(Number("2B5F", 16) * Number("A", 16)) == "1B1B6"
    assert str(Number("42", 10) * 2) == "84"

    # test division
    assert str(Number("100", 10) // 2) == "050"

    quotient, remainder = divmod(Number("A5B", 16), Number("8", 16))
    assert str(quotient) == "14B"
    assert str(remainder) == "3"

    quotient, remainder = divmod(Number("2043", 8), Number("5", 8))
    assert str(quotient) == "0323"
    assert str(remainder) == "4"

    # Test Conversions
    assert str(Number("1010", 2).convert_division(16)) == "A"
    assert str(Number("1010", 2).convert_substitution(16)) == "A"
    assert str(Number("1010", 2).convert_rapid(16)) == "A"

    assert str(Number("10101111", 2).convert_division(16)) == "AF"
    assert str(Number("10101111", 2).convert_substitution(16)) == "AF"
    assert str(Number("10101111", 2).convert_rapid(16)) == "AF"

    assert str(Number("10101010", 2).convert_division(16)) == "AA"
    assert str(Number("10101010", 2).convert_substitution(16)) == "AA"
    assert str(Number("10101010", 2).convert_rapid(16)) == "AA"

    assert str(Number("10101001", 2).convert_division(16)) == "A9"
    assert str(Number("10101001", 2).convert_substitution(16)) == "A9"
    assert str(Number("10101001", 2).convert_rapid(16)) == "A9"

    assert str(Number("A9", 16).convert_division(2)) == "10101001"
    assert str(Number("A9", 16).convert_substitution(2)) == "10101001"
    assert str(Number("A9", 16).convert_rapid(2)) == "10101001"

    assert str(Number("165", 9).convert_rapid(3)) == "012012"
    assert str(Number("12012", 3).convert_rapid(9)) == "165"

    assert str(Number("FF", 16).convert_division(10)) == "255"
    assert str(Number("FF", 16).convert_substitution(10)) == "255"
    assert str(Number("FABF", 16).convert_substitution(10)) == "64191"

    try:
        Number("A9", 16).convert_rapid(10)
        assert False
    except NumberException:
        assert True

    try:
        assert str(Number("5462", 8).convert_rapid(16)) == "B32"
        assert False
    except NumberException:
        assert True


if __name__ == "__main__":
    if sys.version_info[0] < 3:
        sys.exit("ERROR: Must be using at least Python 3")

    run()
