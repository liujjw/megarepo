open Ast
open Main 

(** RI: 
    AF: [["a";"b";"c"];["e";"f";"g"]] represents the analytic function 
        [abc + efg], where for example we could have [abc] and [efg] such that 
        they represent [-5x + 3x^2]. *)
type t = (string list) list 

(** [top sll tok] is [sll'] with [tok] prepended to the first string list of 
    [sll].
    Requires: sll has >= 1 string list.  *)
let rec top tok sll = 
  (List.hd sll |> List.cons tok) :: (List.tl sll)

(** [mult e1 e2] is [t] such that AF(t) -> e1*e2. *)
and mult v e1 e2 = 
  match (make (v,e1), make (v,e2)) with 
  | fe1, fe2 -> begin 
      if List.length fe1 <> 1 || List.length fe2 <> 1 
      then failwith "No grouping please. " 
      else 
        let fe1',fe2' = List.hd fe1, List.hd fe2 in [fe2' @ fe1']
    end 

(** [make f] is the [t] representation of an f_expr [f]. *)
and make (v,fexpr) = 
  match fexpr with 
  | FConst num -> ((string_of_float num.value)::[])::[] 
  | FVar (s,_) -> if s <> v then failwith "Variables don't match" else 
      (s::[])::[] 
  | FBinop (bop,e1,e2) -> begin 
      match bop with 
      | Add -> make (v,e2) @ make (v,e1)
      | Sub -> (make (v,e2) |> top "-") @ make (v,e1)
      | Mult -> mult v e1 e2
      | Div -> failwith "" 
      | Exp -> failwith ""
    end 
  | _ -> failwith "Only polynomials please." 

(** [deriv t] is the derivative of [t], with respect to a single variable.*)
let deriv t = t

let rec root d t = 
  let t' = deriv t in 
  1.0