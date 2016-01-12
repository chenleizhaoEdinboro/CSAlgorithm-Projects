

/******************************************************************************
 *  Compilation:  javac EdgeWeightedDigraph.java
 *  Execution:    java EdgeWeightedDigraph V E
 *  Dependencies: Bag.java DirectedEdge.java
 *
 *  An edge-weighted digraph, implemented using adjacency lists.
 *
 ******************************************************************************/


 import java.io.*;
 import java.util.*;
public class EdgeWeightedDigraphRW {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;                // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    public Bag<DirectedEdgeRW>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;             // indegree[v] = indegree of vertex v
    public String[] placeName;          // hold the string name for places
    /**
     * Initializes an empty edge-weighted digraph with <tt>V</tt> vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     */
    public EdgeWeightedDigraphRW(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        adj = (Bag<DirectedEdgeRW>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<DirectedEdgeRW>();
    }

    /**
     * Initializes a random edge-weighted digraph with <tt>V</tt> vertices and <em>E</em> edges.
     *
     * @param  V the number of vertices
     * @param  E the number of edges
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     * @throws IllegalArgumentException if <tt>E</tt> < 0
     */
	 /*
    public EdgeWeightedDigraphRW(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            double weight = .01 * StdRandom.uniform(100);
            DirectedEdge e = new DirectedEdge(v, w, weight);
            addEdge(e);
        }
    }*/

    /**  
     * Initializes an edge-weighted digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IndexOutOfBoundsException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
	/*
    public EdgeWeightedDigraphRW(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            if (v < 0 || v >= V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
            if (w < 0 || w >= V) throw new IndexOutOfBoundsException("vertex " + w + " is not between 0 and " + (V-1));
            double weight = in.readDouble();
            addEdge(new DirectedEdge(v, w, weight));
        }
    }
	*/
	
	public EdgeWeightedDigraphRW(String fileName) throws IOException{
		Scanner s = null;
		String temp;
		try{
			s = new Scanner(new BufferedReader(new FileReader(fileName)));
			int v = s.nextInt(); //9, the total vertex in the first line
			
			if (v < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
            this.V = v;
            this.E = 0;
			this.indegree = new int[v+1];
			
            adj = (Bag<DirectedEdgeRW>[]) new Bag[v+1];
		    //parallel array to store the name of hops
		    placeName = new String[v+1];
		
		    //adjacency list index starts from 1 instead of zero
            for (int k = 1; k <=v; k++) {
                adj[k] = new Bag<DirectedEdgeRW>();
            }
		    //end of constructor(V)
			
		    temp = s.nextLine();// handle the eof
		    for(int j =1; j<=v; j++){
			
			    temp = s.nextLine();//get the first line
			    placeName[j] = temp; // insert the first name to the name array
			    System.out.println("the placename is in"+j+"is "+placeName[j]);
		    }
		
		//int E = s.nextInt(); //16
		   while(s.hasNextInt()){
			
               int vertex = s.nextInt();//1
               int w = s.nextInt();//2
               int weightDist = s.nextInt();//127  read pair, add edge
			   double weightCost = s.nextDouble(); //200.00
               DirectedEdgeRW e = new DirectedEdgeRW(vertex,w,weightDist, weightCost,placeName);
			  // DirectedEdge eVerse = new DirectedEdge(w,vertex,weightDist, weightCost,placeName);
               addEdge(e);
			   //addEdge(eVerse);
           }
				
		}finally{
			if(s != null){
				s.close();
			}
		  }	
	}//end of the constructor

    /**
     * Initializes a new edge-weighted digraph that is a deep copy of <tt>G</tt>.
     *
     * @param  G the edge-weighted digraph to copy
     */
    public EdgeWeightedDigraphRW(EdgeWeightedDigraphRW G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++)
            this.indegree[v] = G.indegree(v);
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<DirectedEdgeRW> reverse = new Stack<DirectedEdgeRW>();
            for (DirectedEdgeRW e : G.adj(v)) {
                reverse.push(e);
            }
            for (DirectedEdgeRW e : reverse) {
                adj[v].add(e);
            }
        }
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int E() {
        return E;
    }
	
	//get the vertex number of the place
	public int getPlaceNumber(String place){
		int num = -3;
		String temp;
		place = place.toUpperCase();
		
		for(int i=1; i<placeName.length; i++){
			temp = placeName[i].toUpperCase();
			if(temp.equals(place))
				
				num = i;
		}
		return num;
	}
	
	public String getPlaceName(int num){
		return placeName[num];
	}

    // throw an IndexOutOfBoundsException unless 1 <= v <= V
    private void validateVertex(int v) {
        if (v < 1 || v > V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 1 and " + V);
    }

    /**
     * Adds the directed edge <tt>e</tt> to this edge-weighted digraph.
     *
     * @param  e the edge
     * @throws IndexOutOfBoundsException unless endpoints of edge are between 0 and V-1
     */
    public void addEdge(DirectedEdgeRW e) {
		//add an edge from v to w
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }
	
	public void addRoute(String newStart, String newEnd, int dist, double price){
		int start = getPlaceNumber(newStart);
		int end = getPlaceNumber(newEnd);
		int weightDist = dist;
		double weightCost = price;
		
		DirectedEdgeRW e = new DirectedEdgeRW(start,end,weightDist, weightCost,placeName);
			  
        addEdge(e);
	}
  
    /**
     * Returns the directed edges incident from vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<DirectedEdgeRW> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex <tt>v</tt>.
     * This is known as the <em>outdegree</em> of vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the outdegree of vertex <tt>v</tt>
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns the number of directed edges incident to vertex <tt>v</tt>.
     * This is known as the <em>indegree</em> of vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the indegree of vertex <tt>v</tt>
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * <tt>for (DirectedEdge e : G.edges())</tt>.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<DirectedEdgeRW> edges() {
        Bag<DirectedEdgeRW> list = new Bag<DirectedEdgeRW>();
        for (int v = 1; v <= V; v++) {
            for (DirectedEdgeRW e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 1; v <=V; v++) {
            s.append(v + ": ");
            for (DirectedEdgeRW e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

	
    /**
     * Unit tests the <tt>EdgeWeightedDigraph</tt> data type.
     */
    public static void main(String[] args) throws IOException{
       
		System.out.println("What is the file name of flights data ?");
		Scanner fileN = new Scanner(System.in);
		String file = fileN.nextLine();
		EdgeWeightedDigraphRW GRW = new EdgeWeightedDigraphRW(file);
        StdOut.println(GRW);
		//ask for new route
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
    }//end of main

}
