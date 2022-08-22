package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import ast.Program;
import parse.ParserFactory;

public class ParseAndMutateApp {

	public static void main(String[] args) {

		// Implementation of Main Command Line
		int n = 0;
		String file = null;
		try {

			if (args.length == 1) {
				// Parse input-file as a critter program and pretty-print if valid
				file = args[0];
			} else if (args.length == 3 && args[0].equals("--mutate")) {
				// has mutations
				n = Integer.parseInt(args[1]);
				if (n < 0)
					throw new IllegalArgumentException();
				file = args[2];
			} else {
				throw new IllegalArgumentException();
			}

			InputStream in = ClassLoader.getSystemResourceAsStream(file);
			Reader r = new BufferedReader(new InputStreamReader(in));
			Program AST = ParserFactory.getParser().parse(r);
			r.close();

			if (AST == null) {
				System.out.println("Error: invalid program");
				System.exit(-1);
			}

			if (n > 0)
				System.out.println("Initial pretty-print of AST\n––––––");
			else {
				System.out.println("Pretty-print of " + file + " AST\n––––––");
			}
			printAST(AST);
			if (n > 0) {
				performMutations(n, AST);
				System.out.println("––––––\nAfter " + n + " mutations:\n––––––");
				printAST(AST);
			}

		} catch (IllegalArgumentException e) {
			System.out.println("Usage:\n  <input_file>\n  --mutate <n> <input_file>");
		} catch (IOException e) {
			System.out.println("File does not exist");
		}
	}

	/**
	 * Prints the Abstract Syntax Tree represented by p
	 * 
	 * @param p program to be printed
	 */
	private static void printAST(Program p) {
		System.out.println(p); // Automatically calls toString() on p
	}

	/**
	 * Performs n mutations on the program p
	 * 
	 * @param n number of mutations to perform
	 * @param p program to be mutated
	 */
	private static void performMutations(int n, Program p) {
		for (int i = 0; i < n; i++) {
			System.out.println("*** Mutation " + i + " ***");
			p = p.mutate();
			printAST(p);
		}
	}
}
