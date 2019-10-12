#!/bin/bash
cd src/ && ocamlbuild -ocamlyacc "menhir --explain --interpret-show-cst" -tag thread -use-ocamlfind -pkg core test.native
