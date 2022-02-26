import edu.princeton.cs.algs4.*;

public class MaxFlow {
    /*
    Question 1
    Fattest path.
    Given an edge-weighted digraph and two vertices s and t,
    design an ElogE algorithm to find a fattest path from s to t.
    The bottleneck capacity of a path is the minimum weight of an edge on the path.
    A fattest path is a path such that no other path has a higher bottleneck capacity.
    */

    /*
    Dijkstra algorithm with bottleneck (v -> w, bottleneck[w] < min(bottleneck[v], e(v, w))) 
    instead of sum of distance, max PQ instead of min PQ
    Hint: design a linear-time subroutine that takes a real-number TT and determines if there 
    is a path from ss to tt of bottleneck capacity greater than or equal to TT.
    Reference: https://lucatrevisan.wordpress.com/2011/02/04/cs-261-lecture-10-the-fattest-path/
     */
    
    public class FattestPath {
        private final DirectedEdge[] edgeTo;
        private final double[] distTo;
        private final IndexMaxPQ<Double> maxPQ;

        public FattestPath(EdgeWeightedDigraph G, int s) {
            edgeTo = new DirectedEdge[G.V()];
            distTo = new double[G.V()];
            maxPQ = new IndexMaxPQ<>(G.V());

            for (int i = 0; i < G.V(); i++) {
                distTo[i] = Double.NEGATIVE_INFINITY;
            }
            distTo[s] = Double.POSITIVE_INFINITY;

            maxPQ.insert(s, distTo[s]);
            while (!maxPQ.isEmpty()) {
                int v = maxPQ.delMax();
                for (DirectedEdge e : G.adj(v)) {
                    relax(e);
                }
            }
        }

        private void relax(DirectedEdge e) {
            int v = e.from();
            int w = e.to();

            if (distTo[w] < Math.min(distTo[v], e.weight())) {
                distTo[w] = Math.min(distTo[v], e.weight());
                edgeTo[w] = e;
                if (maxPQ.contains(w)) {
                    maxPQ.decreaseKey(w, distTo[w])
                }
                else {
                    maxPQ.insert(w, distTo[w]);
                }
            }

        }

    }

    /*
    Question 2
    Perfect matchings in k-regular bipartite graphs.
    Suppose that there are n men and n women at a dance and that each man knows exactly k women and each woman knows exactly k men (and relationships are mutual).
    Show that it is always possible to arrange a dance so that each man and woman are matched with someone they know.
    */

    /*
    L:R vertices, add s and t and new edges for s to L and R to t,
    we can now send |L| units of flow from s to t by setting the flow to 1 for every new edge 
    and to 1/k for every original edge.
     */
    public boolean PerfectMatchingForKBipartite(int[][] relationships) {
        int n = relationships.length;
        int s = n;
        int t = n + 1;

        // creating flowNetwork
        FlowNetwork flowNetwork = new FlowNetwork(n + 2);

        // men -> women relationships
        for (int v = 0; v < n/2; v++) {
            for (int w = n/2; w < n; w++) {
                if (relationships[v][w] == 0) continue;
                flowNetwork.addEdge(new FlowEdge(v, w, Double.POSITIVE_INFINITY));   // k relationships for men
            }
        }
        // women -> men relationships
        for (int w = n / 2; w < n; w++) {
            for (int v = 0; v < n / 2; v++) {
                if (relationships[w][v] == 0) continue;
                flowNetwork.addEdge(new FlowEdge(w, v, Double.POSITIVE_INFINITY));   // k relationships for women
            }
        }

        // connecting from source to men
        for (int v = 0; v < n / 2; v++) {
            flowNetwork.addEdge(new FlowEdge(s, v, 1));
        }
        // connecting from women to target
        for (int w = n / 2; w < n; w++) {
            flowNetwork.addEdge(new FlowEdge(w, t, 1));
        }

        FordFulkerson maxFlow = new FordFulkerson(flowNetwork, s, t);
        return (int) maxFlow.value() == n / 2;
    }

    /*
    Question 3
    Maximum weight closure problem.
    A subset of vertices S in a digraph is closed if there are no edges pointing from S to a vertex outside S.
    Given a digraph with weights (positive or negative) on the vertices, find a closed subset of vertices of maximum total weight.
     */

    /*
    From wiki:
    Adding two additional vertices s and t.
    For each vertex v with positive weight in G, the augmented graph H contains an edge from s to v with capacity equal to the weight of v,
    and for each vertex v with negative weight in G, the augmented graph H contains an edge from v to t whose capacity is the negation of the weight of v.
    All of the edges in G are given infinite capacity in H.

    A minimum cut separating s from t in this graph cannot have any edges of G passing in the forward direction across the cut:
    a cut with such an edge would have infinite capacity and would not be minimum.
     */
    public class MaxWeightClosure {

        //idea : https://github.com/yixuaz/algorithm4-princeton-cos226/blob/master/princetonSolution/src/part2/week3/maxflow/MaxWeightClosure.java
        //wiki : https://en.wikipedia.org/wiki/Closure_problem

        private final List<Vertex> list;
        private final double weight;

        private static class Vertex {
            private final int id;
            private final double weight;

            public Vertex(int id, double weight) {
                this.id = id;
                this.weight = weight;
            }

            public String toString() {
                return id + " " + weight;
            }
        }

        public MaxWeightClosure(Digraph g, double[] weights) {
            assert g.V() == weights.length;

            List<Vertex> vertices = new LinkedList<>();
            for (int v = 0; v < g.V(); v++) {
                vertices.add(new Vertex(v, weights[v]));
            }

            int V = g.V();
            int s = V;          // source
            int t = V + 1;      // target

            // flowNetwork creation
            FlowNetwork flowNetwork = new FlowNetwork(V + 2);
            double total = 0.0;
            for (Vertex v : vertices) {
                if (v.weight > 0) {
                    total += v.weight;
                    flowNetwork.addEdge(new FlowEdge(s, v.id, v.weight));
                } else {
                    flowNetwork.addEdge(new FlowEdge(v.id, t, -v.weight));
                }
            }

            for (Vertex v : vertices) {
                for (int w : g.adj(v.id)) {
                    flowNetwork.addEdge(new FlowEdge(v.id, w, Double.POSITIVE_INFINITY));
                }
            }

            // maxFlow algorithm
            FordFulkerson maxFlow = new FordFulkerson(flowNetwork, s, t);
            list = new LinkedList<>();
            for (Vertex v : vertices) {
                if (maxFlow.inCut(v.id)) {
                    list.add(v);
                }
            }
            weight = total - maxFlow.value();
        }

    }
