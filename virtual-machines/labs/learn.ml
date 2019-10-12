(* Binary tree with leaves car­rying an integer. *)
type tree = Leaf of int | Node of tree * tree

let rec exists_leaf test tree =
  match tree with
    | Leaf v -> test v
    | Node (left, right) ->
        exists_leaf test left
        || exists_leaf test right;;

let has_even_leaf tree =
  exists_leaf (fun n -> n mod 2 = 0) tree;;


let rec qsort = function
  | [] -> []
  | pivot :: rest ->
      let is_less x = x < pivot in
      let left, right = List.partition is_less rest in
        qsort left @ [pivot] @ qsort right;;

qsort [5;4;3;2;1];;


let is_capital = function
  | 'a' .. 'z' -> false
  | 'A' .. 'Z' -> true
  | _	     -> failwith "Not A valid letter";;

let capitalize = function
  | 'a' .. 'z' as letter -> Char.uppercase letter
  | 'A' .. 'Z' as letter -> letter
  | _                    -> failwith "Not A valid letter";;


let string_of_int x = match x with
  | 0 -> "zero"
  | 1 -> "one"
  | 2 -> "two"
  | _ -> "many";;


(* test comment *)
is_capital 'A';;
capitalize 'a';;

let rec fact x = if x <= 1 then 1 else x * fact (x - 1);;

let sum xs =
  List.fold_left (+) 0 xs;;

sum [1;2;3;4;5];;

let rec range a b =
  if a > b then []
  else a :: range (a + 1) b;;

range 0 10;;

let abs x = if x >= 0 then x else -x;;

abs ~-10


let rec last = function
  | []  -> None
  | [x] -> Some x
  | _ :: t -> last t;;

last [1; 2; 3; 4];;

let rec duplicate = function
  | [] -> []
  | h :: t -> h :: h :: duplicate t;;

duplicate [1;2;3];;

let rec fib_aux n a b =
  match n with
    | 0 -> a
    | _ -> fib_aux (n - 1) b (a + b);;
let fib n = fib_aux n 0 1;;

fib 6;;

List.map (fun x -> x * x) [1; 2; 3; 4];;
