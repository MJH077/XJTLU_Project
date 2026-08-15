public class Array {
    public static int sol(int[] list){
        int count = 0;
        for(int num : list){
            if(num%2!=0){
                count++;
            }
        }
        return count;
    }
    public static void main(String[] args){
        int[] list = {2,5,8,3,9,4};
        int oddNum = sol(list);
        System.out.println("sol(list) >= " + oddNum);
    }
}

