/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
Author: Yu Chen
 */


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    /*
    Reference for SET and TreeSet
    https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/SET.html
    https://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html
     */
    private final SET<Point2D> points;

    public PointSET() {
        // construct an empty set of points
        points = new SET<>();
    }

    public boolean isEmpty() {
        // is the set empty?
        return points.isEmpty();
    }

    public int size() {
        // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("null point");
        if (!points.contains(p)) points.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("null point");
        return points.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D p : points) p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("null rect");

        SET<Point2D> res = new SET<>();
        for (Point2D p : points) {
            if (rect.contains(p)) res.add(p);
        }

        return res;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("null point");
        if (points.isEmpty()) return null;

        Point2D res = null;
        for (Point2D k : points) {
            if (res == null) {
                res = k;
                continue;
            }

            if (p.distanceSquaredTo(k) < k.distanceSquaredTo(res)) {
                res = k;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)

    }
}
