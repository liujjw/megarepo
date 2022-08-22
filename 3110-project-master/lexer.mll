{
open Parser
}

let white = [' ' '\t']+
let digit = ['0'-'9']
let number = '-'? digit+ '.'? digit*
let letter = ['a'-'z' 'A'-'Z']
let id = letter+

rule read = 
  parse
  | white { read lexbuf }
  | "fn" { FUNCTION_BEGIN }
  | "sin" { SIN }
  | "cos" { COS }
  | "tan" { TAN }
  | "asin" { ASIN }
  | "acos" { ACOS }
  | "atan" { ATAN }
  | "ln" { LOG_E }
  | "root" { ROOT }
  | "Mat." { MAT_BEGIN }
  | "transpose" { TRANSP }
  | "random" { RAND } 
  | "identity" { MAT_ID }
  | "rref" { MREF }
  | "det" { DET }
  | "let" { LET }
  | "in" { IN }
  | "=" { EQUALS }
  | ":=" { DEFINES }
  | "*" { TIMES }
  | "+" { PLUS }
  | "/" { DIVIDE }
  | "**" { POWER }
  | "(" { LPAREN }
  | ")" { RPAREN }
  | "-" { MINUS }
  | "print" { CMD_PRINT }
  | id {ID (Lexing.lexeme lexbuf) }
  | "[" { LBRAC }
  | "]" { RBRAC }
  | "," { COMMA }
  | number { NUMBER (Lexing.lexeme lexbuf) }
  | eof { EOF }
