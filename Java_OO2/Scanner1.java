import java.util.Scanner;
public class Scanner1 {
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);

        System.out.println("Input a number of year");
        String year = kb.nextLine();
        int y = Integer.parseInt(year);
        System.out.println("Input a number of month");
        String month = kb.nextLine();
        int m = Integer.parseInt(month);
        System.out.println("Input a number of day");
        String day = kb.nextLine();
        int d = Integer.parseInt(day);
        int tol = y+m+d;
        System.out.println(tol);

        String asString = "2022";
        int number = Integer.parseInt(asString);
        System.out.println("number = " + number);
        double number2 = Double.parseDouble(asString);
        System.out.println("number = " + number2);

        System.out.println("Enter your name: ");
        String name = kb.nextLine();
        System.out.println("Your name is " + name);
        System.out.println("Enter your year of birth: ");
        int yearOfBirth = kb.nextInt();
        kb.nextLine();
        System.out.println("Enter your name: ");
        String name1 = kb.nextLine();
        int age = 2022 - yearOfBirth;
        System.out.println("Your name is " + name1 + ", and you are " + age + " years old");
    }
}