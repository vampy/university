val typeCheckProgram: Syntax.program -> unit
val typeCheckExp: Syntax.exp -> Syntax.typ Environment.t-> Syntax.program -> Syntax.typ
val typeCheckObjectFieldExp: Syntax.id -> Syntax.id -> Syntax.typ Environment.t -> Syntax.program -> Syntax.typ
val typeCheckVariableAssignmentExp: Syntax.id -> Syntax.exp -> Syntax.typ Environment.t -> Syntax.program -> Syntax.typ

val goodInheritance_exn: Syntax.classDeclaration -> Syntax.program -> unit
