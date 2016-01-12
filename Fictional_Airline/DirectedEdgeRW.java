
/******************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Execution:    java DirectedEdge
 *  Dependencies: StdOut.java
 *
 *  Immutable weighted directed edge.
 *
 ******************************************************************************/
/**
 *  The <tt>DirectedEdge</tt> class represents a weighted edge in an 
 *  {@link EdgeWeightedDigraph}. Each edge consists of two integers
 *  (naming the two vertices) and a real-value weight. The data type
 *  provides methods for accessing the two endpoints of the directed edge and
 *  the weight.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class DirectedEdgeRW { 
    private final int v;
    private final int w;
	private final int weightDist;
    private final double weightCost;
	public String[] name;

    /**
     * Initializes a directed edge from vertex <tt>v</tt> to vertex <tt>w</tt> with
     * the given <tt>weight</tt>.
     * @param v the tail vertex
     * @param w the head vertex
     * @param weight the weight of the directed edge
     * @throws IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *    is a negative integer
     * @throws IllegalArgumentException if <tt>weight</tt> is <tt>NaN</tt>
     */
    public DirectedEdgeRW(int v, int w, int weightDist,double weightCost,String[] namePlace) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        //if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weightDist = weightDist;
		this.weightCost = weightCost;
		name = namePlace;
    }

    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int to() {
        return w;
    }

    /**
     * Returns the weight of the directed edge.
     * @return the weight of the directed edge
     */
    public double weightCost() {
        return weightCost;
    }
 
    public int weightDist(){
		return weightDist;
	}
    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    public String toString() {
		String s;
		String vName=null;
		String wName = null;
	      
		for(int i =1; i<name.length;i++){
			if( v == i)
				vName = name[i];
			if( w == i)
				wName = name[i];
		}
		
        return v  +" " + w +" "+ weightDist+" "+weightCost;
    }
	


    /**
     * Unit tests the <tt>DirectedEdge</tt> data type.
     */
    public static void main(String[] args) {
		String[] name = {"pitt","erie","harris"};
        DirectedEdgeRW e = new DirectedEdgeRW(12,34,200,5.67,name);
        StdOut.println(e);
    }
}

