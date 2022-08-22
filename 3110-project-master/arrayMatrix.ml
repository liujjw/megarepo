(** Implementation of Matrix with arrays. *)

open Matrix 

(** The type of an array matrix. *)
type t = Matrix of float array array | Vector of float array 

(** AF: [|[|a11;a12;...;a1n|];
         [|a21;a22;...;a2n|];
         ...;
         [|am1;am2;...;amn|]|] represents the m by n matrix with aij as entries. 
    RI: The length of each array in the array of float arrays should be the 
    same length. 0x0 matrices allowed. *)
let rep_ok (x : t) : t = match x with 
  | Vector v -> Vector v
  | Matrix m -> let len = Array.length (m.(0)) in 
    if ( Array.for_all (fun x -> len = Array.length x) m ) then Matrix m else 
      failwith "Representation invariant for matrices violated"

(** [same_dim a b] checks if [a] and [b] have the same dimensions. 
    Requires: [a] and [b] are valid vectors or matrices.*)
let same_dim (a : t) (b : t) : bool = 
  let a = rep_ok a in 
  let b = rep_ok b in 
  match a, b with 
  | Vector a, Vector b -> Array.length a = Array.length b
  | Matrix a, Matrix b -> 
    let len_col = Array.length a in
    let len_row = Array.length a.(0) in 
    (len_col = Array.length b) && (len_row = Array.length b.(0))
  | _ -> false 

(** [op a b f] is [f] applied to each pair of corresponding elements from
    [a] and [b].*)
let op a b f : t =  
  if not (same_dim a b) then failwith "Wrong dimensions" else 
    match a, b with
    | Vector a, Vector b -> (let _ = Array.iteri (fun index elt -> 
        a.(index) <- (f a.(index) b.(index))) b in Vector a) 
    | Matrix a, Matrix b -> 
      (let _ = Array.iteri (fun row_num b_row -> 
           Array.iteri (fun col_num b_elt -> a.(row_num).(col_num) <- 
                           (f a.(row_num).(col_num) b_elt)) b_row) 
           b in Matrix a)
    | _ -> failwith "Wrong dimensions" 

let add a b = 
  op a b (+.)

let subtract a b = 
  op a b (-.)

let scale s m : t =
  let m = rep_ok m in
  match m with 
  | Vector v -> Vector (Array.map (fun x -> s *. x) v)
  | Matrix m ->  
    let _ = Array.iteri (fun r row -> Array.iteri 
                            (fun c elt -> m.(r).(c) <- 
                                s *. elt) row) m in Matrix m

let transpose a : t = 
  let a = rep_ok a in
  match a with 
  | Vector v -> let m = Array.length v in 
    let t = Array.make_matrix 1 m 0. in 
    let _ = Array.iteri (fun i elt -> t.(1).(i) <- elt) v in Matrix t
  | Matrix x -> 
    let m = Array.length x.(0) in 
    let n = Array.length x in 
    let t = Array.make_matrix m n 0. in 
    let _ = Array.iteri (fun r row -> Array.iteri 
                            (fun c elt -> t.(c).(r) <- elt) row) x in Matrix t

(* [transpose_m a] is the transpose of [a]. To be used internally in [mult] *)
let transpose_m a = 
  if Array.length a = 0 then a else 
    let m = Array.length a.(0) in 
    let n = Array.length a in 
    let t = Array.make_matrix m n 0. in 
    let _ = Array.iteri (fun r row -> Array.iteri 
                            (fun c elt -> t.(c).(r) <- elt) row) a in t 

let mult m1 m2 = 
  if (Array.length m1.(0)) <> (Array.length m2) 
  then failwith "Invalid matrix dimensions for multiplication"
  else
    let m = Array.length m1 in
    let n = Array.length m2.(0) in 
    let p = Array.make_matrix m n 0. in 
    let m2t = transpose_m (m2) in 
    let _ = Array.iteri (fun r row -> 
        Array.iteri (fun c elt -> p.(r).(c) <- 
                        let l1 = Array.to_list m1.(r) in 
                        let l2 = Array.to_list m2t.(c) in 
                        let dot a b c = a +. (b*.c) in 
                        List.fold_left2 dot 0. l1 l2
                    ) row) p in p  

let id n = 
  let mat = Array.make_matrix n n 0. in 
  let _ = (for i = 0 to n-1 do
             mat.(i).(i) <- 1.0
           done) in mat

(** [rand_int b] generates a random integer from 0 inclusive to b exclusive. *)
let rand_int b = let _ = Random.self_init in Random.int (b)

let rand b m n : int array array = 
  let mat = Array.make_matrix m n (0) in 
  let _ = Array.iteri (fun r row -> Array.iteri 
                          (fun c elt -> mat.(c).(r) <- 
                              (rand_int b)) row) mat in mat

(** [swap a b m] switches rows [a] and [b] in matrix [m]. *)
let swap m a b = 
  let temp = m.(a) in 
  m.(a) <- m.(b); m.(b) <- temp 

(** [add m row1 row2 const] adds the scalar multiple of [row2] to [row1]. *)
let row_add m row1 row2 const = 
  for i = 0 to (Array.length m.(0)) - 1 do 
    m.(row1).(i) <- m.(row1).(i) +. m.(row2).(i) *. const
  done

let rref a = 
  let len = Array.length (a.(0)) in 
  let _ = (if ( Array.for_all (fun x -> len = Array.length x) a ) then a else 
             failwith "Representation invariant for matrices violated") in
  let _ = (let m, n = Array.length a, Array.length a.(0) in
           for c = 0 to min (n-1) (m-1) do
             for r = c+1 to m-1 do
               if abs_float a.(c).(c) < abs_float a.(r).(c) then
                 swap a r c
             done;
             let t = a.(c).(c) in 
             if t <> 0.0 then             
               (for r = 0 to m-1 do 
                  if r <> c then row_add a r c (-.a.(r).(c)/.t) 
                done;
                for i = 0 to n-1 do 
                  a.(c).(i) <- a.(c).(i)/.t 
                done)             
           done) in a

(** [is_square m] is true if [m] is a square matrix. *)
let is_square m = 
  Array.length m.(0) = Array.length m

let det a : float = 
  if not (is_square a) 
  then failwith "Cannot find determinant of non-square matrix"
  else match Array.length a with 
    |0 -> failwith "Empty matrix"
    |1 -> a.(0).(0)
    |2 -> a.(0).(0) *. a.(1).(1) -. a.(0).(1) *. a.(1).(0)
    |3 -> a.(0).(0) *. a.(1).(1) *. a.(2).(2) 
          +. a.(0).(1) *. a.(1).(2) *. a.(2).(0) 
          +. a.(0).(2) *. a.(1).(0) *. a.(2).(1) 
          -. a.(0).(2) *. a.(1).(1) *. a.(2).(0) 
          -. a.(0).(1) *. a.(1).(0) *. a.(2).(2) 
          -. a.(0).(0) *. a.(1).(2) *. a.(2).(1)
    |_ -> failwith "Not supported! :( Please use smaller matrices."






