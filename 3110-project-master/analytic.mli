(** Analytic functions: algebraic functions which can be approximated
    locally by power series. *)

open Ast 

(** The type of an analytic function. *)
type t

(**  [make f] is the [t] representation of an f_expr [f]. *)
val make : f_expr -> t 

(** [root e t] is a root of [t] up to [e] digits. 
    Requires: [t] is a single variable function that is supported. 
              1 < [e] <= 16
    Raises: failure("Could not find a root.") if no root could be found. *)
val root : int -> t -> float 