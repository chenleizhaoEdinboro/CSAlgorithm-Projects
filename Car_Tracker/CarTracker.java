
import java.util.*;
import java.io.*;


public class CarTracker{

	public static void main(String [] args) throws IOException{
		
		/*pq refers the priority queue built by the lowest price*/
		IndexMinPQ<Car> pq = new IndexMinPQ<Car>(100+1); //pq is 1-based array
		
		/*pqMile built by the lowest mileages*/
		IndexMinPQ<Car> pqMile =new IndexMinPQ<Car>(100+1,new Car.MileCompare()); 
		
		/*pqSameMakeModel built by the lowest price with same make and model*/
		IndexMinPQ<Car> pqMakeModel = new IndexMinPQ<Car>(100+1);
		
		/*pqMakeModelByMile built by the lowest mile with same make and model*/
		IndexMinPQ<Car> pqMakeModelByMile = new IndexMinPQ<Car>(100+1,new Car.MileCompare());
		
		int totalCar = 0;
		
		Scanner s = null;
		//try to open the file and read from the file
		try{
			s = new Scanner(new BufferedReader(new FileReader("cars.txt")));
			while (s.hasNextLine()){
				//Create an entry from the next line and put on the PQ.
				String line = s.nextLine();
				Car c = new Car(line);
				
				/*Insert the car to the lowest price pq*/
				pq.insert(c.hash(),c);
				
				/*Insert the car to the lowest mileage pq*/
				pqMile.insert(c.hash(),c);
				
				/*Insert tha car to the pqMakeModel pq, the priority is the lowest price*/
				pqMakeModel.insertHash(c.hashByMakeAndModel(),c);
				
				pqMakeModelByMile.insertHash(c.hashByMakeAndModel(),c);
				
				System.out.println("hash by same make and model is"+ c.hashByMakeAndModel()+" "+c.getVinNum());
				System.out.println("regular hash:"+c.getBrand()+" "+c.hash());
				totalCar++;
			}
		} finally{
			if(s != null){
				s.close();
			}
		}
		
		/*Display the menu on the Command prompt*/
        System.out.println(" Please make select an operation:");
		System.out.println("1) Add a Car");
		System.out.println("2) Update a Car");
		System.out.println("3) Remove the Car");
		System.out.println("4) Retrieve the lowest price car");
		System.out.println("5) Retrieve the lowest mileage car");
		System.out.println("6) Retrieve the lowest price car by the same make and model");
		System.out.println("7) Retrieve the lowest mileage car by the same make and modle");
		
		Scanner kb = new Scanner(System.in);
		int input = kb.nextInt();
		
		/* add a new car*/
		if(input == 1){
			System.out.println("Enter the info of the car:");
			Scanner newCar = new Scanner(System.in);
			String carInfo = newCar.nextLine();
			System.out.println("the new car info is carInfo "+carInfo);
			totalCar++;
			
			Car addCar = new Car(carInfo);
			pq.insert(addCar.hash(),addCar);
			
			try{
			  // Open the file.
			  PrintWriter writer = new PrintWriter("cars.txt");
			  
			  for(int i=1; i<=totalCar; i++)
				  //should call toString to get the car info here 
		       writer.println(pq.keyOf(pq.PqOf(i)).getVinNum()+" "+pq.keyOf(pq.PqOf(i)).getBrand()+" "+pq.keyOf(pq.PqOf(i)).getModel()+" "+pq.keyOf(pq.PqOf(i)).getPrice()+" "+pq.keyOf(pq.PqOf(i)).getMile()+" "+pq.keyOf(pq.PqOf(i)).getColor());
			
			  // Close the file.
			  writer.close();
			  System.out.println("Data written to the file.");
		     } catch(IOException e){
			System.out.println(e.getMessage());
		    }
		}
		/*update the car information*/
		else if(input == 2){
			
			System.out.println("Enter the VIN number of the car:");
			Scanner vin = new Scanner(System.in);
			String updateVin = vin.nextLine();
			System.out.println("The vin number you typed is "+updateVin);
			
			int i = (updateVin.hashCode()& 0x7fffffff)%(100+1);
			
			System.out.println("the hascode is "+i);
			if(pq.keyOf(i+1).getVinNum().equals(updateVin))
			    System.out.println(pq.keyOf(i+1).toString()); //since pq is based 1 array, so the key(i+1) is the index corresponds to pq index
			else
				System.out.println("the VIN you typed can't found in the pq");
			
			/*Display the menu for update options of car's attributes*/
			System.out.println("Enter the 1,2 or 3 to update the car information:");
			System.out.println("1)the price of the car");
			System.out.println("2)the mileage of the car");
			System.out.println("3)the color of the car");
			Scanner command = new Scanner(System.in);
			int newInfo = command.nextInt();
			
			
			if(newInfo==1){
				System.out.println("Enter the new price of the car");
				
				Scanner updatePrice = new Scanner(System.in);
				double newPrice = updatePrice.nextDouble();
				pq.keyOf(i+1).setPrice(newPrice);
				System.out.println(pq.keyOf(i+1).toString());
				
				/*reheap after update the price*/
				pq.swim(pq.QpOf(i+1));
				
				
				
			}
			else if(newInfo == 2){
				System.out.println("Enter the new mileage of the car");
				Scanner updateMile = new Scanner(System.in);
				double newMile = updateMile.nextDouble();
				pq.keyOf(i+1).setMile(newMile);
				System.out.println(pq.keyOf(i+1).toString());
			}
			else{
				
				System.out.println("Enter the new color of the car");
				Scanner updateColor = new Scanner(System.in);
				String newColor = updateColor.nextLine();
				pq.keyOf(i+1).setColor(newColor);
				System.out.println(pq.keyOf(i+1).toString());
				
			}
			
			
		}
		/*Remove a specific car from consideration, input = 3*/
		else if (input == 3){
			System.out.println("Enter the VIN number of the car:");
			Scanner vin = new Scanner(System.in);
			String updateVin = vin.nextLine();
			
			System.out.println("The vin number you typed is "+updateVin);
			updateVin = updateVin.toUpperCase();
			
			for(int i=0; i<updateVin.length();i++){
			    if(updateVin.charAt(i)=='I' || updateVin.charAt(i)== 'O' || updateVin.charAt(i)=='Q')
				   System.out.println("vin number should not contain I,O,Q.");
			}
			
			/*find the array index of kays by hashing*/
			int i = (updateVin.hashCode()& 0x7fffffff)%(100+1);
			
			System.out.println("the hascode is "+i);
			
			/*Compare the item*/
			
			if(pq.keyOf(i).getVinNum().equals(updateVin)){
				
			    System.out.println("the car removed is: "+pq.keyOf(i).toString()); //since pq is based 1 array, so the key(i+1) is the index corresponds to pq index
				pq.delete(i);
				
				//reheap after remove,need to be test
				pq.swim(pq.QpOf(i+1));
			}
			else
				System.out.println("the VIN you typed can't found in the pq");
						
		}
		/*Retrieve the lowest price car*/
		else if(input == 4){
			
			System.out.println(" the lowest price key is :"+ pq.minKey().toString());
		}
		else if(input == 5){
			System.out.println(" the lowest mileage car is :"+ pqMile.minKey().toString());
		}
		
		else if(input == 6){
			System.out.println("Please type in the make and model: ");
			Scanner mm = new Scanner(System.in);
			String makeModel = mm.nextLine().toUpperCase();
			String[] m = makeModel.split("\\s+");
			
			int hash = 17;
			
            hash = 31*hash + m[0].hashCode();
            hash = 31*hash + m[1].hashCode();
		    hash = (hash &0x7fffffff)%(100+1);
			System.out.println("the hash is in option 6 "+hash);
            
			System.out.println(" the lowest price with same make: "+m[0]+" and model: "+m[1]+" is "+pqMakeModel.getLowPrice(hash).getPrice());
			
		}
		
		else if(input == 7){
			
			System.out.println("Please type in the make and model: ");
			Scanner mm = new Scanner(System.in);
			String makeModel = mm.nextLine().toUpperCase();
			String[] m = makeModel.split("\\s+");
			
		    int hash = 17;
            hash = 31*hash + m[0].hashCode();
            hash = 31*hash + m[1].hashCode();
		    hash = (hash &0x7fffffff)%(100+1);
            
			System.out.println(" the lowest mile with same make and model is "+pqMakeModelByMile.getLowMile(hash).getMile());						
		}
		
		else
			System.out.println("make a valid selection by type in a number from 1-7");
		
		/*print out the lowest price to high price in the pq*/
		while (!pq.isEmpty()) {		 
            System.out.println( pq.minKey().getBrand());		     
            int i = pq.delMin(); 
        }
		/*print out the lowest mileage to high price in the pq*/
		while (!pqMile.isEmpty()) {		 
            System.out.println( "here is the order by mileage: "+pqMile.minKey().getBrand()+" "+pqMile.minKey().getMile());		     
            int i = pqMile.delMin(); 
        }
		for(int k=50; k<53;k++)
        System.out.println("the i "+k+"item in the keys array is "+pqMakeModel.keyOf(k).toString());
	    for(int k=50; k<53;k++)
        System.out.println("the i "+k+"item in the keys array is "+pqMakeModelByMile.keyOf(k).toString());
	   
        
	}
	
}