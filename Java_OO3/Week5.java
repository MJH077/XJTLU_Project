public class Week5 {
    public static void main(String[] args){
        System.out.println(sol("2222",'o'));
    }
    public static boolean sol(String s, char c){
        int count = 0;
        if(Character.isDigit(c)){
            for(int i = 0; i < s.length(); i++){
                char x = s.charAt(i);
                if((Character.isDigit(x))){
                    count++;
                }
            }
            if(count>0){
                if(count == s.length()){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        } else{
            for(int i = 0; i < s.length(); i++){
                char x = s.charAt(i);
                if((Character.isDigit(x))){
                    count++;
                }
            }
            if(count==0){
                return true;
            }else{
                return false;
            }
        }
    }
}

/* public static int sol(String s) {
        int ans = 0;
        for (int i = 0; i < s.length(); i++){
            if (Character.isDigit(s.charAt(i)))
                ans++;
            else
                ans--;
                }
        if (ans == 0)
            return 0;
        else if (ans < 0)
            return -1;
        else
            return 1;
    }
*/


