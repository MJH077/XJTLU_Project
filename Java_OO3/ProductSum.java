public class ProductSum {
    public static int sol(int[] list) {
        int[] result = new int[(list.length) / 2];
        int sum = 0;

        for (int i = 0; i < (list.length) / 2; i++) {
            result[i] = list[2*i] * list[2*i + 1];
        }

        for(int num : result){
             sum += num;
        }
        return sum;
    }
}

