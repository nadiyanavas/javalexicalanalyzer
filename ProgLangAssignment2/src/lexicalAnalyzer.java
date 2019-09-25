import java.util.*;
import java.io.*;

public class lexicalAnalyzer {
	public static int charClass;
	public static char[] lex = new char[100];
	public static char currentChar;
	public static int position;
	public static int token;
	public static Scanner reader;
	public static int count = 0;
	
	public static final int LETTER = 1;
	public static final int NUM = 2;
	public static final int SYMBOL = 3;
	public static final int EOF = 0;
	
	public static final int INT_LIT = 11;
	public static final int IDENT = 22;
	public static final int EQUALS = 33;
	public static final int ADD_SYM = 44;
	public static final int SUB_SYM = 55;
	public static final int MULT_SYM = 66;
	public static final int DIV_SYM = 77;
	public static final int LEFT_PARENTHESIS = 88;
	public static final int RIGHT_PARENTHESIS = 99;
	public static final int CONDITIONALS = 00;
	
	static int lookup(char ch) {
		count = 0;
		lex[count] = currentChar;
		count++;
		switch (ch) {
		case '(':
			addChar();
			token = LEFT_PARENTHESIS;
			break;
		case ')':
			addChar();
			token = RIGHT_PARENTHESIS;
			break;
		case '+':
			addChar();
			token = ADD_SYM;
			break;
		case '-':
			addChar();
			token = SUB_SYM;
			break;
		case '*':
			addChar();
			token = MULT_SYM;
			break;
		case '/':
			addChar();
			token = DIV_SYM;
			break;
		default:
			addChar();
			token = EOF;
			break;
		}
		return token;
	}
	
		static void addChar() {
			if (position <= 98) {
				position++;
				currentChar = lex[position];
			} 
			else {
				System.out.println("Error - lexeme is too long \n");
			}
		}	
		static void getChar() {
			if (currentChar != ';') {
				if (Character.isLetter(currentChar)) {
					charClass = LETTER;
					
				} 
				else if (Character.isDigit(currentChar)) {
					charClass = NUM;
				} 
				else {
					charClass = SYMBOL;
				}
			} 
			else {
				charClass = EOF;
			}
		}
		static void getNonBlank() {
			while (Character.isWhitespace(currentChar)) {
				addChar();
			}
			getChar();
		}
		
		static int getLexeme() {
			count = 0;
			getNonBlank();
			switch(charClass) {
			case LETTER:
				getChar();
				while (charClass == LETTER || charClass == NUM) {
					lex[count] = currentChar;
					count++;
					addChar();
					getChar();
				}
				token = IDENT;
				break;
			case NUM:
				int warning = 0;
				getChar();
				while (charClass == NUM || charClass == LETTER) {
					lex[count] = currentChar;
					count++;
					addChar();
					getChar();
					if (charClass == LETTER) {
						warning++;
					}
				}
				if (warning > 0) {
					System.out.println("Warning: Declaring a variable starting with numerals may cause an error when identifying tokens.");
				}
				token = INT_LIT;
				break;
			case SYMBOL:
				lookup(currentChar);
				getChar();
				break;
			case EOF:
				token = EOF;
				lex[0] = 'E';
				lex[1] = 'O';
				lex[2] = 'F';
				count = 3;
				break;
			}
			
			char[] test = new char[lex.length];
			for (int i = 0; i < count; i++) {
				test[i] = lex[i];
			}
			String printLexeme = new String(test);
			if (printLexeme.equalsIgnoreCase("if") || printLexeme.equalsIgnoreCase("for") || printLexeme.equalsIgnoreCase("when")) {
				token = CONDITIONALS;
			}
			System.out.printf("Next token is: %d, Next lexeme is %s\n", token, printLexeme);
			return token;
		}
		
	static void main() {
		try {
			reader = new Scanner(new File("/Users/nadiyanavas/Desktop/TSU 19-20/ProgLangAssignment2/src/file.txt"));
		} catch (FileNotFoundException e) {
		}
		 if (!reader.hasNext()) {
			 System.out.println("ERROR- cannot open file.");
		 } else {
	         String line = reader.nextLine();
	         lex = line.toCharArray();
	         position = 0;
	         currentChar = lex[position];
			 do {
				 getLexeme();
			 } while (token != EOF);
		 }
		 reader.close();
	}
	
	public static void main(String[] args) {
		main();
	}
	
}
