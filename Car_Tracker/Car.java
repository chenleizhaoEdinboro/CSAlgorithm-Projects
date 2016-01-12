
import java.util.Comparator;


public class Car implements Comparable<Car>{
	 private String vinNum;
	 private String brand;
	 private String model;
	 private double price;
	 private double mile;
	 private String color;
	 
 /**
  * Initializes a new car by parsing a string of the form vinNum, brand, model, price, mile, color.
  *@param car the string to parse
  *throws IllegalArgumentException if vinNum contains I(i),O(o),or Q(q)
  *throws IllegalArgumentException if price or mile is Double.NaN, Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY
  */
  public Car(String carInfo){
	  
	  String[] a = carInfo.split("\\s+");
	  boolean validVinNum = true;
	  
	  this.vinNum = a[0].toUpperCase();
	  
	  for(int i=0; i<vinNum.length();i++){
		  if(vinNum.charAt(i)=='I' || vinNum.charAt(i)== 'O' || vinNum.charAt(i)=='Q')
			  validVinNum = false;
		  break;  		  
	  }
	  
	  while(vinNum.length() != 17 || validVinNum == false){
		  throw new IllegalArgumentException("vin number should be 17 character long and not contain I,O,Q.");
	  }
	  this.brand = a[1].toUpperCase();
	  this.model = a[2].toUpperCase();
	  this.price = Double.parseDouble(a[3]);
	  this.mile = Double.parseDouble(a[4]);
	  this.color = a[5];  
  }
  
  public String getVinNum(){
	  return vinNum;
  }
  
  public String getBrand(){
	  return brand;
  }
  
  public String getModel(){
	  return model;
  }
  public double getPrice(){
	  return price;
  }
  public double getMile(){
	  return mile;
  }
  public String getColor(){
	  return color;
  }
  
  public void setPrice(double newPrice){
	  this.price = newPrice;
  }
  
  public void setMile(double newMile){
	  this.mile = newMile;
  }
  
  public void setColor(String newColor){
	  this.color = newColor;
  }
  
  public String toString(){
	 return getVinNum()+" "+getBrand()+" "+getModel()+" "+getPrice()+" "+getMile()+" "+getColor();
  }
  
  public int compareTo(Car that){
	
	  if(this.price < that.price)
		  return -1;
	  else if(this.price > that.price)
		  return 1;
	  else 
		  return 0;
  }
  
  /* Compare two car by their mileage*/
  public static class MileCompare implements Comparator<Car> {

        @Override
        public int compare(Car v, Car w) {
			
			//using the compareTo function in the Double class
            return Double.valueOf(v.mile).compareTo(Double.valueOf(w.mile));
        }
    }
	
  /*compare two car by their price*/
  public static class PriceCompare implements Comparator<Car>{
	  @Override
	  public int compare(Car v, Car w){
		  return Double.valueOf(v.price).compareTo(Double.valueOf(w.price));
	  }
  }
  
  public boolean equals(Object aCar){
	  Car c = (Car) aCar;
	  return getVinNum().equals(c.getVinNum()); //Strings hava an overridden equals().
  }
  
  public int hash(){	 
	 
	  return  (vinNum.hashCode() & 0x7fffffff)%(100+1); //String class has an overridden hashCode() method ,so here could return the result
	                             // of calling hashCode() in the String class on the vinNum.
  }
 
  /**
     * Returns a hash code for this Car
     * with same model and make. 
     */
    public int hashByMakeAndModel() {
        int hash = 17;
        hash = 31*hash + brand.hashCode();
        hash = 31*hash + model.hashCode();
		hash = (hash &0x7fffffff)%(100+1);
        return hash;
    } 
}