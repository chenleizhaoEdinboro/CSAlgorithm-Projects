

import java.util.*;
public class DirectedDFS {
	
    private boolean[] marked;  // marked[v] = true if v is reachable
                               // from source (or sources)
    private int count;         // number of vertices reachable from s
	private int[] edgeTo;
	public double totalWeight = 0;
	public double m =0;
	//private int source;

    //constructor
    public DirectedDFS(EdgeWeightedDigraph G, int budget) {
		m=budget;
		edgeTo = new int[G.V()+1];
        marked = new boolean[G.V()+1];
		//start at vertex 0 with weight 0
		for(int i=1; i<=G.V();i++)
	    dfs(G, i, 0.0);
		
    }

    private void dfs(EdgeWeightedDigraph G, int vertex, double weight) { 
        count++;
		totalWeight += weight;
        marked[vertex] = true;
        for (DirectedEdge e: G.adj(vertex)) {
			int w = e.to();
			//System.out.println("the path is from "+e.from()+" to "+e.to());
            if (!marked[w] && totalWeight < m) {
			edgeTo[w] = vertex;
	     	dfs(G, w, e.weightCost());	
			}
		    
        }	
      
    }

    /**
     * Is there a directed path from the source vertex (or any
     * of the source vertices) and vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a directed path, <tt>false</tt> otherwise
     */
    public boolean marked(int v) {
        return marked[v];
    }

    /**
     * Returns the number of vertices reachable from the source vertex
     * (or source vertices).
     * @return the number of vertices reachable from the source vertex
     *   (or source vertices)
     */
    public int count() {
        return count;
    }

	
	
	public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * Returns a path between the source vertex <tt>s</tt> and vertex <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a path between the source vertex
     *   <tt>s</tt> and vertex <tt>v</tt>, as an Iterable
     */
    public Iterable<Integer> pathTo(int v) {
		int s =1;
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }
	
		
		
    /**
     * Unit tests the <tt>DirectedDFS</tt> data type.
     */
	 /*
    public static void main(String[] args) {

        // read in digraph from command-line argument
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
		
        int budget = 5;
		

        // multiple-source reachability
        DirectedDFS dfs = new DirectedDFS(G,budget);

        // print out vertices reachable from sources
        for (int v = 0; v < G.V(); v++) {
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
        StdOut.println();
    }*/

}

