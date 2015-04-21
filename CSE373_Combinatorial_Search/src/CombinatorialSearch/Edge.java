package CombinatorialSearch;

/**
 *
 * @author Brian Sabz
 */
public class Edge {
    // This is where the node is pointing to
    int y;

    int weight;
    Edge next;

    public int getY() {
        return y;
    }

    public Edge getNext() {
        return next;
    }
}
