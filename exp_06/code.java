import java.util.*;

public class Aim6_FractionalKnapsack {

    static class Item {
        int weight, value;
        Item(int w, int v) { weight = w; value = v; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Fractional Knapsack ===");
            System.out.println("1. Run Algorithm");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();

            if (choice == 1) {

                // Input number of items
                System.out.print("Enter number of items: ");
                int n = sc.nextInt();

                Item[] items = new Item[n];

                // Input weights and profits
                for (int i = 0; i < n; i++) {
                    System.out.println("\nItem " + (i + 1) + ":");
                    System.out.print("Enter weight: ");
                    int w = sc.nextInt();
                    System.out.print("Enter profit/value: ");
                    int v = sc.nextInt();

                    items[i] = new Item(w, v);
                }

                // Input capacity
                System.out.print("\nEnter knapsack capacity: ");
                int capacity = sc.nextInt();

                // Sort by value/weight ratio (descending)
                Arrays.sort(items, (a, b) -> 
                    Double.compare((double) b.value / b.weight, (double) a.value / a.weight)
                );

                double totalValue = 0.0;

                for (Item item : items) {
                    if (capacity >= item.weight) {
                        capacity -= item.weight;
                        totalValue += item.value;
                    } else {
                        totalValue += item.value * ((double) capacity / item.weight);
                        break; // knapsack full
                    }
                }

                System.out.println("\nMaximum value possible = " + totalValue);

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