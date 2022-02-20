/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php
Author: Yu Chen
 */

import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private final Picture picture;
    private int width;
    private int height;
    private double[] distTo[];
    private int[] edgeTo[];

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

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
