# CoreJava-Interpreter
Repo: https://github.com/0merta/CoreJava-Interpreter

Interpreter for CoreJava[1] written in OCaml

# First steps
1. Install required dependencies using [scripts/install_dependencies.sh](scripts/install_dependencies.sh)
2. Build test.ml using [scripts/build_test.sh](scripts/build_test.sh)
3. Run the test. Eg: " ./test.native ./core-java-files/helloworld.java "

# Using oasis
1. After updating the [_oasis](_oasis) config file run `oasis setup -setup-update dynamic`.
2. Compile: `make -j 4`. The executables specified in the oasis file will be in the current directory with the extension `.native`.
3. Clean: `make clean`
4. Rinse and repeat.

# Using ocp-indent
1. Run [scripts/ocp-indent.sh](scripts/ocp-indent.sh) from the root of the repo.

# Contributing
The course CS 421: Programming Languages and Compilers[2] from the University of Illinois provides good resources.
You can also check the examples of parsing from realworldocaml[3].

# Common Problems
## Can only use Oasis/make OR build scripts
If `make` fails because it can't find a `.native` file, try to remove the `src/_build` directory and any `.native` files inside `src/` and `setup.data`, `setup.log` from root. Also `make clean` ;).

## Executable does not reflect code changes. WTF? :rage4:
Ah, you did not respect the rule above :clap:. Try to clean all the files indicated above.

Also note that if you delete `src/_build/` first in Atom editor it will not show the broken links `src/*.native` and you will still get an error if you try to `make`.
Go and delete the `.native` files manually.

## SANITIZE: a total of `x` files that should probably not be in your source tree
This is probably due to you running `menhrir -v src/parser.mly` and the files generated
`src/parser.ml` and `src/parser.mli`. Manually remove them or run the sanitize script produced in `_build/`.

# References:
1. http://www.academia.edu/3300125/Core-java_an_expression-oriented_java
2. https://courses.engr.illinois.edu/cs421/sp2012/lectures/
3. https://github.com/realworldocaml/examples/tree/master/code/parsing
