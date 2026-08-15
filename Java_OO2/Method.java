public class Method {
    public static void main(String[] args){
        int i = 100;
        int j = 200;
        int k = max(i,j);
        System.out.println(k); //方法1
        //System.out.println(max(100,200));  方法2
    }
    public static int max(int num1, int num2){
        int result;
        if(num1<num2){
            result = num2;
        }else{
            result = num1;
        }
        return result;
    }
}
