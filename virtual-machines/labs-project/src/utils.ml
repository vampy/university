open Syntax
open Core.Std
open Exn2

let stringOfType = function
  | IntType -> "int"
  | FloatType -> "float"
  | BoolType -> "bool"
  | VoidType -> "void"
  | NullType -> "NullType"
  | LocType -> "LocType"
  | ObjectType(obj) -> obj

let rec stringListOfIdTypList = function
  | []-> []
  | hd :: tl -> match hd with
    | (id, typ) -> ((stringOfType typ) ^ " " ^ id) :: stringListOfIdTypList tl

let stringOfValue = function
  | NullV -> "null"
  | IntV(i) -> "Int " ^ (string_of_int i)
  | FloatV(f) -> "Float " ^ (string_of_float f)
  | BoolV(b) -> "Bool " ^ (string_of_bool b)
  | VoidV -> "void"
  | LocV(l) -> "Location(" ^ (string_of_int l) ^ ")"

let stringOfOp = function
  | IPlus -> " + "
  | IMinus -> " - "
  | IDivide -> " / "
  | IMultiply -> " * "
  | _ -> "floatOp"

let rec stringOfExp = function
  | Value(v) -> stringOfValue v
  | Variable(id) -> "variable " ^ id
  | ObjectField(var, field) -> "ObjectField"
  | VariableAssignment(id, exp) -> "VariableAssignment " ^ id ^ " = " ^ "("^ stringOfExp exp ^ ")"
  | ObjectFieldAssignment((var, f), e) -> "ObjectFieldAssignment"
  | Sequence(e1, e2) -> "Sequence(" ^ stringOfExp e1 ^ "),\n(" ^ stringOfExp e2 ^ "/* endsequence */) "
  | BlockExpression(list, exp) -> "BlockExpression {\n" ^ stringOfExp exp ^ "\n}"
  | If (id, et, ee) -> "If (" ^ id ^ ") then \n"^ stringOfExp et ^ "\n else  \n" ^ stringOfExp ee ^ " /* endif */ \n"
  | Operation(e1, op, e2) -> "Operation("^ stringOfExp e1 ^ ")" ^(stringOfOp op)^"(" ^ stringOfExp e2 ^ ")"
  | Negation(e) -> "negation"
  | New(cn, varList) -> "new"
  | While(var, e) -> "while"
  | Cast(cn, var) -> "cast"
  | InstanceOf(var, cn) -> "InstanceOf"
  | MethodCall(cn, mn, params) -> "MethodCall"
  | Ret(v, exp) -> "ret"

let stringOfEnv env =
  let stringList = Environment.map (fun id typeValue ->
      sprintf "(%s {typ = %s; value = %s})" id (stringOfType typeValue.typ) (stringOfValue typeValue.value)) env in
  (String.concat ~sep:", " stringList)

(* Get the parent class of `obj`. *)
let getParent obj prog =
  (* Lookup the class declaration list *)
  let rec getParentAux className classList = match classList with
    | Class(name, name_parent, _, _) :: tl -> if name = className then ObjectType name_parent else getParentAux className tl
    | [] -> raiseRuntimeError (className ^ " class not declared inside program.")
  in
  match obj with
  | ObjectType(cn) -> let Program(classList) = prog in getParentAux cn classList
  | primitiveType -> raiseRuntimeError ("Primitive type " ^ (stringOfType primitiveType) ^ "has no base class.")

let getProgramClasslist = function Program classList -> classList

(** Get all the fields of the class. Also inherits fields from it's parents. *)
let rec getFieldList obj prog =
  match obj with
  | ObjectType "Object" -> []
  | ObjectType(cn) ->  let p = getParent obj prog in (getFieldList p prog) @ (getFieldListAux cn prog)
  | primitiveType -> raiseRuntimeError ("Primitive type " ^ (stringOfType primitiveType) ^ "has no fields.")

and getFieldListAux class_name = function Program classList ->
  try
    let Class(_, _, fields, _) = List.find_exn  ~f:(function Class(cn, _, _, _) -> cn = class_name) classList in fields
  with
  | Not_found -> raiseRuntimeError (class_name ^ " not defined in the program")


let getTypeOfVar_exn var (env : Syntax.typeValue Environment.t) : Syntax.typ =
  try (Environment.lookup var env).typ with Environment.Not_bound -> Exn2.raiseRuntimeError var

(** Return the types of the `idList`. *)
let getTypeList idList env = List.map idList (fun id -> getTypeOfVar_exn id env)

let getTypeOfVal = function
  | IntV _ -> IntType
  | FloatV _ -> FloatType
  | BoolV _ -> BoolType
  | VoidV -> VoidType
  | LocV _ -> LocType
  | NullV -> NullType

(** Get the field type of `field_name` *)
let getTypeField obj_type field_name prog = match obj_type with
  | ObjectType(cn) -> begin
      let fieldList = getFieldList obj_type prog in
      try
        let _, found_type = List.find_exn fieldList ~f:(fun (name, _) -> name = field_name) in
        Some(found_type)
      with
        Not_found -> None
    end
  | _ -> None

(** The default values of the types *)
let initValue = function
  | IntType -> IntV(0)
  | FloatType -> FloatV(0.0)
  | BoolType -> BoolV(true)
  | VoidType -> VoidV
  | ObjectType(_) -> NullV
  | LocType -> NullV
  | NullType -> NullV

let isValue = function
  | Value(_) -> true
  | _ -> false

let isLocation = function
  | LocV(_) -> true
  | _ -> false

let isObjectType = function
  | ObjectType _ -> true
  | _ -> false

(** Checks if the class `id` is defind in the program *)
let isDefinedInProg id = function Program classList -> if id = "Object" then true
  else match List.find classList ~f:(function Class(n, _, _, _) -> n = id) with
    | Some _ -> true
    | None -> false

(** Checks if the `typ` is defined in the program.
    For primitive types it always returns true. *)
let isTypeDeclared typ prog = match typ with
  | ObjectType cn -> isDefinedInProg cn prog
  | _ -> true

(** Get all the methods defined in the current class `obj`. *)
let getMethods obj prog = let Program classList = prog in
  match obj with
  | ObjectType "Object" -> []
  | ObjectType cn -> begin
      try
        let Class(n, pn, _, methods) = List.find_exn classList ~f:(function Class(c, _, _, _) -> cn = c) in
        methods
      with
        Not_found -> raiseRuntimeError ((stringOfType obj) ^ " is not defined inside program.")
    end
  | _ -> []

(** Find the method definition of `method_name` in the current class or walk up the parent tree. *)
let rec getMethodDefinition obj method_name prog = match obj with
  | ObjectType cn -> begin
      if cn = "Object" then None
      else
        let methods = getMethods obj prog in
        try
          let methodDecl = List.find_exn methods ~f:(function Method(_, n, _, _) -> n = method_name) in
          Some(methodDecl)
        with
          Not_found -> getMethodDefinition (getParent obj prog) method_name prog
    end
  | _ -> None

(** Get the name of the method. *)
let methodName = function Method(_, n, _, _) -> n

(** Get all the methods defined in the parent class of the `obj`. *)
let getParentMethods obj prog = let parent = getParent obj prog in getMethods parent prog

(** Returns the first element in the list that is not in the enviroment `env`. *)
let rec firstUnboundVariable params env = match params with
  | id :: tl -> if Environment.isIn id env then firstUnboundVariable tl env else Some(id)
  | [] -> None

(** Construct the hierarchy list of class `objType`.
    It will go up the parent tree until no parents can be found (aka Object).
    For Example: A extends B, B extends C, C extends Object.
    We want to construct the hierarchy of A, it will be [A, B, C, Object] *)
let rec constructHierarchyList objType prog = match objType with
  | ObjectType("Object") -> [objType]
  | ObjectType(cn) -> let p = getParent objType prog in [objType] @ (constructHierarchyList p prog)
  | primitiveType -> raiseRuntimeError ("Can not construct hierarchy for primitive type " ^ (stringOfType primitiveType))

(** Find the first element that intersects both lists. *)
let findFirstIntersection list1 list2 = try
    let first = List.find_exn list1 ~f:(fun x -> List.exists list2 (fun y -> y = x)) in
    Some(first)
  with
    Not_found -> None

(** Get the closest common type of `t1` and `t2`.
    This is done by constructing the hierarchy list of both types then finding the first intersection. *)
let leastMaxType t1 t2 prog = match t1, t2 with
  | ObjectType(cn1), ObjectType(cn2) -> let h1 = constructHierarchyList t1 prog in
    let h2 = constructHierarchyList t2 prog in
    findFirstIntersection h1 h2
  | primitive1, primitive2 -> if primitive1 = primitive2 then Some(t1) else None

(** Check if t1 is subtype of t2. *)
let rec isSubtype t1 t2 prog = match t1, t2 with
  | NullType, ObjectType(_) -> true
  | ObjectType _, ObjectType("Object") -> true
  | ObjectType("Object"), _ -> false
  | LocType, ObjectType _ -> true
  | ObjectType(cn1), ObjectType(cn2) -> if cn1 = cn2 then true
    else
      let parent_t1 = getParent t1 prog in
      if t2 = parent_t1 then true
      (* walk the hierarchy of t1 *)
      else isSubtype parent_t1 t2 prog
  | a, b -> if a = b then true else false

(** Check that every type in `types` is a subtype of the field type, one by one. *)
let rec checkFieldsTypes fields types prog = match fields, types with
  | (field, field_type) :: tl_fields, type_value :: tl_types ->
    if isSubtype type_value field_type prog then checkFieldsTypes tl_fields tl_types prog
    else Some(field)
  | [], [] -> None
  | _ -> raiseRuntimeError ("Default case reached in Utils.checkFieldsTypes") (*TODO better treat this case*)

(** Create a new enviroment from the fields and ids, used for creating the env in the heap. *)
let rec createFieldEnv (fields : (Syntax.id * Syntax.typ) list) idList (env : Syntax.typeValue Environment.t) = match fields, idList with
  | (field, _) :: tlf, id :: tl -> let value = Environment.lookup id env in
    Environment.extend field value (createFieldEnv tlf tl env)
  | [], [] -> Environment.empty
  | _ ->  raiseRuntimeError ("Default case reached in Utils.createFieldEnv") (*TODO better treat this case*)

let isIntOperator = function IPlus | IMinus | IMultiply | IDivide  -> true | _ -> false
let isFloatOperator = function FPlus | FMinus | FMultiply | FDivide -> true | _ -> false
let isCompOperator = function Syntax.Less | LessEqual | EqEqual | GreaterEqual | Greater | NotEqual -> true | _ -> false
let isBoolOperator = function And | Or -> true | _ -> false

(** Checks the list for duplicate elements.
    If a duplicate is found it throws DuplicateElement.
    Complexity of this method is O(n^2) *)
let eachElementOnce_exn l = List.iteri l ~f:(fun i x ->
    let xList = List.filter l ~f:(fun y -> x = y) in
    (* Only one element in the list *)
    if List.length xList = 1 then ()
    else raise (DuplicateElement i))

(** Compare two ints or floats *)
let compareValues first second op = match first, second with
  | `Int v1, `Int v2 -> begin match op with
      | Syntax.Less -> BoolV (v1 < v2)
      | LessEqual -> BoolV (v1 <= v2)
      | EqEqual -> BoolV (v1 = v2)
      | GreaterEqual -> BoolV (v1 >= v2)
      | Greater -> BoolV (v1 > v2)
      | NotEqual -> BoolV (v1 <> v2)
      | _ -> raiseRuntimeError ("This should never happen")
    end
  | `Float v1, `Float v2 -> begin match op with
      | Less -> BoolV (v1 < v2)
      | LessEqual -> BoolV (v1 <= v2)
      | EqEqual -> BoolV (v1 = v2)
      | GreaterEqual -> BoolV (v1 >= v2)
      | Greater -> BoolV (v1 > v2)
      | NotEqual -> BoolV (v1 <> v2)
      | _ -> raiseRuntimeError ("This should never happen")
    end
  | _ -> raiseRuntimeError ("This should never happen")

(** Replace all occurences of `name` with `newName` *)
let rec substVariableName newName name exp = match exp with
  | Value _ -> exp
  | Variable var -> if var = name then Variable newName else exp
  | ObjectField(var, field) -> if var = name then ObjectField (newName, field) else exp
  | VariableAssignment(var, e) -> let substExp = substVariableName newName name e in
    if var = name then VariableAssignment (newName,substExp)
    else VariableAssignment (var,substExp)

  | ObjectFieldAssignment((var, f), e) -> let substExp = substVariableName newName name e in
    if var = name then ObjectFieldAssignment((newName, f), substExp)
    else ObjectFieldAssignment((var, f), substExp)

  | Sequence(e1, e2) -> Sequence ((substVariableName newName name e1), (substVariableName newName name e2))
  | BlockExpression(list, e) -> BlockExpression(list, substVariableName newName name e)
  | If (var, et, ee) -> let set = substVariableName newName name et in
    let see = substVariableName newName name ee in
    if var = name then If(newName, set, see)
    else If(var, set, see)

  | Operation(e1, op, e2) -> Operation ((substVariableName newName name e1), op, (substVariableName newName name e2))
  | Negation e -> Negation (substVariableName newName name e)
  | New(cn, varList) -> let substVars = List.map varList ~f:(fun x -> if x = name then newName else x) in
    New(cn, substVars)

  | While (var, e) -> let se = substVariableName newName name e in
    if var = name then While (newName, se)
    else While (var, se)

  | Cast (cn, var) -> if var = name then Cast (cn, newName) else exp
  | InstanceOf (var, cn) -> if var = name then InstanceOf (newName, cn) else exp
  | MethodCall (var, mn, params) -> let substVar = (if var = name then newName else var) in
    let substParams = List.map params ~f:(fun p -> if p = name then newName else p) in
    MethodCall(substVar, mn, substParams)

  | Ret (v, e) -> exp (* TODO: Think twice about this case. Do we need to substitute also in this type of exp?*)
