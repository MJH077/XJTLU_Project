import java.util.Scanner;
public class Factorial1 {

    public static int calculateFactorial(int num){
        int a = 1;
        for(int i = 1; i <= num; i++){
            a *= i;
        }
        return a;
    }
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        int num = Integer.parseInt(kb.nextLine());
        System.out.println(calculateFactorial(num));
    }
}