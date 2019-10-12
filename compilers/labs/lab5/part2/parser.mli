type token =
  | IDENTIFIER
  | CONSTANT
  | NEWLINE
  | ARRAY
  | INT
  | CHAR
  | FLOAT
  | WHILE
  | DO
  | BEGIN
  | END
  | IF
  | THEN
  | ELSE
  | INPUT
  | PRINT
  | LEFT_SQB
  | RIGHT_SQB
  | LEFT_PAR
  | RIGHT_PAR
  | LESS
  | GREATER
  | LESS_EQUAL
  | GREATER_EQUAL
  | EQ_EQUAL
  | NOT_EQUAL
  | MULTIPLY
  | DIVIDE
  | PLUS
  | MINUS
  | EQUAL
  | COMMA
  | EOF

val program :
  (Lexing.lexbuf  -> token) -> Lexing.lexbuf -> unit
