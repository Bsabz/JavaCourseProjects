package CombinatorialSearch;

import java.util.*;

/**
 *
 * @author Brian Sabz
 */
public class CombinatorialSearchCC {

    static int bandwidth = 100000;
    static boolean finished = false;                  /* found all solutions yet? */


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ReadFile read = null;

        System.out.println("Enter .txt file path within root directory: ");
        Scanner in = new Scanner(System.in);
        String text = ".txt";
        String filePath = in.nextLine();
        filePath += text;

        try {
            // TYPE THE NAME OF THE FILE HERE!
            read = new ReadFile(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (read == null) {
            // something went wrong!
            System.out.println("Couldn't open file!");
            System.exit(0);
        }

        int[][] graphData = read.getData();
        read = null; // garbage collect

        int numEdge = graphData[0][0];
        int numVertices = graphData[1][0];

        System.out.println(numEdge);
        System.out.println(numVertices);

        // PRINT GIVEN GRAPH
        for (int i = 2; i < graphData.length; i++) {
            System.out.println(graphData[i][0] + " " + graphData[i][1]);
        }

        AdjMatrixGraph adj = new AdjMatrixGraph(numVertices, false);

        // INITIALIZE GRAPH
        for (int i = 2; i < graphData.length; i++) {
            adj.addEdge(graphData[i][0], graphData[i][1], false);
        }

        System.out.println("");
        
        // Now the graph is constructed. We can call the backtrack algorithm on it:
        int a[] = new int[adj.getNumVertices()];

        backtrack(adj, a, 0, adj.getNumVertices());

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Run time: " + totalTime/1000.0 + " sec");
    }

    public static void backtrack(AdjMatrixGraph g, int a[], int k, int n) {

        if (isASolution(k, n)) {
            processSolution(g, a);
            
        } else {
            k++;
            int c[] = constructCandidates(a, k, n);                  // candidates for next position
            int numCandidates = c.length;                            // number of candidates we just constructed

           // searchPruning(g, a, c, k);
            
            for (int i = 0; i < numCandidates; i++) {
                
                if (c[i] != -9000) {
                    a[k - 1] = c[i];
                    backtrack(g, a, k, n);
                }
            }
        }
    }

    public static boolean isASolution(int k, int n) {
        return k == n;
    }

    /**
     * Compute the bandwidth of this solution and compare it against our current
     * estimate.
     *
     * @param a
     */
    public static void processSolution(AdjMatrixGraph g, int a[]) {
        int currentEstimate = 0;
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (g.contains(a[i] + 1, a[j] + 1)) {
                    int distanceBetween = j - i;
                    if (distanceBetween > currentEstimate) {
                        currentEstimate = distanceBetween;
                    }

                    if (currentEstimate >= bandwidth) {
                        return;
                    }
                }
            }
        }

        if (currentEstimate < bandwidth) {
            bandwidth = currentEstimate;

            System.out.print("New solution: ");
            for (int i = 0; i < a.length; i++) {
                System.out.print((a[i] + 1));
                if (i == a.length - 1) {
                    System.out.print("");
                } else {
                    System.out.print(", ");
                }
            }
            System.out.println(" ----------------- bandwidth = " + bandwidth);
        }
    }

    public static int[] constructCandidates(int a[], int k, int n) {
        boolean exists[] = new boolean[n]; // to keep track of which values are already in the partial solution
        for (int i = 0; i < k - 1; i++) {
            exists[a[i]] = true;
        }

        // create new array to hold the candidates:
        int candidates[] = new int[n - k + 1];
        int fillIndex = 0;
        for (int i = 0; i < n && fillIndex < candidates.length; i++) {
            if (!exists[i]) {
                candidates[fillIndex] = i;
                fillIndex++;
            }
        }
        return candidates;
    }
    
    public static void searchPruning(AdjMatrixGraph g, int[]a, int[]c, int k){
        int dist = 0;
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < k-1; j++) {
                if(c[i]!= -9000 && g.contains(c[i], a[j])){
                  dist =  k - j - 1;
                  if(dist >= bandwidth){
                      //if the distance is greater then we delete candidate
                      c[i] = -9000;
                      break;
                  }
                }
            }
        }
    }
}