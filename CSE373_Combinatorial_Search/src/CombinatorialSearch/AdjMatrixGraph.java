package CombinatorialSearch;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Brian Sabz
 */
public class AdjMatrixGraph {

    private int numVertices; // number of vertices in the graph
    private int numEdges; // number of edges in the graph
    int degree[]; // degree of each vertex
    boolean directed;
    boolean[][] adj;

    // Adjacency matrix from a graph with V vertices
    public AdjMatrixGraph(int V, boolean directed) {
        if (V < 0) {
            throw new RuntimeException("Number of vertices must be nonnegative");
        }
        this.numVertices = V;
        this.numEdges = 0;
        this.directed = directed;
        degree = new int[V + 1];
        this.adj = new boolean[V + 1][V + 1];
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public boolean[][] getEdges() {
        return adj;
    }

    public int[] getDegrees() {
        return degree;
    }

    // add undirected/directed edge from x-y
    public void addEdge(int x, int y, boolean directed) {
        this.degree[x]++;
        if (directed) {
            if (!adj[x][y]) {
                numEdges++;
            }
        } else {
            addEdge(y, x, true);
        }
        adj[x][y] = true;
    }

    // does the graph contain the edge x-y
    public boolean contains(int x, int y) {
        return adj[x][y];
    }

    // return list of neighbors of v
    public Iterable<Integer> adj(int v) {
        return new AdjIterator(v);
    }

    // support iteration over graph vertices
    private class AdjIterator implements Iterator<Integer>, Iterable<Integer> {

        int v, w = 0;

        AdjIterator(int v) {
            this.v = v;
        }

        public Iterator<Integer> iterator() {
            return this;
        }

        public boolean hasNext() {
            while (w < numVertices) {
                if (adj[v][w]) {
                    return true;
                }
                w++;
            }
            return false;
        }

        public Integer next() {
            if (hasNext()) {
                return w++;
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // string representation of Graph - takes quadratic time
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(numVertices + " " + numEdges + NEWLINE);
        for (int v = 0; v < numVertices; v++) {
            s.append(v + ": ");
            for (int w : adj(v)) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}
