/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php
Author: Yu Chen
 */

import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    private double[] distTo;
    private int[][] edgeTo;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("picture is null");
        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("out of range");
        }

        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            return 1000.0;
        }

        Color colorW1 = picture.get(x + 1, y);
        Color colorW2 = picture.get(x - 1, y);
        int rDx = colorW1.getRed() - colorW2.getRed();
        int bDx = colorW1.getBlue() - colorW2.getBlue();
        int gDx = colorW1.getGreen() - colorW2.getGreen();
        int Dx = rDx * rDx + bDx * bDx + gDx * gDx;

        Color colorH1 = picture.get(x, y + 1);
        Color colorH2 = picture.get(x, y - 1);
        int rDy = colorH1.getRed() - colorH2.getRed();
        int bDy = colorH1.getBlue() - colorH2.getBlue();
        int gDy = colorH1.getGreen() - colorH2.getGreen();
        int Dy = rDy * rDy + bDy * bDy + gDy * gDy;

        return Math.sqrt(Dx + Dy);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[width];
        distTo = new double[height];
        edgeTo = new int[width][height];

        for (int i = 0; i < height; i++) {
            distTo[i] = 1000.0;
        }

        double[][] energy = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy[i][j] = energy(i, j);
            }
        }

        for (int i = 1; i < width; i++) {
            double[] prevDist = distTo.clone();

            for (int j = 0; j < height; j++) {
                distTo[j] = Double.POSITIVE_INFINITY;
            }

            for (int j = 1; j < height; j++) {
                relaxH(j - 1, i, j, prevDist, energy[i][j]);
                relaxH(j, i, j, prevDist, energy[i][j]);
                relaxH(j + 1, i, j, prevDist, energy[i][j]);
            }
        }

        double min = Double.POSITIVE_INFINITY;
        int minVertex = 0;
        for (int i = 0; i < height; i++) {
            if (distTo[i] < min) {
                min = distTo[i];
                minVertex = i;
            }
        }

        for (int i = width - 1; i >= 0; i--) {
            seam[i] = minVertex;
            minVertex = edgeTo[i][minVertex];
        }

        return seam;
    }

    private void relaxH(int prev, int x, int y, double[] prevDist, double energy) {
        if (prev < 0 || prev > height - 1) return;
        if (distTo[y] > prevDist[prev] + energy) {
            distTo[y] = prevDist[prev] + energy;
            edgeTo[x][y] = prev;
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height];
        distTo = new double[width];
        edgeTo = new int[width][height];

        for (int i = 0; i < width; i++) {
            distTo[i] = 1000.0;
        }

        double[][] energy = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy[i][j] = energy(i, j);
            }
        }

        for (int i = 1; i < height; i++) {
            double[] prevDist = distTo.clone();

            for (int j = 0; j < width; j++) {
                distTo[j] = Double.POSITIVE_INFINITY;
            }

            for (int j = 1; j < width; j++) {
                relaxV(j - 1, j, i, prevDist, energy[j][i]);
                relaxV(j, j, i, prevDist, energy[j][i]);
                relaxV(j + 1, j, i, prevDist, energy[j][i]);
            }
        }

        double min = Double.POSITIVE_INFINITY;
        int minVertex = 0;
        for (int i = 0; i < width; i++) {
            if (distTo[i] < min) {
                min = distTo[i];
                minVertex = i;
            }
        }

        for (int i = height - 1; i >= 0; i--) {
            seam[i] = minVertex;
            minVertex = edgeTo[i][minVertex];
        }

        return seam;
    }

    private void relaxV(int prev, int x, int y, double[] prevDist, double energy) {
        if (prev < 0 || prev > width - 1) return;
        if (distTo[x] > prevDist[prev] + energy) {
            distTo[x] = prevDist[prev] + energy;
            edgeTo[x][y] = prev;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam is null");
        }
        if (height <= 1 || seam.length != width) {
            throw new IllegalArgumentException("height not valid");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height - 1) {
                throw new IllegalArgumentException("out of range");
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("difference is big");
            }
        }

        Picture pic = new Picture(width, height - 1);
        int k = 0;
        for (int i = 0; i < seam.length; i++) {
            for (int j = 0; j < seam[i]; j++) {
                pic.set(i, k, picture.get(i, j));
                k++;
            }
            for (int j = seam[i] + 1; j < height; j++) {
                pic.set(i, k, picture.get(i, j));
                k++;
            }
            k = 0;
        }
        picture = pic;
        width = picture.width();
        height = picture.height();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("argument array is null");
        }
        if (width <= 1 || seam.length != height) {
            throw new IllegalArgumentException("width not valid");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > width - 1) {
                throw new IllegalArgumentException("out of range");
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("difference is big");
            }
        }

        Picture pic = new Picture(width - 1, height);
        int k = 0;
        for (int i = 0; i < seam.length; i++) {
            for (int j = 0; j < seam[i]; j++) {
                pic.set(k, i, picture.get(j, i));
                k++;
            }
            for (int j = seam[i] + 1; j < width; j++) {
                pic.set(k, i, picture.get(j, i));
                k++;
            }
            k = 0;
        }
        picture = pic;
        width = picture.width();
        height = picture.height();
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
