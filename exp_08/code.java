import java.util.*;

public class Aim8_TSP {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== TSP (Dynamic Programming) ===");
            System.out.println("1. Enter Graph & Calculate Cost");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();

            if (choice == 1) {

                // Input number of cities
                System.out.print("Enter number of cities: ");
                int n = sc.nextInt();

                int[][] graph = new int[n][n];

                // Input adjacency matrix
                System.out.println("Enter distance matrix:");
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        graph[i][j] = sc.nextInt();
                    }
                }

                // DP table
                int[][] dp = new int[1 << n][n];
                for (int[] row : dp) Arrays.fill(row, -1);

                int result = tsp(1, 0, n, graph, dp);

                System.out.println("\nMinimum TSP Cost = " + result);

            } else if (choice == 0) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }

    static int tsp(int mask, int pos, int n, int[][] graph, int[][] dp) {
        // All cities visited
        if (mask == (1 << n) - 1) {
            return graph[pos][0]; // return to start
        }

        // Already computed
        if (dp[mask][pos] != -1) {
            return dp[mask][pos];
        }

        int ans = Integer.MAX_VALUE;

        // Try all unvisited cities
        for (int city = 0; city < n; city++) {
            if ((mask & (1 << city)) == 0) {
                int newAns = graph[pos][city] +
                        tsp(mask | (1 << city), city, n, graph, dp);

                ans = Math.min(ans, newAns);
            }
        }

        return dp[mask][pos] = ans;
    }
}