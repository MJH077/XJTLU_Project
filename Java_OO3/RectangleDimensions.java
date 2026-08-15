import java.util.Scanner;
public class RectangleDimensions {
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        double l = Double.parseDouble(kb.nextLine());
        double n = Double.parseDouble(kb.nextLine());
        double m = Double.parseDouble(kb.nextLine());
        double b = (l*n)/(2*(n+m));
        double a = 0.5*l-b;
        System.out.println(b);
        System.out.println(a);
    }
}
