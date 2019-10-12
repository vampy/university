program
program, declarationList, declaration, typeDeclaration, arrayDeclaration, type, compoundStatement, statementList, statement, assignStatement, expression, term, factor, ioStatement, ifStatement, whileStatement, condition, relation
0, 1, 3, 4, 5, 6, 10, 11, 12, 13, 14, 15, 16, 20, 21, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41


program -> declarationList 25 compoundStatement
declarationList -> declaration | declarationList 25 declaration
declaration -> type 0

type -> typeDeclaration | arrayDeclaration
typeDeclaration -> 4 | 5 | 6
arrayDeclaration -> 3 typeDeclaration

compoundStatement -> 12 25 statementList 25 13 25
statementList -> statement | statementList 25 statement
statement -> assignStatement | ioStatement | compoundStatement | ifStatement | whileStatement
assignStatement -> 0 40 expression
whileStatement -> 10 condition 11 25 statementList 25 13
ioStatement -> 20 28 expression 29 | 21 28 expression 29
ifStatement -> 14 condition 15 25 statementList 25 13 | 14 condition 15 25 statementList 25 16 25 statementList 25 13
relation -> 30 |  31 | 32 | 33 | 34 | 35
condition -> expression relation factor

expression -> expression 38 term | expression 39 term | term
term -> term 36 factor | term 37 factor | factor
factor -> 28 expression 29 | 0 | 1
