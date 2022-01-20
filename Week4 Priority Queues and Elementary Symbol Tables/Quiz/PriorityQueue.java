import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SymbolDigraph;

public class PriorityQueue {
    /*
    Question 1
    Dynamic median.
    Design a data type that supports insert in logarithmic time, find-the-median in constant time, and remove-the-median in logarithmic time.
     */
    
    /* 
    Use 2 heaps: a maxHeap contains smaller half of items, a minHeap contains larger half of items.

    First add 2 first items into heaps. Add smaller one into maxHeap, add larger one into minHeap.

    Then process next item with following steps:

    If item is smaller than root of maxHeap, add it to maxHeap, otherwise, add it to minHeap.

    Balance the heaps (this this step heaps will be either balanced or one of them will contain 1 more item). 
    If number of items in one of the heap is greater than other heap by more than 1, remove the root of larger 
    heap and add it to other heap.
    */

    class MediaHeap {
        private MaxPQ<Integer> left;
        private MinPQ<Integer> right;
        private int L;
        private int R;

        MediaHeap() {
            left = new MaxPQ<Integer>();
            right = new MinPQ<Integer>();
        }

        public double findMedian() {
            int L = left.size();
            int R = right.size();
            if (L == R)
                return ((double)left.max() + (double)right.min()) / 2;
            else if (L > R)
                return left.max();
            else
                return right.min();
        }

        public void insert(int key) {
            double median = findMedian();
            int L = left.size();
            int R = right.size();
            if (key <= median) {
                left.insert(key);
                if (L - R > 1)
                    right.insert(left.delMax());
            }
            else {
                right.insert(key);
                if (R - L > 1)
                    left.insert(right.delMin());
            }
        }

        public void removeMedian() {
            int L = left.size();
            int R = right.size();
            if (L > R) {
                left.delMax();
            }
            else {
                right.delMin();
            }
        }

    }

    // reference: https://zhangxycc.github.io/2019/07/01/Coursera-Algorithm-Week4-Interview-Questions/
    class MedianFinder {

        private PriorityQueue<Integer> maxQ = new PriorityQueue<Integer>();

        PriorityQueue<Integer> minQ = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // TODO Auto-generated method stub
                return o2 - o1;
            }
            });
            
        public void addNum(int num) {
            if(minQ.size() == 0 || minQ.peek() >= num){
                minQ.add(num);
            }else{
                maxQ.add(num);
            }
            if(maxQ.size() == minQ.size() + 1){
                minQ.add(maxQ.poll());
            }
            if(maxQ.size() + 2 == minQ.size()){
                maxQ.add(minQ.poll());
            }
        }
    }
    
    public double findMedian() {
        return minQ.size() == maxQ.size() ? (minQ.peek() +maxQ.peek()) / 2.0 : minQ.peek();
    }
}

    /*
    Question 2
    Randomized priority queue.
    Describe how to add the methods sample() and delRandom() to our binary heap implementation.
    The two methods return a key that is chosen uniformly at random among the remaining keys, with the latter method also removing that key.
    The sample() method should take constant time; the delRandom() method should take logarithmic time.
    Do not worry about resizing the underlying array.
     */

    /*
    generate random number from 0 - N, sample() just return that number
    when delete random, exchange with last, delete last, then compare with parent and children to decide whether swim or sink
     */

    /*
    Question 3
    Taxicab numbers.
    A taxicab number is an integer that can be expressed as the sum of two cubes of integers in two different ways: a^3+b^3=c^3+d^3.
    For example, 1729=9^3+10^3=1^3+12^3. Design an algorithm to find all taxicab numbers with a, b, c, and d less than N.
    Version 1: Use time proportional to N^2logN and space proportional to N^2.
    Version 2: Use time proportional to N^2logN and space proportional to N.
     */

     /*
     Imagine a 2-D matrix m[i][j] = i^3 + j^3. We don't have to create this matrix in memory. 
     Row's item are in ascending order and column's item are in ascending order too. We could 
     use a minHeap (minPQ) to store the diagonal first. Then do following steps until minHeap is empty:

    Get current min (minCur) from the minHeap, compare it to the preMin, if they are equal, we 
    find a pair of sums (a^3+b^3=c^3+d^3).
    Put the item to the right of minCur in matrix to the minHeap.
    Algorithm works becuase it guarantees that all items in matrix are added and taken out of 
    the minHeap in order. We always take the min item so far from the minHeap and add smallest 
    larger item to the heap for every iteration. The heap contains N items only.

     */

    class Taxicab implements Comparable<Taxicab>{
        int n1;
        int n2;
        int cube;

        Taxicab(int n1, int n2) {
            this.n1 = n1;
            this.n2 = n2;
            this.cube = n1 * n1 * n1 + n2 * n2 * n2;
        }

        @Override
        public int compareTo(Taxicab that) {
            if (that.cube > this.cube) return -1;
            if (that.cube < this.cube) return 1;
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Taxicab) {
                if (((Taxicab)o).compareTo(this) == 0)
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "number: " + cube + " (" + n1 + ", " + n2 + ")";
        }
    }

    public void findTaxinumber(int N) {
        MinPQ<Taxicab> candidates = new MinPQ<Taxicab>();

        for (int i = 1; i <= N; i++) {
            for (int j = i + 1; j <= N; j++) {
                Taxicab t = new Taxicab(i, j);
                if (candidates.size() < N) {
                    candidates.insert(t);
                }
                else {
                    Queue<Taxicab> temp = new Queue<Taxicab>();
                    Taxicab min = candidates.delMin();
                    while (candidates.min().equals(min)) {
                        temp.enqueue(candidates.delMin());
                    }
                    if (!t.equals(min)) {
                        candidates.insert(t);
                    }
                    else {
                        temp.enqueue(t);
                    }
                    if (!temp.isEmpty()) {
                        for (Taxicab taxi: temp) {
                            System.out.println(taxi);
                        }
                        System.out.println(min);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        PriorityQueue p = new PriorityQueue();
        p.findTaxinumber(12);
    }
}
