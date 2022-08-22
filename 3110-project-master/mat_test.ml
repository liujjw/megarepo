open OUnit2
open Ast
open Main
open Matrix

(** [make_i n i s] makes an OUnit test named [n] that expects
    [s] to evalute to [i]. *)
let make_nointerp n m1 m2 =
  n >:: (fun _ -> assert_equal (m1) (m2))

let tests = [
  make_nointerp "mat-add" [[2;0];[0;2]] Matrix.add [[1;0];[0;1]] [[1;0];[0;1]] 
]

let _ = run_test_tt_main ("matrix suite" >::: tests)
