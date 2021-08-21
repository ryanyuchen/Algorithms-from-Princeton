/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
*************************
This is for Percolation class
Author: Yu Chen
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF backUF; // for isFull()
    private final WeightedQuickUnionUF percUF; // for percolate()
    private final boolean[] status;
    private final int N;
    private int openedNum = 0;
    private final int topNode;
    private final int botNode;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }
        this.N = n;
        this.topNode = 0;
        this.botNode = n * n + 1;  // for percUF only

        // initialization and convert 2D to 1D array
        backUF = new WeightedQuickUnionUF(n * n + 1); // without bottom node
        percUF = new WeightedQuickUnionUF(n * n + 2);
        status = new boolean[n * n + 2];
        status[topNode] = true;
        status[botNode] = true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > N) {
            throw new IllegalArgumentException("row is out of bounds");
        } else if (col < 1 || col > N) {
            throw new IllegalArgumentException("col is out of bounds");
        } else {
            // row base-1 index of row
            // col base-1 index of col
            // for 3x3 and site of (2,2), curIndex = 5
            int curIndex = (row - 1) * N + col;
            status[curIndex] = true;
            openedNum++;

            // union top node to top rows and bottom node to bottom rows
            if (row == 1) {
                percUF.union(curIndex, topNode);
                backUF.union(curIndex, topNode);
            }
            if (row == N) {
                percUF.union(curIndex, botNode);
            }
            // if the north site is open, then union
            // for 3x3 and site of (2,2), northIndex = 2
            int northIndex = (row - 1 - 1) * N + col;
            if (row > 1 && status[northIndex]) {
                percUF.union(curIndex, northIndex);
                backUF.union(curIndex, northIndex);
            }
            // if the south site is open, the union
            // for 3x3 and site of (2,2), southIndex = 8
            int southIndex = (row + 1 - 1) * N + col;
            if (row < N && status[southIndex]) {
                percUF.union(curIndex, southIndex);
                backUF.union(curIndex, southIndex);
            }
            // if the west site is open, the union
            // for 3x3 and site of (2,2), westIndex = 4
            int westIndex = (row - 1) * N + col - 1;
            if (col > 1 && status[westIndex]) {
                percUF.union(curIndex, westIndex);
                backUF.union(curIndex, westIndex);
            }
            // if the east site is open, the union
            // for 3x3 and site of (2,2), eastIndex = 6
            int eastIndex = (row - 1) * N + col + 1;
            if (col < N && status[eastIndex]) {
                percUF.union(curIndex, eastIndex);
                backUF.union(curIndex, eastIndex);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > N) {
            throw new IllegalArgumentException("row is out of bounds");
        } else if (col < 1 || col > N) {
            throw new IllegalArgumentException("col is out of bounds");
        } else {
            int curIndex = (row - 1) * N + col;
            return status[curIndex];
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > N) {
            throw new IllegalArgumentException("row is out of bounds");
        } else if (col < 1 || col > N) {
            throw new IllegalArgumentException("col is out of bounds");
        } else {
            int curIndex = (row - 1) * N + col;
            return backUF.connected(topNode, curIndex);
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedNum;
    }

    // does the system percolate?
    public boolean percolates() {
        return percUF.connected(topNode, botNode);
    }

    // test client (optional)
    public static void main(String[] args) {
        StdOut.println("Please run PercolationStats for testing.");
    }
}
