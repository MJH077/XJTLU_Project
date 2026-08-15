public class String2 {

    public static void main (String[] args){

        String numberAsString = "2022";
        System.out.println("numberAsString = " + numberAsString);

        int number = Integer.parseInt(numberAsString);
        System.out.println("number = " + number);

        double number2 = Double.parseDouble(numberAsString);
        System.out.println("number = " + number2);
    }
}