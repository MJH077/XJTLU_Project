public class DataType1 {
    public static void main(String[] args){
        int value1 = java.lang.Integer.MIN_VALUE;
        int value2 = java.lang.Integer.MAX_VALUE;
        System.out.println(value1);
        System.out.println(value2);

        byte value3 = Byte.MAX_VALUE;
        byte value4 = Byte.MIN_VALUE;
        System.out.println(value3);
        System.out.println(value4);

        short value5 = Short.MAX_VALUE;
        short value6 = Short.MIN_VALUE;
        System.out.println(value5);
        System.out.println(value6);

        long value7 = Long.MIN_VALUE;
        long value8 = Long.MAX_VALUE;
        System.out.println(value7);
        System.out.println(value8);

        byte x = (byte) (value5/2);
        long y = (value5/2);
        int z = (value5/2);
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);

        int a = 10;
        short b = 20;
        byte c = 30;
        long d = 200L;
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);

        char myChar1 = 'A';
        char myChar2 = '\u0097';
        System.out.println(myChar1);
        System.out.println(myChar2);

        boolean myBoolean1 = true;
        boolean myBoolean2 = false;
        System.out.println(myBoolean1);
        System.out.println(myBoolean2);

        float myFloat1 = 5f;
        double myDouble1 = 10d;
        float myFloat2 = (float)(myDouble1/2);
        System.out.println(myFloat1);
        System.out.println(myDouble1);
        System.out.println(myFloat2);

        int o = 5/3;
        float p = 5f/3f;
        double q = 5d/3d;
        System.out.println(o);
        System.out.println(p);
        System.out.println(q);

        String myString1 = "This is myString1";
        String myString2 = myString1 + "This is myString2";
        String myString3 = myString2 + '1' + "100";
        String myString4 = myString3 + o;
        System.out.println(myString1);
        System.out.println(myString2);
        System.out.println(myString3);
        System.out.println(myString4);

        int result1 = 1;
        result1++;
        System.out.println(result1);
        result1--;
        System.out.println(result1);
    }
}
