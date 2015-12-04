#!/usr/bin/python3
"""
@author: Daniel Butum, Group 911
"""
import tests
import sys
from ui.ui import UI


if __name__ == "__main__":
    if sys.version_info[0] < 3:
        sys.exit("ERROR: Must be using at least Python 3")

    tests.run()
    ui = UI()
    ui.run()
