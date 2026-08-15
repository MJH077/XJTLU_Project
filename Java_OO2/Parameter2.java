public class Parameter2 {
    public static void main(String[] args){
        System.out.println("The value is " + toMilesPerHour(1.5));
        System.out.println("The value is " + toMilesPerHour(10.25));
        System.out.println("The value is " + toMilesPerHour(-5.6));
        System.out.println("The value is " + toMilesPerHour(25.42));
        System.out.println("The value is " + toMilesPerHour(75.114));
    }
    private static double toMilesPerHour(double kilometersPerHour){
        if(kilometersPerHour<0){
            return -1;
        }

        double toMilesPerHour = 0.6214*kilometersPerHour;
        return Math.round(toMilesPerHour);
    }
}
