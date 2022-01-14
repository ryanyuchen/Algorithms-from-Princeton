import stdlib.StdRandom;
import java.util.Arrays;

public class Mergesort {
    /*
    Question 1
    Merging with smaller auxiliary array.
    Suppose that the subarray a[0] to a[N-1] is sorted and the subarray a[N] to a[2*N-1] is sorted.
    How can you merge the two subarrays so that a[0] to a[2*N-1] is sorted using an auxiliary array of size N (instead of 2N)?
     */
    private boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

    public void mergeWithSmallerAuxArray(Comparable[] a) {
        // reference: https://massivealgorithms.blogspot.com/2019/03/merging-with-smaller-auxiliary-array.html

        int n = a.length/2;
        Comparable[] aux = new Comparable[n];

        for (int i = 0; i < n; i++) {
            aux[i] = a[i]; // copy subarray of a[0] to a[n-1] to aux
        }

        int l = 0;
        int r = n;

        for (int k = 0; k < 2*n; k++){
            if(l >= n) break; //aux is out
            else if(r >= 2 * n) a[k]=aux[l++]; //copy the left of aux to a
            else if(less(array[r],aux[l])) a[k] = array[r++];
            else a[k] = aux[l++];
        }
    }

    /*
    Question 2
    Counting inversions.
    An inversion in an array a[] is a pair of entries a[i] and a[j] such that i<j but a[i]>a[j].
    Given an array, design a linearithmic algorithm to count the number of inversions.
     */
    public int Countinginversions(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        //reference: https://www.geeksforgeeks.org/counting-inversions/

        // during the merge process
        private static int mergeAndCount(int[] arr, int l, int m, int r)
        {
            // Left subarray
            int[] left = Arrays.copyOfRange(arr, l, m + 1);

            // Right subarray
            int[] right = Arrays.copyOfRange(arr, m + 1, r + 1);

            int i = 0, j = 0, k = l, swaps = 0;

            while (i < left.length && j < right.length) {
                if (left[i] <= right[j])
                    arr[k++] = left[i++];
                else {
                    arr[k++] = right[j++];
                    swaps += (m + 1) - (l + i);
                }
            }
            while (i < left.length)
                arr[k++] = left[i++];
            while (j < right.length)
                arr[k++] = right[j++];
            return swaps;
        }

        // Merge sort function
        private static int mergeSortAndCount(int[] arr, int l, int r)
        {
            // Keeps track of the inversion count at a
            // particular node of the recursion tree
            int count = 0;

            if (l < r) {
                int m = (l + r) / 2;

                // Total inversion count = left subarray count + right subarray count + merge count
                // Left subarray count
                count += mergeSortAndCount(arr, l, m);

                // Right subarray count
                count += mergeSortAndCount(arr, m + 1, r);

                // Merge count
                count += mergeAndCount(arr, l, m, r);
            }

            return count;
        }

    /*
    Question 3
    Shuffling a linked list.
    Given a singly-linked list containing N items, rearrange the items uniformly at random.
    Your algorithm should consume a logarithmic (or constant) amount of extra memory and run in time proportional to NlogN in the worst case.
     */

    private class Node {
        Object item;
        Node next;
    }

    private void merge(Node lh, Node rh) {
        Node left = lh;
        Node right = rh;

        if (StdRandom.uniform(1) > 0) {
            lh = right;
            right = right.next;
        }
        else {
            left = left.next;
        }

        Node runner = lh;

        while (right != null || left != null) {
            if (left == null) {
                runner.next = right;
                right =right.next;
            }
            else if (right == null) {
                runner.next = left;
                left = left.next;
            }
            else if (StdRandom.uniform(1) > 0) {
                runner.next = right;
                right = right.next;
            }
            else {
                runner.next = left;
                left = left.next;
            }
            runner = runner.next;
        }
    }

    public void shuffle(Node head, int N) {
        if (N == 1) return;

        int k = 1;
        Node mid = head;
        while (k < N / 2) {
            mid = mid.next;
            k++;
        }
        Node rh = mid.next;
        mid.next = null;
        shuffle(head, N / 2);
        shuffle(rh, N - N / 2);
        merge(head, rh);
    }

}
