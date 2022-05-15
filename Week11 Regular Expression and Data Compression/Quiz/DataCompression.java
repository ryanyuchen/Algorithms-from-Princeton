import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.MinPQ;

public class DataCompression {
    /*
    Question 1
    Ternary Huffman codes.
    Generalize the Huffman algorithm to codewords over the ternary alphabet (0, 1, and 2) instead of the binary alphabet.
    That is, given a bytestream, find a prefix-free ternary code that uses as few trits (0s, 1s, and 2s) as possible. Prove that it yields optimal prefix-free ternary code.
    */

    /*
    "Pick the smallest three frequencies, join them together and create
    a node with the frequency equal to the sum of the three. Repeat. However,
    notice that every contraction reduces the number of leaves by 2 – we remove
    3 nodes and add 1 back. So to make sure that we end up with just one node,
    we have to have an odd number of nodes to start with. If not, add a dummy
    node with 0 frequency to start with."
     */

    public class TernaryHuffman {    // 0 -> left, 2 -> mid, 1 -> right

        private static final int R = 256;

        private static class Node implements Comparable<Node> {
            private final char ch;
            private final int freq;
            private final Node left;
            private final Node mid;
            private final Node right;

            public Node(char ch, int freq, Node left, Node mid, Node right) {
                this.ch = ch;
                this.freq = freq;
                this.left = left;
                this.mid = mid;
                this.right = right;
            }

            public boolean isLeaf() {
                return left == null && mid == null && right == null;
            }

            public int compareTo(Node that) {
                return Integer.compare(this.freq, that.freq);
            }
        }

        public static void compress() {
            String s = BinaryStdIn.readString();
            char[] input = s.toCharArray();

            int[] freq = new int[R];

            for (char c : input) {
                freq[c]++;
            }

            Node root = buildTrie(freq);

            String[] st = new String[R];
            buildCode(st, root, "");

            writeTrie(root);

            BinaryStdOut.write(input.length);

            for (char c : input) {
                String code = st[c];
                for (int j = 0; j < code.length(); j++) {
                    if (code.charAt(j) == '0') {
                        BinaryStdOut.write(0, 2);
                    }
                    if (code.charAt(j) == '1') {
                        BinaryStdOut.write(1, 2);
                    }
                    if (code.charAt(j) == '2') {
                        BinaryStdOut.write(2, 2);
                    }
                }
            }
            BinaryStdOut.close();
        }

        private static Node buildTrie(int[] freq) {
            MinPQ<Node> minPQ = new MinPQ<>();
            for (char c = 0; c < R; c++) {
                if (freq[c] > 0) {
                    minPQ.insert(new Node(c, freq[c], null, null, null));
                }
            }
            while (minPQ.size() < 3 || minPQ.size() % 2 == 0) {
                minPQ.insert(new Node('\0', 0, null, null, null));
            }
            while (minPQ.size() > 1) {
                Node left = minPQ.delMin();
                Node mid = minPQ.delMin();
                Node right = minPQ.delMin();
                Node parent = new Node('\0', left.freq + mid.freq + right.freq, left, mid, right);
                minPQ.insert(parent);
            }
            return minPQ.delMin();
        }

        private static void buildCode(String[] st, Node x, String s) {
            if (!x.isLeaf()) {
                buildCode(st, x.left, s + '0');
                buildCode(st, x.mid, s + '2');
                buildCode(st, x.right, s + '1');
            } else {
                st[x.ch] = s;
            }
        }

        private static void writeTrie(Node x) {
            if (x.isLeaf()) {
                BinaryStdOut.write(true);
                BinaryStdOut.write(x.ch, 8);
                return;
            }
            BinaryStdOut.write(false);
            writeTrie(x.left);
            writeTrie(x.mid);
            writeTrie(x.right);
        }

        public static void expand() {

            Node root = readTrie();

            int n = BinaryStdIn.readInt();

            for (int i = 0; i < n; i++) {
                Node x = root;
                while (!x.isLeaf()) {
                    int bit = BinaryStdIn.readInt(2);
                    if (bit == 0) {
                        x = x.left;
                    } else if (bit == 1) {
                        x = x.right;
                    } else {
                        x = x.mid;
                    }
                }
                BinaryStdOut.write(x.ch, 8);
            }
            BinaryStdOut.close();
        }

        private static Node readTrie() {
            boolean isLeaf = BinaryStdIn.readBoolean();
            if (isLeaf) {
                return new Node(BinaryStdIn.readChar(), -1, null, null, null);
            }
            return new Node('\0', -1, readTrie(), readTrie(), readTrie());
        }

    }

    /*
    Question 2
    Uniquely decodable code.
    Identify an optimal uniquely-decodable code that is neither prefix free nor suffix tree.
    Identify two optimal prefix-free codes for the same input that have a different distribution of codeword lengths.
    */


    /*
    Question 3
    Move-to-front coding. Design an algorithm to implement move-to-front encoding so that each operation takes logarithmic time in the worst case.
    That is, maintain alphabet of symbols in a list.
    A symbol is encoded as the number of symbols that precede it in the list. After encoding a symbol, move it to the front of the list.
     */

    /*
    1. Logarithmic time operation: use RedBlackBST in algs4, use rank to write the encoding, use delete and min to move the symbol to the front of the tree
    2. Algorithm:
        initialize the symbol table;

        while ( not end-of-file ) {

            K = get character;

            output K’s position(P) in the symbol table; (use RedBlackBST.rank())

            move K to front of the symbol table. (delete K, then get min() and add back K, set K's new value less than min())

        }
     */
    
    public class MoveToFrontCoding {

        private final static int R = 256;

        public static void encode() {
            RedBlackBST<Integer, Character> bst = new RedBlackBST<>();
            for (char i = 0; i < R; i++) {
                bst.put((int) i, i);
            }
            while (!StdIn.isEmpty()) {
                char c = BinaryStdIn.readChar();
                int key = bst.rank((int) c);
                BinaryStdOut.write(key);
                bst.delete(key);
                bst.put(bst.min() - 1, c);
            }
            BinaryStdOut.close();
        }

        public static void decode() {
            RedBlackBST<Integer, Character> bst = new RedBlackBST<>();
            for (char i = 0; i < R; i++) {
                bst.put((int) i, i);
            }
            while (!StdIn.isEmpty()) {
                int x = BinaryStdIn.readChar();
                Integer key = bst.select(x);
                char c = bst.get(key);
                bst.delete(key);
                bst.put(bst.min() - 1, c);
            }
            BinaryStdOut.close();
        }
    }


}
