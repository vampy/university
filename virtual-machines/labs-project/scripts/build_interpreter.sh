#!/bin/bash
cd src/ && ocamlbuild -use-menhir -tag thread -use-ocamlfind -pkg core interpreter.native
