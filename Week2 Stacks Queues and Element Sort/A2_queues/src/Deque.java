/*
Project Link: https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php
Author: Yu Chen
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;  // first node of deque
    private Node<Item> last;   // last node of deque
    private int sz;            // size of deque

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        sz = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return sz;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("added node is null");


        if (isEmpty()) {
            Node<Item> added = new Node<>();
            added.item = item;
            first = added;
            last = added;
        } else {
            Node<Item> oldlast = last;
            last = new Node<>();
            last.item = item;
            last.next = null;
            last.prev = oldlast;
            oldlast.next = last;
        }
        sz++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("added node is null");

        if (isEmpty()) {
            Node<Item> added = new Node<>();
            added.item = item;
            first = added;
            last = added;
        } else {
            Node<Item> oldfirst = first;
            first = new Node<>();
            first.item = item;
            first.next = oldfirst;
            oldfirst.prev = first;
        }
        sz++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");

        Item item = last.item;
        if (sz == 1) {
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
            if (sz == 2) first = last;
        }
        sz--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");

        Item item = first.item;
        if (sz == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
            if (sz == 2) first = last;
        }
        sz--;
        return item;

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;

        // constructor
        public DequeIterator() {
            current = last;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("deque is empty");

            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            String item1 = StdIn.readString();
            if (!item.equals("-")) {
                deque.addFirst(item);
                deque.addLast(item1);
            } else if (!deque.isEmpty()) {
                StdOut.print(deque.removeFirst() + " ");
                StdOut.print(deque.removeLast() + " ");
            }
        }
    }

}
