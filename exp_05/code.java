import java.util.*;

public class Aim5_BinarySearch {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[0]; // initially empty

        while (true) {
            System.out.println("\n=== Binary Search ===");
            System.out.println("1. Enter Array");
            System.out.println("2. Search Element");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    // Input array
                    System.out.print("Enter number of elements: ");
                    int n = sc.nextInt();
                    arr = new int[n];

                    System.out.println("Enter elements (sorted order required):");
                    for (int i = 0; i < n; i++) {
                        arr[i] = sc.nextInt();
                    }

                    System.out.println("Array stored successfully.");
                    break;

                case 2:
                    if (arr.length == 0) {
                        System.out.println("Please enter the array first!");
                        break;
                    }

                    System.out.println("Array: " + Arrays.toString(arr));
                    System.out.print("Enter element to find: ");
                    int target = sc.nextInt();

                    int idx = search(arr, 0, arr.length - 1, target);

                    if (idx != -1)
                        System.out.println("Found at index: " + idx);
                    else
                        System.out.println("Not found.");
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

    static int search(int[] arr, int l, int r, int x) {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            if (arr[mid] == x) return mid;

            if (arr[mid] > x)
                return search(arr, l, mid - 1, x);

            return search(arr, mid + 1, r, x);
        }
        return -1;
    }
}