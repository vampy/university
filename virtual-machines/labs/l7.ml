(*
ret(x0, V)
get rid of x0 from V stack values


downcast example (cast is smart...) 
cn1 = getClassName loc H (where loc is he location of v before casting)
is_subtype cn1 cn
<H, V, (cn)v > -> <H, V, v>


check if v is an instance of cn
val is a bool
<H, V, v instanceof cn > -> <H, V, val>


NOTE: no static classes or methods


# Static semantics
Can't do int operation boolean
eg: 5 + false
The type system is strong typed


T |- e : t
in context T, expression e has type t
|- can be read as "proves" or "shows"

eg:
    x:int |- x + 2 : int
    x:int, y:int |- v < y : bool



*)

let x = 22;
