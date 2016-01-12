
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

public class MySign{
	
	//private instances
	private BigInteger n,d,e;
	 byte[] hashValue;
	
	//constructor
	public MySign(){	      
			  //read file from privkey.rsa
		      try{
				   FileInputStream fis = new FileInputStream("./privkey.rsa");
				   ObjectInputStream ois = new ObjectInputStream(fis);
				  
			       if(ois.available() == -1){
					   
				      System.out.println("can't read from rsa file");
				   }
				
				   d = (BigInteger)ois.readObject();
				   //System.out.println("d in conttructor is "+d);
			       n = (BigInteger)ois.readObject();
		           ois.close();
			  
			      //read file from pubKey.rsa
			      FileInputStream fisPub = new FileInputStream("pubkey.rsa");
			      ObjectInputStream oisPub = new ObjectInputStream(fisPub);
				 
			      if(oisPub.available() == -1){
					  
				      System.out.println("can't read from rsa file");
			      }
			  			  
			     e =(BigInteger) oisPub.readObject();
		         oisPub.close();
			  
			  }catch(Exception e){
				  System.out.println(e.toString());
				  
			  }
			  
	}//end of constructor
	
	public BigInteger getN(){
		return n;
	}
	public BigInteger gete(){
		return e;
	}
	
	public BigInteger getd(){
		return d;
	}
	
	
	//sign: apply decrypt to the hash value with private key d
	
	public String decrypt(String hash){
		return new String ((new BigInteger(hash)).modPow(d,n).toByteArray());
	}
	
	public BigInteger decryptBigInt(BigInteger hash){
	// System.out.println("the d is"+d+"the n is "+n);
	 
		return hash.modPow(d,n);
	}
	
	//verify: aply encrypt to the hash value with public key e
	
	public String encrypt(String decryptText){
		return (new BigInteger (decryptText.getBytes())).modPow(e,n).toString();
	}
	
	public BigInteger encryptBigInt(BigInteger decryptText){
		return decryptText.modPow(e,n);
	}
	
	
	//main
	public static void main(String [] args) throws IOException{
		
	  byte[] digest;
	  String result = null;
	  String resultV = null;
	  BigInteger resultBigInt = null;
	  
	  MySign mySign = new MySign();
	  Scanner s = null;
      StringBuilder sb = new StringBuilder();
	  BigInteger dHash = null;
	  
	   if(args[0].equals("s"))
	  {
		try {
			// read in the file to hash
			Path path = Paths.get(args[1]);
			byte[] data = Files.readAllBytes(path);

			// create class instance to create SHA-256 hash
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// process the file
			md.update(data);
			// generate a hash of the file
			digest = md.digest();
			//System.out.println("the hash is "+digest);

			// convert the bite string to a printable hex representation
			// note that conversion to biginteger will remove any leading 0s in the bytes of the array!
			resultBigInt = new BigInteger(1,digest);
			result = new BigInteger(1, digest).toString(16);
			

			// print the hex representation
			//System.out.println("the result is"+result);
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
		
		//String result = new BigInteger(1, hashRes).toString(16);
		//resultBigInt is the hash value in the form of BigInteger
		BigInteger signed = mySign.decryptBigInt(resultBigInt);
		System.out.println("the result after signed is "+signed.toString());
		
		
		  try{
			     s = new Scanner(new BufferedReader(new FileReader("MyFile.txt")));
				 while( s.hasNextLine())
				 {
					 sb.append(s.nextLine());
				 }
				 
		         //System.out.println("here is the contect in stringbuilder"+sb.toString());
		         //Write out a signed version of the file (e.g., "myfile.txt.signed")
					FileOutputStream fosSigned = new FileOutputStream("myfile.txt.signed");
		            ObjectOutputStream oosSigned = new ObjectOutputStream(fosSigned);
	
		            oosSigned.writeObject(sb.toString());
		            oosSigned.writeObject(signed);
		            oosSigned.close();
		
	        }
			catch(Exception e)
			{
		     System.out.println(e.toString());
			  e.printStackTrace();
	        }
			
	  }
	  else if(args[0].equals("v"))
	 {
		 String fileContent;
		 try{
		        FileInputStream fisRead = new FileInputStream(args[1]);
	            ObjectInputStream oisRead = new ObjectInputStream(fisRead);
				 
	            if(oisRead.available() == -1)
		        {
					  
		           System.out.println("can't read from signed file");
		        }
		        fileContent = (String)oisRead.readObject();
				//read the old hash from file
		        dHash =(BigInteger)oisRead.readObject();
		        oisRead.close();
		       // System.out.println("the fileContent is "+fileContent);
		       // System.out.println("the dhash is "+dHash.toString());
			        
				      try {
			               // read in the file to hash
			             
			               byte[] data = fileContent.getBytes();

			               // create class instance to create SHA-256 hash
			                MessageDigest md = MessageDigest.getInstance("SHA-256");

			               // process the file
			               md.update(data);
			               // generate a hash of the file
			               digest = md.digest();
			               //System.out.println("the hash is "+digest);

			               // convert the bite string to a printable hex representation
			               // note that conversion to biginteger will remove any leading 0s in the bytes of the array!
						  
						   //newly generated hash
			                resultBigInt = new BigInteger(1,digest);
			                resultV = new BigInteger(1, digest).toString(16);
							
			

			               // print the hex representation
			                System.out.println("the new hash result is"+resultV);
		                }
		      catch(Exception e) 
		     {
			    System.out.println(e.toString());
		     }
		
		 }
		 catch(Exception e)
		 {
			e.printStackTrace();
			 
		 }
	    BigInteger vSigned = mySign.encryptBigInt(dHash);
		
		String verify =  vSigned.toString(16);
		System.out.println("the result after Vsigned is "+verify);
		
		//validate the signature
		if(verify.equals(resultV)){
			System.out.println("the signature is valid ");
			System.out.println("since the new Hash "+resultV+ "same to");
			System.out.println(" the encrypted "+verify);
			
		}
	
	 }
	
	  else
	 {
		throw new IllegalArgumentException( "Illegal command line argument" );
	 }
	
	}
}