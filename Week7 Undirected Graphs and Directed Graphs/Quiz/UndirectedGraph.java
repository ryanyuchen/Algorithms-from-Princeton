import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;

public class UndirectedGraph {
    private boolean[] marked;
    //private int[] edgeTo;
    private int V;  // number of vertex

    public Iterator<Integer> adj(int v) {
        return null;
    }

    /*
    Question 1
    Nonrecursive depth-first search.
    Implement depth-first search in an undirected graph without using recursion.
     */

    /*
    Reference: https://www.geeksforgeeks.org/iterative-depth-first-traversal/
    Algorithm: 
    1. Created a stack of nodes and visited array.
    2. Insert the root in the stack.
    3. Run a loop till the stack is not empty.
    4. Pop the element from the stack and print the element.
    5. For every adjacent and unvisited node of current node, mark the node and insert it in the stack.
    */
    private void dfs(UndirectedGraph G, int v) {
        marked[v] = true;
        Stack<Integer> visited = new Stack<Integer>();
        visited.push(v);

        while (!visited.isEmpty()) {
            int tmp = visited.peek();
            if (adj(tmp).hasNext()) {
                int w = adj(tmp).next();
                if (!marked[w]) {
                    marked[w] = true;
                    visited.push(w);
                }
            }
            else {
                visited.pop();
            }
        }

    }

    /*
    Question 2
    Diameter and center of a tree. Given a connected graph with no cycles
    Diameter: design a linear-time algorithm to find the longest simple path in the graph.
    Center: design a linear-time algorithm to find a vertex such that its maximum distance from any other vertex is minimized.
     */

    public static void diameter(Graph g) {
        assert g.V() > 0;
        //first pass, find all paths from 0, and find the farest vertice from 0
        int max = farest(g, 0);
        //second pass, from the farest vertice, find all paths from it
        BreadthFirstPaths path = new BreadthFirstPaths(g, max);
        int end = farest(g, max);
        for (Integer v : path.pathTo(end)) {
            System.out.println(v);
        }
    }

    private static int farest(Graph g, int v) {
        BreadthFirstPaths path = new BreadthFirstPaths(g, v);
        int max = 0;
        int len = 0;
        for (int i = 1; i < g.V(); i++) {
            if (path.distTo(i) > len) {
                max = i;
                len = path.distTo(i);
            }
        }
        return max;
    }

    public static int center(Graph g) {
        assert g.V() > 0;
        //first find the farest, as above
        int start = farest(g, 0);
        int end = farest(g, start);
        BreadthFirstPaths spath = new BreadthFirstPaths(g, start);
        BreadthFirstPaths epath = new BreadthFirstPaths(g, end);
        int result = start;
        for (int i = 0; i < g.V(); i++) {
            if (spath.distTo(i) == epath.distTo(i) || spath.distTo(i) == epath.distTo(i) + 1)
                result = i;
        }
        return result;
    }

    /*
    Question 3
    Euler cycle.
    An Euler cycle in a graph is a cycle (not necessarily simple) that uses every edge in the graph exactly one.
    Show that a connected graph has an Euler cycle if and only if every vertex has even degree.
    Design a linear-time algorithm to determine whether a graph has an Euler cycle, and if so, find one.
     */

    /*
    use the even edge number to check, then use dfs to print the cycle
     */
    
    /*
    Reference: https://www.geeksforgeeks.org/eulerian-path-and-circuit/
    Eulerian Cycle 
    An undirected graph has Eulerian cycle if following two conditions are true. 
    ….a) All vertices with non-zero degree are connected. We don’t care about vertices with zero degree because they don’t belong to Eulerian Cycle or Path (we only consider all edges). 
    ….b) All vertices have even degree.

    */
    void DFS(int v, boolean visited[]) {
        visited[v] = true;

        Iterator<Integer> i = adj[v].listIterator();
        while (i.hasNext()) {
            int j = i.next();
            if (!visited[j]) {
                DFS(j, visited);

            }
        }
    }

    boolean isCoonected() {
        boolean visited[] = new boolean[V];
        // initialize visted[] to false
        for (int i = 0; i < V; i++) {
            visited[i] = false;
        }

        // find a vertex with non-zero degree
        for (int i = 0; i < V; i++) {
            if (adj[i].size() != 0) break;
        }
        // if no degree, return true
        if (i == V) return true;

        // start dfs
        DFS(i, visited);

        for (int i = 0; i < V; i++) {
            if (visited[i] == false && adj[i].size() != 0) return false;
        }

        return true;

    }

    boolean isEular() {
        if (isCoonected() == false) return false;
        // vertex with odd degree
        int odd = 0;
        for (int i = 0; i < V; i++) {
            if (adj[i].size() % 2 != 0) odd++;
        }

        if (odd > 2) return false;
        if (odd == 0) return true;

    }


}
