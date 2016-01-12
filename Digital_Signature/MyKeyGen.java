import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.*;

public class MyKeyGen{
	private BigInteger n,d,e;
	
	private int bitlen = 1024;
	
	
	public MyKeyGen(BigInteger newn, BigInteger newe){
		n = newn;
		e = newe;
	}
	
	public MyKeyGen(int bits){
		bitlen = bits;
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen/2,10,r);
		BigInteger q = new BigInteger(bitlen/2,10,r);
		n = p.multiply(q);
		
		BigInteger phiN= (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		//e is the odd number greater than 1 and less than phiN
		e = new BigInteger("3");
		while(phiN.gcd(e).intValue() >1){
			e = e.add(new BigInteger("2"));
		}
		//find the private key by extended euclidean
		d = e.modInverse(phiN);
	}// end of constructor
	
	public String Encrypt(String plainText){
		return (new BigInteger (plainText.getBytes())).modPow(e,n).toString();
	}
	
	public BigInteger Encrypt(BigInteger plainText){
		return plainText.modPow(e,n);
	}
	
		/*toByteArray()
         Returns a byte array containing the two's-complement representation of this BigInteger.*/
	public String decrypt(String cipherText){
		return new String ((new BigInteger(cipherText)).modPow(d,n).toByteArray());
	}
	
	public BigInteger decrypt(BigInteger cipher){
		return cipher.modPow(d,n);
	}
	public void generateKeys(){
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen/2 ,20, r);
		BigInteger q = new BigInteger(bitlen/2 , 20,r);
		
		n = p.multiply(q);
		
		BigInteger phiN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		e = new BigInteger("3");
		while(phiN.gcd(e).intValue() > 1){
			e = e.add(new BigInteger("2"));
		}
		
		d = e.modInverse(phiN);
	}
	
	public BigInteger getN(){
		return n;
	}
	public BigInteger gete(){
		return e;
	}
	
	public BigInteger getd(){
		return d;
	}
	
	public static void main(String[] args) throws IOException{
		
		/* For printing out testing
		  MyKeyGen rsa = new MyKeyGen(1024);
		  String text1 = "For Testing";
		  System.out.println("plaintext "+text1);
		  BigInteger plainText = new BigInteger(text1.getBytes());
		
		  BigInteger cipher = rsa.Encrypt(plainText);
		  System.out.println("cipher text "+cipher.toString());
		
		  plainText = rsa.decrypt(cipher);
		  String text2 = new String(plainText.toByteArray());
		  System.out.println("plaintext: "+text2);
		  
		  BigInteger pubE = rsa.gete();
		  System.out.println("the public key is "+pubE.toString());
		  
		  BigInteger pubN = rsa.getN();
		  System.out.println("the public key is "+pubN.toString());
		  */
		 
		
		MyKeyGen rsa = new MyKeyGen(1024);
		rsa.generateKeys();
		
		BigInteger pubE = rsa.gete();
		System.out.println("the public key E is "+pubE.toString(16));
		
		BigInteger pubN = rsa.getN();
		System.out.println("the public key N is "+pubN.toString(16));
		
		FileOutputStream fos = new FileOutputStream("pubkey.rsa");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(pubE);
		oos.writeObject(pubN);
		oos.close();
		System.out.println("---------------------------------------------");
		System.out.println("pubkey.rsa has created to the current folder");
		System.out.println("---------------------------------------------");
		BigInteger pubD = rsa.getd();
		System.out.println("the public key D is "+pubD.toString(16));
		
		FileOutputStream fosPriv = new FileOutputStream("privkey.rsa");
		ObjectOutputStream oosPriv = new ObjectOutputStream(fosPriv);
	
		oosPriv.writeObject(pubD);
		oosPriv.writeObject(pubN);
		oosPriv.close();
		System.out.println("---------------------------------------------");
		System.out.println("privkey.rsa has created to the current floder");
		
		
	}
}