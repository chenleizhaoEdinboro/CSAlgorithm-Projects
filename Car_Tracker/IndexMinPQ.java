/******************************************************************************
 *  Compilation:  javac IndexMinPQ.java
 *  Execution:    java IndexMinPQ
 *  Dependencies: StdOut.java
 *
 *  Minimum-oriented indexed PQ implementation using a binary heap.
 *  Modified for assignment 3, add the update method.
 ******************************************************************************/

import java.util.*;
import java.io.*;


/**
 *  The IndexMinPQ class represents an indexed priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum
 *  operations, along with delete and change-the-key
 *  methods. In order to let the client refer to keys on the priority queue,
 *  an integer between 0 and maxN-1 is associated with each key;the client
 *  uses this integer to specify which key to delete or change.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  
 *  This implementation uses a binary heap along with an array to associate
 *  keys with integers in the given range.
 *  The insert,delete-the-minimum, delete,
 *  change-key,decrease-key, and increase-key
 *  operations take logarithmic time.
 *  The is-empty, size, min-index, min-key, and key-of
 *  operations take constant time.
 *  Construction takes time proportional to the specified capacity.
 *  
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/24pq">Section 2.4 of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Key> the generic type of key on this priority queue
 */
public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
    private int maxN;        // maximum number of elements on PQ
    private int N;           // number of elements on PQ
    private int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;      // keys[i] = priority of i
	private Comparator<Key> comparator; //optional comparator for compare mileage
	

    /**
	 * maxN = 100+1 in the car program
     * Initializes an empty indexed priority queue with indices between 0
     * and maxN - 1.
     * @param  maxN the keys on this priority queue are index from 0
     *         maxN - 1
     * @throws IllegalArgumentException if maxN <0
     */
    public IndexMinPQ(int maxN) {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
		
        pq   = new int[maxN + 1];
        qp   = new int[maxN + 1];                   // make this of length maxN??
        for (int i = 0; i <= maxN; i++)             //make flag??
            qp[i] = -1;
    }

	public IndexMinPQ(int maxN, Comparator<Key> comparator){
		
		if (maxN < 0) throw new IllegalArgumentException();
		this.maxN = maxN;
		keys = (Key[]) new Comparable[maxN+1];       //1-based array so initilized with capacity+1
		this.comparator = comparator;
		pq   = new int[maxN +1];
		qp   = new int[maxN +1];
		for (int i = 0; i<=maxN; i++)
			qp[i] = -1;
	}

    /**
     * Returns true if this priority queue is empty.
     *
     * @return <tt>true</tt> if this priority queue is empty;
     *         <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Is <tt>i</tt> an index on this priority queue?
     *
     * @param  i an index
     * @return <tt>true</tt> if <tt>i</tt> is an index on this priority queue;
     *         <tt>false</tt> otherwise
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     */
    public boolean contains(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        return qp[i] != -1;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return N;
    }

    /**
     * Associates key with index <tt>i</tt>.
     *
     * @param  i an index
     * @param  key the key to associate with index <tt>i</tt>
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws IllegalArgumentException if there already is an item associated
     *         with index <tt>i</tt>
     */
    public void insert(int i, Key key) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        N++;
			
        qp[i] = N;	
        pq[N] = i;    //i is the priority, size n is from 1...n.
        keys[i] = key; //key stored upon their priority, hashmap<vin,i>
        swim(N);      //N is the pq array index
		
    }//end of insert
	
	
	 /* @param i = hashvalue by same make and model*/
	 
	 public void insertHash(int i, Key key) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        //if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        N++;
		if(N >=maxN/2) resize(2*maxN); //resize the array 
		int j=i;
		while(qp[j]!=-1)
			j = (j+1)%(maxN+1);
			
        qp[j] = N;	
		
        pq[N] = i;    //i is the priority, size n is from 1...n.
		//linearprobe
		int k=i;
		while(keys[k]!=null)
			k=(k+1)%(maxN+1);
		
        keys[k] = key; //key stored upon their priority, hashmap<vin,i>
		System.out.println("key has been inserted into "+i);
		//swimkey of the key array
        swim(N);      //N is the pq array index
		
    }//end of insert
	
     private void resize(int capacity) {
        //temp = new LinearProbingHashST<Key, Value>(capacity);
		
	  IndexMinPQ<Car> temp = new IndexMinPQ<Car>(capacity);
        for (int i = 1; i <=(100+1); i++) {
            if (keys[i] != null) {
                temp.insertHash( pq[i],(Car) keys[i]);
            }
        }
    }


     /**
     * Associates key with index <tt>i</tt>.
     *
     * @param  i is the hashcode index
     * @param  key the key to associate with index <tt>i</tt>
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws IllegalArgumentException if there already is an item associated
     *         with index <tt>i</tt>
     
	 public void update(int i, Key key){
		if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
				 
	 }
	 */

    /**
     * Returns an index associated with a minimum key.
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int minIndex() { 
        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];        //pq[0] is unuseable, the minimum element is in the index 1 pos
    }

    /**
     * Returns a minimum key.
     *
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Key minKey() { 
        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
        return keys[pq[1]];        
    }

    /**
     * Removes a minimum key and returns its associated index.
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMin() { 
        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
        int min = pq[1];        
        exch(1, N--);            // swap
        sink(1);                 //sink for removal
        qp[min] = -1;            // delete
        keys[pq[N+1]] = null;    // to help with garbage collection
        pq[N+1] = -1;            // not needed
        return min; 
    }

    /**
     * Returns the key associated with index <tt>i</tt>.
     *
     * @param  i the index of the key to return
     * @return the key associated with index <tt>i</tt>
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
     */
    public Key keyOf(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
       if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
		
        else return keys[i];
    }
	
	public Key getLowPrice(int hash){
		
		Key lowPrice = keys[hash];
		System.out.println("the initial hash is "+hash);
		
		for(int i = hash; keys[i+1]!=null; i = (i+1)% (maxN+1)){
			
			System.out.println("hash in greater is"+hash);
			System.out.println("i"+i);
			if(greaterPrice(i,i+1)){
				System.out.println("the lowest price is in"+i);
				lowPrice = keys[i+1];
			}
		}
			return lowPrice;
	}
	
	public Key getLowMile(int hash){
		
		Key lowMile = keys[hash];
		System.out.println("the initial hash is "+hash);
		
		for(int i = hash; keys[i+1]!=null; i = (i+1)% (maxN+1)){
			
			System.out.println("hash in greater is"+hash);
			System.out.println("i"+i);
			if(greaterMile(i,i+1)){
				System.out.println("the lowest Mile is in"+i);
				lowMile = keys[i+1];
			}
		}
			return lowMile;
	}
	
	
    
	public int PqOf(int i){
		return pq[i];
	}
	
	public int QpOf(int hashAsIndex){
		return qp[hashAsIndex];
	}
    /**
     * Change the key associated with index <tt>i</tt> to the specified value.
     *
     * @param  i the index of the key to change
     * @param  key change the key assocated with index <tt>i</tt> to this key
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
     */
    public void changeKey(int i, Key key) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }

    /**
     * Change the key associated with index <tt>i</tt> to the specified value.
     *
     * @param  i the index of the key to change
     * @param  key change the key assocated with index <tt>i</tt> to this key
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @deprecated Replaced by {@link #changeKey(int, Key)}.
     */
    public void change(int i, Key key) {
        changeKey(i, key);
    }

    /**
     * Decrease the key associated with index <tt>i</tt> to the specified value.
     *
     * @param  i the index of the key to decrease
     * @param  key decrease the key assocated with index <tt>i</tt> to this key
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws IllegalArgumentException if key &ge; key associated with index <tt>i</tt>
     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
     */
    public void decreaseKey(int i, Key key) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        if (keys[i].compareTo(key) <= 0)
            throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
        keys[i] = key;
        swim(qp[i]);
    }

    /**
     * Increase the key associated with index <tt>i</tt> to the specified value.
     *
     * @param  i the index of the key to increase
     * @param  key increase the key assocated with index <tt>i</tt> to this key
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws IllegalArgumentException if key &le; key associated with index <tt>i</tt>
     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
     */
    public void increaseKey(int i, Key key) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        if (keys[i].compareTo(key) >= 0)
            throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key");
        keys[i] = key;
        sink(qp[i]);
    }

    /**
     * Remove the key associated with index <tt>i</tt>.
     *
     * @param  i the index of the key to remove
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws NoSuchElementException no key is associated with index <t>i</tt>
     */
    public void delete(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = qp[i];
        exch(index, N--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }


   /***************************************************************************
    * General helper functions.
    ***************************************************************************/
    private boolean greater(int i, int j) {
		if(comparator == null){
		    return keys[pq[i]].compareTo(keys[pq[j]]) > 0; //compared upon  car price,maybe need to typecast Comparable for keys
		}
		else {
			return comparator.compare(keys[pq[i]],keys[pq[j]])>0;
		}  
    }

	
	 private boolean greaterPrice(int i, int j) {
		// System.out.println("I'm here in the lessprice");
		if(comparator == null){
			
		    return keys[i].compareTo(keys[j]) > 0; //compared upon  car price,maybe need to typecast Comparable for keys
			
		}
		else {
			return comparator.compare(keys[i],keys[j]) > 0;
		}  
    }
	
	private boolean greaterMile(int i, int j) {
		// System.out.println("I'm here in the lessprice");
		if(comparator == null){
			
		    return keys[i].compareTo(keys[j]) > 0; //compared upon  car price,maybe need to typecast Comparable for keys
			
		}
		else {
			return comparator.compare(keys[i],keys[j]) > 0;
		}  
    }
    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }


   /***************************************************************************
    * Heap helper functions.
    ***************************************************************************/
   public void swim(int k)  {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }


   /***************************************************************************
    * Iterators.
    ***************************************************************************/

    /**
     * Returns an iterator that iterates over the keys on the
     * priority queue in ascending order.
     * The iterator doesn't implement <tt>remove()</tt> since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    public Iterator<Integer> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Integer> {
        // create a new pq
        private IndexMinPQ<Key> copy;

        // add all elements to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            copy = new IndexMinPQ<Key>(pq.length - 1);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i], keys[pq[i]]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }
    

    /**
     * Unit tests the <tt>IndexMinPQ</tt> data type.
     */
    public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // delete and print each key
        while (!pq.isEmpty()) {
            int i = pq.delMin();
            StdOut.println(i + " " + strings[i]);
        }
        StdOut.println();

        // reinsert the same strings
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // print each key using the iterator
        for (int i : pq) {
            StdOut.println(i + " " + strings[i]);
        }
        while (!pq.isEmpty()) {
            pq.delMin();
        }

      }//end of main
	}// end of class
