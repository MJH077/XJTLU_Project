import java.util.Scanner;
public class MaxArea{
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        double m = Double.parseDouble(kb.nextLine());
        double area = (m/2)*(m/2)*Math.PI;
        System.out.println(area);
    }
}
