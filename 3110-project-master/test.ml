(** 
   Testing Strategy

   We have OUnit tests for almost all the calculator functionality, either 
   explicitly or implicitly. The four lists [arith_tests], [mat_tests],
   [analytic_fun_tests], and [calculus_tests] test the main calculations we
   implemented (largely, floats, non-add or multiply arithmetic, matrix 
   operations, and analytic function operations). In testing these features, we
   also get to test features such as let expressions and variables for free. 
   However, the feature global bindings was tested on the REPL, as well as the
   REPL itself. 

   All modules we coded in, including ocamllex and menhir files, get run at some
   level of the tests, since the nature of the tests is that they require that
   the whole module stack be working properly. All of the OUnit tests are 
   black box, asserting that some user input to the REPL interpreter of the full 
   program and resulting output matches some processed output. 

   The tests demonstrate correctness because they exhaustively simulate most of 
   what a user would enter into our program, including some malformed inputs. 
   Pathological cases within the domain of the math that we implement, such as 
   matrices with the wrong dimensions or polynomials with no roots, are also 
   tested. As mentioend, with each test, we effectively test the full parsing
   engine of our program, minus specific features.
   Thus if our tests are passing, it is highly likely that whatever a 
   user enters into the actual program it will result in intended behavior. 
*)

open OUnit2
open Ast
open Main
open Matrix

(** [make_e n e s] makes an OUnit test named [n] that expects [s] to
    throw error [e]. *)
let make_e n e s = 
  n >:: (fun _ -> assert_raises e (fun () -> interp empty_env s))

(** [make_i n i s] makes an OUnit test named [n] that expects
    [s] to evalute to [Int i]. *)
let make_i n i s =
  n >:: (fun _ -> assert_equal (string_of_int i) (interp empty_env s))

(** [make_f n i s] makes an OUnit test named [n] that expects
    [s] to evalute to [Float i]. *)
let make_f n f s =
  n >:: (fun _ -> assert_equal (string_of_float f) (interp empty_env s))

let arith_tests = [
  (* from Prof. Foster's demo code *)
  make_i "int" 22 "22";
  make_i "add" 22 "11+11";
  make_i "adds" 22 "(10+1)+(5+6)";
  make_i "mul1" 22 "2*11";
  make_i "mul2" 22 "2+2*10";
  make_i "mul3" 14 "2*2+10";
  make_i "mul4" 40 "2*2*10"; 
  (* end *)

  make_f "float" 69.99 "69.99";
  make_f "float2" 69. "  69.  ";
  make_f "addf2 w/ ints" 24.009999999999998 "(10.2+1)+(5.92+6.89)";
  make_f "mulf" 26.2924712634302971 "2.7+2.31*10.21319102313";
  make_f "addf3 w/ ints" 9.5 "6+3.5";

  make_f "zero" 0. "1.5 * 0.0";
  make_e "div_zero" (Failure "Division by zero.") "1/0";

  make_f "divint" 2. "6/3";
  make_f "divfloat" 1.5 "3/2";
  make_f "divfloat2" 1.5 "3.75/2.5";
  make_f "divfloat w/ ints" 18.5 "55.5/3";

  make_i "expint" 9 "3**2";
  make_f "expfloat" 2.75567596063107523 "1.5**2.5";

  make_f "pemdasint" 27. "(1+2)**2*6/2"; 
  make_f "pemdasfloat" 40.5 "3/2*3**(2+1)";
]

(** [make_v n i s] makes an OUnit test named [n] that expects
    [s] to evalute to [row i]. *)
let make_v n (f : float array) s =
  n >:: (fun _ -> assert_equal 
            (string_of_row (Main.row_to_num f)) (interp empty_env s))

(** [make_v n i s] makes an OUnit test named [n] that expects
    [s] to evalute to [matrix i]. *)
let make_m n (f : float array array) s =
  n >:: (fun _ -> assert_equal 
            (string_of_matrix (Main.mat_to_num f)) (interp empty_env s))

let mat_tests = [
  make_v "vec-add" [|4.;6.|] "[1,2] + [3,4]";
  make_v "vec-sub" [|1.;1.|] "[5,3] - [4,2]";
  make_v "vec-scale" [|4.;6.|] " 2 * [2, 3]";

  make_m "mat-add" [|[|2.;4.|];[|6.;8.|]|] "[[1,2],[3,4]] + [[1,2],[3,4]]";
  make_m "mat-sub" [|[|2.;3.|];[|1.;2.|]|] "[[4,4],[2,2]] - [[2,1],[1,0]]";
  make_m "mat-scale" [|[|2.;4.|];[|6.;8.|]|] "2 * [[1,2],[3,4]]";
  make_m "mat-mult" [|[|13.;11.|];[|17.;16.|]|] "[[2,3],[1,5]] * [[2,1],[3,3]]";

  make_e "mat-mult-vec" 
    (Failure "Please only attempt matrix multiplication for matrices! ") 
    "[[1,2],[3,4]] * [1,2,3] ";
  make_e "wrong-mat-dim" 
    (Failure "Invalid matrix dimensions for multiplication") 
    "[[1,2]] * [[1,2]] ";

  make_m "mat-trans" [|[|1.;3.|];[|2.;4.|]|] "Mat.transpose([1,2],[3,4])";
  make_m "mat-trans v to m" [|[|1.|];[|2.|]|] "Mat.transpose([1,2])";
  make_m "mat-trans m to v" [|[|1.;2.|]|] "Mat.transpose([1],[2])";

  make_m "mat-id" [|[|1.;0.;0.|];[|0.;1.;0.|];[|0.;0.;1.|]|] 
    "Mat.identity(3)";

  make_m "rref 2x2" [|[|1.;0.|];[|0.;1.|]|] "Mat.rref([1,2],[3,4])";
  make_m "rref 3x3" [|[|1.;0.;0.|];[|0.;1.;0.|];[|0.;0.;1.|]|] 
    "Mat.rref([0,1,3],[-1,-3,3],[1,-3,0])";
  make_m "rref 4x4" 
    [|[|1.;0.;0.;0.|];[|0.;1.;0.;0.;|];[|-0.;-0.;1.;0.|];[|0.;0.;0.;1.;|] |]
    "Mat.rref([-1,1,-1,-2],[2,0,0,3],[-1,0,-1,-2],[-3,0,2,-1])";
  make_m "rref 3x2" [|[|1.;0.|];[|0.;1.|];[|0.;0.|]|] 
    "Mat.rref([-1,1],[-1,2],[-3,2])";

  make_f "det 2x2" 5. "Mat.det([5,5],[5,6])";
  make_f "det 3x3" 0. "Mat.det([2,3,1],[-1,2,3],[3,2,-1])";
]

(** [make_af n i s] makes an OUnit test named [n] that expects
    [i] to evalute to [s]. *)
let make_af n s v =
  n >:: (fun _ -> assert_equal (interp empty_env s) v)

let analytic_fun_tests = [
  make_af "simple multi" 
    "let f = (fn(x) := x(0) + x(1) - x(3)**2) in f([[-1,0,1,2]])" "-5";
  make_af "simple trig" "let g = (fn(x) := sin(x(0))) in g([[0]])" "0.";
  make_af "simple trig2" "let g = (fn(x) := asin(sin(x(0)))) in g([[0]])" "0.";
  make_af "simple trig3" 
    "let g = (fn(x) := tan( x(0) )**cos( x(1) ) ) in g([[0,1]])" "0.";
  make_af "simple trig4" 
    "let g = (fn(x) := tan( x(0) )**cos( x(1) )/ acos( x(2) ) ) in g([[0,1,0]])" 
    "0.";
  make_af "exponential" "let f = (fn(x) := 2**x + 1) in f(4)" "17.";
  make_af "log" 
    "let g = (fn(x) := ln(x(0))) in g([[1]])" "0.";
  make_af "quadratic" 
    "let f = (fn(x) := x**2 + x - 5) in f(2)" "1."; 
  make_af "quadratic2" 
    "let f = (fn(x) := 0.5*x**2 + x - 5) in f(2)" "-1."; 
  make_af "one var trig" 
    "let f = (fn(x) := 9 * cos(x) - sin(x)) in f(0)" "9."; 
  make_af "id" "let f = (fn(x) := x) in f(4)" "4."; 
  make_af "line" "let f = (fn(x) := 1 + x) in f(4)" "5."; 
]

(** [make_c n i s] makes a test named [n] that expects [i] to evaluate
    to one of [s]. *)
let make_c n i s =
  n >:: (fun _ -> 
      if List.exists 
          (fun root -> (string_of_float root) = (interp empty_env i)) s 
      then  assert_equal 0 0 else assert_equal 0 1
    )

(** [make_cf n i] makes a test named [n] that expects [i] to fail. *)
let make_cf n i = 
  n >:: (fun _ -> assert_raises 
            (Failure "Could not find a root.") (fun () -> (interp empty_env i)))

let calculus_tests = [
  make_c "quadratic with root" "let f = (fn(x) := x**2 - 2) in root(f,11)" 
    [1.41421356237;-1.41421356237]; 
  make_cf "parabola no root" "let f = (fn(x) := x**2 + 4) in root(f,11)";
  make_c "cubic" "let f = (fn(x) := x**3 - x + 1) in root(f,11)"
    [-1.32471795724]; (*only finds real roots *)
  make_cf "horizontal" "let f = (fn(y) := 3) in root(f,11)";
  make_c "higher degree polynomial" 
    "let f = (fn(y) := y**7 + 3*y - 7 + y**4) in root(f,11)"
    [1.11348952323];
  make_c "line id" 
    "let f = (fn(y) := y) in root(f,1)"
    [0.0];
  make_c "line" 
    "let f = (fn(y) := 4*y - 4) in root(f,1)"
    [1.0];
  make_c "line more precision" 
    "let f = (fn(y) := 4*y - 4) in root(f,14)"
    [1.00000000000000];
  make_c "higher degree polynomial less precision" 
    "let f = (fn(y) := y**7 + 3*y - 7 + y**4) in root(f,2)"
    [1.11];
  make_c "higher degree polynomial higher precision" 
    "let f = (fn(y) := y**7 + 3*y - 7 + y**4) in root(f,16)"
    [1.1134895232336382];
  make_c "root at 1004" "let f = fn(x) := x - 1004 in root(f,3)" [1004.000];
  make_c "very large root" "let f = fn(x) := x - 3912461941 in root(f,13)" 
    [3912461941.0000000000000];
  make_c "very small root" "let f = fn(x) := x - 0.00023 in root(f,5)" 
    [0.00023];
]

let _ = run_test_tt_main ("arithemtic" >::: arith_tests)
let _ = run_test_tt_main ("matrices" >::: mat_tests)
let _ = run_test_tt_main ("analytic" >::: analytic_fun_tests)
let _ = run_test_tt_main ("calculus" >::: calculus_tests)

