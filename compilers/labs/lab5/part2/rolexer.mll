{
open Parser
}
let digit = ['0'-'9']
let id = '_'['a'-'z' 'A'-'Z']+
let constant = ['-''+']?(['0'-'9']|['1'-'9']['0'-'9']+)|"adevarat"|"fals"|'"'['a'-'z''A'-'Z''0'-'9']
rule token = parse
| [' ' '\t'] { token lexbuf }
|'\n' { NEWLINE }
|"sir" { SIR } 
|"fa"  { FA }
|"atunci" { ATUNCI }
|"altfel" { ALTFEL }
|"daca" { DACA }
|"citeste" { CITESTE }
|"scrie" { SCRIE }
|"caracter" { CARACTER }
|"sfarsit" { SFARSIT }
|"inceput" { INCEPUT }
|"cat_timp" { CAT_TIMP }
|"bool" { BOOL }
|"intreg" { INTREG }
| id { IDENTIFIER }
| constant {CONSTANT}
| '+' { PLUS } 
| '-' { MINUS }
| '*' { MULTIPLY } 
| '/' { DIVIDE }
|"<" { LESS }
|"<=" { LESS_EQUAL }
|"==" { EQ_EQUAL }
|"=>" { GREATER_EQUAL }
|'>' { GREATER }
|"!=" { NOT EQUAL }
|"=" { EQUAL }
|'[' { LEFT_SQB }
|']' { RIGHT_SQB }
|'(' { LEFT_PAR }
|')' { RIGHT_PAR }
|',' { COMMA }
| _ as c { token lexbuf}
| eof  { raise End_of_file }
