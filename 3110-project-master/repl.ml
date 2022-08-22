open Main 
open ANSITerminal

(** [repl ()] starts a REPL. *)
let repl () = 
  ANSITerminal.(print_string [cyan] "
  =========================================================================
  =========================================================================
         __________
        | ________ |
        ||hello wo||    (• ◡ •) / Welcome to the Calculator. 
        |----------|
        |[M|#|C][-]|    You can evaluate arithmetic, matrix, and polynomial  
        |[7|8|9][+]|    expressions and bind them to variables. 
        |[4|5|6][x]|  
        |[1|2|3][%]|    See [test.ml] for examples!
        |[.|O|:][=]|
  =========================================================================
  =========================================================================\n"); 

  run empty_env

let () = repl ()