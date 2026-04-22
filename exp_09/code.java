import java.util.Scanner;

public class Aim10_LCS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== LCS ===");
            System.out.println("1. Calculate LCS");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            
            if (sc.nextInt() == 1) {
                System.out.print("String 1: "); String s1 = sc.next();
                System.out.print("String 2: "); String s2 = sc.next();
                
                int m = s1.length(), n = s2.length();
                int[][] L = new int[m + 1][n + 1];

                for (int i = 0; i <= m; i++) {
                    for (int j = 0; j <= n; j++) {
                        if (i == 0 || j == 0) L[i][j] = 0;
                        else if (s1.charAt(i - 1) == s2.charAt(j - 1)) L[i][j] = L[i - 1][j - 1] + 1;
                        else L[i][j] = Math.max(L[i - 1][j], L[i][j - 1]);
                    }
                }
                System.out.println("LCS Length: " + L[m][n]);
            } else break;
        }
        sc.close();
    }
}