public class Star2{
    public static void main(String[] args){
        for(int i=1;i<=9;i++){  //i为*左边的数字，是行号
            for(int j=1;j<=i;j++){
                System.out.print(i + "*" + j + "=" + i * j + " ");
            }
            System.out.println();
        }
    }
}