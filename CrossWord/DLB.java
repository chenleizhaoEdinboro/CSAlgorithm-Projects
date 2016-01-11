import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class DLB implements DictInterface{

    private DLBnode root=null;

    private class DLBnode{

        private char val;
        private DLBnode rightSib;
        private DLBnode child;

        //constuctor of DLB node

        private DLBnode(char aVal){

            val=aVal;

            rightSib=null;

            child= new DLBnode();
        }

        private DLBnode(){

            val=' ';

            rightSib=null;

            child=null;
        }



        public char getVal(){

            return this.val;
        }

        public void setVal(char aVal){
            val = aVal;
        }

        public DLBnode getRightSib(){
            return rightSib;
        }

        public void setRightSib(DLBnode rhs){
            rightSib = rhs;
        }


        public DLBnode getChild(){
            return child;
        }

        public void setChild(DLBnode nextChild){
            child = nextChild;
        }

    }// end of DLBnode class


    public DLB(){

        root = new DLBnode() ;

    }


    public boolean add(String word){

        //if the DLB is empty
        DLBnode curr=root;

        for(int i=0; i<word.length(); i++)
        {

            // loop through the word to check each character
            curr = add(curr, word.charAt(i));

        }
        curr.setVal('$');
        curr.setRightSib(null);
        curr.setChild(null);
        return true;     // for linked list add an object could be always true
    }

    /*
     * Adds to [[head]] a child node containing [[val]] and return that child
     * if exists child containing [[val]], simply return that child
     */
    public DLBnode add(DLBnode head, char val){

        DLBnode thisNode = head, nextNode = head.getChild();

        if (nextNode == null)
        {
            DLBnode newlyAddedChild = new DLBnode(val);
            thisNode.setChild(newlyAddedChild);
            return newlyAddedChild;
        }

        if (nextNode.getVal() == val)
        {
            return nextNode;
        }

        // if node.getVal() != val
        while (true)
        {
            thisNode = nextNode; nextNode = nextNode.getRightSib();

            if (nextNode == null)
            {
                DLBnode newlyAddedSibling = new DLBnode(val);
                thisNode.setRightSib(newlyAddedSibling);
                return newlyAddedSibling;
            }

            if (nextNode.getVal() == val)
            {
                return nextNode;
            }

        }
    }

  public int searchPrefix(StringBuilder s)
  {
     
      return searchPrefix(s, 0, s.length()-1);

  }

    
    public int searchPrefix(StringBuilder word, int start, int end)
        {

        boolean isPrefix = false, isWord = false;

        //if the DLB is empty
        DLBnode curr=root;

        for ( ; start<=end; start++)
        {

            // loop through the word to check each character
            curr = searchPrefix(curr, word.charAt(start));
            if (curr == null) break;
        }

        // curr cannot be null, due to the way we add words -- it or one of its siblings will be '$'

        // If start > end, meaning if the entire word to search for did match (the for loop finished)
        if (start > end)
        {
            // If curr or one of its siblings is '$', then word to search for is a word
            // If curr or one of its siblings is '$', then word to search for is a prefix
            while (true)
            {
                if (curr == null) break;

                if (curr.getVal() == '$') isWord = true;
                if (curr.getVal() != '$') isPrefix = true;

                curr = curr.getRightSib();
            }
        }
        // Else not prefix not word

        return (isPrefix ? 1 : 0) + (isWord ? 2 : 0) ;
    }

    public DLBnode searchPrefix(DLBnode head, char val)
    {
        DLBnode thisNode = head, nextNode = head.getChild();

        if (nextNode == null)
        {
            return nextNode; // Null
        }

        if (nextNode.getVal() == val)
        {
            return nextNode;
        }

        // if node.getVal() != val
        while (true)
        {
            thisNode = nextNode; nextNode = nextNode.getRightSib();

            if (nextNode == null)
            {
                return nextNode; // Null
            }

            if (nextNode.getVal() == val)
            {
                return nextNode;
            }
        }
    }
}//end of the DLB class
