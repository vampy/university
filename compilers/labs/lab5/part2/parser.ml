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

open Parsing;;
let _ = parse_error;;
# 3 "parser.mly"
open Printf
let set_trace = 
  true
# 43 "parser.ml"
let yytransl_const = [|
  257 (* IDENTIFIER *);
  258 (* CONSTANT *);
  259 (* NEWLINE *);
  260 (* ARRAY *);
  261 (* INT *);
  262 (* CHAR *);
  263 (* FLOAT *);
  264 (* WHILE *);
  265 (* DO *);
  266 (* BEGIN *);
  267 (* END *);
  268 (* IF *);
  269 (* THEN *);
  270 (* ELSE *);
  271 (* INPUT *);
  272 (* PRINT *);
  273 (* LEFT_SQB *);
  274 (* RIGHT_SQB *);
  275 (* LEFT_PAR *);
  276 (* RIGHT_PAR *);
  277 (* LESS *);
  278 (* GREATER *);
  279 (* LESS_EQUAL *);
  280 (* GREATER_EQUAL *);
  281 (* EQ_EQUAL *);
  282 (* NOT_EQUAL *);
  283 (* MULTIPLY *);
  284 (* DIVIDE *);
  285 (* PLUS *);
  286 (* MINUS *);
  287 (* EQUAL *);
  288 (* COMMA *);
    0 (* EOF *);
    0|]

let yytransl_block = [|
    0|]

let yylhs = "\255\255\
\001\000\002\000\002\000\004\000\005\000\005\000\006\000\006\000\
\006\000\007\000\003\000\008\000\008\000\009\000\009\000\009\000\
\009\000\009\000\010\000\013\000\011\000\011\000\012\000\012\000\
\016\000\016\000\016\000\016\000\016\000\016\000\015\000\014\000\
\014\000\014\000\017\000\017\000\017\000\018\000\018\000\018\000\
\000\000"

let yylen = "\002\000\
\003\000\001\000\003\000\002\000\001\000\001\000\001\000\001\000\
\001\000\002\000\006\000\001\000\003\000\001\000\001\000\001\000\
\001\000\001\000\003\000\007\000\004\000\004\000\007\000\011\000\
\001\000\001\000\001\000\001\000\001\000\001\000\003\000\003\000\
\003\000\001\000\003\000\003\000\001\000\003\000\001\000\001\000\
\002\000"

let yydefred = "\000\000\
\000\000\000\000\000\000\007\000\008\000\009\000\041\000\000\000\
\002\000\000\000\005\000\006\000\010\000\000\000\004\000\000\000\
\001\000\003\000\000\000\000\000\000\000\000\000\000\000\000\000\
\016\000\000\000\012\000\014\000\015\000\017\000\018\000\000\000\
\039\000\040\000\000\000\000\000\000\000\000\000\037\000\000\000\
\000\000\000\000\000\000\000\000\000\000\025\000\026\000\027\000\
\028\000\029\000\030\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\013\000\038\000\000\000\
\000\000\000\000\000\000\035\000\036\000\000\000\021\000\022\000\
\011\000\000\000\000\000\000\000\000\000\020\000\023\000\000\000\
\000\000\000\000\000\000\024\000"

let yydgoto = "\002\000\
\007\000\008\000\025\000\009\000\010\000\011\000\012\000\026\000\
\027\000\028\000\029\000\030\000\031\000\036\000\037\000\054\000\
\038\000\039\000"

let yysindex = "\004\000\
\044\255\000\000\039\255\000\000\000\000\000\000\000\000\011\255\
\000\000\025\255\000\000\000\000\000\000\119\255\000\000\059\255\
\000\000\000\000\096\255\041\255\001\255\001\255\051\255\054\255\
\000\000\071\255\000\000\000\000\000\000\000\000\000\000\001\255\
\000\000\000\000\001\255\092\255\066\255\014\255\000\000\063\255\
\001\255\001\255\084\255\023\255\237\254\000\000\000\000\000\000\
\000\000\000\000\000\000\001\255\001\255\001\255\074\255\001\255\
\001\255\083\255\244\254\248\254\086\255\000\000\000\000\014\255\
\014\255\023\255\096\255\000\000\000\000\096\255\000\000\000\000\
\000\000\087\255\088\255\082\255\249\254\000\000\000\000\095\255\
\096\255\098\255\091\255\000\000"

let yyrindex = "\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\010\255\000\000\000\000\
\000\000\000\000\000\000\059\255\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\034\255\
\058\255\016\255\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\000\
\000\000\000\000\000\000\000\000"

let yygindex = "\000\000\
\000\000\000\000\089\000\091\000\000\000\104\000\000\000\000\000\
\213\255\000\000\000\000\000\000\000\000\230\255\087\000\000\000\
\013\000\012\000"

let yytablesize = 129
let yytable = "\062\000\
\063\000\033\000\034\000\079\000\001\000\044\000\080\000\071\000\
\045\000\052\000\053\000\072\000\034\000\014\000\059\000\060\000\
\052\000\053\000\034\000\035\000\052\000\053\000\034\000\074\000\
\031\000\015\000\075\000\066\000\031\000\034\000\034\000\034\000\
\034\000\034\000\034\000\034\000\032\000\082\000\034\000\034\000\
\056\000\057\000\032\000\004\000\005\000\006\000\032\000\003\000\
\004\000\005\000\006\000\052\000\053\000\032\000\032\000\032\000\
\032\000\032\000\032\000\032\000\033\000\019\000\032\000\032\000\
\064\000\065\000\033\000\068\000\069\000\041\000\033\000\032\000\
\042\000\043\000\055\000\058\000\067\000\033\000\033\000\033\000\
\033\000\033\000\033\000\033\000\020\000\070\000\033\000\033\000\
\073\000\076\000\077\000\021\000\078\000\016\000\061\000\022\000\
\020\000\081\000\023\000\024\000\083\000\084\000\017\000\021\000\
\018\000\016\000\013\000\022\000\040\000\000\000\023\000\024\000\
\046\000\047\000\048\000\049\000\050\000\051\000\000\000\000\000\
\052\000\053\000\003\000\004\000\005\000\006\000\000\000\000\000\
\016\000"

let yycheck = "\043\000\
\020\001\001\001\002\001\011\001\001\000\032\000\014\001\020\001\
\035\000\029\001\030\001\020\001\003\001\003\001\041\000\042\000\
\029\001\030\001\009\001\019\001\029\001\030\001\013\001\067\000\
\009\001\001\001\070\000\054\000\013\001\020\001\021\001\022\001\
\023\001\024\001\025\001\026\001\003\001\081\000\029\001\030\001\
\027\001\028\001\009\001\005\001\006\001\007\001\013\001\004\001\
\005\001\006\001\007\001\029\001\030\001\020\001\021\001\022\001\
\023\001\024\001\025\001\026\001\003\001\003\001\029\001\030\001\
\052\000\053\000\009\001\056\000\057\000\019\001\013\001\031\001\
\019\001\003\001\009\001\013\001\003\001\020\001\021\001\022\001\
\023\001\024\001\025\001\026\001\001\001\003\001\029\001\030\001\
\003\001\003\001\003\001\008\001\011\001\010\001\011\001\012\001\
\001\001\003\001\015\001\016\001\003\001\011\001\014\000\008\001\
\014\000\010\001\003\000\012\001\022\000\255\255\015\001\016\001\
\021\001\022\001\023\001\024\001\025\001\026\001\255\255\255\255\
\029\001\030\001\004\001\005\001\006\001\007\001\255\255\255\255\
\010\001"

let yynames_const = "\
  IDENTIFIER\000\
  CONSTANT\000\
  NEWLINE\000\
  ARRAY\000\
  INT\000\
  CHAR\000\
  FLOAT\000\
  WHILE\000\
  DO\000\
  BEGIN\000\
  END\000\
  IF\000\
  THEN\000\
  ELSE\000\
  INPUT\000\
  PRINT\000\
  LEFT_SQB\000\
  RIGHT_SQB\000\
  LEFT_PAR\000\
  RIGHT_PAR\000\
  LESS\000\
  GREATER\000\
  LESS_EQUAL\000\
  GREATER_EQUAL\000\
  EQ_EQUAL\000\
  NOT_EQUAL\000\
  MULTIPLY\000\
  DIVIDE\000\
  PLUS\000\
  MINUS\000\
  EQUAL\000\
  COMMA\000\
  EOF\000\
  "

let yynames_block = "\
  "

let yyact = [|
  (fun _ -> failwith "parser")
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'declarationList) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'compoundStatement) in
    Obj.repr(
# 45 "parser.mly"
                                                   ()
# 234 "parser.ml"
               : unit))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'declaration) in
    Obj.repr(
# 47 "parser.mly"
                             ()
# 241 "parser.ml"
               : 'declarationList))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'declarationList) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'declaration) in
    Obj.repr(
# 48 "parser.mly"
                                       ()
# 249 "parser.ml"
               : 'declarationList))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 1 : 'typeD) in
    Obj.repr(
# 50 "parser.mly"
                              ()
# 256 "parser.ml"
               : 'declaration))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'typeDeclaration) in
    Obj.repr(
# 51 "parser.mly"
                       ()
# 263 "parser.ml"
               : 'typeD))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'arrayDeclaration) in
    Obj.repr(
# 52 "parser.mly"
                       ()
# 270 "parser.ml"
               : 'typeD))
; (fun __caml_parser_env ->
    Obj.repr(
# 54 "parser.mly"
                     ()
# 276 "parser.ml"
               : 'typeDeclaration))
; (fun __caml_parser_env ->
    Obj.repr(
# 55 "parser.mly"
             ()
# 282 "parser.ml"
               : 'typeDeclaration))
; (fun __caml_parser_env ->
    Obj.repr(
# 56 "parser.mly"
              ()
# 288 "parser.ml"
               : 'typeDeclaration))
; (fun __caml_parser_env ->
    let _2 = (Parsing.peek_val __caml_parser_env 0 : 'typeDeclaration) in
    Obj.repr(
# 58 "parser.mly"
                                        ()
# 295 "parser.ml"
               : 'arrayDeclaration))
; (fun __caml_parser_env ->
    let _3 = (Parsing.peek_val __caml_parser_env 3 : 'statementList) in
    Obj.repr(
# 60 "parser.mly"
                                                                   ()
# 302 "parser.ml"
               : 'compoundStatement))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'statement) in
    Obj.repr(
# 62 "parser.mly"
                         ()
# 309 "parser.ml"
               : 'statementList))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'statementList) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'statement) in
    Obj.repr(
# 63 "parser.mly"
                                        ()
# 317 "parser.ml"
               : 'statementList))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'assignStatement) in
    Obj.repr(
# 65 "parser.mly"
                          ()
# 324 "parser.ml"
               : 'statement))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'ioStatement) in
    Obj.repr(
# 66 "parser.mly"
               ()
# 331 "parser.ml"
               : 'statement))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'compoundStatement) in
    Obj.repr(
# 67 "parser.mly"
                     ()
# 338 "parser.ml"
               : 'statement))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'ifStatement) in
    Obj.repr(
# 68 "parser.mly"
               ()
# 345 "parser.ml"
               : 'statement))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'whileStatement) in
    Obj.repr(
# 69 "parser.mly"
                  ()
# 352 "parser.ml"
               : 'statement))
; (fun __caml_parser_env ->
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'expression) in
    Obj.repr(
# 70 "parser.mly"
                                             ()
# 359 "parser.ml"
               : 'assignStatement))
; (fun __caml_parser_env ->
    let _2 = (Parsing.peek_val __caml_parser_env 5 : 'condition) in
    let _5 = (Parsing.peek_val __caml_parser_env 2 : 'statement) in
    Obj.repr(
# 72 "parser.mly"
                                                                 ()
# 367 "parser.ml"
               : 'whileStatement))
; (fun __caml_parser_env ->
    let _3 = (Parsing.peek_val __caml_parser_env 1 : 'expression) in
    Obj.repr(
# 74 "parser.mly"
                                                 ()
# 374 "parser.ml"
               : 'ioStatement))
; (fun __caml_parser_env ->
    let _3 = (Parsing.peek_val __caml_parser_env 1 : 'expression) in
    Obj.repr(
# 75 "parser.mly"
                                          ()
# 381 "parser.ml"
               : 'ioStatement))
; (fun __caml_parser_env ->
    let _2 = (Parsing.peek_val __caml_parser_env 5 : 'condition) in
    let _5 = (Parsing.peek_val __caml_parser_env 2 : 'statement) in
    Obj.repr(
# 77 "parser.mly"
                                                             ()
# 389 "parser.ml"
               : 'ifStatement))
; (fun __caml_parser_env ->
    let _2 = (Parsing.peek_val __caml_parser_env 9 : 'condition) in
    let _5 = (Parsing.peek_val __caml_parser_env 6 : 'statement) in
    let _9 = (Parsing.peek_val __caml_parser_env 2 : 'statement) in
    Obj.repr(
# 78 "parser.mly"
                                                                                     ()
# 398 "parser.ml"
               : 'ifStatement))
; (fun __caml_parser_env ->
    Obj.repr(
# 80 "parser.mly"
               ()
# 404 "parser.ml"
               : 'relation))
; (fun __caml_parser_env ->
    Obj.repr(
# 81 "parser.mly"
           ()
# 410 "parser.ml"
               : 'relation))
; (fun __caml_parser_env ->
    Obj.repr(
# 82 "parser.mly"
              ()
# 416 "parser.ml"
               : 'relation))
; (fun __caml_parser_env ->
    Obj.repr(
# 83 "parser.mly"
                 ()
# 422 "parser.ml"
               : 'relation))
; (fun __caml_parser_env ->
    Obj.repr(
# 84 "parser.mly"
            ()
# 428 "parser.ml"
               : 'relation))
; (fun __caml_parser_env ->
    Obj.repr(
# 85 "parser.mly"
             ()
# 434 "parser.ml"
               : 'relation))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'expression) in
    let _2 = (Parsing.peek_val __caml_parser_env 1 : 'relation) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'expression) in
    Obj.repr(
# 86 "parser.mly"
                                          ()
# 443 "parser.ml"
               : 'condition))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'expression) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'term) in
    Obj.repr(
# 88 "parser.mly"
                                 ()
# 451 "parser.ml"
               : 'expression))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'expression) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'term) in
    Obj.repr(
# 89 "parser.mly"
                           ()
# 459 "parser.ml"
               : 'expression))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'term) in
    Obj.repr(
# 90 "parser.mly"
          ()
# 466 "parser.ml"
               : 'expression))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'term) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'factor) in
    Obj.repr(
# 92 "parser.mly"
                           ()
# 474 "parser.ml"
               : 'term))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 2 : 'term) in
    let _3 = (Parsing.peek_val __caml_parser_env 0 : 'factor) in
    Obj.repr(
# 93 "parser.mly"
                         ()
# 482 "parser.ml"
               : 'term))
; (fun __caml_parser_env ->
    let _1 = (Parsing.peek_val __caml_parser_env 0 : 'factor) in
    Obj.repr(
# 94 "parser.mly"
             ()
# 489 "parser.ml"
               : 'term))
; (fun __caml_parser_env ->
    let _2 = (Parsing.peek_val __caml_parser_env 1 : 'expression) in
    Obj.repr(
# 96 "parser.mly"
                                      ()
# 496 "parser.ml"
               : 'factor))
; (fun __caml_parser_env ->
    Obj.repr(
# 97 "parser.mly"
                   ()
# 502 "parser.ml"
               : 'factor))
; (fun __caml_parser_env ->
    Obj.repr(
# 98 "parser.mly"
                 ()
# 508 "parser.ml"
               : 'factor))
(* Entry program *)
; (fun __caml_parser_env -> raise (Parsing.YYexit (Parsing.peek_val __caml_parser_env 0)))
|]
let yytables =
  { Parsing.actions=yyact;
    Parsing.transl_const=yytransl_const;
    Parsing.transl_block=yytransl_block;
    Parsing.lhs=yylhs;
    Parsing.len=yylen;
    Parsing.defred=yydefred;
    Parsing.dgoto=yydgoto;
    Parsing.sindex=yysindex;
    Parsing.rindex=yyrindex;
    Parsing.gindex=yygindex;
    Parsing.tablesize=yytablesize;
    Parsing.table=yytable;
    Parsing.check=yycheck;
    Parsing.error_function=parse_error;
    Parsing.names_const=yynames_const;
    Parsing.names_block=yynames_block }
let program (lexfun : Lexing.lexbuf -> token) (lexbuf : Lexing.lexbuf) =
   (Parsing.yyparse yytables 1 lexfun lexbuf : unit)
;;
