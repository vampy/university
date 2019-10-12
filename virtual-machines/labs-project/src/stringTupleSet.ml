open Core.Std

(** A set of values of type tuple(string, string) *)
module StringTuple = struct
  type t = string * string

  let compare (x0, y0) (x1, y1) =
    match Pervasives.compare x0 x1 with
      0 -> Pervasives.compare y0 y1
    | c -> c

  let t_of_sexp tuple = Tuple2.t_of_sexp String.t_of_sexp String.t_of_sexp tuple
  let sexp_of_t tuple = Tuple2.sexp_of_t String.sexp_of_t String.sexp_of_t tuple
end

module StringTupleSet = Set.Make(StringTuple)
