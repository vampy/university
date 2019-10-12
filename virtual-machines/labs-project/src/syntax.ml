type program = Program of classDeclaration list
  [@@deriving show]

(** A class if formed of (name class, name parent class, list of fields, list of methods) *)
and classDeclaration = Class of id * id * ((id * typ) list) * methodDeclaration list
  [@@deriving show]

and typ = IntType
        | FloatType
        | BoolType
        | VoidType
        | NullType
        | LocType
        | ObjectType of id
  [@@deriving show]

(** A method has a (type method, name method, list of method parameters, the body of the method) *)
and methodDeclaration = Method of typ * id * ((id * typ) list) * exp
  [@@deriving show]

and exp = Value of value
        | Variable of id
        | ObjectField of id * id
        | VariableAssignment of id * exp
        | ObjectFieldAssignment of (id * id) * exp
        | BlockExpression of ((id * typ) list) * exp
        | Sequence of exp * exp
        | If of id * exp * exp
        | Operation of exp * binaryOperator * exp
        | Negation of exp
        | New of id * (id list)
        | MethodCall of id * id * (id list)
        | While of id * exp
        | Cast of id * id
        | InstanceOf of id * id
        | Ret of id * exp
  (* and variableDeclaration = VariableDeclaration of typ * id *)
  (* and blkExp = BlockExpression of *)
  (* | BnVar of exp *)
  [@@deriving show]

and value = NullV
          | IntV of int
          | FloatV of float
          | BoolV of bool
          | VoidV
          | LocV of int
  [@@deriving show]

and typeValue = {
  typ: typ;
  value: value
}
  [@@deriving show]

and binaryOperator = IPlus
                   | IMinus
                   | IMultiply
                   | IDivide
                   | FPlus
                   | FMinus
                   | FMultiply
                   | FDivide
                   | Less
                   | LessEqual
                   | EqEqual
                   | GreaterEqual
                   | Greater
                   | NotEqual
                   | And
                   | Or
  [@@deriving show]

and id = string
  [@@deriving show]
