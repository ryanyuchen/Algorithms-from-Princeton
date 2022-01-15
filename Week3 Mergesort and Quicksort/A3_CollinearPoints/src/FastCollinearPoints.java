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

public class FastCollinearPoints {

    private final LineSegment[] segmentsArray;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points

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

        List<LineSegment> temp = new ArrayList<>();
        for (int i = 0; i < sortedPoints.length; i++) {
            // sort the array by slope to p
            Point p = sortedPoints[i];
            Point[] pointsBYSlope = sortedPoints.clone();
            Arrays.sort(pointsBYSlope, p.slopeOrder());

            int idx = 1;
            int start = 0;
            double prevSlope = Double.NEGATIVE_INFINITY;

            // Candidates have a max line segment if ...
            // 1. Candidates are collinear: At least 4 points are located
            //    at the same line, so at least 3 without "p".
            // 2. The max line segment is created by the point "p" and the
            //    last point in candidates: so "p" must be the smallest
            //    point having this slope comparing to all candidates.
            for (int j = 0; j < pointsBYSlope.length; j++) {
                double currSlope = p.slopeTo(pointsBYSlope[j]);

                if (Double.compare(currSlope, prevSlope) != 0) {
                    if (idx >= 4 && p.compareTo(pointsBYSlope[start]) <= 0) {
                        temp.add(new LineSegment(p, pointsBYSlope[j - 1]));
                    }
                    idx = 1;
                    start = j;
                } else if (j == pointsBYSlope.length - 1) {
                    if (idx >= 3 && p.compareTo(pointsBYSlope[start]) <= 0) {
                        temp.add(new LineSegment(p, pointsBYSlope[j]));
                    }
                    idx = 1;
                }

                idx++;
                prevSlope = currSlope;

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
