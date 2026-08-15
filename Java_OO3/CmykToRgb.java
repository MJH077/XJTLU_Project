import java.util.Scanner;
public class CmykToRgb {
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        double cyan = Double.parseDouble(kb.nextLine());
        double magenta = Double.parseDouble(kb.nextLine());
        double yellow = Double.parseDouble(kb.nextLine());
        double black = Double.parseDouble(kb.nextLine());
        double white = 1 - black;
        double red = 255 * white * (1 - cyan);
        double green = 255 * white * (1 - magenta);
        double blue = 255 * white * (1 - yellow);
        System.out.println("red = " + Math.round(red));
        System.out.println("green = " + Math.round(green));
        System.out.println("blue = " + Math.round(blue));
    }
}
