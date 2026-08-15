public class Object4 {
    /*
    构造方法需要方法名与类名相同，大小写也要一致
    没有返回值类型，连void也没有，没有具体的返回值（不能由return带回结果数据）
    创建对象的时候由虚拟机调用，不能够手动调用构造方法
    每创建一次对象，就会调用一次构造方法
     */
    private String name;
    private int age;

    public Object4(String name, int age){
        this.name = name;
        this.age = age;
    }
    /*
    如果没有构造方法，虚拟机就会给我们加一个空参构造方法，即ObjectTest4 mm = new ObjectTest4();
    此时已经构造了方法，那么创建对象时就需要输入参数，即ObjectTest4 mm = new ObjectTest4("zz", 23);
     */

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setAge(int age){
        this.age = age;
    }
    public int getAge(){
        return age;
    }
}
