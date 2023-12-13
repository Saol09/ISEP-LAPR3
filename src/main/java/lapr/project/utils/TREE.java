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
 * The type Tree.
 *
 * @param <E> the type parameter
 * @author andre
 */
public class TREE<E extends Comparable<E>> extends AVL<E>{

    /**
     * Contains boolean.
     *
     * @param element the element
     * @return the boolean
     */
    /*
   * @param element A valid element within the tree
   * @return true if the element exists in tree false otherwise
   */   
    public boolean contains(E element) {
         for(E e : posOrder()){
             if(e.compareTo(element)==0)
                 return true;
         }
         
         return false;
    }


    /**
     * Is leaf boolean.
     *
     * @param element the element
     * @return the boolean
     */
    public boolean isLeaf(E element){
         Node node = find(root, element);
         
         if (node!=null && node.getLeft()==null && node.getRight() == null)
             return true;
         
         return false;
         
    }

    /**
     * Ascdes iterable.
     *
     * @return the iterable
     */
    /*
   * build a list with all elements of the tree. The elements in the 
   * left subtree in ascending order and the elements in the right subtree 
   * in descending order. 
   *
   * @return    returns a list with the elements of the left subtree 
   * in ascending order and the elements in the right subtree is descending order.
   */
    public Iterable<E> ascdes(){
        
        List<E> ascList = new ArrayList<>();
        List<E> desList = new ArrayList<>();
        List<E> finalList = new ArrayList<>();
        
        ascSubtree(root, ascList);
        desSubtree(root, ascList);
        
        return ascList;
            
    }

    private void ascSubtree(Node<E> node, List<E> snapshot) {
        for(E e: inOrder()){
            snapshot.add(e);
            node = find(root, e);  //vai procurar sempre os node com dterminado E
            if(node==root) //chegou ao root ppor isso parou
                break;
        }          
    }
    
    private void desSubtree(Node<E> node, List<E> snapshot) {
        
        if(node==root)
        desSubtree(root.getRight(),snapshot);
            
        for(E e: inOrder()){
            if(node.getElement().compareTo(e)<0){
                if(!(snapshot.contains(e)))
                snapshot.add(e);
            }
                
        }
        
        
        if(node.getLeft()!=null)
            desSubtree(node.getLeft(),snapshot);
        
            
                
    }

    /**
     * Returns the tree without leaves.
     *
     * @return tree without leaves
     */
    public BST<E> autumnTree() {
        BST<E> withoutLeaves = new BST<>();
        
        
        withoutLeaves = this;
        if (withoutLeaves == this) {
            System.out.println("E igual");
        }
        
        for (E element : withoutLeaves.preOrder()) {
            
           Node<E> nodeToCheck = withoutLeaves.find(root, element);
           
           if(isLeaf(nodeToCheck.getElement())==true){
               withoutLeaves.remove(element);
           }         
        }
        
        return withoutLeaves;
        
    }
    
    private Node<E> copyRec(Node<E> node){
           
        return null;
    }

    /**
     * Num nodes by level int [ ].
     *
     * @return the the number of nodes by level.
     */
    public int[] numNodesByLevel(){
        
       int[] result = new int[height() + 1];
       
       numNodesByLevel(root,result,0);
       
       return result;
       
    }
    
    private void numNodesByLevel(Node<E> node, int[] result, int level){
        
        Map<Integer,List<E>> map = new HashMap<>();
           
        map=nodesByLevel();
        int num;
        
        for(Integer i: map.keySet()){
            level=i;
            
                result[level] = map.get(i).size();
            
            
        }
         
    }

    /**
     * Perfect balanced boolean.
     *
     * @return the boolean
     */
    public boolean perfectBalanced(){
        
       return perfectBalanced(root());
    }
    
    private boolean perfectBalanced(Node<E> node){
        
        if (node == null) return true;

        int bf = balanceFactor(node);

        if (bf!= 0 ) return false;

        return perfectBalanced(node.getLeft()) && perfectBalanced(node.getRight());
    }

}

