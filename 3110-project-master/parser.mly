%{
open Ast
%}

%token SIN COS TAN ACOS ASIN ATAN LOG_E ROOT 
%token DEFINES
%token FUNCTION_BEGIN
%token MAT_BEGIN
%token TRANSP RAND MAT_ID MREF DET
%token <string> NUMBER
%token TIMES PLUS MINUS DIVIDE POWER
%token <string> ID 
%token LPAREN RPAREN
%token LET EQUALS IN 
%token LBRAC RBRAC
%token COMMA
%token CMD_PRINT
%token EOF

%nonassoc IN
%left PLUS
%left MINUS
%left TIMES
%left DIVIDE
%left POWER

%start <Ast.expr> prog

%%

prog:
	| e = expr; EOF { e }
	;

f_expr:
	| SIN; LPAREN; f = f_expr; RPAREN { FUop (Sin, f) }
	| COS; LPAREN; f = f_expr; RPAREN { FUop (Cos, f) }
	| TAN; LPAREN; f = f_expr; RPAREN { FUop (Tan, f) }
	| ASIN; LPAREN; f = f_expr; RPAREN { FUop (ASin, f) }
	| ACOS; LPAREN; f = f_expr; RPAREN { FUop (ACos, f) }
	| ATAN; LPAREN; f = f_expr; RPAREN { FUop (ATan, f) }
	| LOG_E; LPAREN; f = f_expr; RPAREN { FUop (LogE, f) }
	| v = ID { FVarS (v) } 
	| v = ID; LPAREN; index = NUMBER; RPAREN
	{ FVarM (v, int_of_string index) } 
	| c = NUMBER { FConst (to_num c) }
	| e1 = f_expr; TIMES; e2 = f_expr { FBinop (Mult, e1, e2) }
	| e1 = f_expr; PLUS; e2 = f_expr { FBinop (Add, e1, e2) }
	| e1 = f_expr; MINUS; e2 = f_expr { FBinop (Sub, e1, e2) }
	| e1 = f_expr; DIVIDE; e2 = f_expr { FBinop (Div, e1, e2) }
	| e1 = f_expr; POWER; e2 = f_expr { FBinop (Exp, e1, e2) }
	;

expr: 
  	| n = NUMBER { Number (to_num n) }
	| ROOT; LPAREN; x = ID; COMMA; num = NUMBER; RPAREN 
	{ CRoot ( x, to_int(to_num num) ) }
	| x = ID; LPAREN; LBRAC; args = vect_fields; RBRAC; RPAREN
	{ EvalAnalyticFuncM (x, args) } 
	| x = ID; LPAREN; n = NUMBER; RPAREN { EvalAnalyticFuncS (x, to_num n) }
	| FUNCTION_BEGIN; LPAREN; var_id = ID; RPAREN; DEFINES; f = f_expr 
	{ AnalyticFunc (var_id, f) } 
	| MAT_BEGIN; TRANSP; LPAREN; m = vect_fields; RPAREN; { Matop (Tran, m) }
	| MAT_BEGIN; RAND; LPAREN; m = vect_fields; RPAREN; { Matop (Rand, m) }
	| MAT_BEGIN; MAT_ID; LPAREN; m = vect_fields; RPAREN; { Matop (Id, m) }
	| MAT_BEGIN; MREF; LPAREN; m = vect_fields; RPAREN; { Matop (Mref, m) }
	| MAT_BEGIN; DET; LPAREN; m = vect_fields; RPAREN; { Matop (Det, m) }
	| x = ID { Var x }
	| e1 = expr; TIMES; e2 = expr { Binop (Mult, e1, e2) }
	| e1 = expr; PLUS; e2 = expr { Binop (Add, e1, e2) }
	| e1 = expr; MINUS; e2 = expr { Binop (Sub, e1, e2) }
	| e1 = expr; DIVIDE; e2 = expr { Binop (Div, e1, e2) }
	| e1 = expr; POWER; e2 = expr { Binop (Exp, e1, e2) }
	| LET; x = ID; EQUALS; e1 = expr; IN; e2 = expr { Let_o (x, e1, e2) }
	| LET; x = ID; EQUALS; e1 = expr { Let (x, e1) }
	| LBRAC; vl = vect_fields ; RBRAC { Matrix (vl) }
	| LPAREN; e = expr; RPAREN {e}
	| CMD_PRINT ; var = ID { Var var }
	;

vect_fields:
    vl = separated_list(COMMA, expr) { vl } ;
