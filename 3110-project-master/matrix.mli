(** Matrix abstraction from matrix algebra: [mxn] grids of values. *)

(** The type of a matrix. *)
type t 

(** [add m1 m2] is the sum of matrices [m1] and [m2].
    Requires: Matrices have correct dimensions. *)
val add : t -> t -> t 

(** [subtract m1 m2] is the difference of matrices [m1] and [m2].
    Requires: Matrices have correct dimensions. *)
val subtract : t -> t -> t

(** [mult m1 m2] is the matrix product of [m1] and [m2]. 
    Requires: Matrices have orrect dimensions. *)
val mult : t -> t -> t

(** [scale r m1] is the scalar multiple of [m1] by [r]. *)
val scale : float -> t -> t 

(** [transpose m1] is the transpose of [m1]. *)
val transpose : t -> t 

(** [id n] is the [n]x[n] identity matrix. *)
val id : int -> t

(** [rand i n m] is the random [n]x[m] matrix with ints from 
    0 to [i]. *)
val rand : int -> int -> int array array

(** [rref t] is the reduced-row echelon form of matrix [t]. *)
val rref : t -> t

(** [det a] is the determinant of matrix [a].
    Requires: Matrix is square. *)
val det : t -> int 

