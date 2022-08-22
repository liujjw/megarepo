(** Implementation of Analytic on a subset of polynomials. *)

open Ast

(** The type of a subset of the mononomial expressions. *)
type mononomial = {
  neg: bool;
  const: float;
  var: string;
  power: int 
}

(** AF: [mono_1;mono_2;...;mono_n] represents the polynomial 
    [mono_1 + mono_2 + ... + mono_n], where [mono_i] represents the mononomial 
    [(-)cx^k].

    RI: Each [mono_i] in [t] represents an element in the subset of standard 
    form mononomials, ie (-)cx^k, where c is a real constant, x is a variable, 
    and k is a nonegative integer power. No division, multiplcation, more than 
    one variables, etc are allowed. The empty list is a possible polynomial. *)
type t = mononomial list 

(** [top_neg sll] is [sll'] such that its first mononomial is negative. 
    Requires: sll has >= 1 mononomials.  *)
let rec top_neg sll = 
  let s = List.hd sll in {s with neg=true}::(List.tl sll) 

and make fexpr = 
  match fexpr with 
  | FConst num -> {neg=false;const=num.value;var="";power=1}::[]
  | FVarS s -> {neg=false;const=1.0;var=s;power=1}::[]
  | FBinop (bop,e1,e2) -> begin 
      match bop with 
      | Add -> (make e2)@(make e1)
      | Sub -> (make e2 |> top_neg)@make e1
      | Mult -> mult e1 e2
      | Div -> failwith "Not a supported polynomial expression." 
      | Exp -> exp e1 e2
    end 
  | _ -> failwith "Only polynomials please." 

(** [exp e1 e2] is e1 ^ e2. *)
and exp e1 e2 = 
  match (make e1, make e2) with 
  | fe1, fe2 -> begin 
      if List.length fe1 <> 1 || List.length fe2 <> 1 
      then failwith "Not a supported polynomial expression." 
      else
        begin 
          match List.hd fe1, List.hd fe2 with 
          | {const=1.0;neg=false;var=a;power=1},
            {const=b;neg=false;var="";power=1} -> 
            {const=1.0;neg=false;var=a;power=int_of_float b}::[]
          | _,_ -> failwith "Not a supported polynomial expresssion." 
        end 
    end 

(* [mult' e1 e2] is [mult e1' e2'] after processing. *)
and mult' e1 e2 = 
  match List.hd e1, List.hd e2 with 
  | {const=a;neg=b;var="";power=1},
    {const=1.0;neg=false;var=c;power=d} -> 
    {const=a;neg=b;var=c;power=d}::[]
  | _,_ -> failwith "Not a supported polynomial expresssion." 

(** [mult e1 e2] is e1 * e2. *)
and mult e1 e2 = 
  match (make e1, make e2) with 
  | fe1, fe2 -> begin 
      if List.length fe1 <> 1 || List.length fe2 <> 1 
      then failwith "Not a supported polynomial expression." 
      else mult' fe1 fe2
    end 

(** [deriv_rules e] is the derivative of a single mononomial. *)
let deriv_rules e = 
  match e with 
  | {const=_;neg=_;var="";power=1} -> None 
  | {const=a;neg=b;var=c;power=1} -> Some {const=a;neg=b;var="";power=1}
  | {const=a;neg=b;var=c;power=d} -> Some {
      const=Float.mul a (float_of_int d);
      neg=b;
      var=c;
      power=d-1;
    }

(** [deriv t] is the derivative of [t], with respect to a single variable.*)
let rec deriv poly = 
  match poly with 
  | [] -> []
  | h::t -> begin 
      match (deriv_rules h) with 
      | None -> (deriv t) 
      | Some r -> r::(deriv t)
    end

(** [eval' t x] is the [v] such that [eval' t x = v], where [t] is a
    mononomial. *)
let eval' m x = 
  match m with 
  | {const=a;neg=b;var="";power=1} -> if b then Float.neg a else a
  | {const=a;neg=b;var=_;power=p} -> begin 
      let r = Float.mul a (Float.pow x (float_of_int p)) in 
      if b then r |> Float.neg else r
    end 

(** [eval t x] is the [v] such that [eval t x = v]. *)
let rec eval t x = 
  match t with 
  | [] -> 0.
  | h::t -> eval' h x +. eval t x 

(** [iterations] is the number of iterations to run. *)
let iterations = 100;;

(** [tries] is the number of tries to run before giving up. *)
let tries = 10;;

(** [generate bound] is a "best guess" to begin calculating the root using 
    Newton's method, where [bound] is the positive bound to guess. *)
let generate bound = 
  let _ = Random.self_init in 
  if Random.bool () then Random.float bound 
  else Random.float bound |> Float.neg

(** [next_bound prev] is the next bound size from [prev]. *)
let next_bound prev = 
  Float.mul prev 10.  

(** The acceptable error of f(root) to 0. *)
let zero_error = 0.001;;

(** [root' d f x n o] is [root d n f]. *)
let rec root' digits f x n upper_n bound = 
  let fx = eval f x in 
  if n > iterations then (check fx x zero_error digits n f bound) 
  else 
    let f' = deriv f in 
    let f'x = eval f' x in 
    if (fx = 0. && f'x = 0.) || (f'x = 0.) 
    then root digits (upper_n+1) f bound 
    else 
      let x' = x -. (fx /. f'x) in 
      root' digits f x' (n + 1) upper_n bound

(** [check fx x error] is [x] to [digits] of decimal precision
    if [fx] is within [error] of [0]. *)
and check fx x error digits n f bound = 
  if Float.abs fx < error then  
    let s = string_of_float x in 
    let s' = String.split_on_char '.' s in 
    let whole,frac = s' |> List.hd, s' |> List.tl |> List.hd in 
    if String.length frac > digits then 
      let frac' = String.sub frac 0 digits in  
      let s'' = whole ^ "." ^ frac' in float_of_string s''
    else 
      let zeroes = String.make (digits - String.length frac) '0' in 
      let s'' = whole ^  "." ^ frac ^ zeroes in float_of_string s''
  else 
    root digits (n+1) f bound

and root digits n f bound = 
  if n > tries then failwith "Could not find a root." 
  else
    let bound' = next_bound bound in 
    let x = generate bound' in root' digits f x 1 n bound'

