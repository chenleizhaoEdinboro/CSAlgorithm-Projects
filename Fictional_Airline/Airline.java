
import java.io.*;
import java.util.*;

public class Airline{
	public static void main(String args[]) throws IOException{
		
		 String NEWLINE = System.getProperty("line.separator");
		
		System.out.println("What is the file name of flights data ?");
		Scanner fileN = new Scanner(System.in);
		String file = fileN.nextLine();
	
	    // print the graph
		EdgeWeightedGraph G = new EdgeWeightedGraph(file);
		System.out.println("-------------------------");
		System.out.println("THE graph information");
		StdOut.println(G);
		
		//minimum spanning tree
		System.out.println("Here is the MINIMUM SPANNING TREE based on Kruskal's algorithm: ");
		KruskalMST mst = new KruskalMST(G);
		for(Edge e: mst.edges()){
			StdOut.println(e);
		}
		StdOut.printf("%.2f\n",mst.weight());
		
		//shorest path
		//Initialize the graph with directions
		EdgeWeightedDigraph GDi = new EdgeWeightedDigraph(file);
		
       // int s = Integer.parseInt(args[0]); //start the vertex 0

        // compute shortest paths
		System.out.println("Ask for the beginning and end vertex for display Shortest Path");
		System.out.println("Where do you want to start?");
		Scanner start = new Scanner(System.in);
		String startPlace = start.nextLine();
		
		System.out.println("Where is your destination?");
		Scanner end = new Scanner(System.in);
		String endPlace = end.nextLine();
		
		//for shortest path upon distance
        DijkstraSP sp = new DijkstraSP(GDi, startPlace,endPlace);
        // print shortest path upon distance
			int t = GDi.getPlaceNumber(endPlace);
			    System.out.println("--------------------------");
				System.out.println("the SHOREST DISTANCE PATH:");
            if (sp.hasPathTo(t)) {
                StdOut.printf("%s to %d (%.2f)  ", startPlace, t, sp.distTo(t));
                for (DirectedEdge e : sp.pathTo(t)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%s to %d         no path\n", startPlace, t);
            }
			
			
		System.out.println("--------------------------");
		System.out.println("the SHOREST COST PATH:");
		//for shorest path upon cost
		DijkstraSPCost spCost = new DijkstraSPCost(GDi, startPlace,endPlace);
            if (spCost.hasPathTo(t)) {
                StdOut.printf("%s to %d (%.2f)  ", startPlace, t, spCost.distTo(t));
                for (DirectedEdge e : spCost.pathTo(t)) {
                    StdOut.print(e.toStringCost() + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%s to %d         no path\n", startPlace, t);
            }
		
		//the bfs for fewest hops
	   
        Graph Graph = new Graph(file);
        
        BreadthFirstPaths bfs = new BreadthFirstPaths(Graph, startPlace, endPlace);
		
         int a = Graph.getPlaceNumber(startPlace);
         int b = Graph.getPlaceNumber(endPlace);
            if (bfs.hasPathTo(b)) {
				         System.out.println("----------------------------");
                         System.out.println("THE FEWEST HOPS from "+startPlace+" to "+endPlace);
						 
                StdOut.printf("%s to %s (%d) %s:  ", startPlace, endPlace, bfs.distTo(b),"hops");
                for (int x : bfs.pathTo(b)) {
                    if (x == a) {
						System.out.println(Graph.getPlaceName(x));
						//StdOut.print(x);
					}
                    else{
						System.out.println(Graph.getPlaceName(x));
						//StdOut.print("-" + x);
					}
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%s to %s (-):  not connected\n", startPlace, endPlace);
            }
		
		//for path less than certain amount
		 //EdgeWeightedDigraph GB = new EdgeWeightedDigraph(file);
		 System.out.println("what's your budget?");
		 Scanner bmount = new Scanner(System.in);
		 int budget = bmount.nextInt();
		

        // multiple-source reachability
        DirectedDFS dfs = new DirectedDFS(GDi,budget);

        // print out vertices reachable from sources
        for (int v = 0; v < GDi.V(); v++) {
			int s =0;
           if (dfs.hasPathTo(v)) {
                StdOut.printf("%d to %d:  ", s, v);
                for (int x : dfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d:  over budget\n", s, v);
            }
        }
		
		// Add a route to the file airline_data1.txt
        EdgeWeightedDigraphRW GRW = new EdgeWeightedDigraphRW(file);
        StdOut.println(GRW);
		//ask for new route
		System.out.println("Add for new Route");
		System.out.println("what's the new start city?");
		Scanner newStart = new Scanner(System.in);
		String newSCity = newStart.nextLine();
		
		System.out.println("what's the new end city?");
		Scanner newEnd = new Scanner(System.in);
		String newECity = newEnd.nextLine();
		
		System.out.println("what's the new travel distance?");
		Scanner dist = new Scanner(System.in);
		int newDist = dist.nextInt();
		
		System.out.println("what's the new price?");
		Scanner price = new Scanner(System.in);
		double newPrice = price.nextDouble();
		
		GRW.addRoute(newSCity, newECity, newDist, newPrice);
		
		//write to the file
		 PrintWriter writer = new PrintWriter("airline_data1.txt");
		 writer.println(GRW.V());
		 
		 for(int i=1; i <= GRW.V();i++ )
			 writer.println(GRW.placeName[i]);
		 
		StringBuilder sb = new StringBuilder();
        //s.append(V + " " + E + NEWLINE);
        for (int v = 1; v <=GRW.V(); v++) {
			
			writer.println(sb);
			
            //sb.append(v);
            for (DirectedEdgeRW e : GRW.adj(v)) {
                sb.append(e);
				sb.append(NEWLINE);
            }
            sb.append(NEWLINE);
			
        }
		
		writer.close();
        //end of add new route		
			
			
			
			
	}//end of main
	
}//end of Airline class