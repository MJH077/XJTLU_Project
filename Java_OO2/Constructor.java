/*构造方法（constructor method）：
  1.java中构造方法又被称为构造函数、构造器、constructor
  2.语法结构：[修饰符列表] 构造方法名（形式参数列表）{
             构造方法体；
             }
  3.对于构造方法来说，“返回值类型”不需要指定，并且也不能够写void，只要写了void，那么这个方法就成为普通方法了
  4.构造方法的方法名必须和类名保持一致
  5.构造方法存在的意义是通过构造方法的调用，可以创建对象
  6.普通方法的调用：类名.方法名（实参列表）;（方法修饰符中有static的时候）  引用.方法名（实参列表）;（方法修饰符中没有static的时候）
    构造方法的调用：new 构造方法名（实参列表）;
  7.每一个构造方法实际上执行结束后都会有返回值，但是“ return 值”语句不需要书写
    构造方法结束的时候java程序自动返回值，并且返回值类型是构造方法所在的类型
    由于构造方法的返回值类型就是类本身，所以返回值类型不需要书写
  8.当一个类中没有定义任何构造方法的话，系统默认给该类提供一个无参数的构造方法，这个构造方法称为缺省构造器
  9.当一个类显示的构造方法定义出来了，那么系统则不再默认为这个类提供缺省构造器
     建议开发当中手动地为当前类提供无参数构造方法，因为无参数构造方法太常用了
  11.构造方法支持重载机制，在一个类当中编写多个构造方法，这多个构造方法显然已经构成方法重载机制
 */
public class Constructor{
    String name;
    int age;
    String designation;
    double salary;
    public Constructor(String name){
        this.name = name; //this是本类的意思
    }
    public void conAge(int conAge){
        age =  conAge;
    }
    public void conDesignation(String conDesign) {
        designation = conDesign;
    }
    public void conSalary(double conSalary){
        salary = conSalary;
    }
    public void printConstructor(){
        System.out.println("名字:"+ name );
        System.out.println("年龄:" + age );
        System.out.println("职位:" + designation );
        System.out.println("薪水:" + salary);
    }
}

/*public class Constructor2{

    public static void main(String[] args){

        Constructor conOne = new Constructor("Jack");
        Constructor conTwo = new Constructor("Tim");

        conOne.conAge(26);
        conOne.conDesignation("高级");
        conOne.conSalary(1000);
        conOne.printEmployee();

        conTwo.conAge(21);
        conTwo.conDesignation("菜鸟");
        conTwo.conSalary(500);
        conTwo.printEmployee();
    }
}
 */

/*  输出：
        名字:Jack
        年龄:26
        职位:高级
        薪水:1000.0
        名字:Tim
        年龄:21
        职位:菜鸟
        薪水:500.0
*/




