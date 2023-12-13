/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Bst.
 *
 * @param <E> the type parameter
 * @author andre
 */
public class BST <E extends Comparable<E>> implements BSTInterface<E> {

    /**
     * Nested static class for a binary search tree node.  @param <E>  the type parameter
     *
     * @param <E> the type parameter
     */
    protected static class Node<E> {
        private E element;          // an element stored at this node
        private Node<E> left;       // a reference to the left child (if any)
        private Node<E> right;      // a reference to the right child (if any)

        /**
         * Constructs a node with the given element and neighbors.
         *
         * @param e          the element to be stored
         * @param leftChild  reference to a left child node
         * @param rightChild reference to a right child node
         */
        public Node(E e, Node<E> leftChild, Node<E> rightChild) {
          element = e;
          left = leftChild;
          right = rightChild;
        }

        /**
         * Gets element.
         *
         * @return the element
         */
// accessor methods
        public E getElement() { return element; }

        /**
         * Gets left.
         *
         * @return the left
         */
        public Node<E> getLeft() { return left; }

        /**
         * Gets right.
         *
         * @return the right
         */
        public Node<E> getRight() { return right; }

        /**
         * Sets element.
         *
         * @param e the e
         */
// update methods
        public void setElement(E e) { element = e; }

        /**
         * Sets left.
         *
         * @param leftChild the left child
         */
        public void setLeft(Node<E> leftChild) { left = leftChild; }

        /**
         * Sets right.
         *
         * @param rightChild the right child
         */
        public void setRight(Node<E> rightChild) { right = rightChild; }
      } 

    //----------- end of nested Node class -----------

    /**
     * The Root.
     */
    protected Node<E> root = null;     // root of the tree


    /**
     * Instantiates a new Bst.
     */
    /* Constructs an empty binary search tree. */
    public BST() {    
          root = null;
    }

    /**
     * Root node.
     *
     * @return the node
     */
    /*
    * @return root Node of the tree (or null if tree is empty)
    */
    protected Node<E> root() {
       return root;
    }  

    /*
    * Verifies if the tree is empty
    * @return true if the tree is empty, false otherwise
    */
    public boolean isEmpty(){
        return root==null;
    }

    /**
     * Returns the Node containing a specific Element, or null otherwise.
     *
     * @param node    the node
     * @param element the element to find
     * @return the Node that contains the Element, or null otherwise This method despite not being essential is very useful.  It is written here in order to be used by this class and its  subclasses avoiding recoding. So its access level is protected
     */
    protected Node<E> find(Node<E> node, E element){
        if(node==null)
            return null;
        
        if(node.getElement()==element)
            return node;
        
        if(node.getElement().compareTo(element)>0)
            return find(node.getLeft(), element);
        
        if(node.getElement().compareTo(element)<0)
            return find(node.getRight(), element);
          
        return null;
        
    }
    
    /*
    * Inserts an element in the tree.
    */
    public void insert(E element){
        
        root=insert(element, root());
      
    }
    
    private Node<E> insert(E element, Node<E> node){
            
        if(node == null){
            return new Node(element, null, null); //new node
        }
        
        if (node.getElement().compareTo(element)>0)
            node.setLeft(insert(element, node.getLeft()));
        else
            if(node.getElement().compareTo(element)<0)
                node.setRight(insert(element, node.getRight()));
             

        return node;
    }

    /**
    * Removes an element from the tree maintaining its consistency as a Binary Search Tree.
    */
    public void remove(E element){
        root = remove(element, root());
    }
 
    private Node<E> remove(E element, Node<E> node) {
        
        if (node == null) {
            return null;    //throw new IllegalArgumentException("Element does not exist");
        }   
        if (element.compareTo(node.getElement())==0) {
            // node is the Node to be removed
            if (node.getLeft() == null && node.getRight() == null) { //node is a leaf (has no childs)
                return null;
            }
            if (node.getLeft() == null) {   //has only right child
                return node.getRight();
            }
            if (node.getRight() == null) {  //has only left child
                return node.getLeft();
            }
            E min = smallestElement(node.getRight());
            node.setElement(min);
            node.setRight(remove(min, node.getRight()));
        }
        else if (element.compareTo(node.getElement()) < 0) 
            node.setLeft( remove(element, node.getLeft()) );
        else 
            node.setRight( remove(element, node.getRight()) );

        return node;
    }

    /*
    * Returns the number of nodes in the tree.
    * @return number of nodes in the tree
    */
    public int size(){
            return size(root);
    }
    
    private int size(Node<E> node){
            if(node==null)
                return 0;
            
            else
                return(size(node.left)+ 1 + size(node.right));
    }
    
    /*
    * Returns the height of the tree
    * @return height 
    */
    public int height(){
        return height(root);
    }

    /**
     * Height int.
     *
     * @param node the node
     * @return the int
     */
    /*
    * Returns the height of the subtree rooted at Node node.
    * @param node A valid Node within the tree
    * @return height 
    */  
    protected int height(Node<E> node){
        if (node == null)
            return -1;

        int left = height(node.getLeft());
        int right = height(node.getRight());

        return Math.max(left, right) + 1;
    }

    /**
    * Returns the smallest element within the tree.
    * @return the smallest element within the tree
    */
    public E smallestElement(){
        return smallestElement(root);
    }

    /**
     * Smallest element e.
     *
     * @param node the node
     * @return the e
     */
    protected E smallestElement(Node<E> node){
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return (node.getElement());
    } 
    
   /*
   * Returns an iterable collection of elements of the tree, reported in in-order.
   * @return iterable collection of the tree's elements reported in in-order
   */
    public Iterable<E> inOrder(){
        List<E> snapshot = new ArrayList<>();
        if (root!=null)
          inOrderSubtree(root, snapshot);   // fill the snapshot recursively
        return snapshot;    
    }
  /**
   * Adds elements of the subtree rooted at Node node to the given
   * snapshot using an in-order traversal
   * @param node       Node serving as the root of a subtree
   * @param snapshot  a list to which results are appended
   */
    private void inOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null)
            return;
        inOrderSubtree(node.getLeft(), snapshot);
        snapshot.add(node.getElement());
        inOrderSubtree(node.getRight(), snapshot);
    }
  /**
   * Returns an iterable collection of elements of the tree, reported in pre-order.
   * @return iterable collection of the tree's elements reported in pre-order
   */
    public Iterable<E> preOrder(){
        List<E> snapshot = new ArrayList<>();
        
        preOrderSubtree(root,snapshot);
        
        return snapshot;
        
    }
  /**
   * Adds elements of the subtree rooted at Node node to the given
   * snapshot using an pre-order traversal
   * @param node       Node serving as the root of a subtree
   * @param snapshot  a list to which results are appended
   */
    private void preOrderSubtree(Node<E> node, List<E> snapshot) {
        
        if (root == null) return;

        if (node == null) return;

        snapshot.add(node.getElement());
        preOrderSubtree(node.getLeft(), snapshot);
        preOrderSubtree(node.getRight(), snapshot);
            
    }
  /**
   * Returns an iterable collection of elements of the tree, reported in post-order.
   * @return iterable collection of the tree's elements reported in post-order
   */
    public Iterable<E> posOrder(){
        List<E> snapshot = new ArrayList<>();
        
        posOrderSubtree(root,snapshot);
        
        return snapshot;
    }
  /**
   * Adds positions of the subtree rooted at Node node to the given
   * snapshot using an post-order traversal
   * @param node       Node serving as the root of a subtree
   * @param snapshot  a list to which results are appended
   */
    private void posOrderSubtree(Node<E> node, List<E> snapshot) {
            if (node == null) return;
            
            
            posOrderSubtree(node.getLeft(),snapshot);
            posOrderSubtree(node.getRight(),snapshot);
            snapshot.add(node.getElement());
           
    }
    
    /*
    * Returns a map with a list of nodes by each tree level.
    * @return a map with a list of nodes by each tree level
    */
    public Map<Integer,List<E>> nodesByLevel(){
           
           Map<Integer,List<E>> map = new HashMap<>();
           
           processBstByLevel(root,map,0);
           
           return map;
  
    }
    
    private void processBstByLevel(Node<E> node, Map<Integer,List<E>> result, int level){
           
        if (root == null) return;

        if (node == null) return;

        if(!result.keySet().contains(level))
            result.put(level, new ArrayList<E>());
        
        if(!result.get(level).contains(node.getElement()))
            result.get(level).add(node.getElement());
        
        processBstByLevel(node.getLeft(), result, level + 1);
        processBstByLevel(node.getRight(), result, level + 1);
        
    }

//#########################################################################
  
    /**
    * Returns a string representation of the tree.
    * Draw the tree horizontally 
    */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        toStringRec(root, 0, sb);
        return sb.toString();    
    }

    private void toStringRec(Node<E> root, int level, StringBuilder sb){
        if(root==null)
             return;
        toStringRec(root.getRight(), level+1, sb);
        if (level!=0){
            for(int i=0;i<level-1;i++)
                sb.append("|\t");
            sb.append("|-------"+root.getElement()+"\n");
        }
        else
            sb.append(root.getElement()+"\n");
        toStringRec(root.getLeft(), level+1, sb);
    }
  
}//----------- end of BST class -----------