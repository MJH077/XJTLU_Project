import java.util.Scanner;
public class Comparison {
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        int n = Integer.parseInt(kb.nextLine());
        int m = Integer.parseInt(kb.nextLine());
        int k = Integer.parseInt(kb.nextLine());
        if((n+m)==k){
            System.out.println("-1");
        }else if((n+m)>k){
            System.out.println(n+m);
        }else{
            System.out.println(k);
        }
    }
}
