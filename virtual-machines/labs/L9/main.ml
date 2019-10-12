open Ast

(* [subst e1 e2 x] is [e1] with [e2] substituted for [x]. *)
let rec subst e1 e2 x = match e1 with
  | Var y      -> if x=y then e2 else e1
  | Int c      -> Int c
  | Add(el,er) -> Add(subst el e2 x, subst er e2 x)
  | Let(y,ebind,ebody) ->
    if x=y 
    then Let(y, subst ebind e2 x, ebody)
    else Let(y, subst ebind e2 x, subst ebody e2 x)

(* A single step of evaluation. *)
let rec step = function
  | Int n               -> failwith "Does not step"
  | Var _               -> failwith "Unbound variable"
  | Add(Int n1, Int n2) -> Int (n1+n2)
  | Add(Int n1, e2)     -> Add(Int n1, step e2)
  | Add(e1,e2)          -> Add(step e1, e2)
  | Let(x,Int n,e2)     -> subst e2 (Int n) x
  | Let(x,e1,e2)        -> Let(x,step e1, e2)

(* The reflexive, transitive closure of [step]. *)
let rec multistep = function
  | Int n -> Int n
  | e     -> multistep (step e)

(***********************************************************************)
(* Everything above this is essentially the same as we saw in lecture. *)
(***********************************************************************)

(* Parse a string into an ast *)
let parse s =
  let lexbuf = Lexing.from_string s in
  let ast = Parser.prog Lexer.read lexbuf in
  ast

(* Extract a value from an ast node.
   Raises Failure if the argument is a node containing a value. *)
let extract_value = function
  | Int i -> i
  | _ -> failwith "Not a value"

(* Interpret an expression *)
let interp e =
  e |> parse |> multistep |> extract_value

(* A few test cases *)
let run_tests () =
  assert (22 = interp "22");
  assert (22 = interp "11+11");
  assert (22 = interp "(10+1)+(5+6)");
  assert (22 = interp "let x = 22 in x");
  assert (22 = interp "let x = 0 in let x = 22 in x")
      

