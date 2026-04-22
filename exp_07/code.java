import java.util.*;

public class Aim7_ZeroOneKnapsack {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== 0/1 Knapsack ===");
            System.out.println("1. Enter Data & Run Algorithm");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();

            if (choice == 1) {

                // Input number of items
                System.out.print("Enter number of items: ");
                int n = sc.nextInt();

                int[] val = new int[n];
                int[] wt = new int[n];

                // Input values and weights
                for (int i = 0; i < n; i++) {
                    System.out.println("\nItem " + (i + 1) + ":");
                    System.out.print("Enter value: ");
                    val[i] = sc.nextInt();
                    System.out.print("Enter weight: ");
                    wt[i] = sc.nextInt();
                }

                // Input capacity
                System.out.print("\nEnter knapsack capacity: ");
                int W = sc.nextInt();

                int[][] K = new int[n + 1][W + 1];

                // DP computation
                for (int i = 0; i <= n; i++) {
                    for (int w = 0; w <= W; w++) {

                        if (i == 0 || w == 0) {
                            K[i][w] = 0;
                        } 
                        else if (wt[i - 1] <= w) {
                            K[i][w] = Math.max(
                                val[i - 1] + K[i - 1][w - wt[i - 1]],
                                K[i - 1][w]
                            );
                        } 
                        else {
                            K[i][w] = K[i - 1][w];
                        }
                    }
                }

                System.out.println("\nMaximum Value (No Fractions) = " + K[n][W]);

            } else if (choice == 0) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }
}