open Syntax
open Core.Std
open StringTupleSet
open Exn2

let rec typeCheckProgram program =
  try
    wellFoundedClasses program;
    let Program classList = program in
    List.iter classList (fun x -> methodsOnce_exn x);
    List.iter classList (fun x -> fieldsOnce_exn x);
    List.iter classList (fun x -> goodInheritance_exn x program);
    List.iter classList (fun x -> wellTypedClass_exn x program)
  with
  | DuplicateField (fn, tp) -> raise (StaticError ("Field " ^ fn ^ " is declared more than once"))
  | DuplicateMethod Method(mt ,mn, _, _) -> raise (StaticError ("Method " ^ mn ^ " is declared more than once"))
  | BadMethodOverriding Method(mt, mn, _, _) -> raise (StaticError ("Overriding method  " ^ mn ^ " is not sound"))
  | BadTypedMethod Method(mt, mn, _, _) -> raise (StaticError ("Method " ^ mn ^ " is not well typed."))
  | BadFoundedClassesError msg -> raise (StaticError msg)

(** Checks so that there are no duplicate method declarations in the class. *)
and methodsOnce_exn class_decl = let Class(_, _, _, methods) = class_decl in
  let methodsNames = List.map methods ~f:(function Method(_, n, _, _) -> n) in
  try
    Utils.eachElementOnce_exn methodsNames
  with
    DuplicateElement index -> raise (DuplicateMethod (List.nth_exn methods index))

(** Checks so that there are no duplicate fields declarations in the class. *)
and fieldsOnce_exn class_decl = let Class(_, _, fields, _) = class_decl in
  let fieldsNames = List.map fields ~f:(function (n, _) -> n) in
  try
    Utils.eachElementOnce_exn fieldsNames
  with
    DuplicateElement index -> raise (DuplicateField (List.nth_exn fields index))

(** Checks if the `m1` can override the method `m2`.
    This means that both methods have the same arguments and
    the return type of `m1` is a subtype of the return type of `m2`.
    Note: Does not check if the methods have the same name. This is just an auxiliary function. *)
and goodOverride m1 m2 prog = match m1, m2 with
  | Method(t1, _, args1, e1), Method(t2, _, args2, e2) -> args1 = args2 && Utils.isSubtype t1 t2 prog

(** Checks if the methods if this class correctly override those of the parent class. *)
and goodInheritance_exn class_decl prog = let Class(cn, pn, _, methods) = class_decl in
  let parentMethods = Utils.getMethods (ObjectType pn) prog in
  List.iter methods ~f:(fun m ->
      List.iter parentMethods ~f:(fun mp ->
          if Utils.methodName m = Utils.methodName mp then
            if goodOverride m mp prog then ()
            else raise(BadMethodOverriding m)
          else ()))

(** Checks if there no duplicate definitions of classes, no cycle in the classes hierarchy
    and the last class is the Main class with the main method. *)
and wellFoundedClasses prog = let Program classList = prog in
  (* Contains the set of (class name, parent name) tuples *)
  let set_class_parent = List.fold classList ~init:StringTupleSet.empty
      ~f:(fun acc cl -> let Class(c, p, _, _) = cl in StringTupleSet.add acc (c, p)) in

  (* Contains the set of (class name, class name) tuples. Used for checking for cycles in the class hierarchy
     by intersecting with the transitive closure. *)
  let set_class_class = List.fold classList ~init:StringTupleSet.empty
      ~f:(fun acc cl -> let Class(c, _, _, _) = cl in StringTupleSet.add acc (c, c)) in

  let set_class_parent = transitiveClosure set_class_parent in
  let inter = StringTupleSet.inter set_class_parent set_class_class in
  (* No path that leads back to itself, yay no cycles ;) *)
  if StringTupleSet.length inter = 0 then
    (* Find duplicates of the same class name *)
    let classNames = List.map classList ~f:(function Class(class_name, _ ,_ , _) -> class_name) in
    let dup = List.find_a_dup classNames in
    match dup with
    | None -> checkLastClass classList
    | Some d -> raise (BadFoundedClassesError ("Error: Redefinition of class " ^ d))
  else
    raise (BadFoundedClassesError ("Error: There is a cycle in the class hierarchy"))

(** Build the string tuple set of all class paths that can go from any starting point. *)
and transitiveClosure ir =
  let newIR = StringTupleSet.fold ir ~init:ir ~f:(fun acc pair ->
      let cn1, cn2 = pair in
      let newPairs = StringTupleSet.fold ir ~init:StringTupleSet.empty ~f:(fun acc2 pair ->
          let cn3, cn4 = pair in
          (* The parent class cn2 is the same with cn3 then it means there exists a path from cn1 to cn4 *)
          if cn2 = cn3 then StringTupleSet.add acc2 (cn1, cn4)
          else acc2) in
      (* Accumulate results *)
      StringTupleSet.union newPairs acc) in

  (* Until no modifications can be done *)
  if StringTupleSet.equal ir newIR then newIR
  else transitiveClosure newIR

(** Checks the last class so that it has the name `Main` and the last method is `void main` *)
and checkLastClass classList = match List.last classList with
  | None -> ()
  | Some Class(n, _, _, methods) -> if n = "Main" then begin
      match List.last methods with
      | None -> raise (BadFoundedClassesError "Error: The main class has no methods.")
      | Some Method(mt, mn, args, e) -> if mn = "main" then
          if mt = VoidType then ()
          else raise (BadFoundedClassesError ("Error: The main method has " ^ Utils.stringOfType mt
                                              ^ " return type but was expected of return type " ^ Utils.stringOfType VoidType))
        else raise (BadFoundedClassesError "Error: There is no main method inside Main class.")
    end
    else
      raise (BadFoundedClassesError ("Error: Name of the last class has to be \"Main\"." ^ n))

(** Checks if the methods of the class are well typed.
    Meaning that it will apply `wellTypedMethod` to all methods of this class. *)
and wellTypedClass_exn class_decl prog = let Class(cn, _, _, methods) = class_decl in
  (* (this, cn) is the initial type enviroment *)
  let tenv = Environment.extend "this" (ObjectType cn) Environment.empty in
  List.iter methods (fun m -> if wellTypedMethod m tenv prog then () else raise(BadTypedMethod m))

(** Type check the method body and see if it's type is a subtype of the method return type. *)
and wellTypedMethod m tenv prog = let Method(method_type, _, args, body) = m in
  (* Add the arguments of the method to the type enviroment *)
  let newTE = Environment.union args tenv in
  let body_type = typeCheckExp body newTE prog in
  Utils.isSubtype body_type method_type prog

(** Type check an expression.
    The type environment is a dictionary containing mappings from the variable name
    to the type associated to that variable. It is usually denoted in this file by `tenv`. *)
and typeCheckExp exp tenv prog = match exp with
  | Value(v) -> typeCheckValueExp v
  | Variable(id) -> typeCheckVariableExp id tenv
  | ObjectField(var, field) -> typeCheckObjectFieldExp var field tenv prog
  | VariableAssignment(id, exp) -> typeCheckVariableAssignmentExp id exp tenv prog
  | ObjectFieldAssignment((var, f), e) -> typeCheckObjectFieldAssignmentExp var f e tenv prog
  | Sequence(e1, e2) -> typeCheckSequenceExp e1 e2 tenv prog
  | BlockExpression(list, exp) -> typeCheckBlockExp list exp tenv prog
  | If (id, et, ee) -> typeCheckIfExp id et ee tenv prog
  | Operation(e1, op, e2) -> typeCheckOperationExp e1 e2 op tenv prog
  | Negation(e) -> typeCheckNegationExp e tenv prog
  | New(cn, varList) -> typeCheckNewExp cn varList tenv prog
  | While(var, e) -> typeCheckWhileExp var e tenv prog
  | Cast(cn, var) -> typeCheckCastExp cn var tenv prog
  | InstanceOf(var, cn) -> typeCheckInstanceOfExp var cn tenv prog
  | MethodCall(cn, mn, params) -> typeCheckMethodCallExp cn mn params tenv prog
  | Ret(v, exp) -> raiseStaticError ("Expression 'Ret' should not occur while type checking.")

(** Simply return the type of the value. *)
and typeCheckValueExp = function
  | IntV _ -> IntType
  | FloatV _ -> FloatType
  | BoolV _ -> BoolType
  | VoidV -> VoidType
  | NullV -> NullType
  | LocV _ -> LocType

(** Checks if the `class_name` is defined in the program. *)
and typeCheckClassName class_name prog = if Utils.isDefinedInProg class_name prog then ObjectType class_name
  else raiseStaticError ("Unbound class " ^ class_name)

(** Checks if the variable `id` exists in the type enviroment. *)
and typeCheckVariableExp id tenv = if Environment.isIn id tenv then Environment.lookup id tenv
  else raiseUnboundVar id

(** Check if `var` is an object and if the `field` exists in that object type.
    Eg: var.field *)
and typeCheckObjectFieldExp var field tenv prog =
  let varType = typeCheckVariableExp var tenv in
  if Utils.isObjectType varType then
    let fieldType = Utils.getTypeField varType field prog in
    if Option.is_some fieldType then Option.value_exn fieldType
    else raiseStaticError ("Field " ^ field ^ " not declared inside " ^ var)
  else raiseStaticError ("Variable " ^ var ^ " is not an object." )

(** Check if `id` can be assigned the value of `exp` by checking if the `id` type is a subtype of the `exp` type.
    Eg: id = exp *)
and typeCheckVariableAssignmentExp id exp tenv prog = let varType = typeCheckVariableExp id tenv in
  let expType = typeCheckExp exp tenv prog in
  if Utils.isSubtype expType varType prog then VoidType
  else raiseStaticError ("Type of " ^ id ^ "(" ^ (Utils.stringOfType varType) ^ ") is incompatible with " ^ (Utils.stringOfType expType))

(** Check if the `exp` type is a subtype of the `field` type.
    Eg: var.field = exp *)
and typeCheckObjectFieldAssignmentExp var field exp tenv prog =
  let fieldType = typeCheckObjectFieldExp var field tenv prog in
  let expType = typeCheckExp exp tenv prog in
  if Utils.isSubtype expType fieldType prog then VoidType
  else raiseStaticError ("Type of " ^ var ^ "." ^ field ^ "(" ^ (Utils.stringOfType fieldType) ^ ") is incompatible with " ^ (Utils.stringOfType expType))

(** Just check both expression. *)
and typeCheckSequenceExp e1 e2 tenv prog = let _ = typeCheckExp e1 tenv prog in typeCheckExp e2 tenv prog

(** The variables defined at the top of the block expression are now part of the type enviroment.
    Check the block expression. *)
and typeCheckBlockExp list exp tenv prog = let newTE = Environment.union list tenv in typeCheckExp exp newTE prog

(** Check if the condition is a subtype of bool and the `then` and `else` branch have at least a common type.
    Eg: if (var) { exp_then } else { exp_else } *)
and typeCheckIfExp var exp_then exp_else tenv prog =
  let varType = typeCheckVariableExp var tenv in
  if Utils.isSubtype varType BoolType prog then
    let exp_then_type = typeCheckExp exp_then tenv prog in
    let exp_else_type = typeCheckExp exp_else tenv prog in
    let lmt = Utils.leastMaxType exp_then_type exp_else_type prog in
    match lmt with Some t -> t
                 | None -> raiseStaticError ("Type " ^ (Utils.stringOfType exp_then_type) ^ " is not compatible with type " ^ (Utils.stringOfType exp_else_type))
  else raiseStaticError ("Variable " ^ var ^ " has type " ^ (Utils.stringOfType varType) ^ " but a variable was expected of type " ^ (Utils.stringOfType BoolType) )

(** Check if operations are perfomed on the same data types. Aka `e1` and `e2`.
    Eg: 34 + 54, 32.4 > 42.33 *)
and typeCheckOperationExp e1 e2 op tenv prog =
  if Utils.isIntOperator op then typeCheckSpecOperation e1 e2 IntType tenv prog
  else if Utils.isFloatOperator op then typeCheckSpecOperation e1 e2 FloatType tenv prog
  else if Utils.isBoolOperator op then typeCheckSpecOperation e1 e2 BoolType tenv prog
  else (* op is compOperator *)  typeCheckCompOperation e1 e2 tenv prog

(** Check if `e1` and `e2` are subtype of `typ`
    Eg: 25 - 45, true && false *)
and typeCheckSpecOperation e1 e2 typ tenv prog =
  let e1t = typeCheckExp e1 tenv prog in
  if Utils.isSubtype e1t typ prog then
    let e2t = typeCheckExp e2 tenv prog in
    if Utils.isSubtype e2t typ prog then typ
    else raiseDifferentTypeExpErr e2 [e2t] typ
  else raiseDifferentTypeExpErr e1 [e1t] typ

(** Check comparison.
    TODO: reconsider type checking for this case
    Eg: 100 >= 101 *)
and typeCheckCompOperation e1 e2 tenv prog =
  let e1t = typeCheckExp e1 tenv prog in
  let e2t = typeCheckExp e2 tenv prog in
  if (Utils.isSubtype e1t e2t prog) && (Utils.isSubtype e2t e1t prog)
     && (not (Utils.isObjectType e1t)) && (not (Utils.isObjectType e2t)) then BoolType
  else raiseStaticError (sprintf "Can not compare an expression of type `%s` with an expression of type `%s`" (Utils.stringOfType e1t) (Utils.stringOfType e2t))

(** Check if the `exp` is a subtype of bool
    Eg: !true *)
and typeCheckNegationExp exp tenv prog =
  let et = typeCheckExp exp tenv prog in
  if Utils.isSubtype et BoolType prog then BoolType
  else raiseDifferentTypeExpErr exp [et] BoolType

(** Check if we can cast `var` to type `class_name` by checking if one is the subtype of the other
    Eg: (class_name)var *)
and typeCheckCastExp class_name var tenv prog =
  let cnType = typeCheckClassName class_name prog in
  let varType = typeCheckVariableExp var tenv in
  if (Utils.isSubtype cnType varType prog) || (Utils.isSubtype varType cnType prog) then cnType
  else raiseStaticError (sprintf "Can not cast variable `%s` of type `%s` to type `%s`" var (Utils.stringOfType varType) (Utils.stringOfType cnType))

(** Checks if `var` and `class_name` exists in the program.
    Eg: var instanceof class_name *)
and typeCheckInstanceOfExp var class_name tenv prog =
  let _ = typeCheckClassName class_name prog in
  let _ = typeCheckVariableExp var tenv in BoolType

(** Check if the arguments of the new expression are subtypes of the class fields (aka constructor).
    Eg: new class_name(varList) *)
and typeCheckNewExp class_name varList tenv prog =
  let class_name_type = typeCheckClassName class_name prog in
  let fieldList = Utils.getFieldList class_name_type prog in
  (* Get the types of the arguments of the constructor *)
  let varTypes = List.map varList (fun var -> typeCheckVariableExp var tenv) in
  try
    (* All must be a subtype of their respective parameter. *)
    if List.for_all2_exn fieldList varTypes (fun field var_type -> match field with (_, field_typ) -> Utils.isSubtype var_type field_typ prog) then
      class_name_type
    else
      raise (Invalid_argument ".")
  with
    Invalid_argument _ -> raiseStaticError ("Number of arguments is not equal with the number of fields")

(** Check if the condition is of type bool and check the body of the while.
    Eg: while (var) { body_exp } *)
and typeCheckWhileExp var body_exp tenv prog =
  let varType = typeCheckVariableExp var tenv in
  if Utils.isSubtype varType BoolType prog then
    let _ = typeCheckExp body_exp tenv prog in
    VoidType
  else
    raiseDifferentTypeExpErr (Variable var) [varType] BoolType

(** Checks if `var`, `mehtod_name` exist and the `params` of agree with the params in the class.
    Eg: var.method_name(params) *)
and typeCheckMethodCallExp var method_name params tenv prog =
  let varType = typeCheckVariableExp var tenv in
  (* TODO check here for primitive types? *)
  if Utils.isTypeDeclared varType prog then
    let methodDecl = Utils.getMethodDefinition varType method_name prog in
    (* Method exists *)
    if Option.is_some methodDecl then
      (* Check parameters number and types. *)
      let Method(return_type, _, idTypLst, _) = Option.value_exn methodDecl in
      let paramTypes = List.map params (fun x -> typeCheckVariableExp x tenv)  in
      try
        (* All the parameters must be a subtype of the method signature params. *)
        let validParams = List.for_all2_exn paramTypes idTypLst
            (fun paramType -> function (_, typ) -> Utils.isSubtype paramType typ prog) in
        if validParams then return_type
        else raiseStaticError (sprintf "Types of the parameters are not valid in method `%s`." method_name)
      with
        Invalid_argument _ -> raiseStaticError (sprintf "Number of passed parameters to method `%s` is not valid." method_name)
    else raiseStaticError (sprintf "Method `%s` is not defined inside" method_name)
  else raiseStaticError ((Utils.stringOfType varType) ^ " not declared inside program.")
