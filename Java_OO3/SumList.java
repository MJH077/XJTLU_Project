public class SumList {
    public static int sol(int[] list){
        int sum = 0;
        for(int num : list){
            sum += num;
        }
        int ans = sum%(list.length);
        return ans;
    }
}
