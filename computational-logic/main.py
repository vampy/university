#!/usr/bin/python
"""
@author: Daniel Butum, Group 911
"""
from domain.number import Number, NumberException
from ui.ui import UI
import tests

# TODO fix dividing in hexa
if __name__ == "__main__":
    tests.run()
    ui = UI()
    ui.run()