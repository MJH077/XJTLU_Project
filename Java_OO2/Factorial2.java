import java.util.Scanner;
public class Factorial2 {
    public static long fact(int n){
        if(n == 0){
            return 1;
        }else{
            return n * fact(n - 1);
        }
    }
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        int n = Integer.parseInt(kb.nextLine());
        System.out.println(fact(n));
    }
}
