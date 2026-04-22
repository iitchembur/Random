import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n================ MAIN MENU ================");
            System.out.println("1. Use B-Tree");
            System.out.println("2. Use B+ Tree");
            System.out.println("0. Exit");
            System.out.print("Choose tree type: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                runBTreeMenu();
            } else if (choice == 2) {
                runBPlusTreeMenu();
            } else if (choice == 0) {
                System.out.println("Exiting...");
                System.exit(0);
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    // ==========================================
    // B-TREE MENU & LOGIC
    // ==========================================
    private static void runBTreeMenu() {
        System.out.print("Enter the order (m) for the B-Tree: ");
        int m = sc.nextInt();
        BTree bTree = new BTree(m);

        while (true) {
            System.out.println("\n--- B-TREE (" + m + ") MENU ---");
            System.out.println("1. Insert");
            System.out.println("2. Delete");
            System.out.println("3. Display");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.print("Enter value to insert: ");
                bTree.insert(sc.nextInt());
            } else if (choice == 2) {
                System.out.print("Enter value to delete: ");
                bTree.delete(sc.nextInt());
            } else if (choice == 3) {
                bTree.display();
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    static class BTreeNode {
        int[] keys;
        BTreeNode[] children;
        int numKeys;
        boolean isLeaf;
        int t; // Minimum degree (t = ceil(m/2))

        public BTreeNode(int t, boolean isLeaf) {
            this.t = t;
            this.isLeaf = isLeaf;
            this.keys = new int[2 * t - 1];
            this.children = new BTreeNode[2 * t];
            this.numKeys = 0;
        }

        public void traverse() {
            int i;
            for (i = 0; i < numKeys; i++) {
                if (!isLeaf) children[i].traverse();
                System.out.print(keys[i] + " ");
            }
            if (!isLeaf) children[i].traverse();
        }

        public BTreeNode search(int k) {
            int i = 0;
            while (i < numKeys && k > keys[i]) i++;
            if (i < numKeys && keys[i] == k) return this;
            if (isLeaf) return null;
            return children[i].search(k);
        }

        public void insertNonFull(int k) {
            int i = numKeys - 1;
            if (isLeaf) {
                while (i >= 0 && keys[i] > k) {
                    keys[i + 1] = keys[i];
                    i--;
                }
                keys[i + 1] = k;
                numKeys++;
            } else {
                while (i >= 0 && keys[i] > k) i--;
                if (children[i + 1].numKeys == 2 * t - 1) {
                    splitChild(i + 1, children[i + 1]);
                    if (keys[i + 1] < k) i++;
                }
                children[i + 1].insertNonFull(k);
            }
        }

        public void splitChild(int i, BTreeNode y) {
            BTreeNode z = new BTreeNode(y.t, y.isLeaf);
            z.numKeys = t - 1;
            for (int j = 0; j < t - 1; j++) z.keys[j] = y.keys[j + t];
            if (!y.isLeaf) {
                for (int j = 0; j < t; j++) z.children[j] = y.children[j + t];
            }
            y.numKeys = t - 1;
            for (int j = numKeys; j >= i + 1; j--) children[j + 1] = children[j];
            children[i + 1] = z;
            for (int j = numKeys - 1; j >= i; j--) keys[j + 1] = keys[j];
            keys[i] = y.keys[t - 1];
            numKeys++;
        }

        public int findKey(int k) {
            int idx = 0;
            while (idx < numKeys && keys[idx] < k) idx++;
            return idx;
        }

        public void remove(int k) {
            int idx = findKey(k);
            if (idx < numKeys && keys[idx] == k) {
                if (isLeaf) removeFromLeaf(idx);
                else removeFromNonLeaf(idx);
            } else {
                if (isLeaf) {
                    System.out.println("The key " + k + " is not in the tree.");
                    return;
                }
                boolean flag = (idx == numKeys);
                if (children[idx].numKeys < t) fill(idx);
                if (flag && idx > numKeys) children[idx - 1].remove(k);
                else children[idx].remove(k);
            }
        }

        public void removeFromLeaf(int idx) {
            for (int i = idx + 1; i < numKeys; ++i) keys[i - 1] = keys[i];
            numKeys--;
        }

        public void removeFromNonLeaf(int idx) {
            int k = keys[idx];
            if (children[idx].numKeys >= t) {
                int pred = getPred(idx);
                keys[idx] = pred;
                children[idx].remove(pred);
            } else if (children[idx + 1].numKeys >= t) {
                int succ = getSucc(idx);
                keys[idx] = succ;
                children[idx + 1].remove(succ);
            } else {
                merge(idx);
                children[idx].remove(k);
            }
        }

        public int getPred(int idx) {
            BTreeNode cur = children[idx];
            while (!cur.isLeaf) cur = cur.children[cur.numKeys];
            return cur.keys[cur.numKeys - 1];
        }

        public int getSucc(int idx) {
            BTreeNode cur = children[idx + 1];
            while (!cur.isLeaf) cur = cur.children[0];
            return cur.keys[0];
        }

        public void fill(int idx) {
            if (idx != 0 && children[idx - 1].numKeys >= t) borrowFromPrev(idx);
            else if (idx != numKeys && children[idx + 1].numKeys >= t) borrowFromNext(idx);
            else {
                if (idx != numKeys) merge(idx);
                else merge(idx - 1);
            }
        }

        public void borrowFromPrev(int idx) {
            BTreeNode child = children[idx];
            BTreeNode sibling = children[idx - 1];
            for (int i = child.numKeys - 1; i >= 0; --i) child.keys[i + 1] = child.keys[i];
            if (!child.isLeaf) {
                for (int i = child.numKeys; i >= 0; --i) child.children[i + 1] = child.children[i];
            }
            child.keys[0] = keys[idx - 1];
            if (!child.isLeaf) child.children[0] = sibling.children[sibling.numKeys];
            keys[idx - 1] = sibling.keys[sibling.numKeys - 1];
            child.numKeys += 1;
            sibling.numKeys -= 1;
        }

        public void borrowFromNext(int idx) {
            BTreeNode child = children[idx];
            BTreeNode sibling = children[idx + 1];
            child.keys[child.numKeys] = keys[idx];
            if (!child.isLeaf) child.children[child.numKeys + 1] = sibling.children[0];
            keys[idx] = sibling.keys[0];
            for (int i = 1; i < sibling.numKeys; ++i) sibling.keys[i - 1] = sibling.keys[i];
            if (!sibling.isLeaf) {
                for (int i = 1; i <= sibling.numKeys; ++i) sibling.children[i - 1] = sibling.children[i];
            }
            child.numKeys += 1;
            sibling.numKeys -= 1;
        }

        public void merge(int idx) {
            BTreeNode child = children[idx];
            BTreeNode sibling = children[idx + 1];
            child.keys[t - 1] = keys[idx];
            for (int i = 0; i < sibling.numKeys; ++i) child.keys[i + t] = sibling.keys[i];
            if (!child.isLeaf) {
                for (int i = 0; i <= sibling.numKeys; ++i) child.children[i + t] = sibling.children[i];
            }
            for (int i = idx + 1; i < numKeys; ++i) keys[i - 1] = keys[i];
            for (int i = idx + 2; i <= numKeys; ++i) children[i - 1] = children[i];
            child.numKeys += sibling.numKeys + 1;
            numKeys--;
        }
    }

    static class BTree {
        BTreeNode root;
        int t; // Minimum degree

        public BTree(int m) {
            this.t = (int) Math.ceil(m / 2.0);
            this.root = null;
        }

        public void display() {
            if (root != null) {
                System.out.print("B-Tree Contents (In-Order): ");
                root.traverse();
                System.out.println();
            } else {
                System.out.println("Tree is empty.");
            }
        }

        public void insert(int k) {
            if (root == null) {
                root = new BTreeNode(t, true);
                root.keys[0] = k;
                root.numKeys = 1;
            } else {
                if (root.search(k) != null) {
                    System.out.println("Key " + k + " already exists.");
                    return;
                }
                if (root.numKeys == 2 * t - 1) {
                    BTreeNode s = new BTreeNode(t, false);
                    s.children[0] = root;
                    s.splitChild(0, root);
                    int i = 0;
                    if (s.keys[0] < k) i++;
                    s.children[i].insertNonFull(k);
                    root = s;
                } else {
                    root.insertNonFull(k);
                }
                System.out.println("Inserted " + k);
            }
        }

        public void delete(int k) {
            if (root == null) {
                System.out.println("Tree is empty.");
                return;
            }
            root.remove(k);
            if (root.numKeys == 0) {
                if (root.isLeaf) root = null;
                else root = root.children[0];
            }
        }
    }

    // ==========================================
    // B+ TREE MENU & LOGIC
    // ==========================================
    private static void runBPlusTreeMenu() {
        System.out.print("Enter the order (m) for the B+ Tree: ");
        int m = sc.nextInt();
        BPlusTree bpt = new BPlusTree(m);

        while (true) {
            System.out.println("\n--- B+ TREE (" + m + ") MENU ---");
            System.out.println("1. Insert");
            System.out.println("2. Delete");
            System.out.println("3. Display");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.print("Enter value to insert: ");
                bpt.insert(sc.nextInt());
            } else if (choice == 2) {
                System.out.print("Enter value to delete: ");
                bpt.delete(sc.nextInt());
            } else if (choice == 3) {
                bpt.display();
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    static class BPTNode {
        boolean isLeaf;
        ArrayList<Integer> keys;
        BPTNode next; 
        public BPTNode(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new ArrayList<>();
        }
    }

    static class BPTInternalNode extends BPTNode {
        ArrayList<BPTNode> children;
        public BPTInternalNode() {
            super(false);
            this.children = new ArrayList<>();
        }
    }

    static class BPTLeafNode extends BPTNode {
        public BPTLeafNode() {
            super(true);
        }
    }

    static class BPlusTree {
        private int m;
        private BPTNode root;
        private BPTLeafNode firstLeaf;

        public BPlusTree(int m) {
            this.m = m;
            this.root = new BPTLeafNode();
            this.firstLeaf = (BPTLeafNode) root;
        }

        public void insert(int key) {
            if (root == null) {
                root = new BPTLeafNode();
                firstLeaf = (BPTLeafNode) root;
            }
            BPTLeafNode leaf = findLeafNode(root, key);
            if (leaf.keys.contains(key)) {
                System.out.println("Key " + key + " already exists.");
                return;
            }
            int i = 0;
            while (i < leaf.keys.size() && leaf.keys.get(i) < key) i++;
            leaf.keys.add(i, key);

            if (leaf.keys.size() == m) splitLeafNode(leaf);
            else System.out.println("Inserted " + key);
        }

        private BPTLeafNode findLeafNode(BPTNode node, int key) {
            if (node.isLeaf) return (BPTLeafNode) node;
            BPTInternalNode internal = (BPTInternalNode) node;
            int i = 0;
            while (i < internal.keys.size() && key >= internal.keys.get(i)) i++;
            return findLeafNode(internal.children.get(i), key);
        }

        private void splitLeafNode(BPTLeafNode leaf) {
            int splitIndex = (int) Math.ceil(m / 2.0) - 1;
            BPTLeafNode newLeaf = new BPTLeafNode();
            newLeaf.keys.addAll(leaf.keys.subList(splitIndex, leaf.keys.size()));
            leaf.keys.subList(splitIndex, leaf.keys.size()).clear();
            newLeaf.next = leaf.next;
            leaf.next = newLeaf;
            insertIntoParent(leaf, newLeaf.keys.get(0), newLeaf);
        }

        private void insertIntoParent(BPTNode left, int key, BPTNode right) {
            if (left == root) {
                BPTInternalNode newRoot = new BPTInternalNode();
                newRoot.keys.add(key);
                newRoot.children.add(left);
                newRoot.children.add(right);
                root = newRoot;
                return;
            }
            BPTInternalNode parent = findParent(root, left);
            int i = 0;
            while (i < parent.keys.size() && parent.keys.get(i) < key) i++;
            parent.keys.add(i, key);
            parent.children.add(i + 1, right);
            if (parent.keys.size() == m) splitInternalNode(parent);
        }

        private void splitInternalNode(BPTInternalNode internal) {
            int splitIndex = (int) Math.ceil(m / 2.0) - 1;
            int upKey = internal.keys.get(splitIndex);
            BPTInternalNode newInternal = new BPTInternalNode();
            newInternal.keys.addAll(internal.keys.subList(splitIndex + 1, internal.keys.size()));
            newInternal.children.addAll(internal.children.subList(splitIndex + 1, internal.children.size()));
            internal.keys.subList(splitIndex, internal.keys.size()).clear();
            internal.children.subList(splitIndex + 1, internal.children.size()).clear();
            insertIntoParent(internal, upKey, newInternal);
        }

        private BPTInternalNode findParent(BPTNode current, BPTNode child) {
            if (current.isLeaf) return null;
            BPTInternalNode internal = (BPTInternalNode) current;
            if (internal.children.contains(child)) return internal;
            for (BPTNode c : internal.children) {
                BPTInternalNode parent = findParent(c, child);
                if (parent != null) return parent;
            }
            return null;
        }

        public void delete(int key) {
            if (root == null) return;
            BPTLeafNode leaf = findLeafNode(root, key);
            if (!leaf.keys.contains(key)) {
                System.out.println("Key " + key + " not found.");
                return;
            }
            leaf.keys.remove(Integer.valueOf(key));
            System.out.println("Deleted " + key);
            if (leaf == root) {
                if (leaf.keys.isEmpty()) { root = null; firstLeaf = null; }
                return;
            }
            int minKeys = (int) Math.ceil(m / 2.0) - 1;
            if (leaf.keys.size() < minKeys) handleLeafUnderflow(leaf);
        }

        private void handleLeafUnderflow(BPTLeafNode leaf) {
            BPTInternalNode parent = findParent(root, leaf);
            int leafIndex = parent.children.indexOf(leaf);
            BPTLeafNode leftSibling = (leafIndex > 0) ? (BPTLeafNode) parent.children.get(leafIndex - 1) : null;
            BPTLeafNode rightSibling = (leafIndex < parent.children.size() - 1) ? (BPTLeafNode) parent.children.get(leafIndex + 1) : null;
            int minKeys = (int) Math.ceil(m / 2.0) - 1;

            if (leftSibling != null && leftSibling.keys.size() > minKeys) {
                int borrowedKey = leftSibling.keys.remove(leftSibling.keys.size() - 1);
                leaf.keys.add(0, borrowedKey);
                parent.keys.set(leafIndex - 1, leaf.keys.get(0));
                return;
            }
            if (rightSibling != null && rightSibling.keys.size() > minKeys) {
                int borrowedKey = rightSibling.keys.remove(0);
                leaf.keys.add(borrowedKey);
                parent.keys.set(leafIndex, rightSibling.keys.get(0));
                return;
            }
            if (leftSibling != null) {
                leftSibling.keys.addAll(leaf.keys);
                leftSibling.next = leaf.next;
                parent.children.remove(leafIndex);
                parent.keys.remove(leafIndex - 1);
                if (parent != root && parent.keys.size() < minKeys) handleInternalUnderflow(parent);
                else if (parent == root && parent.keys.isEmpty()) root = leftSibling;
                return;
            }
            if (rightSibling != null) {
                leaf.keys.addAll(rightSibling.keys);
                leaf.next = rightSibling.next;
                parent.children.remove(leafIndex + 1);
                parent.keys.remove(leafIndex);
                if (parent != root && parent.keys.size() < minKeys) handleInternalUnderflow(parent);
                else if (parent == root && parent.keys.isEmpty()) root = leaf;
            }
        }

        private void handleInternalUnderflow(BPTInternalNode node) {
            BPTInternalNode parent = findParent(root, node);
            if (parent == null) return;
            int nodeIndex = parent.children.indexOf(node);
            BPTInternalNode leftSibling = (nodeIndex > 0) ? (BPTInternalNode) parent.children.get(nodeIndex - 1) : null;
            BPTInternalNode rightSibling = (nodeIndex < parent.children.size() - 1) ? (BPTInternalNode) parent.children.get(nodeIndex + 1) : null;
            int minKeys = (int) Math.ceil(m / 2.0) - 1;

            if (leftSibling != null && leftSibling.keys.size() > minKeys) {
                int borrowedKey = leftSibling.keys.remove(leftSibling.keys.size() - 1);
                BPTNode borrowedChild = leftSibling.children.remove(leftSibling.children.size() - 1);
                node.keys.add(0, parent.keys.get(nodeIndex - 1));
                node.children.add(0, borrowedChild);
                parent.keys.set(nodeIndex - 1, borrowedKey);
                return;
            }
            if (rightSibling != null && rightSibling.keys.size() > minKeys) {
                int borrowedKey = rightSibling.keys.remove(0);
                BPTNode borrowedChild = rightSibling.children.remove(0);
                node.keys.add(parent.keys.get(nodeIndex));
                node.children.add(borrowedChild);
                parent.keys.set(nodeIndex, borrowedKey);
                return;
            }
            if (leftSibling != null) {
                leftSibling.keys.add(parent.keys.remove(nodeIndex - 1));
                leftSibling.keys.addAll(node.keys);
                leftSibling.children.addAll(node.children);
                parent.children.remove(nodeIndex);
                if (parent != root && parent.keys.size() < minKeys) handleInternalUnderflow(parent);
                else if (parent == root && parent.keys.isEmpty()) root = leftSibling;
                return;
            }
            if (rightSibling != null) {
                node.keys.add(parent.keys.remove(nodeIndex));
                node.keys.addAll(rightSibling.keys);
                node.children.addAll(rightSibling.children);
                parent.children.remove(nodeIndex + 1);
                if (parent != root && parent.keys.size() < minKeys) handleInternalUnderflow(parent);
                else if (parent == root && parent.keys.isEmpty()) root = node;
            }
        }

        public void display() {
            if (root == null) {
                System.out.println("Tree is empty.");
                return;
            }
            System.out.println("--- Tree Structure (Level Order) ---");
            Queue<BPTNode> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                int levelSize = queue.size();
                for (int i = 0; i < levelSize; i++) {
                    BPTNode current = queue.poll();
                    System.out.print(current.keys + "  ");
                    if (!current.isLeaf) {
                        BPTInternalNode internal = (BPTInternalNode) current;
                        queue.addAll(internal.children);
                    }
                }
                System.out.println();
            }
            System.out.println("\n--- Linked Leaves ---");
            BPTNode current = firstLeaf;
            while (current != null) {
                System.out.print(current.keys);
                if (current.next != null) System.out.print(" -> ");
                current = current.next;
            }
            System.out.println();
        }
    }
}