# About
Almost every directory has the requirements at the top of `main.c`/`main.cpp` file.

The `.pro` file are meant to be opened with Qt Creator or `qmake` (if you want to compile using the terminal).

The `CMakeLists.txt` are cmake files describing the project in that directory, you can compile using the terminal 
or open them with any editor that supports cmake projects.
To get a debug build use `-DCMAKE_BUILD_TYPE=Debug`.

Compiling using the terminal is as simple as:
```
mkdir build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Debug
make -j2
<run executable, if no errors occured>
```

# Clean code
To clean the code using the specified `.clang-format` style, run:
```
clang-format -i *.cpp *.hpp *.c *.h
```
