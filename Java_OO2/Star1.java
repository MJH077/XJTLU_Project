import java.util.Scanner;
public class Star1 {

    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        int n = Integer.parseInt(kb.nextLine());
        for(int i = 1; i <= n; i++){
            for(int x = 1; x <= i; x++){
                System.out.print("*");
            }
            for(int y = 1; y <= (n-i); y++){
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
