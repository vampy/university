# Running
If you have python installed run the script in the 'src' directory:
    `python main.py`

Or run the executable in the 'exe' directory' (if you compile it):
    `main.exe`

# Compiling
## Create virtual environment and activate it
```
virtualenv -p /usr/bin/python3 env

. env/bin/activate

pip install -r requirements.txt
```

## Building
`pyinstaller --onefile --windowed main.py`

The above command will compile the project and the executable file will be in 'dist' directory
More details [here](https://pythonhosted.org/PyInstaller/).
