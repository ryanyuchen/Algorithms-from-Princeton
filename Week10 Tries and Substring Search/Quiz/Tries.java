import edu.princeton.cs.algs4.*;

public class Tries {
    /*
    Question 1
    Prefix free codes.
    In data compression, a set of binary strings is prefix free if no string is a prefix of another.
    For example, {01,10,0010,1111} is prefix free, but {01,10,0010,10100} is not because 10 is a prefix of 10100.
    Design an efficient algorithm to determine if a set of binary strings is prefix-free.
    The running time of your algorithm should be proportional the number of bits in all of the binary stings.
    */
    static class Prefixfree {

        private Node root;
        private int N;

        private class Node {
            private Node[] next = new Node[2];
            private boolean isString;
        }

        private Node add(Node x, String binary, int d) {
            if (x == null) x = new Node();
            if (d == binary.length()) {
                if (x.next[0] != null || x.next[1] != null) throw new IllegalArgumentException("This input is a prefix of existing binary!");
                x.isString = true;
                N++;
            } else {
                if (x.isString) throw new IllegalArgumentException("Prefix exists for this input!");
                int c = (int)binary.charAt(d) - 48;
                x.next[c] = add(x.next[c], binary, d + 1);
            }
            return x;
        }

        public void add(String binary) {
            root = add(root, binary, 0);
        }

        public int size() {
            return N;
        }

        public static boolean isPrefixFree(String filename) {
            TrieST<Integer> dictionary = new TrieST<>();

            // read in dictionary of binary strings
            In dict = new In(filename);
            while (!dict.isEmpty()) {
                String word = dict.readString();
                dictionary.put(word, dictionary.size());
            }

            for (String s : dictionary.keys()) {
                for (String t:dictionary.keysWithPrefix(s)) {
                    if(!t.equals(s) && t.startsWith(s)){
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /*
    Question 2
    Boggle.
    Boggle is a word game played on an 4-by-4 grid of tiles, where each tile contains one letter in the alphabet.
    The goal is to find all words in the dictionary that can be made by following a path of adjacent tiles (with no tile repeated),
    where two tiles are adjacent if they are horizontal, vertical, or diagonal neighbors.
    */

    // programming assignment

    /*
    Question 3
    Suffix trees. Learn about and implement suffix trees, the ultimate string searching data structure.
     */
    public class SuffixTree {

        // implementation from : https://github.com/yixuaz/algorithm4-princeton-cos226/blob/master/princetonSolution/src/part2/week4/trie/SuffixTree.java

        public static final int MAX_CHAR = 128;

        class Node {
            int start, index = -1, end;
            Node suffixLink;
            Node[] chds = new Node[MAX_CHAR];

            public Node(int start, int end) {
                this.start = start;
                this.end = end;
                if (end != inf) {
                    suffixLink = root;
                }
            }

            int len() {
                return Math.min(end, globalEnd) - start + 1;
            }
        }

        Node root = new Node(-1, -1);
        Node lastNewNode = null;
        int globalEnd = -1;
        int inf = Integer.MAX_VALUE / 2;
        char[] text;

        Node activeNode = root;
        int activeEdgeAsTextIndex = -1;
        int activeLength = 0;
        int remaining = 0;

        boolean tryWalkDown(Node cur) {
            int edgeLen = cur.len();
            if (activeLength >= edgeLen) {
                activeEdgeAsTextIndex += edgeLen;
                activeLength -= edgeLen;
                activeNode = cur;
                return true;
            }
            return false;
        }

        private void extend(char c) {
            text[++globalEnd] = c;
            remaining++;
            lastNewNode = null;
            while (remaining > 0) {
                if (activeLength == 0) activeEdgeAsTextIndex = globalEnd;
                if (activeNode.chds[text[activeEdgeAsTextIndex]] == null) {
                    activeNode.chds[text[activeEdgeAsTextIndex]] = new Node(globalEnd, inf);
                    addSuffixLinkIfLastNodeExists(activeNode);
                } else {
                    Node chd = activeNode.chds[text[activeEdgeAsTextIndex]];
                    if (tryWalkDown(chd)) continue;
                    if (text[chd.start + activeLength] == c) { // do nothing
                        addSuffixLinkIfLastNodeExists(activeNode);
                        activeLength++;
                        break;
                    }
                    Node internalSplit = new Node(chd.start, chd.start + activeLength - 1);
                    activeNode.chds[text[activeEdgeAsTextIndex]] = internalSplit;
                    internalSplit.chds[c] = new Node(globalEnd, inf);
                    chd.start += activeLength;
                    internalSplit.chds[text[chd.start]] = chd;
                    addSuffixLinkIfLastNodeExists(internalSplit);
                }
                remaining--;
                if (activeNode != root) activeNode = activeNode.suffixLink;
                else if (activeLength > 0) {
                    activeLength--;
                    activeEdgeAsTextIndex = globalEnd - remaining + 1;
                }
            }
        }

        private void addSuffixLinkIfLastNodeExists(Node node) {
            if (lastNewNode != null)
                lastNewNode.suffixLink = node;
            lastNewNode = node;
        }

        private void setSuffixIndexByDFS(Node cur, int labelHeight) {
            if (cur.suffixLink == null && cur.start != -1) {
                cur.index = globalEnd + 1 - labelHeight;
            } else {
                for (int i = 0; i < MAX_CHAR; i++) {
                    if (cur.chds[i] == null) continue;
                    setSuffixIndexByDFS(cur.chds[i], labelHeight + cur.chds[i].len());
                }
            }
        }

        public SuffixTree(String str) {
            this.text = new char[str.length() + 1];
            for (int i = 0; i <= str.length(); i++) {
                char c = (i == str.length() ? '$' : str.charAt(i));
                extend(c);
            }
            setSuffixIndexByDFS(root, 0);
        }

        public int[] buildSuffixArray() {
            int[] res = new int[globalEnd];
            preorder(root, new int[1], res);
            return res;
        }

        private void preorder(Node cur, int[] idx, int[] res) {
            if (cur == null) return;
            if (cur.index != -1) {
                if (cur.index != globalEnd) {
                    res[idx[0]++] = cur.index;
                }
                return;
            }
            for (int i = 0; i < MAX_CHAR; i++) {
                preorder(cur.chds[i], idx, res);
            }
        }

    }

}
