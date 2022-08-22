(** Contains types for an abstract syntax tree. *)

(** The type of binary operators. *)
type bop = 
  | Add
  | Mult
  | Sub
  | Div
  | Exp

(** Type of unary operators .*)
type uop = 
  | Sin 
  | Cos
  | Tan 
  | ASin 
  | ACos 
  | ATan 
  | LogE 
  | Root

(** Type of unary matrix operators. *)
type mop = 
  | Tran
  | Rand 
  | Id 
  | Mref
  | Det

(** The type of "Numbers". *)
type num = {
  converted: bool;
  value: float;
}

(** The type of the AST along with expr. *)
type f_expr = 
  | FVarM of string * int 
  | FVarS of string 
  | FConst of num 
  | FBinop of bop * f_expr * f_expr
  | FUop of uop * f_expr 
  | FNil  

(** The type of the abstract syntax tree (AST). *)
type expr =
  | Number of num
  | Binop of bop * expr * expr
  | Matrix of expr list
  | Let_o of string * expr * expr
  | Let of string * expr 
  | Var of string 
  | AnalyticFunc of string * f_expr 
  | EvalAnalyticFuncM of string * (expr list)
  | EvalAnalyticFuncS of string * num 
  | CRoot of string * int
  | Matop of mop * (expr list) 

(** [to_num s] is the num representation of a string [s]. *)
let to_num s = 
  if (String.contains s '.') then 
    {converted=false; value=(float_of_string s)} 
  else
    {converted=true; value=(float_of_string s)}

(** [to_int s] is the int representation of a num [s] 
    Requires: [s.value] is between 1 and 16. *)
let to_int n =
  if n.converted = false then failwith "Invalid precision argument." 
  else 
    let x = int_of_float n.value in 
    if x >= 1 && x <= 16 then x else failwith "Invalid precision argument." 

