public class Exercise {
    public static void main(String[] args){
        System.out.println(getCount("acbbb"));
        System.out.println(getCount("a#c#bbb#"));
        System.out.println(getCount("#111#12"));
        System.out.println(getCount("#11112"));
    }
    public static int getCount(String input){
        if(input.length()==1)
            return 0;
        return input.charAt(0)==input.charAt(1)? getCount(input.substring(1)): 1+getCount(input.substring(1));
    }
}

