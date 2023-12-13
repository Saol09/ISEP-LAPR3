
package lapr.project.utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * The type Kdtree.
 *
 * @param <E> the type parameter
 */
public class KDTREE<E> implements KDTREEInterface<E> {

    /**
     * Node da classe KDTREE
     *
     * @param <E> the type parameter
     */
    public static class Node2D<E> {

        /**
         * The Element.
         */
        protected E element;

        /**
         * The Coords.
         */
        protected Point2D.Double coords;

        /**
         * The Left.
         */
        protected Node2D<E> left;

        /**
         * The Right.
         */
        protected Node2D<E> right;

        /**
         * Construtor de um Node
         *
         * @param e          element
         * @param leftChild  leftChild
         * @param rightChild rightChild
         * @param latitude   latitude
         * @param longitude  longitude
         */
        public Node2D(E e, Node2D<E> leftChild, Node2D<E> rightChild, double latitude, double longitude) {
            this.element = e;
            coords = new Point2D.Double(latitude, longitude);
            this.left = leftChild;
            this.right = rightChild;

        }

        /**
         * retorna o elemento do Node2d
         *
         * @return E element
         */
        public E getElement() {
            return element;
        }

        /**
         * Gets coords.
         *
         * @return the coords
         */
        public Point2D.Double getCoords() {
            return coords;
        }

        /**
         * Gets left.
         *
         * @return the left
         */
        public Node2D<E> getLeft() {
            return left;
        }

        /**
         * Gets right.
         *
         * @return the right
         */
        public Node2D<E> getRight() {
            return right;
        }

        /**
         * Sets left.
         *
         * @param left the left
         */
        public void setLeft(Node2D<E> left) {
            this.left = left;
        }

        /**
         * Sets right.
         *
         * @param right the right
         */
        public void setRight(Node2D<E> right) {
            this.right = right;
        }

        /**
         * Sets coords.
         *
         * @param coords the coords
         */
        public void setCoords(Point2D.Double coords) {
            this.coords = coords;
        }

        /**
         * Sets element.
         *
         * @param element the element
         */
        public void setElement(E element) {
            this.element = element;
        }
    }

    private final Comparator<Node2D<E>> cmpX = (o1, o2) -> Double.compare(o1.coords.getX(), o2.getCoords().getX());

    private final Comparator<Node2D<E>> cmpY = (o1, o2) -> Double.compare(o1.coords.getY(), o2.getCoords().getY());

    /**
     * The Root.
     */
    protected Node2D<E> root;

    /**
     * Instantiates a new Kdtree.
     */
    public KDTREE() {
        root = null;
    }

    /**
     * Root node 2 d.
     *
     * @return the node 2 d
     */
    public  Node2D<E> root() {
        return root;
    }

    public boolean isEmpty() {
        return root == null;
    }


    public int height() {
        return height(root);
    }


    /**
     * Height int.
     *
     * @param node the node
     * @return the int
     */
    protected int height(Node2D<E> node) {
        if (node == null)
            return -1;

        int left = height(node.getLeft());
        int right = height(node.getRight());

        return Math.max(left, right) + 1;
    }

    /**
     * insert da arvore
     *
     * @param node node2d
     */
    public void insert(Node2D<E> node) {
        if (root == null)
            root = insert(root(), node, true);

        insert(root(), node, true);
    }

    /**
     * insert da kdtree
     *
     * @param currentNode current node
     * @param nodeToAdd   node to add
     * @param divX        divx
     * @return node2d
     */
    private Node2D<E> insert(Node2D<E> currentNode, Node2D<E> nodeToAdd, boolean divX) {
        if (root == null) return nodeToAdd;

        if (nodeToAdd.coords.equals(currentNode.coords)) {
            return null;
        }

        int cmpResult = (divX ? cmpX : cmpY).compare(nodeToAdd, currentNode);

        if (cmpResult < 0) {
            if (currentNode.getLeft() == null)
                currentNode.setLeft(nodeToAdd);
            else
                insert(currentNode.left, nodeToAdd, !divX);
        } else {
            if (currentNode.right == null)
                currentNode.setRight(nodeToAdd);
            else
                insert(currentNode.right, nodeToAdd, !divX);
        }

        return nodeToAdd;

    }

    public int numberOfNodes() {
        return numberOfNodes(root());
    }

    private int numberOfNodes(Node2D<E> node) {

        if (node == null) return 0;

        return 1 + numberOfNodes(node.getLeft()) + numberOfNodes(node.getRight());
    }

    /**
     * encontra o neighbour mais proximo
     *
     * @param x the x
     * @param y the y
     * @return E
     */
    public E findNearestNeighbour(double x, double y) {
        return findNearestNeighbour(root, x, y, root, true);
    }


    /**
     * Returns an iterable collection of elements of the tree, reported in in-order.
     * @return iterable collection of the tree's elements reported in in-order
     */
    public Iterable<E> inOrder() {
        List<E> snapshot = new ArrayList<>();
        if (root != null)
            inOrderSubtree(root, snapshot);   // fill the snapshot recursively
        return snapshot;
    }

    /**
     * Adds elements of the subtree rooted at Node node to the given
     * snapshot using an in-order traversal
     *
     * @param node     Node serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void inOrderSubtree(Node2D<E> node, List<E> snapshot) {
        if (node == null)
            return;
        inOrderSubtree(node.getLeft(), snapshot);
        snapshot.add(node.getElement());
        inOrderSubtree(node.getRight(), snapshot);
    }

    /**
     * Returns an iterable collection of elements of the tree, reported in pre-order.
     *
     * @return iterable collection of the tree's elements reported in pre-order
     */
    public Iterable<E> preOrder() {
        List<E> snapshot = new ArrayList<>();

        preOrderSubtree(root, snapshot);

        return snapshot;

    }

    /**
     * Adds elements of the subtree rooted at Node node to the given
     * snapshot using an pre-order traversal
     *
     * @param node     Node serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void preOrderSubtree(Node2D<E> node, List<E> snapshot) {

        if (root == null) return;

        if (node == null) return;

        snapshot.add(node.getElement());
        preOrderSubtree(node.getLeft(), snapshot);
        preOrderSubtree(node.getRight(), snapshot);

    }

    /**
     * Returns an iterable collection of elements of the tree, reported in post-order.
     *
     * @return iterable collection of the tree's elements reported in post-order
     */
    public Iterable<E> posOrder() {
        List<E> snapshot = new ArrayList<>();

        posOrderSubtree(root, snapshot);

        return snapshot;
    }

    /**
     * Adds positions of the subtree rooted at Node node to the given
     * snapshot using an post-order traversal
     *
     * @param node     Node serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void posOrderSubtree(Node2D<E> node, List<E> snapshot) {
        if (node == null) return;


        posOrderSubtree(node.getLeft(), snapshot);
        posOrderSubtree(node.getRight(), snapshot);
        snapshot.add(node.getElement());

    }

    /**
     * encontra o neighbour mais proximo
     *
     * @param node        node
     * @param x           x
     * @param y           y
     * @param closestNode closest node
     * @param divX        divx
     * @return E
     */
    private E findNearestNeighbour(Node2D<E> node, double x, double y, Node2D<E> closestNode, boolean divX) {

        double distance = DistancesCalculus.distance(node.coords.x, node.coords.y, x, y, "K");
        double closestDistance = DistancesCalculus.distance(closestNode.coords.x, closestNode.coords.y, x, y, "K");

        if (closestDistance > distance)
            closestNode = node;

        double delta = divX ? x - node.coords.x : y - node.coords.y;
        double delta2 = delta * delta;

        Node2D<E> node1 = delta < 0 ? node.left : node.right;
        Node2D<E> node2 = delta < 0 ? node.right : node.left;

        if (node1 != null)
            return findNearestNeighbour(node1, x, y, closestNode, !divX);

        if (node2 != null && delta2 < closestDistance)
            return findNearestNeighbour(node2, x, y, closestNode, !divX);

        return closestNode.element;
    }

    public boolean verifyBalanced() {
        return verifyIfBalanced(root);
    }

    private boolean verifyIfBalanced(Node2D<E> node) {
        if (node.left == null || node.right == null) return true;

        if (height(node.left) - height(node.right) > Math.abs(1)) {
            return false;
        }
        verifyIfBalanced(node.left);
        verifyIfBalanced(node.right);

        return true;
    }

    /**
     * Returns a string representation of the tree.
     * Draw the tree horizontally
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringRec(root, 0, sb);
        return sb.toString();
    }

    private void toStringRec(Node2D<E> root, int level, StringBuilder sb) {
        if (root == null)
            return;
        toStringRec(root.right, level + 1, sb);
        if (level != 0) {
            for (int i = 0; i < level - 1; i++)
                sb.append("|\t");
            sb.append("|-------" + root.element + "\n");
        } else
            sb.append(root.element + "\n");
        toStringRec(root.left, level + 1, sb);
    }

}