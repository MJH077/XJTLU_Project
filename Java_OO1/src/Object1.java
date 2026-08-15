import java.util.Random;
import java.util.Scanner;

public class  Object1 {
    public static void main(String[] args){
        Random r = new Random();
        int data = r.nextInt(10) + 1;
        System.out.println(data);
        // get an object number randomly

        Scanner sc = new Scanner(System.in);
        System.out.println("Your age: ");
        int age = sc.nextInt();
        System.out.println(age);
        // get an age from users
    }
}
