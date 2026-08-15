public class Parameter1 {
    public static void main(String[] args){
        printAllNames("name = *Makima*");
        printAllNames("instructor name = *Andrew* and name = *Erick*");


    }
    public static void printAllNames(String str){
        String openPattern = "name = *";
        String closePattern = "*";
        int i = 0;
        while(true){
            int start = str.indexOf(openPattern,i);
            if(start==-1){
                break;
            }
            int end = str.indexOf("*",start+8);
            System.out.println(str.substring(start+8,end));
            i = end+1;
        }
    }
}
