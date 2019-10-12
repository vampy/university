val getTypeOfVar_exn: Syntax.id -> Syntax.typeValue Environment.t -> Syntax.typ
val getTypeOfVal: Syntax.value -> Syntax.typ
val getTypeField: Syntax.typ -> Syntax.id -> Syntax.program -> Syntax.typ option
val getMethods: Syntax.typ -> Syntax.program -> Syntax.methodDeclaration list
val getMethodDefinition: Syntax.typ -> Syntax.id -> Syntax.program -> Syntax.methodDeclaration option
val methodName: Syntax.methodDeclaration -> Syntax.id

val leastMaxType: Syntax.typ -> Syntax.typ -> Syntax.program -> Syntax.typ option

val initValue: Syntax.typ -> Syntax.value
val isValue: Syntax.exp -> bool
val isLocation: Syntax.value -> bool
val isIntOperator: Syntax.binaryOperator -> bool
val isFloatOperator: Syntax.binaryOperator -> bool
val isCompOperator: Syntax.binaryOperator -> bool
val isBoolOperator: Syntax.binaryOperator -> bool
val isObjectType: Syntax.typ -> bool
val isDefinedInProg: Syntax.id -> Syntax.program -> bool
val isTypeDeclared: Syntax.typ -> Syntax.program -> bool
val isSubtype: Syntax.typ -> Syntax.typ -> Syntax.program -> bool
val eachElementOnce_exn: 'a list -> unit


val firstUnboundVariable: Syntax.id list -> Syntax.typeValue Environment.t -> Syntax.id option
val getFieldList: Syntax.typ -> Syntax.program-> (Syntax.id * Syntax.typ) list
val getTypeList: Syntax.id list -> Syntax.typeValue Environment.t -> Syntax.typ list
val checkFieldsTypes: (Syntax.id * Syntax.typ) list -> Syntax.typ list -> Syntax.program -> Syntax.id option
val createFieldEnv: (Syntax.id * Syntax.typ) list -> Syntax.id list -> Syntax.typeValue Environment.t -> Syntax.typeValue Environment.t
val getParent: Syntax.typ -> Syntax.program -> Syntax.typ

val substVariableName: Syntax.id -> Syntax.id -> Syntax.exp -> Syntax.exp

val compareValues: [> `Float of float | `Int of int ] -> [> `Float of float | `Int of int ] -> Syntax.binaryOperator-> Syntax.value

val stringOfType: Syntax.typ -> string
val stringListOfIdTypList: (Syntax.id * Syntax.typ) list -> string list
val stringOfValue: Syntax.value -> string
val stringOfEnv: Syntax.typeValue Environment.t -> string
val stringOfExp: Syntax.exp -> string
