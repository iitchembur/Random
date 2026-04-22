import java.util.Scanner;

public class Aim2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Master Theorem Analysis ---");
            System.out.println("1. Evaluate T(n) = aT(n/b) + O(n^k)");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.print("Enter a: "); int a = sc.nextInt();
                System.out.print("Enter b: "); int b = sc.nextInt();
                System.out.print("Enter k: "); double k = sc.nextDouble();

                double p = Math.log(a) / Math.log(b);
                System.out.println("Critical exponent: log_b(a) = " + p);

                if (p > k + 0.0001) {
                    System.out.println("Result (Case 1): T(n) = O(n^" + p + ")");
                } else if (Math.abs(p - k) <= 0.0001) {
                    System.out.println("Result (Case 2): T(n) = O(n^" + k + " * log n)");
                } else {
                    System.out.println("Result (Case 3): T(n) = O(n^" + k + ")");
                }
            } else if (choice == 0) {
                break;
            }
        }
        sc.close();
    }
}