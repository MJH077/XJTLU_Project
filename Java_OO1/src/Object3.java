public class Object3 {
    public static void main(String[] arg) {
        String s = "xjtlu";
        int len = s.length();
        System.out.println(len);
        String ss = s.toUpperCase();
        System.out.println(ss);
    }

    private int age;
    public void method(){
        int age = 10;
        System.out.println(this.age);
        System.out.println(age);
    }//就近原则和this关键字
}
