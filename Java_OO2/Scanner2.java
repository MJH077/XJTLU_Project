import java.util.Scanner;
public class Scanner2 {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        int f = 1;
        float e = 1.0f;
        System.out.println("输入整数：");
        if(scan.hasNextInt()){
            f = scan.nextInt();
            System.out.println("整数为： "+f);
        }else {
            System.out.println("不是整数");
        }
        System.out.println("输入小数：");

        if(scan.hasNextFloat()){
            e = scan.nextFloat();
            System.out.println("小数为： "+e);
        }else{
            System.out.println("不是小数");
        }
        scan.close();
    }
}
