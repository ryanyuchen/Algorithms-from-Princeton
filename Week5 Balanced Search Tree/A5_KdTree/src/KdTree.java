/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
Author: Yu Chen
 */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private int size;
    private Node root;
    //private Point2D point;

    private static class Node {
        Point2D point;
        RectHV rect;
        Node left;
        Node right;
        boolean isVertical;

        private Node(Point2D point, boolean isVertical, Node prev) {
            this.point = point;
            this.isVertical = isVertical;

            if (prev == null) {
                this.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            } else {
                double xmin = prev.rect.xmin();
                double xmax = prev.rect.xmax();
                double ymin = prev.rect.ymin();
                double ymax = prev.rect.ymax();

                int comp = prev.compareTo(this.point);

                if (this.isVertical) {
                    if (comp > 0.0) {
                        ymax = prev.point.y();
                    } else {
                        ymin = prev.point.y();
                    }
                } else {
                    if (comp > 0.0) {
                        xmax = prev.point.x();
                    } else {
                        xmin = prev.point.x();
                    }
                }

                this.rect = new RectHV(xmin, xmax, ymin, ymax);
            }
        }

        private int compareTo(Point2D that) {
            if (this.isVertical) {
                return Double.compare(this.point.x(), that.x());
            } else {
                return Double.compare(this.point.y(), that.y());
            }
        }
    }

    public KdTree() {
        // construct an empty set of points
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        // is the set empty?
        return size == 0;
    }

    public int size() {
        // number of points in the set
        return size;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("null point");

        root = insertAt(root, p, true, null);
    }

    private Node insertAt(Node curr, Point2D point, boolean isVertical, Node prev) {
        if (curr == null) {
            size++;
            return new Node(point, isVertical, prev);
        }

        if (curr.point.compareTo(point) == 0.0) {
            return curr;
        }

        /*
        Search and insert. The algorithms for search and insert are similar
        to those for BSTs, but at the root we use the x-coordinate (if the
        point to be inserted has a smaller x-coordinate than the point at
        the root, go left; otherwise go right); then at the next level, we
        use the y-coordinate (if the point to be inserted has a smaller
        y-coordinate than the point in the node, go left; otherwise go right);
        then at the next level the x-coordinate, and so forth
         */
        int comp = curr.compareTo(point);
        if (comp > 0.0) {
            curr.left = insertAt(curr.left, point, !isVertical, curr);
        } else {
            curr.right = insertAt(curr.right, point, !isVertical, curr);
        }

        return curr;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("null point");
        return search(root, p);
    }

    private boolean search(Node curr, Point2D point) {
        if (curr == null) {
            return false;
        } else if (curr.point.equals(point)) {
            return true;
        } else {
            int comp = curr.compareTo(point);

            if (comp > 0.0) {
                // point is at the left of curr node
                return search(curr.left, point);
            } else {
                // point is at the right of curr node
                return search(curr.right, point);
            }
        }
    }

    public void draw() {
        // draw all points to standard draw
        // to-do
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("null rect");
        if (isEmpty()) return null;
        /*
        Range search. To find all points contained in a given query rectangle,
        start at the root and recursively search for points in both subtrees
        using the following pruning rule: if the query rectangle does not intersect
        the rectangle corresponding to a node, there is no need to explore that
        node (or its subtrees). A subtree is searched only if it might contain
        a point contained in the query rectangle.
         */

        List<Point2D> res = new LinkedList<>();
        searchRange(root, rect, res);
        return res;
    }

    private void searchRange(Node curr, RectHV rect, List<Point2D> list) {
        // search to the leave
        if (curr == null) return;

        if (rect.intersects(curr.rect)) {
            if (rect.contains(curr.point)) {
                list.add(curr.point);
            }
            searchRange(curr.left, rect, list);
            searchRange(curr.right, rect, list);
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("null point");
        if (isEmpty()) return null;
        /*
        Nearest-neighbor search. To find a closest point to a given query point,
        start at the root and recursively search in both subtrees using the following
        pruning rule: if the closest point discovered so far is closer than the
        distance between the query point and the rectangle corresponding to a node,
        there is no need to explore that node (or its subtrees). That is, search a
        node only only if it might contain a point that is closer than the best one
        found so far. The effectiveness of the pruning rule depends on quickly finding
        a nearby point. To do this, organize the recursive method so that when there
        are two possible subtrees to go down, you always choose the subtree that is
        on the same side of the splitting line as the query point as the first subtree
        to explore—the closest point found while exploring the first subtree may enable
        pruning of the second subtree.
         */
        return searchNearest(root, p, root.point);
    }

    private Point2D searchNearest(Node curr, Point2D target, Point2D nearest) {
        // search to the leave
        if (curr == null) return nearest;
        // if curr point is equal to target, then return
        if (curr.point.equals(target)) return curr.point;

        if (curr.rect.distanceSquaredTo(target) < target.distanceSquaredTo(nearest)) {
            if (target.distanceSquaredTo(curr.point) < target.distanceSquaredTo(nearest)) {
                nearest = curr.point;
            }

            int comp = curr.compareTo(target);
            if (comp < 0) {
                nearest = searchNearest(curr.right, target, nearest);
            } else {
                nearest = searchNearest(curr.left, target, nearest);
            }
        }

        return nearest;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)

    }
}
