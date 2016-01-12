import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA{
	private BigInteger n,d,e;
	
	private int bitlen = 1024;
	
	
	public RSA(BigInteger newn, BigInteger newe){
		n = newn;
		e = newe;
	}
	
	public RSA(int bits){
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
	
	public static void main(String[] args){
		RSA rsa = new RSA(1024);
		String text1 = "chenlei zhao";
		System.out.println("plaintext "+text1);
		BigInteger plainText = new BigInteger(text1.getBytes());
		BigInteger cipher = rsa.Encrypt(plainText);
		System.out.println("cipher text "+cipher.toString());
		
		rsa.generateKeys();
		
	}
}