public class String3{
    public static void main(String[] args){
        System.out.println(countQuestionMark("ab???ab"));
        System.out.println(countQuestionMark("?abc?"));
        System.out.println(countQuestionMark(""));
    }
    public static int countQuestionMark(String str){
        int count = 0;
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i)=='?'){ //str.substring(i,i+1).equals("?");
                count++;
            }
        }
        return count;
    }

}
