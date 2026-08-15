import java.util.Scanner;
public class Power{
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        int n = Integer.parseInt(kb.nextLine());
        int a = 0;
        int b = 1;
        int k = 3;
            for(int m = a+b ;m<n;m=a+b){
            a=b;
            b=m;
            k++;
            }
            System.out.println(k);
    }
}