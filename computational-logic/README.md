# Computational logic
[Website Course](http://www.cs.ubbcluj.ro/~lupea/LOGICA/Engleza/)

The final optional homework project is located in `src-number-converter`.

## Problem statement:
Implement the basic arithmetic operations: addition, subtraction, multiplication by one digit and division  by one digit in any base between 2 and 16.
Implement also the following conversions method between bases: substitution, rapid conversions,

## Implementation
The main number logic is implemented in the `Number` class which gets the numbers as a string of
characters. The string is manipulated and every character is converted to an integer corresponding to it.
Internally the list of numbers is reversed so the least significant digit will be on position 0
in the list and the most significant digit will be on the last position in the list (eg: “1234” string is internally
as the list [4, 3, 2, 1]).
This reversed implementation of the number is done because of easier computation and cleaner code.

## Running
If you have python installed run the script:
    `python3 main.py`

## Compiling
### Create virtual environment and activate it
```
virtualenv -p /usr/bin/python3 env

. env/bin/activate

pip install -r requirements.txt
```

### Building
`pyinstaller --onefile --windowed main.py`

The above command will compile the project and the executable file will be in 'dist' directory
More details [here](https://pythonhosted.org/PyInstaller/).
