import java.util.Scanner;

public class Aim11_GeneticAlgorithm {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Genetic Algorithm ===");
            System.out.println("1. Cross-over bits");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            
            if (sc.nextInt() == 1) {
                System.out.print("Enter Parent 1 (e.g., 11000): ");
                String p1 = sc.next();
                System.out.print("Enter Parent 2 (e.g., 00111): ");
                String p2 = sc.next();
                System.out.print("Enter Crossover Index: ");
                int idx = sc.nextInt();
                
                String off1 = p1.substring(0, idx) + p2.substring(idx);
                String off2 = p2.substring(0, idx) + p1.substring(idx);
                
                System.out.println("Offspring 1: " + off1);
                System.out.println("Offspring 2: " + off2);
            } else break;
        }
        sc.close();
    }
}