(* TODO use core std, Heap conflicts with core Heap *)
(* open Core.Std *)
open Heap
open Syntax
open Exn2

type state =
  { e: exp;
    heap: Heap.t;

    (* a list of variables id => (type, value) <- not actual represenation in structure *)
    env: Syntax.typeValue Environment.t;
    prog: program;
  }
let stepCounter = ref 0

let rec step (state : state) : state = match state.e with
  | Value _ -> raiseRuntimeError ("Does not step") (*This case is unreachable if step is called from multistep*)
  | Variable(id) ->  stepVariable id state
  | ObjectField(var, field) -> stepObjectField var field state
  | VariableAssignment(id, exp) -> stepVariableAssignment id exp state
  | ObjectFieldAssignment((c, f), e) -> stepObjectFieldAssignment c f e state
  | Sequence(e1, e2) -> stepSequence e1 e2 state
  | BlockExpression(list, exp) -> stepBlockExpression list exp state
  | Ret(v, exp) -> stepRet v exp state
  | If (var, et, ee) -> stepIf var et ee state
  | Operation(e1, op, e2) -> stepOperation e1 e2 op state
  | Negation(e) -> stepNegation e state
  | New(id, idList) -> stepNew id idList state
  | While(var, e) -> stepWhile var e state
  | Cast(cn, var) -> stepCast cn var state
  | InstanceOf(var, cn) -> stepInstanceOf var cn state
  | MethodCall(cn, mn, params) -> stepMethodCall cn mn params state

(* Return the value of a variable defined in the env *)
and evalVariable (var : id) (state : state) : value =
  if Environment.isIn var state.env then (Environment.lookup var state.env).value
  else raiseUnboundVar var

(** Get the adress location of the object `var`. *)
and evalObjectVariable (var : id) (state : state) : value = let varValue = evalVariable var state in
  if Utils.isLocation varValue then
    if Heap.isIn varValue state.heap then varValue
    else raiseRuntimeError ("Variable " ^ var ^ " does not point to a valid location in heap.")
  else raiseRuntimeError("Variable " ^ var ^ " is not an instantiated object." )

and stepVariable (var : id) (state : state) : state = let v = evalVariable var state in
  {state with e = Value(v)}

and stepObjectField (var : id) (field : id) (state : state) : state =
  if Environment.isIn var state.env then
    let loc = (Environment.lookup var state.env).value in
    if Utils.isLocation loc then
      if Heap.isIn loc state.heap then
        let field_env = (Heap.getFieldEnv_exn loc state.heap) in
        (* print_endline ((Utils.stringOfEnv field_env)); *)
        (* Get the value of the field from the heap location of this object *)
        if Environment.isIn field field_env then
          let v = (Environment.lookup field field_env).value in
          {state with e = Value(v)}
        else raiseRuntimeError ("Field " ^ field ^ " not declared inside " ^ var)
      else raiseRuntimeError ((Utils.stringOfValue loc) ^ " is not in heap.")
    else raiseRuntimeError (var ^ " is not an object.")
  else raiseRuntimeError (var ^ " not declared.")

and stepVariableAssignment (var : id) (e : exp) (state : state) : state = match e with
  | Value(v) ->
    if Environment.isIn var state.env then
      let varT = Utils.getTypeOfVar_exn var state.env in
      let valT = Utils.getTypeOfVal v in
      if Utils.isSubtype valT varT state.prog then
        let nEnv = Environment.update var {typ = varT; value = v} state.env in
        {state with env = nEnv; e = Value(VoidV)}
      else raiseRuntimeError ("Invalid types")
    else raiseUnboundVar var
  | _ -> let ns = step {state with e = e} in {ns with e = VariableAssignment(var, ns.e)}

and stepObjectFieldAssignment (var : id) (field : id) (e : exp) (state : state) : state = match e with
  | Value(v) ->
    if Environment.isIn var state.env then
      let loc = (Environment.lookup var state.env).value in
      if Utils.isLocation loc then
        let fldE = (Heap.getFieldEnv_exn loc state.heap) in
        if Environment.isIn field fldE then
          let fieldType = Utils.getTypeOfVar_exn field fldE in
          let valType = Utils.getTypeOfVal v in
          (* Update value of the object field in the heap *)
          if Utils.isSubtype valType fieldType state.prog then
            let nHeap = Heap.update loc field {typ = fieldType; value = v} state.heap  in
            {state with heap = nHeap; e = Value(VoidV)}
          else
            raiseRuntimeError ("Type of " ^ var ^ "." ^ field ^ "(" ^ (Utils.stringOfType fieldType) ^ ") is incompatible with " ^ (Utils.stringOfType valType))
        else raiseRuntimeError ("Field " ^ field ^ " not declared inside " ^ var)
      else raiseRuntimeError (var ^ " is not an object.")
    else raiseUnboundVar var
  | _ -> let ns = (step {state with e = e}) in {ns with e = ObjectFieldAssignment((var,field), ns.e)}

and stepSequence (e1 : exp) (e2 : exp) (state : state) : state = match e1 with
  | Value v -> { state with e = e2 }
  | _ -> let ns = step {state with e = e1} in
    {ns with e = Sequence(ns.e, e2)}

(* Give scope to variables, using ret
   The argument `l` is a list of defined variables. *)
and stepBlockExpression (l : ((id * typ) list)) (exp : exp) (state : state) : state = match l with
  | [] -> {state with e = exp}
  | [(id, typ)] -> {state with
                    env = Environment.extend id {typ = typ; value = Utils.initValue typ} state.env;
                    e = Ret(id, exp)}
  | (id, typ) :: tl -> {state with
                        env = Environment.extend id {typ = typ; value = Utils.initValue typ} state.env;
                        e = Ret(id, BlockExpression(tl, exp))}

and stepRet (v : id) (exp : exp) (state : state) : state =
  if Utils.isValue exp then
    (* End of block `exp` execution, pop v from the stack *)
    {state with env = Environment.pop v state.env; e = exp}
  else
    (* Execute the expression until it is reduce to a value. Here it walks the ret path created. *)
    let ns = step {state with e = exp} in
    {ns with e = Ret(v, ns.e)}

and stepIf (var: id) (et : exp) (ee : exp) (state : state) : state = match evalVariable var state with
  | BoolV true -> {state with e = et}
  | BoolV false -> {state with e = ee}
  | _ -> let varType = Utils.getTypeOfVar_exn var state.env in
    raiseRuntimeError ("Variable " ^ var ^ " has type " ^ (Utils.stringOfType varType) ^ " but a variable was expected of type " ^ (Utils.stringOfType BoolType) )

(* TODO: don't use multistep evaluation for Operation subexpressions in order to output better errors*)
and stepOperation (e1 : exp) (e2 : exp) (op : binaryOperator) (state : state) : state = match op with
  | op when Utils.isBoolOperator op -> stepBoolOperation e1 e2 op state
  | op when Utils.isIntOperator op -> let expectedType = IntType in stepArithmeticOperation e1 e2 op expectedType state
  | op when Utils.isFloatOperator op -> let expectedType = FloatType in stepArithmeticOperation e1 e2 op expectedType state
  | op (*compare operator*) -> stepCompareOperation e1 e2 op state

and stepCompareOperation (e1 : exp) (e2 : exp) (op : binaryOperator) (state : state) : state =
  match e1, e2 with
  | Value v1, Value v2 -> let val1Type = Utils.getTypeOfVal v1 in
    let val2Type = Utils.getTypeOfVal v2 in
    if val1Type = val2Type then
      if val1Type = IntType || val1Type = FloatType then
        let result = applyOp v1 v2 op in
        {state with e = Value result}
      else raiseDifferentTypeExpErr e1 [IntType; FloatType] val1Type
    else raiseRuntimeError ("Can not compare an expression " ^ (Utils.stringOfExp e1) ^ " of type "^ (Utils.stringOfType val1Type) ^
                            "with an expression " ^ (Utils.stringOfExp e2) ^ " of type " ^ (Utils.stringOfType val2Type))

  | Value v1, e2 -> let val1Type = Utils.getTypeOfVal v1 in
    if val1Type = IntType || val1Type = FloatType then
      let ns = step {state with e = e2} in
      {ns with e = Operation(e1 ,op, ns.e)}
    else
      raiseDifferentTypeExpErr (Value v1) [IntType;FloatType] val1Type

  | e1, e2 -> let ns = (step {state with e = e1}) in {ns with e = Operation(ns.e,op,e2)}

and stepArithmeticOperation (e1 : exp) (e2 : exp) (op : binaryOperator) (expectedType : typ) (state : state) : state =
  match e1,e2 with
  | Value v1, Value v2 -> let val1Type = Utils.getTypeOfVal v1 in
    let val2Type = Utils.getTypeOfVal v2 in
    if val1Type = expectedType then
      if val2Type = expectedType then
        let result = applyOp v1 v2 op in
        {state with e = Value result}
      else raiseDifferentTypeExpErr e2 [expectedType] val2Type
    else raiseDifferentTypeExpErr e1 [expectedType] val1Type

  | Value v1, e2 -> let val1Type = Utils.getTypeOfVal v1 in
    if val1Type = expectedType then
      let ns = (step {state with e = e2}) in
      {ns with e = Operation(e1,op,ns.e)}
    else raiseDifferentTypeExpErr e1 [expectedType] val1Type

  | e1, e2 -> let ns = (step {state with e = e1}) in {ns with e = Operation(ns.e,op,e2)}

and stepBoolOperation (e1 : exp) (e2 : exp) (op : binaryOperator) (state : state) : state = match op with
  | And -> begin match e1 with
      (* The value of this AND is `e2` *)
      | Value BoolV true -> {state with e = e2}
      (* The value of this AND is `false` *)
      | Value BoolV false -> {state with e = e1}
      | Value nonBool -> raiseDifferentTypeExpErr e1 [BoolType] (Utils.getTypeOfVal nonBool)
      | e -> let ns = (step {state with e = e}) in {ns with e = Operation(ns.e, op, e2)}
    end
  (*TODO: reduce this code*)
  | Or -> begin match e1 with
      | Value BoolV false -> {state with e = e2}
      | Value BoolV true -> {state with e = e1}
      | Value nonBool -> raiseDifferentTypeExpErr e1 [BoolType] (Utils.getTypeOfVal nonBool)
      | e -> let ns = (step {state with e = e}) in {ns with e = Operation(ns.e, op, e2)}
    end
  | _ -> raiseRuntimeError ("This should never happen")

and stepNegation (e : exp) (state : state) : state = match e with
  | Value BoolV v -> {state with e = Value (BoolV (not v))}
  | Value v -> raiseDifferentTypeExpErr e [BoolType] (Utils.getTypeOfVal v)
  | e -> let ns = (step {state with e = e}) in
    {ns with e = Negation(ns.e)}

and stepWhile (var : id) (e : exp) (state : state) : state = let varValue = evalVariable var state in
  match varValue with
  | BoolV true -> {state with e = Sequence (e, While(var,e))}
  | BoolV false -> {state with e = Value VoidV}
  | v -> raiseDifferentTypeExpErr (Variable var) [BoolType] (Utils.getTypeOfVal v)

and stepNew (id : id) (idList : id list) (state : state) : state = match Utils.firstUnboundVariable idList state.env with
  (* All variables are bound in the id list *)
  | None -> begin
      (* Class exists *)
      if Utils.isDefinedInProg id state.prog then
        let fieldList = Utils.getFieldList (ObjectType id) state.prog in
        let typeList = Utils.getTypeList idList state.env in
        (* If the types match the constructor then create a new location in heap for the fields of this class. *)
        match Utils.checkFieldsTypes fieldList typeList state.prog with
        | None -> (let fEnv = Utils.createFieldEnv fieldList idList state.env in
                   let nl = Heap.nextLocation () in
                   let nh = Heap.extend nl {id = id; env = fEnv} state.heap in
                   (* Return the state with the new heap and the location created. *)
                   {state with heap = nh; e = Value(nl)})
        | Some field -> raiseRuntimeError ("Field" ^ field ^ "uncompatibile with its corresponding value")
      else raiseRuntimeError (id ^ " not defined in prog")
    end
  | Some var -> raiseRuntimeError ("Unbound variable " ^ var)

and stepCast (cn : id) (var : id)  (state : state) : state =
  if Utils.isDefinedInProg cn state.prog then
    let loc = evalObjectVariable var state in
    let varType = Heap.getObjectType_exn loc state.heap in
    if Utils.isSubtype varType (ObjectType cn) state.prog then {state with e = (Variable var)}
    else raiseRuntimeError ("Can not cast variable " ^ var ^ " of type " ^ (Utils.stringOfType varType) ^ " to type " ^ cn)
  else raiseRuntimeError ("Unbound class " ^ cn)

and stepInstanceOf (var : id) (cn : id) (state : state) : state =
  if Utils.isDefinedInProg cn state.prog then
    let loc = evalObjectVariable var state in
    let varType = Heap.getObjectType_exn loc state.heap in
    let isInstance = Utils.isSubtype varType (ObjectType cn) state.prog in
    {state with e = (Value (BoolV isInstance))}
  else raiseRuntimeError ("Unbound class " ^ cn)

and stepMethodCall (var : id) (mn : id) (params : id list) (state : state) : state = let loc = evalObjectVariable var state in
  let varType = Heap.getObjectType_exn loc state.heap in
  let methodDecl = Utils.getMethodDefinition varType mn state.prog in
  match methodDecl with
  | Some Method(rt, _, idTypLst, body_expr) -> begin
      (* TODO only use core std *)
      (* Get the values of the parameters. *)
      let valList = List.map (fun x -> evalVariable x state) params in
      (* Construct the list of params as (id, type). *)
      let paramIdTypList = Core.Std.List.map params ~f:(fun id -> let typ = Utils.getTypeOfVar_exn id state.env in (id, typ)) in
      try
        (* Check that the type of the method correspond to the types of the params given. *)
        Core.Std.List.iter2_exn paramIdTypList idTypLst ~f:(fun (pn, ptype) (_,typ) ->
            if Utils.isSubtype ptype typ state.prog then () else raise (IncompatibleTypes ((Variable pn), ptype, typ)));
        (* generate fresh variables (_x0, _x1, ... , _xn) *)
        let freshVars = Core.Std.List.init (List.length params) ~f:(fun i -> "_x" ^ (string_of_int i))  in
        (* Build the new enviroment with the new fresh vars. Eg: (_x0, {typ = first param type; value = first param value}) *)
        let envExtension = ("_this", {typ = varType; value = loc}) ::
                           Core.Std.List.map3_exn freshVars paramIdTypList valList ~f: (fun x (_, t) v -> (x, {typ = t; value = v})) in
        let newEnv = Environment.union envExtension state.env in
        (* Build the substitution list (new_name, old_name). Eg: (_x0, first param) *)
        let substList = ("_this", "this") :: Core.Std.List.map2_exn freshVars params ~f:(fun x y -> (x, y)) in
        (* Replace all the body variables with the fresh variables *)
        let substExp = Core.Std.List.fold substList ~init:body_expr ~f:(fun expr (newName, name) -> Utils.substVariableName newName name expr) in
        (* Remove from stack all the fresh variables at the end of the body execution. *)
        let retE = Core.Std.List.fold substList ~init:substExp ~f:(fun expr (newName, _)-> Ret(newName, expr)) in
        {state with e = retE; env = newEnv}
      with
        Invalid_argument _ -> raiseRuntimeError "Number of passed parameters is not valid."
      | IncompatibleTypes (exp, expectedType, actualType) -> raiseDifferentTypeExpErr exp [expectedType] actualType
    end
  | None -> raiseRuntimeError ((Utils.stringOfType varType) ^ " does not have a method called " ^ mn)

and applyOp (e1 : value) (e2 : value) (op : binaryOperator) : value = match op with
  | op when Utils.isIntOperator op -> begin
      match e1, e2 with
      | IntV v1, IntV v2 -> (match op with
          | IPlus -> IntV (v1 + v2)
          | IMinus -> IntV (v1 - v2)
          | IMultiply -> IntV (v1 * v2)
          | IDivide -> (try IntV (v1 / v2) with Division_by_zero -> raiseRuntimeError "Division by zero.")
          | _ -> raiseRuntimeError ("This should never happen")
        )
      | _ -> raiseRuntimeError ("Error: Expected values of type " ^ (Utils.stringOfType IntType))
    end
  | op when Utils.isFloatOperator op -> begin
      match e1, e2 with
      | FloatV v1, FloatV v2 -> (match op with
          | FPlus -> FloatV (v1 +. v2)
          | FMinus -> FloatV (v1 -. v2)
          | FMultiply -> FloatV (v1 *. v2)
          | FDivide -> (try FloatV (v1 /. v2) with Division_by_zero -> raiseRuntimeError "Division by zero.")
          | _ -> raiseRuntimeError ("This should never happen")
        )
      | _ -> raiseRuntimeError ("Error: Expected values of type " ^ (Utils.stringOfType FloatType))
    end
  | op when Utils.isCompOperator op -> begin
      match e1, e2 with
      | IntV v1, IntV v2 -> Utils.compareValues (`Int v1) (`Int v2) op
      | FloatV v1, FloatV v2 -> Utils.compareValues (`Float v1) (`Float v2) op
      | _ -> raiseRuntimeError ("Error: Both values should be of type " ^ (Utils.stringOfType IntType) ^" or "^ (Utils.stringOfType FloatType))
    end
  | op -> raiseRuntimeError ("This should never happen")


let rec multistep (state : state) : value =
  (* debug print every 10 steps *)
  if !stepCounter mod 10 = 0 then begin
    print_endline (Core.Std.Printf.sprintf "-------------------\n\n>ENV: %s\n>HEAP: %s\n>EXP: %s\n\n-----------------"
                     (Environment.show Syntax.pp_typeValue state.env) (Heap.show state.heap) (Syntax.show_exp state.e));
  end else ();
  stepCounter := !stepCounter + 1;
  match state.e with
  | Value(v) -> print_endline ((Utils.stringOfEnv state.env));print_endline ("Exp:" ^ (Syntax.show_exp state.e));v
  | exp -> multistep (step state)

let interpretExp (e : exp) (prog : program) : value =
  let initialState = {heap = Heap.empty; env = Environment.empty; e = e; prog = prog} in
  multistep initialState

let interpretProgram (prog : program) : value = let Program classList = prog in
  try
    let Class (cname, _, fields, methods) = Core.Std.List.last_exn classList in

    (* Start execution from the class Main which does not have any fields and only one method called `main` *)
    if cname = "Main" then
      if List.length fields = 0 then begin
        try
          if List.length methods = 1 then
            let Method(rt, mname, args, e) = Core.Std.List.last_exn methods in
            if rt = VoidType && mname = "main" then interpretExp e prog
            else raise (Invalid_argument "The Main class does not have a `void main` method")
          else raise (Invalid_argument "The Main class doesn't have exactly ONE method")
        (* else raiseRuntimeError "The Main class should have only one method called main." *)
        with
          Invalid_argument el -> raiseRuntimeError ("There is no method main with return type " ^ (Utils.stringOfType VoidType))
      end
      else raiseRuntimeError "The Main class shouldn't have any fields."
    else raiseRuntimeError "The last declared class is not Main."
  with
    Invalid_argument el -> raiseRuntimeError ("The program has no class declarations. " ^ el)

let initialHeap = (Heap.union [
    ((LocV 1), {id = "a"; env = (Environment.union
                                   [("f1",{typ = IntType; value = IntV 3})] Environment.empty)
               } );
    ((LocV 2), {id = "b"; env = (Environment.union
                                   [("f1",{typ = IntType; value = IntV 4})] Environment.empty)
               } );
  ] Heap.empty)
let interpret (e : exp) (program : program) : value =
  let initialEnv = (Environment.union [
      ("a", {typ = IntType; value = IntV 3});
      ("i", {typ = IntType; value = IntV 0});
      ("cond", {typ = BoolType; value = BoolV true});
      ("mya", {typ = ObjectType("a"); value = LocV 1});
      ("myb", {typ = ObjectType("b"); value = LocV 2})] Environment.empty) in

  let initialState = {heap = initialHeap; env = initialEnv; e = e; prog = program} in
  multistep initialState

let prg = Program( [Class ("a", "Object", [("f1",IntType)], [Method(IntType,"add",[("p1",IntType)],Value(IntV 3))]);
                    Class ("b", "a", [("f2",IntType)], [Method(IntType,"add",[("p1",IntType)],Value(IntV 3))]);Class ("c", "b", [], []);Class ("d", "Object", [], [])] )
(* Tests for Typechecker *)
let methods = [Method(IntType,"m1",[],Value(IntV 3));Method(IntType,"m1",[],Value(IntV 3))]
let classDecl = Class ("a", "Object", [("f1",IntType)], methods)
let te = (Environment.union [("a",IntType);("cond",BoolType);("mya",ObjectType("a"));("myc",ObjectType("c"));("myd",ObjectType("d"))] Environment.empty)
let _ = assert (true = Utils.isSubtype VoidType VoidType prg)
let _ = assert (true = Utils.isSubtype (ObjectType "b") (ObjectType "Object") prg)
let _ = assert (true = Utils.isSubtype (ObjectType "c") (ObjectType "Object") prg)
let _ = assert (true = Utils.isSubtype (ObjectType "c") (ObjectType "a") prg)
let _ = assert (false = Utils.isSubtype (ObjectType "b") (ObjectType "d") prg)
let _ = assert (false = Utils.isSubtype (ObjectType "d") (ObjectType "b") prg)
let _ = assert ((ObjectType "Object") = Utils.getParent (ObjectType "a") prg)
let _ = assert (Some (ObjectType "Object") = Utils.leastMaxType (ObjectType "c") (ObjectType "d") prg )
let _ = assert (Some (ObjectType "a") = Utils.leastMaxType (ObjectType "c") (ObjectType "a") prg )

let _ = assert (IntType = Typechecker.typeCheckExp (Value(IntV 3)) te prg )
let _ = assert (IntType = Typechecker.typeCheckExp (ObjectField("mya", "f1")) te prg)
let _ = assert (VoidType = Typechecker.typeCheckExp (VariableAssignment ("a", Value(IntV 3)) ) te prg)
let _ = assert (ObjectType "a" = Typechecker.typeCheckExp (Variable "mya") te prg)
let _ = assert (ObjectType "Object" = Typechecker.typeCheckExp (If ("cond", (Variable "myc"), (Variable "myd")) ) te prg)

let _ = assert (IntType = Typechecker.typeCheckExp (Operation ((Value(IntV 3)),IPlus,(Value(IntV 3)))) te prg)
let _ = assert (BoolType = Typechecker.typeCheckExp (Operation ((Value(IntV 3)),EqEqual,(Value(IntV 3)))) te prg)
let _ = assert (true = Utils.isIntOperator IPlus)
let _ = assert (ObjectType "a" = Typechecker.typeCheckExp (Cast("a","myc")) te prg)
let _ = assert (ObjectType "Object" = Typechecker.typeCheckExp (Cast("Object","myc")) te prg)
let _ = assert (ObjectType "a" = Typechecker.typeCheckExp (New("a",["a"])) te prg)
let _ = assert (ObjectType "b" = Typechecker.typeCheckExp (New("b",["a";"a"])) te prg)
let _ = assert (VoidType = Typechecker.typeCheckExp (While ("cond",(Value(IntV 3))) ) te prg)
let _ = assert (IntType = Typechecker.typeCheckExp (MethodCall("mya","add",["a"])) te prg )
let _ = assert (IntType = Typechecker.typeCheckExp (MethodCall("myc","add",["a"])) te prg )

let _ = Typechecker.goodInheritance_exn (Class ("b", "a", [("f2",IntType)], [Method(IntType,"add",[("p1",IntType)],Value(IntV 3));Method(IntType,"minus",[("p1",IntType)],Value(IntV 3))])) prg
(* let _ = print_endline (Syntax.show_program prg) *)
(* let _ = Typechecker.typeCheckProgram prg *)
(* Tests for interpreter*)
(* let _ = assert (IntV 21 = interpret (Sequence(Value(IntV 22),Value(IntV 21) )) prg )
   let _ = assert (IntV 22 = interpret (Sequence(VariableAssignment("a",Value(IntV 22)),Variable("a") )) prg )
   let _ = assert (IntV 666 = interpret (Sequence(VariableAssignment("a",Sequence(Value(IntV 22),Value(IntV 666) )),Variable("a") )) prg )
   let _ = assert (IntV 0 = interpret (BlockExpression([(IntType,"b")], Variable("b") )) prg )
   let _ = assert (IntV 22 = interpret (BlockExpression([(IntType,"b")], Sequence(VariableAssignment("b",Value(IntV 22)),Variable("b") )))  prg )
   let _ = assert (LocV 666 = interpret (Sequence(VariableAssignment("mya", New("a", ["a"])), Variable("mya"))) prg)
   let _ = assert (IntV 3 = interpret (Sequence(VariableAssignment("mya", New("a", ["a"])), ObjectField("mya", "f1"))) prg)
   let _ = assert (IntV 3 = interpret (Sequence(VariableAssignment("myb", New("b", ["a";"a"])), ObjectField("myb", "f1"))) prg)
   let _ = assert (IntV 3 = interpret (Variable "a") prg ) *)
(* let _ = assert (IntV 10 = interpret(Sequence(ObjectFieldAssignment(("mya","f1"),Value(IntV 10)),ObjectField ("mya", "f1") )) prg )
   let _ = let v = interpret(Sequence(ObjectFieldAssignment(("mya","f1"),Value(IntV 10)),ObjectField ("mya", "f1") )) prg  in print_endline(Utils.stringOfValue v)
   let _ = let v = interpret (Sequence(VariableAssignment("mya", New("a", ["a"])),
                                    Sequence(VariableAssignment("mya", New("a", ["a"])), Variable("mya")))) prg in print_endline(Utils.stringOfValue v)
   let _ = assert (IntV 10 = interpret(Operation((Value (IntV 5)),IPlus,(Value (IntV 5))   )) prg )
   let _ = assert (BoolV true = interpret(Operation((Value (BoolV true)),Or,(Value (BoolV false))   )) prg )
   let _ = assert (BoolV true = interpret(Operation((Value (IntV 5)),Less,(Value (IntV 6))   )) prg )

   let _ = assert (IntV 101 = interpret (
    Syntax.Sequence (
      Syntax.While ("cond",
                    Syntax.BlockExpression ([],
                                            Syntax.Sequence (
                                              Syntax.VariableAssignment ("cond",
                                                                         Syntax.Operation ((Syntax.Variable "i"),
                                                                                           Syntax.Less, (Syntax.Value (Syntax.IntV 100)))),
                                              Syntax.VariableAssignment ("i",
                                                                         Syntax.Operation ((Syntax.Variable "i"),
                                                                                           Syntax.IPlus, (Syntax.Value (Syntax.IntV 1))))))),
      (Syntax.Variable "i"))
   ) prg)
   let _ = assert ((LocV 2) = interpret (Syntax.Cast ("a","myb")) prg)
   let _ = assert (BoolV true = interpret (Syntax.InstanceOf ("myb","a")) prg)
   let _ = assert (IntV 3 = interpret (Syntax.MethodCall("mya","add",["a"])) prg) *)
(* let _ = assert (BoolV true = interpret(Operation((Value (BoolV true)),Less, (Value (BoolV true))   )) prg ) *)
(* let _ = assert (VoidType ) *)
