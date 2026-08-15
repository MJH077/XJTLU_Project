import java.util.Scanner;
public class Power2 {
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        int n = Integer.parseInt(kb.nextLine());
        int m = Integer.parseInt(kb.nextLine());
        for(int k = 0;Math.pow(n,k-1)<m;k++){
            if(Math.pow(n,k)>=m){
                System.out.println(Math.round(Math.pow(n,k)-m));
            }else{
            }
        }
    }
}

