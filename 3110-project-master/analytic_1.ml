open Ast

type t = f_expr

let deriv t = 
  match t with 
  | FVar (a,b) -> FVar (a,b)
  | _ -> ()

let root t = 
  let x0 = 0 in 
  if 