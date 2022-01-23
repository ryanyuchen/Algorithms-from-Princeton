/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php
Author: Yu Chen
 */

import edu.princeton.cs.algs4.StdIn;

import java.util.LinkedList;
import java.util.List;

public class Board {
    private final int[][] puzzles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        puzzles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                puzzles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String s = Integer.toString(n) + '\n';
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s += ' ' + Integer.toString(puzzles[i][j]);
            }
            s += '\n';
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (puzzles[i][j] != 0 && puzzles[i][j] != n * i + j + 1) count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (puzzles[i][j] != 0 && puzzles[i][j] != n * i + j + 1) {
                    int goalRow = (puzzles[i][j] - 1) / n;
                    int goalCol = puzzles[i][j] - n * goalRow - 1;
                    dist += Math.abs(goalRow - i) + Math.abs(goalCol - j);
                }
            }
        }
        return dist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (puzzles[i][j] != 0 && puzzles[i][j] != n * i + j + 1) return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board board = (Board) y;
        if (this.dimension() != ((Board) y).dimension()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (puzzles[i][j] != board.puzzles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbours = new LinkedList<>();
        int[][] neighboursarray = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                neighboursarray[i][j] = puzzles[i][j];
            }
        }

        int c = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (puzzles[i][j] == 0 && i - 1 >= 0) {
                    neighboursarray[i][j] = neighboursarray[i - 1][j];
                    neighboursarray[i - 1][j] = 0;
                    neighbours.add(new Board(neighboursarray));
                    neighboursarray[i][j] = 0;
                    neighboursarray[i - 1][j] = puzzles[i - 1][j];
                    c = 1;
                }
                if (puzzles[i][j] == 0 && i + 1 < n) {
                    neighboursarray[i][j] = neighboursarray[i + 1][j];
                    neighboursarray[i + 1][j] = 0;
                    neighbours.add(new Board(neighboursarray));
                    neighboursarray[i][j] = 0;
                    neighboursarray[i + 1][j] = puzzles[i + 1][j];
                    c = 1;
                }
                if (puzzles[i][j] == 0 && j - 1 >= 0) {
                    neighboursarray[i][j] = neighboursarray[i][j - 1];
                    neighboursarray[i][j - 1] = 0;
                    neighbours.add(new Board(neighboursarray));
                    neighboursarray[i][j] = 0;
                    neighboursarray[i][j - 1] = puzzles[i][j - 1];
                    c = 1;
                }
                if (puzzles[i][j] == 0 && j + 1 < n) {
                    neighboursarray[i][j] = neighboursarray[i][j + 1];
                    neighboursarray[i][j + 1] = 0;
                    neighbours.add(new Board(neighboursarray));
                    neighboursarray[i][j] = 0;
                    neighboursarray[i][j + 1] = puzzles[i][j + 1];
                    c = 1;
                }
                if (c == 1) {
                    break;
                }
            }
            if (c == 1) {
                break;
            }
        }

        return neighbours;

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinall = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinall[i][j] = puzzles[i][j];
            }
        }
        if (puzzles[0][0] != 0 && puzzles[0][1] != 0) {
            twinall[0][0] = puzzles[0][1];
            twinall[0][1] = puzzles[0][0];
        } else {
            twinall[1][0] = puzzles[1][1];
            twinall[1][1] = puzzles[1][0];
        }
        return new Board(twinall);

    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        int[][] set = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                set[i][j] = StdIn.readInt();
            }
        }
        Board board = new Board(set);
        System.out.println(board.dimension());
        System.out.println(board.hamming());
        System.out.println(board.isGoal());
        System.out.println(board.manhattan());
        for (Board k : board.neighbors()) {
            System.out.println(k);
        }
        System.out.println(board.twin());

    }

}
