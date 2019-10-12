{
open Lexing
open Parser
open Syntax

exception SyntaxError of string

let next_line lexbuf =
  let pos = lexbuf.lex_curr_p in
  lexbuf.lex_curr_p <-
    { pos with pos_bol = lexbuf.lex_curr_pos;
               pos_lnum = pos.pos_lnum + 1
    }
}

let white = [' ' '\t']+
let newline = '\r' | '\n' | "\r\n"

let digit = ['0'-'9']
let id = ['a'-'z' 'A'-'Z' '_'] ['a'-'z' 'A'-'Z' '0'-'9' '_']*
let int = '-'? ['0'-'9'] ['0'-'9']*

let digit = ['0'-'9']
let frac = '.' digit*
let exp = ['e' 'E'] ['-' '+']? digit+
let float = digit* frac? exp?


rule read = parse
     | white    { read lexbuf }
     | newline  { next_line lexbuf; read lexbuf }

     | '.'      { DOT }
     | '{'      { LEFT_BRACE }
     | '}'      { RIGHT_BRACE }
     (*| '['      { LEFT_BRACK }
     | ']'      { RIGHT_BRACK }
     | ':'      { COLON }*)
     | '('      { OPARENT }
     | ')'      { CPARENT }

     | ','      { COMMA }
     | ';'      { SEMICOLON }
     | '#'	    { HASHTAG }

     | "&&" 	{ AND }
     | "||" 	{ OR }
     | '!'    { NOT }

     | "+." 	{ FPLUS }
     | "-." 	{ FMINUS }
     | "*." 	{ FMULTIPLY }
     | "/." 	{ FDIVIDE }

     | '+' 	{ IPLUS }
     | '-' 	{ IMINUS }
     | '*' 	{ IMULTIPLY }
     | '/' 	{ IDIVIDE }

     | '<' 	  { LESS }
     | "<=" 	{ LESS_EQUAL }
     | "==" 	{ EQ_EQUAL }
     | ">=" 	{ GREATER_EQUAL }
     | '>' 	  { GREATER }
     | "!=" 	{ NOT_EQUAL }
     | '=' 	  { EQUAL }

     | int   	  { INT(int_of_string (Lexing.lexeme lexbuf)) }
     | float 	  { FLOAT(float_of_string (Lexing.lexeme lexbuf)) }
     | "true"  	{ BOOL(true) }
     | "false"  { BOOL(false) }
     | "null"   { NULL }

     | "class"      { CLASS }
     | "extends"    { EXTENDS }
     | "while" 	    { WHILE }
     | "new" 	      { NEW }
     | "float" 	    { TFLOAT }
     | "int"   	    { TINT }
     | "bool" 	    { TBOOL}
     | "void"       { TVOID}
     | "if"   	    { IF }
     | "else" 	    { ELSE }
     | "instanceof" { INSTANCEOF }
     | id 	    { ID(Lexing.lexeme lexbuf) }

     | _ 	{ raise (SyntaxError  ("Unexpected char: " ^ Lexing.lexeme lexbuf)) }
     | eof 	{ EOF }
