/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
Author: Yu Chen
*/

import edu.princeton.cs.algs4.*;

public class WordNet {

    private final Digraph digraph;
    private final SAP sap;
    private final ST<Integer, SET<String>> nounNet;
    private final ST<String, SET<Integer>> nounIds;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("input is null");

        // read synsets into nounNet
        nounNet = new ST<>();
        nounIds = new ST<>();
        In in = new In(synsets);
        while (in.hasNextLine()) {
            SET<String> line = new SET<>();
            String[] line_strings = in.readLine().split(",");
            int id = Integer.parseInt(line_strings[0]);
            String[] strings = line_strings[1].split(" ");

            for (String s : strings) {
                line.add(s);
                SET<Integer> ints = nounIds.get(s);
                // if s in not in nounIds, create a new one
                if (ints == null) {
                    ints = new SET<>();
                    nounIds.put(s, ints);
                }
                ints.add(id);
            }
            nounNet.put(id, line);
        }

        // create digraph from hypernyms
        digraph = new Digraph(nounNet.size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] strings = in.readLine().split(",");
            int u = Integer.parseInt(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                int v = Integer.parseInt(strings[i]);
                digraph.addEdge(u, v);
            }
        }

        // check digraph has cycle or root
        DirectedCycle cycle = new DirectedCycle(digraph);
        if (cycle.hasCycle()) throw new IllegalArgumentException("not a dag");
        if (!checkRoot(digraph)) throw new IllegalArgumentException("not rooted dag");

        // initialize sap
        sap = new SAP(digraph);
    }

    private boolean checkRoot(Digraph g) {
        int count = 0;
        for (int i = 0; i < g.V(); i++) {
            if (g.indegree(i) != 0 && g.outdegree(i) == 0) count++;
        }

        return count == 1;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounIds.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("word is null");

        return nounIds.get(word) != null;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("input is null");
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("input is not in WordNet");

        return sap.length(nounIds.get(nounA), nounIds.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("input is null");
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("input is not in WordNet");

        int ancestor = sap.ancestor(nounIds.get(nounA), nounIds.get(nounB));
        SET<String> ancestor_strings = nounNet.get(ancestor);
        String res = "";
        for (String s : ancestor_strings) res = res.concat(s);

        return res;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("./synsets8.txt", "./hypernyms8ManyAncestors.txt");
        System.out.println(wordNet.distance("a", "c"));
        System.out.println(wordNet.sap("a", "c"));

    }
}
