type 'a t = (Syntax.id * 'a) list
  [@@deriving show]
exception Not_bound
exception RuntimeError of string

let empty = []
let extend x v env = (x, v) :: env

let rec union lst env = match lst with
  | [] -> env
  | x :: xs -> x :: union xs env

let rec isIn id env =
  try List.assoc id env; true with Not_found -> false

let rec lookup id env =
  try List.assoc id env with Not_found -> raise Not_bound

let rec update id nv env = match env  with
  | [] -> []
  | (key, v) :: tl -> if key = id then (key, nv) :: tl
    else (key, v) :: update id nv tl

let rec map f = function
  | [] -> []
  | (id, v) :: rest -> f id v :: map f rest

let pop id = function
  | (id, _) :: tl -> tl
  | _ -> raise(RuntimeError (id ^ " is not in top of the stack"))
