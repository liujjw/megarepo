(** Lexes, parses, and evaluates expressions. *)

open Ast
module Mat = ArrayMatrix 

(** [Env] is a module to help with environments, which 
    are maps that have strings as keys. *)
module Env = Map.Make(String)

(** [empty_env] is the empty [env]. *)
let empty_env = Env.empty

(** Type of a 1d array. *)
type row = num array

(** Type of a 2d array. *)
type mat = num array array

type env = value Env.t  
(** [value] is the type of a value *)
and value = 
  | VNum of num
  | VRow of row
  | VMatrix of mat
  | VAnalyticFunc of string * f_expr 

(** [row_to_float r] converts a row to a float array*)
let row_to_float r = Array.map (fun x -> x.value) r

(** [row_to_arrvec r] converts a row to a Mat.Vector*)
let row_to_arrvec r = Mat.Vector (row_to_float r)

(** [row_to_num r] converts a float array to a row*)
let row_to_num r = Array.map (fun x -> {value = x; converted=false}) r

(** [mat_to_float m] converts a mat to a float array array *)
let mat_to_float (m : mat) = 
  Array.map (fun row -> Array.map (fun x -> x.value) row) m

(** [mat_to_arrmat m] converts a mat to a Mat.Matrix*)
let mat_to_arrmat m = Mat.Matrix (mat_to_float m)

(** [mat_to_num m] converts a float array array to a mat*)
let mat_to_num (m : float array array) = 
  Array.map (fun row -> 
      Array.map (fun x -> {value = x; converted=false}) row ) m

(** [arr_to_row r] converts r from type Mat.Vector into a row.*)
let arr_to_row (r : Mat.t) = 
  match r with 
  | Vector v -> row_to_num v
  | Matrix m -> failwith "not a vector"

(** [arr_to_mat m] converts r from type Mat.Matrix into a mat *)
let arr_to_mat (m : Mat.t) = 
  match m with
  | Matrix m -> mat_to_num m
  | Vector v -> failwith "not a matrix"

(** [eval env e] is the [v] such that [<env, e> ==> v]. *)
let rec eval (env : env) (e : expr) : value = 
  match e with 
  | Number n -> VNum n
  | Var x -> eval_var env x 
  | Binop (bop, e1, e2) -> (match (eval env e2) with 
      | VNum n -> eval_bop_num bop (eval env e1) (VNum n)
      | VRow r -> eval_bop_row env bop (eval env e1) r
      | VMatrix m -> eval_bop_mat env bop (eval env e1) m
      | _ -> failwith "Unimplemented")
  | Let (x, e1) -> eval_let env x e1 
  | Let_o (x, e1, e2) -> eval_let_o env x e1 e2
  | Matrix m -> eval_mat env m 
  | AnalyticFunc (vs,f) -> VAnalyticFunc (vs,f)
  | EvalAnalyticFuncM (id,args) -> eval_func env id args 
  | EvalAnalyticFuncS (id,value) -> eval_func_uniform env id value 
  | CRoot (id,n) -> eval_root (Env.find id env) n
  | Matop (mop, m) -> eval_matop env mop (eval_mat env m)

(** [eval_func_uniform env id value] is [v] such that [<env, id(value)> ==> v] 
    with [id] the binding to an analytic function and value a Number. *)
and eval_func_uniform env id value = 
  match Env.find id env, value with 
  | VAnalyticFunc (vs,f), n -> 
    eval_func'_uniform vs f FNil n.value |> eval_func''
  | _ -> failwith "Invalid function evalution." 

(** [eval_func'_uniform vs f acc gv] is [f'] such that 
    [<_, f(vs) := vs(gv)...vs(gv)> ==> 
    <_, f(vs) := gv...gv>]. *)
and eval_func'_uniform vs fexpr acc gv = 
  match fexpr with 
  | FVarM (id,index) -> failwith "Invalid syntax for multivariable function."
  | FVarS id -> if vs <> id then failwith "Invalid variable identifier"
    else FConst {value=gv;converted=false}
  | FConst v -> FConst v 
  | FBinop (bop,fe1,fe2) -> 
    FBinop (bop, eval_func'_uniform vs fe1 acc gv, 
            eval_func'_uniform vs fe2 acc gv) 
  | FUop (uop,fe) -> FUop (uop, eval_func'_uniform vs fe acc gv)
  | FNil -> failwith "Impossible FNil."

(** [eval_root f n] is [v] such that [<_,root(f)> ==> v]. *)
and eval_root fe n = 
  match fe with 
  | VAnalyticFunc (vs,f) -> 
    let module Analy = MonoAnalytic in 
    VNum {value=Analy.root n 1 (Analy.make f) 1.; converted=false} 
  | _ -> failwith "Not an analytic function"

(** [eval_func id args] is [v] such that [<env, id(args)> ==> v] with
    [id] the binding to an analytic function and [args] a one by n matrix. *)
and eval_func env id args = 
  match Env.find id env,eval_mat env args with 
  | VAnalyticFunc (vs,f), VMatrix arr -> 
    (eval_func' vs f arr FNil) |> eval_func''
  | _ -> failwith "Invalid function evalution." 

(** [eval_func' vs f arr] is [f'] such that 
    [<_, f(vs) := vs(arr.(0))...vs(arr.(n))> ==> 
    <_, f(vs) := arr.(0)...arr.(n)>]. *)
and eval_func' vs f arr acc = 
  match f with 
  | FVarM (id,index) -> if vs <> id then failwith "Invalid variable identifier"
    else FConst (arr.(0).(index))
  | FVarS id -> failwith "Invalid syntax for multivariable function."
  | FConst v -> FConst v 
  | FBinop (bop,fe1,fe2) -> 
    FBinop (bop, eval_func' vs fe1 arr acc, eval_func' vs fe2 arr acc) 
  | FUop (uop,fe) -> FUop (uop, eval_func' vs fe arr acc)
  | FNil -> failwith "Impossible FNil."

(** [eval_func'' f] is [v] such that [<_, f> ==> v]. *)
and eval_func'' f = 
  match f with 
  | FVarS _ 
  | FVarM (_,_) -> failwith "Impossible FVar." 
  | FConst value -> VNum value 
  | FBinop (bop,fe1,fe2) -> eval_func_binop bop fe1 fe2 
  | FUop (uop,fe) -> eval_func_uop uop fe
  | FNil -> failwith "Impossible FNil." 

(** [eval_func_uop uop fe] is [v] such that [<_, uop fe> ==> v]. *)
and eval_func_uop uop fe = 
  match uop, eval_func'' fe with 
  | Sin, VNum a -> VNum {value=sin a.value; converted=false}
  | Cos, VNum a -> VNum {value=cos a.value; converted=false}
  | Tan, VNum a -> VNum {value=tan a.value; converted=false}
  | ASin, VNum a -> VNum {value=asin a.value; converted=false}
  | ACos, VNum a -> VNum {value=acos a.value; converted=false}
  | ATan, VNum a -> VNum {value=atan a.value; converted=false}
  | LogE, VNum a -> VNum {value=log a.value; converted=false}
  | _, _ -> failwith "Invalid uop."

(** [eval_func_binop bop fe1 fe2] is [v] such that 
    [<_, fe1 bop fe2> ==> v]. *)
and eval_func_binop bop fe1 fe2 = 
  eval_bop_num bop (eval_func'' fe1) (eval_func'' fe2)

(** [eval_bop_num env bop e1 e2] is the [v] such that 
    [<env, e1 bop e2> ==> v]. Applies to numerical binary operations.*)
and eval_bop_num (bop : Ast.bop) (e1 : value) (e2 : value) =
  match bop, e1, e2 with
  | Add, VNum a, VNum b -> 
    VNum {value=a.value +. b.value; converted=a.converted && b.converted}
  | Mult, VNum a, VNum b -> 
    VNum {value=a.value *. b.value; converted=a.converted && b.converted}
  | Sub, VNum a, VNum b -> 
    VNum {value=a.value -. b.value; converted=a.converted && b.converted}
  | Div, VNum a, VNum b -> if b.value = 0. then failwith "Division by zero."
    else VNum {value=a.value /. b.value; converted=false}
  | Exp, VNum a, VNum b ->
    VNum {value=a.value ** b.value; converted=a.converted && b.converted}
  | _ -> failwith "Faulty application of numerical binary operations"

(** [eval_bop_row env bop e1 e2] is the [v] such that 
    [<env, e1 bop e2> ==> v]. Applies to vector binary operations.*)
and eval_bop_row env bop e1 e2 = 
  match bop, e1, VRow e2 with
  | Add, VRow a, VRow b -> 
    VRow (arr_to_row (Mat.add (row_to_arrvec a) (row_to_arrvec b)))
  | Sub, VRow a, VRow b -> 
    VRow (arr_to_row (Mat.subtract (row_to_arrvec a) (row_to_arrvec b)))
  | Mult, VNum a, VRow b -> 
    VRow (arr_to_row (Mat.scale a.value (row_to_arrvec b)))
  | Mult, VMatrix a, VRow b -> 
    failwith "Please only attempt matrix multiplication for matrices! "
  | Mult, VRow a, VRow b -> 
    failwith "Please only attempt matrix multiplication for matrices! "
  | _ -> failwith "Faulty application of vector binary operations"

(** [eval_bop_mat env bop e1 e2] is the [v] such that 
    [<env, e1 bop e2> ==> v]. Applies to matrix binary operations.*)
and eval_bop_mat env bop e1 e2 = 
  match bop, e1, VMatrix e2 with
  | Add, VMatrix a, VMatrix b -> 
    VMatrix (arr_to_mat (Mat.add (mat_to_arrmat a) (mat_to_arrmat b)))
  | Sub, VMatrix a, VMatrix b -> 
    VMatrix (arr_to_mat (Mat.subtract (mat_to_arrmat a) (mat_to_arrmat b)))
  | Mult, VNum a, VMatrix b -> 
    VMatrix (arr_to_mat (Mat.scale a.value (mat_to_arrmat b)))
  | Mult, VRow a, VMatrix b -> 
    failwith "Please only attempt matrix multiplication for matrices! "
  | Mult, VMatrix a, VMatrix b -> 
    VMatrix (mat_to_num (Mat.mult (mat_to_float a) (mat_to_float b)))
  | _ -> failwith "Faulty application of matrix binary operations"

(** [eval_bop env bop e1 e2] is the [v] such that [<env, e1 bop e2> ==> v]. *)
and eval_bop env bop e1 e2 = 
  eval_bop_num bop (e1) (e2) 

(** [eval_matop env mop e] is the [v] such that [<env, mop e> ==> v]. *)
and eval_matop env mop e = match mop, e with 
  | Tran, VRow r -> VRow (arr_to_row (Mat.transpose (row_to_arrvec r)))
  | Tran, VMatrix m -> VMatrix (arr_to_mat (Mat.transpose (mat_to_arrmat m)))
  | Rand, VRow r -> VMatrix (eval_mat_rand r) 
  | Id, VRow r -> VMatrix (eval_mat_id r)
  | Mref, VRow r -> 
    failwith "Please attempt row reduction for matrices only"
  | Mref, VMatrix m -> VMatrix (mat_to_num (Mat.rref (mat_to_float m)))
  | Det, VRow r -> 
    failwith "Please attempt to find the determinant of matrices only"
  | Det, VMatrix m -> 
    VNum ({converted=false; value= (Mat.det (mat_to_float m))})
  | _ -> failwith "Invalid input for matrix operations"

(** [eval_mat_rand env mop m] checks if [m] is a vector with three entries and 
    calls Mat.rand with those arguments. *)
and eval_mat_rand m = 
  if (Array.length m) = 3 
  then 
    (let b = int_of_float m.(0).value in 
     let row = int_of_float m.(1).value in 
     let col = int_of_float m.(2).value in 
     to_num_array (Mat.rand b row col) )     
  else failwith "Invalid input for random matrix generation"

(** [to_num_array m] converts [m] from an int matrix to a num matrix. *)
and to_num_array (m : int array array) = 
  Array.map (fun row -> 
      Array.map (fun x -> {value = float_of_int x; converted=false}) row ) m

and eval_mat_id m = if (Array.length m) = 1
  then (let n = int_of_float m.(0).value in mat_to_num (Mat.id n))
  else failwith "Invalid input for identity matrix generation"

(** [eval_var env x] is the [v] such that [<env, x> ==> v], 
    The local environment takes precedence over the 
    global static scope (lexical scoping). *)
and eval_var env x = 
  try Env.find x env
  with Not_found -> failwith "Unbound variable." 

(** [eval_let_o env x e1 e2] is the [v] such that
    [<env, let x = e1 in e2> ==> v]. *)
and eval_let_o env x e1 e2 =
  let v1 = eval env e1 in
  let env' = Env.add x v1 env in
  eval env' e2

(** [eval_let env x e1] is [<env, let x = e1> ==> <env[x |-> e1]>].*)
and eval_let env x e1 =
  let v1 = eval env e1 in
  let env' = Env.add x v1 env in 
  let () = run env' in v1 

(** [eval_mat env x] is the [v] such that [<env, x] ==> [v] *)
and eval_mat env x = 
  match x with 
  | [] -> failwith "Empty matrix"
  | h::t -> let eval_h = eval env h in match eval_h with
    | VNum n -> eval_vect env x
    | VRow r -> eval_mat_help env x
    | VMatrix m -> failwith "Too many nested lists; consider removing
    a set of brackets."
    | _ -> failwith "Not a matrix"

(** [eval_vect env x] is the [v] such that [<env, vec] ==> [v] *)
and eval_vect env vec = 
  let len = List.length vec in 
  let arr = Array.make len ({converted=false;value=0.}) in 
  let y = List.map (fun x -> (eval env x)) vec in 
  let _ = List.iteri (fun index x -> 
      match x with 
      | VNum a -> arr.(index) <- a
      | _ -> failwith "Not a number" ) y
  in VRow arr

(** [eval_mat_help env m] takes [m], evaluates each row, and constructs 
    a VMatrix*)
and eval_mat_help env m = 
  let len = List.length m in
  let mat = Array.make len (Array.make len ({converted=false;value=0.})) in 
  let y = List.map (fun x -> eval env x) m in 
  let _ = List.iteri (fun index x -> 
      match x with 
      | VRow a -> mat.(index) <- a
      | VMatrix b -> failwith "Invalid input"
      | _ -> failwith "Not a vector or matrix" ) y
  in VMatrix mat

(** [parse s] parses [s] Numbero an AST. *)
and parse (s : string) : expr =
  let lexbuf = Lexing.from_string s in
  let ast = Parser.prog Lexer.read lexbuf in
  ast

(** [add_brackets s] returns the string "[s]" given "s". *)
and add_brackets (s : string) : string = 
  "[" ^ s ^ "]"

(** [string_of_row r] converts [r] to a string. 
    Requires: [r] is a row.*)
and string_of_row (r : row) : string = 
  let list = Array.to_list r in  
  let string = (List.map (fun x -> (string_of_float x.value)) list) in 
  let string_2 = String.concat "," string in 
  add_brackets string_2

(** [string_of_row m] converts [m] to a string. 
    Requires: [m] is a matrix.*)
and string_of_matrix (m : mat) : string = 
  let list = Array.to_list m in 
  let string = List.map (fun x -> string_of_row x) list in 
  let string_2 = String.concat "," string in 
  add_brackets string_2

(** [string_of_val e] converts [e] to a string.
    Requires: [e] is a value. *)
and string_of_val (v : value) : string =
  match v with
  | VNum n -> if n.converted then int_of_float n.value |> string_of_int else 
      string_of_float n.value
  | VRow r -> string_of_row r 
  | VMatrix m -> string_of_matrix m 
  | VAnalyticFunc _ -> "<analytic function>"

(** [interp s] interprets [s] by lexing and parsing it, 
    evaluating it, and converting the result to a string. *)
and interp (base_env : env) (s : string) : string =
  s |> parse |> eval base_env |> string_of_val 

(** [run env] starts a REPL with [env]. *)
and run env = 
  ANSITerminal.(print_string [cyan] ">> ");
  match read_line () with 
  | input -> try print_endline (interp env input); run env with e -> 
    let msg = (Printexc.to_string e) in 
    print_endline ("There was an error: " ^ msg); run env 
