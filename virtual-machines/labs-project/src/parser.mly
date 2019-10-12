%token <int> INT
%token <float> FLOAT
%token <bool> BOOL
%token <string> ID
%token CLASS
%token EXTENDS
%token NEW
%token WHILE
%token IF
%token ELSE
/*%token MAIN*/

%token TINT
%token TFLOAT
%token TBOOL
%token TVOID

%token NULL
%token LEFT_BRACE
%token RIGHT_BRACE
/*%token LEFT_BRACK*/
/*%token RIGHT_BRACK*/
%token OPARENT
%token CPARENT
/*%token COLON*/
%token SEMICOLON
%token DOT
%token COMMA
%token HASHTAG
%token AND
%token OR
%token NOT
%token FPLUS
%token FMINUS
%token FMULTIPLY
%token FDIVIDE
%token IPLUS
%token IMINUS
%token IMULTIPLY
%token IDIVIDE
%token LESS
%token LESS_EQUAL
%token EQ_EQUAL
%token GREATER_EQUAL
%token GREATER
%token NOT_EQUAL
%token EQUAL
%token INSTANCEOF
%token EOF


%left EQUAL
%right OR
%right AND
%left LESS LESS_EQUAL EQ_EQUAL GREATER_EQUAL GREATER NOT_EQUAL
%left IPLUS IMINUS FPLUS FMINUS
%left IMULTIPLY IDIVIDE FMULTIPLY FDIVIDE
%{ open Syntax %}
%start <Syntax.program option> program
%%

program:
| clist = classDeclarationList {Some (Program(clist))}
;

classDeclarationList:
| EOF  {[]}
| classDeclaration classDeclarationList {$1 :: $2}
;

classDeclaration:
| CLASS obj = ID LEFT_BRACE RIGHT_BRACE {Class(obj, "Object", [], [])}
| CLASS obj = ID EXTENDS parent = ID LEFT_BRACE fields = fieldList HASHTAG methods = methodList RIGHT_BRACE {Class(obj, parent, fields, methods)}
;

fieldList:
| (* empty *) {[]}
| fieldDeclaration fieldList {$1 :: $2}
;

fieldDeclaration:
| varDecl {$1}
;

methodList:
| (* empty *) {[]}
| methodDeclaration methodList {$1 :: $2}
;


methodDeclaration:
| typeD ID OPARENT methodParameterList CPARENT be = blockExpression  {Method($1, $2, $4, be)}
;
methodParameterList:
|(* empty *) {[]}
| methodParameterListAux {$1}
;

methodParameterListAux:
| methodParameter {[$1]}
| methodParameter COMMA methodParameterListAux {$1 :: $3}
;

methodParameter: typeD ID {($2, $1)}
;

blockExpression:
| LEFT_BRACE vars = varDeclList HASHTAG e = seqExpression RIGHT_BRACE {BlockExpression(vars, e) }
;
varDeclList:
| (* empty *) {[]}
| varDeclListAux {$1}
;

varDeclListAux:
| varDecl {[$1]}
| varDecl varDeclListAux { $1 :: $2}

varDecl:
| TINT ID SEMICOLON  {($2, IntType)}
| TFLOAT ID SEMICOLON {($2, FloatType)}
| TBOOL ID SEMICOLON {($2, BoolType)}
| TVOID ID SEMICOLON {($2, VoidType)}
| ID ID SEMICOLON {($2, ObjectType($1))}
;

/*Add semicolon at the end of some expressions*/
expression:
| INT       {Value(IntV($1))}
| FLOAT     {Value(FloatV($1))}
| BOOL      {Value(BoolV($1))}
| NULL      {Value(NullV)}
| ID        {Variable($1)}

| ID DOT ID {ObjectField($1, $3)}
| ID DOT ID EQUAL expression {ObjectFieldAssignment(($1, $3), $5)}

| ID EQUAL expression {VariableAssignment($1, $3)}
| IF OPARENT guard = ID CPARENT thenExp = groupExpression ELSE elseExp = groupExpression {If(guard, thenExp, elseExp)}
| operationExpression {$1}

| OPARENT NOT expression CPARENT {Negation($3)}
| NEW ID OPARENT argsList CPARENT {New($2, $4)}
| ID DOT ID OPARENT argsList CPARENT {MethodCall($1, $3, $5)}
| WHILE OPARENT guard = ID CPARENT be = blockExpression {While(guard, be)}
/*modify these 2 productions to support also non primitive types.*/
| OPARENT ID CPARENT ID {Cast($2, $4)}
| ID INSTANCEOF ID {InstanceOf($1, $3)}
/*| blockExpression {$1}*/
;

groupExpression:
| e = expression { e }
| OPARENT e = seqExpression CPARENT { e }
;

operationExpression:
| exp1 = expression IPLUS exp2 = expression {Operation(exp1, IPlus, exp2)}
| exp1 = expression FPLUS exp2 = expression {Operation(exp1, FPlus, exp2)}
| exp1 = expression IMINUS exp2 = expression {Operation(exp1, IMinus, exp2)}
| exp1 = expression FMINUS exp2 = expression {Operation(exp1, FMinus, exp2)}
| exp1 = expression IMULTIPLY exp2 = expression {Operation(exp1, IMultiply, exp2)}
| exp1 = expression FMULTIPLY exp2 = expression {Operation(exp1, FMultiply, exp2)}
| exp1 = expression IDIVIDE exp2 = expression {Operation(exp1, IDivide, exp2)}
| exp1 = expression FDIVIDE exp2 = expression {Operation(exp1, FDivide, exp2)}
| exp1 = expression EQ_EQUAL exp2 = expression {Operation(exp1, EqEqual, exp2)}
| exp1 = expression GREATER_EQUAL exp2 = expression {Operation(exp1, GreaterEqual, exp2)}
| exp1 = expression GREATER exp2 = expression {Operation(exp1, Greater, exp2)}
| exp1 = expression LESS exp2 = expression {Operation(exp1, Less, exp2)}
| exp1 = expression LESS_EQUAL exp2 = expression {Operation(exp1, LessEqual, exp2)}
| exp1 = expression NOT_EQUAL exp2 = expression {Operation(exp1, NotEqual, exp2)}
| exp1 = expression AND exp2 = expression {Operation(exp1, And, exp2)}
| exp1 = expression OR exp2 = expression {Operation(exp1, Or, exp2)}
;

seqExpression:
| expression {$1}
| expression SEMICOLON  {$1}
| e1 = expression SEMICOLON e2 = seqExpression {Sequence(e1, e2)}
;

argsList:
| /*empty*/ {[]}
| ID {[$1]}
| ID COMMA argsList {$1 :: $3}
;

typeD:
| TINT {IntType}
| TFLOAT {FloatType}
| TBOOL {BoolType}
| TVOID {VoidType}
;
