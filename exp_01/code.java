import java.util.Scanner;

public class Aim1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Recursion Tree Analysis ---");
            System.out.println("1. Analyze Recurrence T(n) = aT(n/b) + O(n^k)");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.print("Enter a (number of subproblems): ");
                int a = sc.nextInt();
                System.out.print("Enter b (subproblem size reduction factor): ");
                int b = sc.nextInt();
                System.out.print("Enter k (exponent of n in non-recursive work): ");
                double k = sc.nextDouble();

                System.out.println("\nRecursion Tree Breakdown:");
                System.out.println("Level 0 (Root) Cost: O(n^" + k + ")");
                System.out.println("Level 1 Cost: " + a + " * O((n/" + b + ")^" + k + ") = " + (a / Math.pow(b, k)) + " * O(n^" + k + ")");
                System.out.println("Number of leaves at bottom level: n^(log_" + b + "(" + a + "))");
                System.out.println("Total time complexity is the sum of costs across all levels.");
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }
}