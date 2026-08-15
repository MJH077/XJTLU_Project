public class String1{
    public static void main(String[] args){
        char[] myArray = {'f','u','c','k'};
        String myString = new String(myArray);
        System.out.println(myString);

        String string = "What can I say?";
        int len = string.length();
        System.out.println("The length is " + len);
    }
}