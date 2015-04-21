/**
 *
 * @author Brian Sabz
 */
package CombinatorialSearch;

public class Graph {

    int numVertices; // number of vertices in the grapph
    int numEdges; // number of edges in the graph
    Edge[] edges; //weighted edges
    int degree[]; // degree of each vertex
    boolean directed;

    public int getNumVertices() {
        return numVertices;
    }
    
    public boolean edgeExists(int a, int b) {
        Edge e = edges[a];
        while (e != null) {
            if (e.y == b)
                return true;
            e = e.next;
        }
        return false;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public int[] getDegrees() {
        return degree;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public Graph(boolean directed, int vertices) {
        edges = new Edge[vertices + 1];
        degree = new int[vertices + 1];

        numVertices = vertices;
        numEdges = 0;
        this.directed = directed;

        // initialize the degrees to null
        for (int i = 1; i <= numVertices; i++) {
            degree[i] = 0;
        }
    }

    public void insertEdge(int x, int y, boolean directed) {
        Edge edge = new Edge();
        edge.y = y;
        edge.next = edges[x]; // connect this new edge to the first edge in the list
        edges[x] = edge; // insert at the head of the list

        this.degree[x]++;

        if (directed) {
            this.numEdges++;
        } else {
            insertEdge(y, x, true);
        }
    }

    public void printGraph() {
        for (int i = 1; i <= numVertices; i++) {
            System.out.printf("Vertex %d: ", i);
            Edge e = edges[i];
            while (e != null) {
                System.out.printf("%d -> ", e.y);
                e = e.next;
            }
            System.out.println();
        }
    }
}
