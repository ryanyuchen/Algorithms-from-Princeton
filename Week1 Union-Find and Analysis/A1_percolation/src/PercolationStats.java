/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
***********************
This file is for a series of computational experiments
Author: Yu Chen
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] results;
    private final double d_95 = 1.96;
    private final int N;
    private final int T;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        this.N = n;
        this.T = trials;

        // check n and trials
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trails have to be > 0");
        }

        // initialize results
        results = new double[trials];

        // go through trails
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            int openedNum = 0;
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;

                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    openedNum++;
                }
            }

            results[i] = openedNum * 1.0 / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - d_95 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + d_95 * stddev() / Math.sqrt(T);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats testStats = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + testStats.mean());
        StdOut.println("stddev                  = " + testStats.stddev());
        StdOut.println("95% confidence interval = "
                + testStats.confidenceLo() + ", "
                + testStats.confidenceHi());

    }

}
