import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Crossword{
	
	public static String fName;

	public static void main(String[] args) throws IOException {

		// Create puzzle board
		Scanner keyIn = new Scanner(System.in);
		
		System.out.print("Input file name: ");
		
		fName = keyIn.nextLine();
		
		File inFile = new File(fName);

		Scanner fileIn = new Scanner(inFile);

		int side = fileIn.nextInt();

		char[][] board = new char[side][side];

		for (int x = 0; x < side; ++x) {
			String row = fileIn.next();
			for (int y = 0; y < side; ++y) {
				board[x][y] = row.charAt(y);
			}
		}

		// Load dictionary

		fileIn= new Scanner(new File("dict8.txt"));
        
		int option;
		DictInterface dictionary;
		
		System.out.println("Choose a method: 1) linear search 2) DLB ,enter 1 or 2 please");
		
		Scanner sc = new Scanner(System.in);
		
		option =sc.nextInt();
		
		if(option == 1){
			
			dictionary = new MyDictionary(); 
			
		}else{
			
			 dictionary = new DLB();
		}

		while (fileIn.hasNext()) {
			
			dictionary.add(fileIn.nextLine());
		}
         
		fileIn.close();
		// Solve puzzle
        System.out.println("Please see the puzzle solutions in the output file "+fName+"-out, open by editor");
		placeChar(board, dictionary, 0, 0);

	}// end of main

	private static void placeChar(char[][] board, DictInterface dictionary,
			int x, int y) throws IOException{
		
		
		if (x >= board.length) // We have a solution
		{
			String fileOutPut = fName+"-out";
			
			PrintWriter fileOut = new PrintWriter(fileOutPut);
			
			//System.out.println("the slution has print to outfile.");
			printBoard(board,fileOut);
			
			fileOut.close();
			return;
		}

		switch (board[x][y]) {
		case '+': {
			for (char c = 'a'; c <= 'z'; ++c) {
				// Place value of c at (x,y)
				char cold = board[x][y];
				board[x][y] = c;

				// Check the "validity" of current row and column (see v())
				boolean v = v(board, dictionary, x, y);

				if (v) {
					// Advance to (x2,y2)
					int y2 = y + 1;
					int x2 = x;
					if (y2 == board[x].length) {
						y2 = 0;
						x2 = x + 1;
					}
					placeChar(board, dictionary, x2, y2);
				}

				board[x][y] = cold;
			}
		}
		break;

		case '-': {
			// Advance to (x2,y2)
			int y2 = y + 1;
			int x2 = x;
			if (y2 == board[x].length) {
				y2 = 0;
				x2 = x + 1;
			}
			placeChar(board, dictionary, x2, y2);
		}
		break;

		default: // When there is a pre-existing letter at this position
		{
			// Advance to (x2,y2)
			int y2 = y + 1;
			int x2 = x;
			if (y2 == board[x].length) {
				y2 = 0;
				x2 = x + 1;   // go to the next row if out of boundary
			}
			placeChar(board, dictionary, x2, y2);
		}
		}

	}

	private static boolean v(char[][] board, DictInterface dictionary, int x,
			int y) {
		char[] row = new char[board[x].length];
		char[] col = new char[board.length];

		for (int i = 0; i < row.length; ++i) {
			row[i] = board[x][i];
		}
		for (int i = 0; i < col.length; ++i) {
			col[i] = board[i][y];
		}


		boolean rowIsValid = checkLine(row, dictionary);
		boolean colIsValid = checkLine(col, dictionary);

		return rowIsValid && colIsValid;
	}

	private static boolean checkLine(char[] line, DictInterface dictionary) {
		StringBuilder s = new StringBuilder();

		int currentPositionFlag;
		// 4: We are outside; check isWord and return true or return false
		// 3: -; check s isWord and advance+erase s or return false
		// 2: +; check s isPrefix and return true or return false
		// 1: A letter: push to s and advance

		int i = 0; // index for iteration by following while loop

		while (true) {
			if (i >= line.length) {
				currentPositionFlag = 4;
			} else {
				switch (line[i]) {
				case '-':
					currentPositionFlag = 3;
					break;

				case '+':
					currentPositionFlag = 2;
					break;

				default:
					currentPositionFlag = 1;
				}
			}

			switch (currentPositionFlag) {

			case 4: {
				boolean isWord = (s.toString().equals("") || (dictionary.searchPrefix(s) >= 2));
				if (isWord)
					return true;
				else
					return false;
			}

			case 3: {
				boolean isWord = (s.toString().equals("") || (dictionary.searchPrefix(s) >= 2));
				if (isWord) {
					++i;
					s = new StringBuilder();
				} else
					return false;
			}
			break;

			case 2: {
				boolean isPrefix = (s.toString().equals("") || ((dictionary.searchPrefix(s) % 2) == 1));
				if (isPrefix)
					return true;
				else
					return false;
			}

			case 1: {
				s.append(line[i]);
				++i;
			}
			break;
			}
		}
	}

	private static void printBoard(char[][] board,PrintWriter outF) {
		
		outF.println(" Here is the current solution of input board");
		
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				outF.print(board[x][y]);
			}
			outF.println();
		}
	}

}
