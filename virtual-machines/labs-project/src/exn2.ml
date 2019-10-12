open Core.Std


exception StaticError of string
exception RuntimeError of string
exception BadFoundedClassesError of string
exception DuplicateElement of int
exception DuplicateField of (Syntax.id * Syntax.typ)
exception DuplicateMethod of Syntax.methodDeclaration
exception BadMethodOverriding of Syntax.methodDeclaration
exception BadTypedMethod of Syntax.methodDeclaration
exception IncompatibleTypes of (Syntax.exp * Syntax.typ * Syntax.typ)

let raiseStaticError (msg : string) = raise (StaticError msg)
let raiseRuntimeError (msg : string) = raise (RuntimeError msg)

let raiseUnboundVar var =  raise(RuntimeError ("Error 3: Unbound variable "^ var))

let raiseDifferentTypeExpErr exp expectedTypes actualType =
  let types = List.map expectedTypes ~f:(fun x -> Syntax.show_typ x) in
  let length = List.length types in
  let stringTypes = List.foldi types ~f:(fun i acc x -> if i = length - 1 then acc ^ x
                                          else if i = length -2 then acc ^ x ^ " or " else acc ^ x ^ ", ") ~init:"" in
  raise(RuntimeError ("Error 5: Expression " ^ (Syntax.show_exp exp) ^
                      " has type " ^ (Syntax.show_typ actualType) ^ " but an expression was expected of type " ^
                      stringTypes ))
