
%{
open Printf
let set_trace = 
  true
%}
%token IDENTIFIER
%token CONSTANT
%token NEWLINE
%token ARRAY
%token INT
%token CHAR
%token FLOAT
%token WHILE
%token DO
%token BEGIN
%token END
%token IF
%token THEN
%token ELSE
%token INPUT
%token PRINT
%token LEFT_SQB
%token RIGHT_SQB
%token LEFT_PAR
%token RIGHT_PAR
%token LESS
%token GREATER
%token LESS_EQUAL
%token GREATER_EQUAL
%token EQ_EQUAL
%token NOT_EQUAL
%token MULTIPLY
%token DIVIDE
%token PLUS
%token MINUS
%token EQUAL
%token COMMA
%token EOF

%start program
%type <unit> program
%%

program: declarationList NEWLINE compoundStatement {}
  ;
declarationList: declaration {}
		|declarationList NEWLINE declaration {}
  ;
declaration: typeD IDENTIFIER {};
typeD: typeDeclaration {}
    | arrayDeclaration {}
  ;
typeDeclaration: INT {}
	      |CHAR {}
	      |FLOAT {} 
	      ;
arrayDeclaration: ARRAY typeDeclaration {}
		;
compoundStatement: BEGIN NEWLINE statementList NEWLINE END NEWLINE {}
		 ;
statementList: statement {} 
	      |statementList NEWLINE statement {}
	      ;
statement:assignStatement {} 
	| ioStatement {}
	| compoundStatement {} 
	| ifStatement {}
	| whileStatement {};
assignStatement: IDENTIFIER EQUAL expression {}
		;
whileStatement: WHILE condition DO NEWLINE statement NEWLINE END {}
		;
ioStatement: INPUT LEFT_PAR expression RIGHT_PAR {} 
	   | PRINT LEFT_PAR expression RIGHT_PAR {}
	   ;
ifStatement: IF condition THEN NEWLINE statement NEWLINE END {}
	   | IF condition THEN NEWLINE statement NEWLINE ELSE NEWLINE statement NEWLINE END {}
	   ;
relation: LESS {}
	| GREATER {} 
	| LESS_EQUAL {}
	| GREATER_EQUAL {}
	| EQ_EQUAL {}
	| NOT_EQUAL {};
condition: expression relation expression {};

expression: expression PLUS term {}
	  | expression MINUS term {}
	  | term {}
	  ;
term: term MULTIPLY factor {}
    | term DIVIDE factor {}
    | factor {}
    ;
factor: LEFT_PAR expression RIGHT_PAR {}
      | IDENTIFIER {}
      | CONSTANT {}
      ;
%%