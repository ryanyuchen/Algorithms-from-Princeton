/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/boggle/specification.php
Author: Yu Chen
*/


public class BoggleSolver {

    private final Node root;
    private final int[] rowLabel = {-1, 0, 1, -1, 1, -1, 0, 1};
    private final int[] colLabel = {-1, -1, -1, 0, 0, 1, 1, 1};

    private static class Node {
        Node[] next = new Node[26];
        int value = -1;
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {


    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        
    }
}
