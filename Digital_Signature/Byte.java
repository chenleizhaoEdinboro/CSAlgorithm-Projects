public class Byte{

public static void main(String [] args){
	String test;
	test = "aa";
	String command = args[0];
	
	
	System.out.println("the args0 is "+args[0]);
	
	if(args[0].equals("v"))
		System.out.println("the command is v");
	if(args[0].equals("s"))
		System.out.println("the command is s");
	
	System.out.println(	test.getBytes());
	byte[] b = test.getBytes(); 
	//type cast from byte to string, getBytes to String
	String test1 = new String(b);
	System.out.println("test1 "+test1);

}
}