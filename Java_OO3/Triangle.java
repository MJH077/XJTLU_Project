import java.util.Scanner;
public class Triangle {
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        int n = Integer.parseInt(kb.nextLine());
        int m = Integer.parseInt(kb.nextLine());
        int k = Integer.parseInt(kb.nextLine());
        if(n<=0||m<=0||k<=0){
            System.out.println("-1");
        }else if((n+m<=k)||(n+k<=m)||(m+k<=n)){
            System.out.println("-1");
        }else{
            System.out.println("1");
        }
    }
}