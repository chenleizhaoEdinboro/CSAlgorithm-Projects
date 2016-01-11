
// Simple test program to demonstrate DictInterface and MyDictionary.  Note that
// the variable is type DictInterface but the object is type MyDictionary.  This
// is fine since MyDictionary implements DictInterface.  In your assignment you
// will initially use MyDictionary for your object but in Part B you will create
// a new implementation of DictInterface that should be much more efficient than
// MyDictionary.

import java.io.*;
import java.util.*;
public class DictTest {
	public static void main(String [] args) throws IOException
	{
		Scanner fileScan = new Scanner(new FileInputStream("data/dict8.txt"));
		String st;
		StringBuilder sb;
		//DictInterface D = new MyDictionary();//DLB
		DictInterface D = new DLB();//DLB
		
		while (fileScan.hasNext())
		{
			st = fileScan.nextLine();
			D.add(st);
		}

		String [] tests = {"a", "abe", "abet", "abx", "ace", "acid", "hives",
						   "iodin", "inval", "zoo", "zool", "zurich"};
		for (int i = 0; i < tests.length; i++)
		{
			
			sb = new StringBuilder(tests[i]);
			
			int ans = D.searchPrefix(sb);
			System.out.print(sb + " is ");
			switch (ans)
			{
				case 0: System.out.println("not found");
					break;
				case 1: System.out.println("a prefix");
					break;
				case 2: System.out.println("a word");
					break;
				case 3: System.out.println("a word and prefix");
			}
		}
	}

}
