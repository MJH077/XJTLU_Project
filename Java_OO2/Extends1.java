/*java中的继承:
    1.继承是面向对象三大特征之一（封装，继承，多态）
    2.继承的基本作用是代码复用，最重要的作用是有了继承才有了以后“方法的覆盖”和“多态机制”
    3.继承语法格式：[修饰符列表] class 类名 extends 父类名{
                  类体=属性+方法
                  }
    4.java语言中的继承只支持单继承，一个类不能够同时继承很多的类
    5.B类继承A类：A类称为父类、基类、超类、superclass；B类称为子类、派生类、subclass
    6.在java语言当中子类继承父类所继承的数据：私有的不支持继承，构造方法不支持继承，其他数据都可以被继承
    7.虽然java语言当中只支持单继承，但是一个类也可以间接继承其他的类
      C extends B{
      }
      B extends A{
      }
      A extends T{
      }
      C直接继承B类，但是C类间接继承T、A类
    8.java语言中假设一个类没有显示继承任何类，该类默认继承JavaSE库当中提供的java.lang.Object类
 */

public class Extends1 {
    public static void main(String[] args){
        Extends1 et = new Extends1();
        String s = et.toString();
        // 编译不报错，说明可以调用toString方法，Extends类中有toString方法，是从Object类中继承过来的
        System.out.println(s);
    }
}

// 输出：Extends@723279cf

/*
public class CreditAccount extends Account{
    private double credit;
    public CreditAccount(){
        super();
    }
    public double getCredit(){
        return credit;
    }
    public void setCredit(double credit){
        this.credit = credit;
    }
}

public class Account {
    private String actNo;
    private double balance;
    public Account(){
    }
    public Account(String actNo, double balance){
        this.actNo = actNo;
        this.balance = balance;
    }
    public String getActNo(){
        return actNo;
    }
    public void setActNo(String actNo){
        this.actNo = actNo;
    }
    public double getBalance(){
        return balance;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
}
*/

/*
public class Student extends Person{ //extend Person
  private final String major;
  private final int yearOfStudy;  //Extra variables and getters
  public Student(String name, String gender, String city, String DoB, int id, int yearOfStudy, String maj){
  super(name,gender,city,DoB,id,yearOfStudy,maj)  //Student calls superclass constructor
  this.major = maj;
  this.yearOfStudy = yearOfStudy;
  }
  public int getYearOfStudy(){
  return yearOfStudy;
  }
  public String getMajor(){
  return major;
  }
  public static void main(String[] args) {
  Person p1 = new Person("James Bond");
  Student s1 = new Student("A Student");
  System.out.println("Name is " + s1.getName());
  System.out.println("Major is " + s1.getMajor());
  }
}
 */
