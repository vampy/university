(* An interpreter for arithmetic expressions with integers and addition,
   and let bindings. *)

(* The type of the abstract syntax tree (AST). *)
type expr =
  | Var of string
  | Int of int
  | Add of expr*expr
  | Let of string*expr*expr

(* [subst e1 e2 x] is [e1] with [e2] substituted for [x]. *)
let rec subst e1 e2 x = match e1 with
  | Var y -> if x=y then e2 else e1
  | Int c -> Int c
  | Add(el,er) -> Add(subst el e2 x, subst er e2 x)
  | Let(y,ebind,ebody) ->
    if x=y then Let(y, subst ebind e2 x, ebody)
    else Let(y, subst ebind e2 x, subst ebody e2 x)

(* A single step of evaluation. *)
let rec step = function
  | Int n -> failwith "Does not step"
  | Var _ -> failwith "Unbound variable"
  | Add(Int n1, Int n2) -> Int (n1+n2)
  | Add(Int n1, e2) -> Add(Int n1, step e2)
  | Add(e1,e2) -> Add(step e1, e2)
  | Let(x,Int n,e2) -> subst e2 (Int n) x
  | Let(x,e1,e2) -> Let(x,step e1, e2)

(* The reflexive, transitive closure of [step]. *)
let rec multistep = function
  | Int n -> Int n
  | e -> multistep (step e)

(* A few test cases *)
let _ = assert (Int 22 = multistep (Int 22))
let _ = assert (Int 22 = multistep (Add(Int 11,Int 11)))
let _ = assert (Int 22 = multistep (Add(Add(Int 10, Int 1),Add(Int 5, Int 6))))
let _ = assert (Int 22 = multistep (Let("x",Int 22,Var "x")))
let _ = assert (Int 22 = multistep (Let("x",Int 0,Let("x",Int 22,Var "x"))))
      
