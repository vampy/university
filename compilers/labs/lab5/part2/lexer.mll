{
open Parser
let line_num = ref 1
exception Syntax_error of string
let syntax_error msg = raise (Syntax_error (msg ^ " on line " ^ (string_of_int !line_num)))

}
let blank = [' ' '\r' '\t']
let digit = ['0'-'9']
let id = ['a'-'z' 'A'-'Z']+
let constant = ['-''+']?(['0'-'9']|['1'-'9']['0'-'9']+)|'"'['a'-'z''A'-'Z''0'-'9']'"' |"true"|"false"
rule token = parse
  |'\n' { incr line_num; NEWLINE }
  | blank { token lexbuf }
  |"array" { ARRAY } 
  |"do"  { DO }
  |"then" { THEN }
  |"else" { ELSE }
  |"if" { IF }
  |"input" { INPUT }
  |"print" { PRINT }
  |"char" { CHAR }
  |"end" { END }
  |"begin" { BEGIN }
  |"while" { WHILE }
  |"float" { FLOAT }
  |"int" { INT }
  | id { IDENTIFIER}
  | constant{CONSTANT}
  | '+' { PLUS } 
  | '-' { MINUS }
  | '*' { MULTIPLY } 
  | '/' { DIVIDE }
  |"<" { LESS }
  |"<=" { LESS_EQUAL }
  |"==" { EQ_EQUAL }
  |"=>" { GREATER_EQUAL }
  |'>' { GREATER }
  |"!=" { NOT_EQUAL }
  |"=" { EQUAL }
  |'[' { LEFT_SQB }
  |']' { RIGHT_SQB }
  |'(' { LEFT_PAR }
  |')' { RIGHT_PAR }
  |',' { COMMA }
  | _ { syntax_error "couldn't identify the token" }
  | eof  { raise End_of_file }
