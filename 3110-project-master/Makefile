MODULES_NO_MLIs=arrayMatrix ast main monoAnalytic repl  
INTERFACES=matrix analytic 
OBJECTS=$(MODULES_NO_MLIs:=.cmo)
MLS=$(MODULES_NO_MLIs:=.ml)
MLIS=$(INTERFACES:=.mli)
TEST=test.byte
REPL=repl.byte
OCAMLBUILD=ocamlbuild -use-ocamlfind -pkg ANSITerminal

clean:
	ocamlbuild -clean

interpret:
	$(OCAMLBUILD) $(MAIN) && ./$(MAIN)

default: build
	utop

run:
	$(OCAMLBUILD) $(REPL) && ./$(REPL)

build:
	$(OCAMLBUILD) $(OBJECTS)

test:
	$(OCAMLBUILD) -tag 'debug' $(TEST) && ./$(TEST)

check:
	bash checkenv.sh && bash checktypes.sh
	
docs: docs-public docs-private
	
docs-public: build
	mkdir -p doc.public
	ocamlfind ocamldoc -I _build -package ANSITerminal \
		-html -stars -d doc.public $(MLIS)

docs-private: build
	mkdir -p doc.private
	ocamlfind ocamldoc -I _build -package ANSITerminal \
		-html -stars -d doc.private \
		-inv-merge-ml-mli -m A $(MLIS) $(MLS)
