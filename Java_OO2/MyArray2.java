public class MyArray2 {
    public static void main(String[] args){

        int[] x = new int[7];
        double[] y;
        y = new double[8];

        double[] array = new double[10];
        array[0]= 1.0;
        array[1]= 2.0;
        array[3]= 3.0;
        array[4]= 4.0;
        //double[] array = {1.0,2.0,3.0,4.0};
        double a = 0;
        for(int i = 0;i<10;i++){
            a += array[i];
        }
        System.out.println(a);

        double[] myList = {1.9, 2.9, 3.4, 3.5};
        for (double element: myList) {
            System.out.println(element); //打印数组所有元素
        }

        String[] s = new String[10];     // default values: null
        boolean[] b = new boolean[4];    // default values: false
        int[] i = new int[10];           // default values: 0
        System.out.println(s[0]);
        System.out.println(b[0]);
        System.out.println(i[0]);
    }
}









