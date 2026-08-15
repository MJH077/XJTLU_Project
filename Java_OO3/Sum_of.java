public class Sum_of {
    public static int[] sol(int[] list){
        int[] result = new int[(list.length)/2];
        for(int i = 0; i < (list.length)/2; i++ ){
            int sum = list[2*i] + list[2*i+1];
            result[i] = sum;
        }
        return result;
    }
}
