package lapr.project.utils;

/**
 * The interface Kdtree interface.
 *
 * @param <E> the type parameter
 */
public interface KDTREEInterface<E> {

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty();

    /**
     * Height int.
     *
     * @return the int
     */
    public int height();

    /**
     * Number of nodes int.
     *
     * @return the int
     */
    public int numberOfNodes();

    /**
     * Find nearest neighbour e.
     *
     * @param x the x
     * @param y the y
     * @return the e
     */
    public E findNearestNeighbour(double x, double y);

    /**
     * Verify balanced boolean.
     *
     * @return the boolean
     */
    public boolean verifyBalanced();

    /**
     * In order iterable
     *
     * @return iterable
     */
    public Iterable<E> inOrder();

    /**
     * Pre order iterable.
     *
     * @return the iterable
     */
    public Iterable<E> preOrder();

    /**
     * Pos order iterable.
     *
     * @return the iterable
     */
    public Iterable<E> posOrder();
}
