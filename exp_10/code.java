import java.util.*;

public class Aim9_10_NP_All {

    static boolean found = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== NP Problems Menu ===");
            System.out.println("1. NP-Complete (Subset Sum)");
            System.out.println("2. NP-Hard (TSP Approximation)");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                // ================= NP-COMPLETE =================
                case 1:
                    System.out.println("\n--- Subset Sum (NP-Complete) ---");

                    System.out.print("Enter number of elements: ");
                    int n = sc.nextInt();

                    int[] arr = new int[n];
                    System.out.println("Enter elements:");
                    for (int i = 0; i < n; i++) {
                        arr[i] = sc.nextInt();
                    }

                    System.out.print("Enter target sum: ");
                    int target = sc.nextInt();

                    found = false;
                    System.out.println("Subsets with sum " + target + ":");

                    subsetSum(arr, 0, 0, target, new ArrayList<>());

                    if (!found) {
                        System.out.println("No subset found.");
                    }
                    break;

                // ================= NP-HARD =================
                case 2:
                    System.out.println("\n--- TSP Approximation (NP-Hard) ---");

                    System.out.print("Enter number of cities: ");
                    int cities = sc.nextInt();

                    int[][] graph = new int[cities][cities];

                    System.out.println("Enter distance matrix:");
                    for (int i = 0; i < cities; i++) {
                        for (int j = 0; j < cities; j++) {
                            graph[i][j] = sc.nextInt();
                        }
                    }

                    boolean[] visited = new boolean[cities];
                    int cost = 0;
                    int current = 0;

                    visited[0] = true;
                    System.out.print("Tour: 0 ");

                    for (int count = 1; count < cities; count++) {
                        int nextCity = -1;
                        int minDist = Integer.MAX_VALUE;

                        for (int i = 0; i < cities; i++) {
                            if (!visited[i] && graph[current][i] < minDist) {
                                minDist = graph[current][i];
                                nextCity = i;
                            }
                        }

                        visited[nextCity] = true;
                        cost += minDist;
                        current = nextCity;

                        System.out.print("-> " + current + " ");
                    }

                    cost += graph[current][0];
                    System.out.println("-> 0");

                    System.out.println("Approximate TSP Cost = " + cost);
                    break;

                case 0:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= SUBSET SUM =================
    static void subsetSum(int[] arr, int index, int sum, int target, List<Integer> subset) {

        if (sum == target) {
            System.out.println(subset);
            found = true;
            return;
        }

        if (index >= arr.length || sum > target) return;

        // include
        subset.add(arr[index]);
        subsetSum(arr, index + 1, sum + arr[index], target, subset);

        // exclude
        subset.remove(subset.size() - 1);
        subsetSum(arr, index + 1, sum, target, subset);
    }
}