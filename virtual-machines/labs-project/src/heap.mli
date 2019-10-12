open Core.Std
(* TODO: look at and maybe use this instead -> https://ocaml.janestreet.com/ocaml-core/109.12.00/doc/core/Heap.html *)
type t
  [@@deriving show]
and heapEntry = {id: Syntax.id; env: Syntax.typeValue Environment.t;}
  [@@deriving show]

val empty : t
val nextLocation: unit -> Syntax.value
val extend : Syntax.value -> heapEntry -> t -> t
val union : (Syntax.value * heapEntry) list -> t -> t
val update: Syntax.value -> Syntax.id -> Syntax.typeValue -> t -> t
val isIn : Syntax.value -> t -> bool
val getFieldEnv_exn : Syntax.value -> t -> Syntax.typeValue Environment.t
val getObjectType_exn : Syntax.value -> t -> Syntax.typ
