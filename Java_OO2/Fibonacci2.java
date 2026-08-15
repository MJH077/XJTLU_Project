public class Fibonacci2{
    public static int[] sol(int n) {
        int[] fibonacciArray = new int[n];
        int a = 0;
        int b = 1;
        int m = 0;
        for (int i = 0; i <= n; i++) {
            fibonacciArray[i] = a;
            m = a + b;
            a = b;
            b = m;
        }
        return fibonacciArray;
    }
}