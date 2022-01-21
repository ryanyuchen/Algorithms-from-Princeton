import edu.princeton.cs.algs4.Shell;

public class ElementarySorts {
    /*
    Question 1
    Intersection of two sets.
    Given two arrays a[] and b[], each containing N distinct 2D points in the plane,
    design a subquadratic algorithm to count the number of points that are contained both in array a[] and array b[].
     */

    class Point implements Comparable<Point>{
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point that) {
            if (that.x > this.x) return -1;
            if (that.x < this.x) return 1;
            if (that.y > this.y) return -1;
            if (that.y < this.y) return 1;
            return 0;
        }

        public int countIntersection(Point[] a, Point[] b) {
            Shell.sort(a);
            Shell.sort(b);

            int i = 0;
            int j = 0;
            int count = 0;

            while (i < a.length && j < b.length) {
                if (a[i].compareTo(b[j]) == 0) {
                    count++;
                    i++;
                    j++;
                }
                else if (a[i].compareTo(b[j]) < 0) i++;
                else j++;
            }
            return count;
        }
    }

    // add leetcode solution, reference: https://afteracademy.com/blog/find-the-intersection-of-two-unsorted-arrays
    class Solution {
        public int[] intersection(int[] nums1, int[] nums2) {
            Set<Integer> temp = new HashSet<Integer>();
            int n = nums1.length;
            int m = nums2.length;
            int i = 0;
            int j = 0;
            Arrays.sort(nums1);
            Arrays.sort(nums2);
            
            while (i < n && j < m) {
                if (nums1[i] == nums2[j]) {
                    temp.add(nums1[i]);
                    i++;
                    j++;
                }
                else if (nums1[i] < nums2[j]) i++;
                else j++;
            }
            
            System.out.print(temp.size());
            int[] res = new int[temp.size()];
            i = 0;
            for(Integer k : temp) res[i++] = k;
            
            return res;
            
        }
    }

    /*
    Question 2
    Permutation.
    Given two integer arrays of size N, design a subquadratic algorithm to determine whether one is a permutation of the other.
    That is, do they contain exactly the same entries but, possibly, in a different order.
     */

    public boolean isPerm(Integer[] a, Integer[] b) {
        if (a.length != b.length) return false;
        Shell.sort(a);
        Shell.sort(b);

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    /*
    Question 3
    Dutch national flag. Given an array of N buckets, each containing a red, white, or blue pebble, sort them by color. The allowed operations are:
    * swap(i,j): swap the pebble in bucket i with the pebble in bucket j.
    * color(i): color of pebble in bucket i.
    The performance requirements are as follows:
    * At most N calls to color().
    * At most N calls to swap().
    * Constant extra space.
     */

    enum Pebble {
        Red,
        White,
        Blue
    }

    class Buckets {
        private Pebble[] pebbles;

        private Pebble color(int i) {
            return pebbles[i];
        }

        private int compare(Pebble p) {
            Pebble white = Pebble.White;
            return p.ordinal() - white.ordinal();
        }

        private void swap(int i, int j) {
            Pebble tmp = pebbles[i];
            pebbles[i] = pebbles[j];
            pebbles[j] = tmp;
        }

        public void sort() {
            assert pebbles.length > 0;
            int r = 0;
            int runner = 0;
            int b = pebbles.length - 1;

            while (runner <= b) {
                Pebble color = color(runner);
                int cmp = compare(color);
                if (cmp < 0) {
                    swap(runner++, r++);
                }
                else if (cmp > 0) {
                    swap(runner, b--);
                }
                else {
                    runner++;
                }

            }
        }


    }
}
