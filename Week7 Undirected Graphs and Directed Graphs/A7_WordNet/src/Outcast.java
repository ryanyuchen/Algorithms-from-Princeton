/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
Author: Yu Chen
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordnets;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wordnets = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxlen = 0;
        int d;
        int ancestor = -1;

        for (int i = 0; i < nouns.length; i++) {
            d = 0;
            for (int j = 0; j < nouns.length; j++) {
                d += wordnets.distance(nouns[i], nouns[j]);
            }

            if (d > maxlen) {
                maxlen = d;
                ancestor = i;
            }
        }

        return nouns[ancestor];
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
