public class Fibonacci{
    public static int[] sol(int n) {
    int[] fibonacciArray = new int[n];
    int a = 0;
    int b = 1;
    for (int i = 0; i < n; i++) {
        fibonacciArray[i] = a;
        int m = a + b;
        a = b;
        b = m;
    }
    return fibonacciArray;
    }
}
