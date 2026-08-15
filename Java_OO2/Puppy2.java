public class Puppy2 {
    int puppyAge;
    public Puppy2(String name){
        System.out.println("小狗的名字是： " + name);
    }
    public void setAge(int age){
        puppyAge = age;
    }
    public int getAge(){
        System.out.println("小狗的年龄为： " + puppyAge);
        return puppyAge;
    }
    public static void main(String[] args){
        Puppy2 myPuppy = new Puppy2("Jack");
        myPuppy.setAge(2);
        myPuppy.getAge();
        System.out.println("变量值： " + myPuppy.puppyAge);
    }
}