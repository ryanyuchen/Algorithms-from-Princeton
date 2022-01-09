/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php
Author: Yu Chen
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // performance requires constant amortized time, so array is used
    private Item[] items;
    private int sz;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        sz = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return sz == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return sz;
    }

    // resize array
    private void resize(int capacity) {
        assert capacity >= sz;
        // slide 18
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < sz; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("added item is null");

        if (sz == items.length) resize(2 * items.length);
        items[sz++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("stack is empty");

        int idx = StdRandom.uniform(sz);
        Item item = items[idx];
        items[idx] = items[sz - 1];
        sz--;

        if (sz > 0 && sz == items.length / 4) resize(items.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("stack is empty");
        return items[StdRandom.uniform(sz)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }

    private class RandomArrayIterator implements Iterator<Item> {
        private int[] idx = new int[sz];
        private int current;

        public RandomArrayIterator() {
            for (int i = 0; i < sz; i++) {
                idx[i] = i;
            }
            StdRandom.shuffle(idx);
        }

        public boolean hasNext() {
            return current < sz;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("stack is empty");
            return items[idx[current++]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> stack = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) stack.enqueue(item);
            else if (!stack.isEmpty()) StdOut.print(stack.dequeue() + " ");
        }
    }

}
