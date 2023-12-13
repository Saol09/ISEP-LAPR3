/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils;

/**
 * The type Avl.
 *
 * @param <E> the type parameter
 * @author andre
 */
public class AVL <E extends Comparable<E>> extends BST<E>{

    /**
     * Balance factor int.
     *
     * @param node the node
     * @return the int
     */
    public int balanceFactor(Node<E> node){
        
        int height = height(node.getRight()) - height(node.getLeft());
        
        return height;
    }
    
    private Node<E> rightRotation(Node<E> node){
        
        Node<E> leftSon = node.getLeft();
        
        node.setLeft(leftSon.getRight());
        leftSon.setRight(node);
        node = leftSon;
        return node;
    }
    
    private Node<E> leftRotation(Node<E> node){
        
        Node<E> rightSon = node.getRight();
        
        node.setRight(rightSon.getLeft());
        rightSon.setLeft(node);
        node = rightSon;
        return node;
    }
    
    private Node<E> twoRotations(Node<E> node){

        if (balanceFactor(node) > 0) {
            node.setRight(rightRotation(node.getRight()));
            node = leftRotation(node);

        } else {
            node.setLeft(leftRotation(node.getLeft()));
            node = rightRotation(node);
        }

        return node;
    }

    /**
     * Balance node node.
     *
     * @param node the node
     * @return the node
     */
    public Node<E> balanceNode(Node<E> node) {

        int balance = balanceFactor(node);

        if (balance < -1) {

            if (balanceFactor(node.getLeft()) > 0) return twoRotations(node);

            return rightRotation(node);
        } else if (balance > 1) {

            if (balanceFactor(node.getRight()) < 0) return twoRotations(node);

            return leftRotation(node);
        }

        return node;
    }
    
    @Override
    public void insert(E element){
        root = insert(element, root);
    }

    private Node<E> insert(E element, Node<E> node){
        if (node == null) return new Node(element, null, null);

        int comp = node.getElement().compareTo(element);

        if (comp < 0)
            node.setRight(insert(element, node.getRight()));
        else if (comp > 0)
            node.setLeft(insert(element, node.getLeft()));

        return balanceNode(node);
    }
    
    @Override  
    public void remove(E element){
        root = remove(element, root());
    }

    private Node<E> remove(E element, BST.Node<E> node) {
        if (node == null) return null;

        int comp = node.getElement().compareTo(element);

        if (comp < 0)
            node.setRight(remove(element, node.getRight()));
        else if (comp > 0)
            node.setLeft(remove(element, node.getLeft()));

        else {
            if (node.getLeft() == null || node.getRight() == null)
                node = (node.getLeft() == null) ? node.getRight() : node.getLeft();
            else {
                E smallElem = smallestElement(node.getRight());

                node.setElement(smallElem);
                node.setRight(remove(smallElem, node.getRight()));
            }
        }

        if (node != null)
            return balanceNode(node);

        return null;
    }
    
    
    public boolean equals(Object otherObj) {

        if (this == otherObj) 
            return true;

        if (otherObj == null || this.getClass() != otherObj.getClass())
            return false;

        AVL<E> second = (AVL<E>) otherObj;
        return equals(root, second.root);
    }

    /**
     * Equals boolean.
     *
     * @param root1 the root 1
     * @param root2 the root 2
     * @return the boolean
     */
    public boolean equals(Node<E> root1, Node<E> root2) {
        if (root1 == null && root2 == null) 
           return true;
        else if (root1 != null && root2 != null) {
            if (root1.getElement().compareTo(root2.getElement()) == 0) {
                return equals(root1.getLeft(), root2.getLeft())
                        && equals(root1.getRight(), root2.getRight());
            } else  
                return false; 
        }
        else return false;
    }
    
}
