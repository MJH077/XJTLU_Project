public class Fibonacci1 {
    public static void main(String[] args){
        System.out.println(computeFibonacci(20));
    }
    public static int computeFibonacci(int num){
        int a = 0;
        int b = 1;
        int m = 0;
        for(int i = 1; i <= num; i++){
            m = a + b;
            a = b;
            b = m;
        }
        return m;
    }
}