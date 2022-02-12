/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
Author: Yu Chen
*/

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {

    private final Digraph digraph;
    private static final int INFINITY = Integer.MAX_VALUE;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("input is null");
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v > digraph.V() - 1 || w > digraph.V() - 1)
            throw new IllegalArgumentException("input is not valid");

        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(digraph, w);

        return PathAncestor(pathV, pathW)[0];
    }

    private int[] PathAncestor(BreadthFirstDirectedPaths pathV, BreadthFirstDirectedPaths pathW) {
        int[] res = new int[2]; // 1st item is path length and second is ancestor
        int minlen = INFINITY;
        int ancestor = -1;

        for (int i = 0; i < digraph.V(); i++) {
            if (pathV.hasPathTo(i) && pathW.hasPathTo(i)) {
                int lenV = pathV.distTo(i);
                int lenW = pathW.distTo(i);
                if (lenV + lenW < minlen) {
                    minlen = lenV + lenW;
                    ancestor = i;
                }
            }
        }

        if (minlen < INFINITY) {
            res[0] = minlen;
            res[1] = ancestor;
        } else {
            res[0] = -1;
            res[1] = ancestor;
        }

        return res;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v > digraph.V() - 1 || w > digraph.V() - 1)
            throw new IllegalArgumentException("input is not valid");

        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(digraph, w);

        return PathAncestor(pathV, pathW)[1];

    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("input is null");
        for (Integer val : v) {
            if (val == null) throw new IllegalArgumentException("iterable is not valid");
        }
        for (Integer val : w) {
            if (val == null) throw new IllegalArgumentException("iterable is not valid");
        }

        // calculate the length of iterable
        int countV = 0, countW = 0;
        for (int i : v) countV++;
        for (int i : w) countW++;
        if (countV == 0 || countW == 0) return -1;

        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(digraph, w);

        return PathAncestor(pathV, pathW)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("input is null");
        for (Integer val : v) {
            if (val == null) throw new IllegalArgumentException("iterable is not valid");
        }
        for (Integer val : w) {
            if (val == null) throw new IllegalArgumentException("iterable is not valid");
        }

        // calculate the length of iterable
        int countV = 0, countW = 0;
        for (int i : v) countV++;
        for (int i : w) countW++;
        if (countV == 0 || countW == 0) return -1;

        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(digraph, w);

        return PathAncestor(pathV, pathW)[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
