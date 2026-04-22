import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Aim4_RedBlackTree {

    static final int RED = 1;
    static final int BLACK = 0;

    static class Node {
        int data;
        Node parent, left, right;
        int color;

        Node(int data) {
            this.data = data;
            this.color = RED;
        }
    }

    private Node root;
    private Node TNULL; // Sentinel node for null leaves

    public Aim4_RedBlackTree() {
        TNULL = new Node(0);
        TNULL.color = BLACK;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }

    // ==========================================
    // TREE ROTATIONS
    // ==========================================
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // ==========================================
    // INSERTION LOGIC
    // ==========================================
    public void insert(int key) {
        Node node = new Node(key);
        node.parent = null;
        node.data = key;
        node.left = TNULL;
        node.right = TNULL;
        node.color = RED;

        Node y = null;
        Node x = this.root;

        while (x != TNULL) {
            y = x;
            if (node.data < x.data) {
                x = x.left;
            } else if (node.data > x.data) {
                x = x.right;
            } else {
                System.out.println("Key " + key + " already exists.");
                return;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.data < y.data) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == null) {
            node.color = BLACK;
            return;
        }
        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
        System.out.println("Inserted " + key);
    }

    private void fixInsert(Node k) {
        Node u;
        while (k.parent.color == RED) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left; // uncle
                if (u.color == RED) {
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right; // uncle
                if (u.color == RED) {
                    u.color = BLACK;
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = BLACK;
    }

    // ==========================================
    // DELETION LOGIC
    // ==========================================
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }

    public void delete(int data) {
        Node z = TNULL;
        Node node, x, y;
        node = this.root;

        while (node != TNULL) {
            if (node.data == data) {
                z = node;
            }
            if (node.data <= data) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (z == TNULL) {
            System.out.println("Key " + data + " not found in the tree.");
            return;
        }

        y = z;
        int yOriginalColor = y.color;
        if (z.left == TNULL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == TNULL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == BLACK) {
            fixDelete(x);
        }
        System.out.println("Deleted " + data);
    }

    private void fixDelete(Node x) {
        Node s;
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                s = x.parent.right;
                if (s.color == RED) {
                    s.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }
                if (s.left.color == BLACK && s.right.color == BLACK) {
                    s.color = RED;
                    x = x.parent;
                } else {
                    if (s.right.color == BLACK) {
                        s.left.color = BLACK;
                        s.color = RED;
                        rightRotate(s);
                        s = x.parent.right;
                    }
                    s.color = x.parent.color;
                    x.parent.color = BLACK;
                    s.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                s = x.parent.left;
                if (s.color == RED) {
                    s.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }
                if (s.right.color == BLACK && s.left.color == BLACK) {
                    s.color = RED;
                    x = x.parent;
                } else {
                    if (s.left.color == BLACK) {
                        s.right.color = BLACK;
                        s.color = RED;
                        leftRotate(s);
                        s = x.parent.left;
                    }
                    s.color = x.parent.color;
                    x.parent.color = BLACK;
                    s.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    // ==========================================
    // DISPLAY LOGIC
    // ==========================================
    public void inorder() {
        System.out.print("Inorder Traversal: ");
        inorderHelper(this.root);
        System.out.println();
    }

    private void inorderHelper(Node node) {
        if (node != TNULL) {
            inorderHelper(node.left);
            String color = (node.color == RED) ? "Red" : "Black";
            System.out.print(node.data + "(" + color + ") ");
            inorderHelper(node.right);
        }
    }

    public void displayTree() {
        if (root == TNULL) {
            System.out.println("Tree is empty.");
            return;
        }
        System.out.println("Tree Structure (Level Order):");
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                String colorStr = (current.color == RED) ? "R" : "B";
                System.out.print(current.data + "[" + colorStr + "]  ");

                if (current.left != TNULL) queue.add(current.left);
                if (current.right != TNULL) queue.add(current.right);
            }
            System.out.println();
        }
    }

    // ==========================================
    // MAIN MENU
    // ==========================================
    public static void main(String[] args) {
        Aim4_RedBlackTree rbt = new Aim4_RedBlackTree();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== RED-BLACK TREE MENU ===");
            System.out.println("1. Insert Element");
            System.out.println("2. Delete Element");
            System.out.println("3. Display (Inorder)");
            System.out.println("4. Display (Tree Structure / Level Order)");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter value to insert: ");
                    rbt.insert(sc.nextInt());
                    break;
                case 2:
                    System.out.print("Enter value to delete: ");
                    rbt.delete(sc.nextInt());
                    break;
                case 3:
                    rbt.inorder();
                    break;
                case 4:
                    rbt.displayTree();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}