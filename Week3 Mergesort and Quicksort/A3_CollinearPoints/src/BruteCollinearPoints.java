/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php
Author: Yu Chen
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final LineSegment[] segmentsArray;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points

        // check null
        if (points == null) throw new IllegalArgumentException("Points are null");
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("point is null");
        }

        // check repeated point
        Point[] sortedPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(sortedPoints);
        for (int i = 1; i < sortedPoints.length; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0)
                throw new IllegalArgumentException("Duplicate Points");
        }

        // store the results in ArrayList
        // reference: https://www.geeksforgeeks.org/how-to-add-an-element-to-an-array-in-java/
        List<LineSegment> temp = new ArrayList<>();
        // add results to ArrayList
        for (int i = 0; i < sortedPoints.length; i++) {
            for (int j = i + 1; j < sortedPoints.length; j++) {
                for (int k = j + 1; k < sortedPoints.length; k++) {
                    for (int l = k + 1; l < sortedPoints.length; l++) {
                        if (sortedPoints[i].slopeTo(sortedPoints[j]) == sortedPoints[i].slopeTo(sortedPoints[k]) && sortedPoints[i].slopeTo(sortedPoints[j]) == sortedPoints[i].slopeTo(sortedPoints[l])) {
                            temp.add(new LineSegment(sortedPoints[i], sortedPoints[l]));
                        }
                    }
                }
            }
        }
        segmentsArray = temp.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {
        // the number of line segments
        return segmentsArray.length;
    }

    public LineSegment[] segments() {
        // the line segments
        return segmentsArray.clone();
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
