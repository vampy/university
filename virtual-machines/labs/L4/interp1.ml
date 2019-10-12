(* An interpreter for arithmetic expressions with integers and addition. *)

(* The type of the abstract syntax tree (AST). *)
type expr =
  | Int of int
  | Add of expr*expr

(* A single step of evaluation. *)
let rec step = function
  | Int n               -> failwith "Does not step"
  | Add(Int n1, Int n2) -> Int (n1+n2)
  | Add(Int n1, e2)     -> Add(Int n1, step e2)
  | Add(e1,e2)          -> Add(step e1, e2)

(* The reflexive, transitive closure of [step]. *)
let rec multistep = function
  | Int n -> Int n
  | e     -> multistep (step e)

(* A few test cases *)
let _ = assert (Int 22 = multistep (Int 22))
let _ = assert (Int 22 = multistep (Add(Int 11,Int 11)))
let _ = assert (Int 22 = multistep (Add(Add(Int 10, Int 1),Add(Int 5, Int 6))))
  
