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

        // check duplicate
        Point[] copy = Arrays.copyOf(points, points.length);
        Arrays.sort(copy);
        for (int i = 1; i < copy.length; i++) {
            if (copy[i].compareTo(copy[i - 1]) == 0) throw new IllegalArgumentException("Duplicate Points");
        }

        // sort the results in ArrayList
        // reference: https://www.geeksforgeeks.org/how-to-add-an-element-to-an-array-in-java/
        List<LineSegment> temp = new ArrayList<>();
        // add results to ArrayList
        for (int i = 0; i < copy.length; i++) {
            for (int j = i + 1; j < copy.length; j++) {
                for (int k = j + 1; k < copy.length; k++) {
                    for (int l = k + 1; l < copy.length; l++) {
                        if (copy[i].slopeTo(copy[j]) == copy[i].slopeTo(copy[k]) && copy[i].slopeTo(copy[j]) == copy[i].slopeTo(copy[l])) {
                            temp.add(new LineSegment(copy[i], copy[l]));
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
