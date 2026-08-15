/* 面向对象：java语言是面向对象，想要创建对象必须先定义类
   定义类的语法：[修饰符列表] class 类名{
   属性;
   方法;
   }
    属性通常采用变量来表示，既然是变量那么变量肯定会有数据类型，属性对应的就是状态信息
*/

public class Object {
    //学生类：只是一个模板，描述了所有学生的共同特征（状态+行为），当前类只描述学生的状态信息（属性）
    //Object是一个类，属于引用数据类型，类型名就叫做Object
    //类体=属性+方法（属性即存储数据采用变量的形式），由于变量都定义在类体中且方法体外，这种变量也称为成员变量
    //由于每个学生都有学号且互不相同，学号不能直接通过类去访问，所以访问学号就要创建对象，通过对象去访问学号
    //这种成员变量又叫做实例变量，对象又被称作实例，实例变量又被称作对象变量（对象级别的变量）
    int no;
    //不创建对象的话，这个no变量的内存空间是不存在的，只有创建了对象这个no变量内存才会创建
    String name;
    //姓名，String是一种引用数据类型，name是一个实例变量，是一个引用
    int age;
    //年龄，int是一种基本数据类型，age是一个实例变量，是一个引用
    boolean sex;
    //性别
    String add;
    //地址
    //上面都是成员变量之实例变量（属性）
    // 成员变量没有手动赋值的话，系统默认赋值：整数型0，浮点型0.0，布尔型false，字符型\u0000，引用数据类型null（空值）
    public static void main(String[] args){
        //每一个类中都可以编写主方法，但是一般情况下一个系统只有一个入口，所以主方法一般写一个，以上就是一个程序入口
        Object s = new Object();
        //通过一个类可以实例化n个对象，实例化对象的语法：new 类名(); new是一个运算符，作用是创建对象并在JVM堆内存中开辟新的内存空间
        //Object是一个引用数据类型，s是一个变量名和局部变量，new Object()是一个学生对象
        //对象就是new运算符在堆内存中开辟的内存空间，引用就是一个变量，它保存了另一个java对象的内存地址
        int stuNo = s.no;
        String stuName = s.name;
        boolean stuSex = s.sex;
        String stuAdd = s.add;
        //访问实例变量的语法格式：读取数据：引用.变量名;  修改数据：引用.变量名=值;
        System.out.println("学号"+stuNo);//默认值为0
        System.out.println("姓名"+stuName);//默认值为null
        System.out.println("性别"+stuSex);//默认值为false
        System.out.println("地址"+stuAdd);//默认值为null
        //上面的输出为输出Object对象内部实例变量的值
        /*System.out.println("学号"+s.no);
          System.out.println("姓名"+s.name);
          System.out.println("性别"+s.sex);
          System.out.println("地址"+s.add);
          这样的写法输出结果一致
         */
        s.no = 110;
        s.name = "jack";
        s.sex = true;
        s.add = "A";
        System.out.println("学号"+s.no);
        System.out.println("姓名"+s.name);
        System.out.println("性别"+s.sex);
        System.out.println("地址"+s.add);
        //上面为修改Object对象内部实例变量的值,后输出
        Object stu = new Object();
        System.out.println("学号"+stu.no);
        System.out.println("姓名"+stu.name);
        System.out.println("性别"+stu.sex);
        System.out.println("地址"+stu.add);
        //再实例化一个全新的对象，stu是一个引用同时也是一个局部变量，Object是变量的数据类型
        /*System.out.println(Object.no);
          这样会导致编译报错，no这个实例变量不能够直接采用类名的方式访问，因为no是实例变量且是对象级别的，它储存在java对象的内部
          必须要先有对象，通过对象才能访问no这个实例变量，不能直接通过类名访问
         */
    }
}

/* 输出：
        学号0
        姓名null
        性别false
        地址null
        学号110
        姓名jack
        性别true
        地址A
        学号0
        姓名null
        性别false
        地址null
*/

/*  public class OOT{
      public static void main(String[] args){
      创建一个丈夫对象
      Husband jack = new Husband();
      jack.name = "jack";
      创建一个妻子对象
      Wife lucy = new Wife;
      lucy.name = "lucy";
      结婚（能够通过丈夫找到妻子，也能够通过妻子找到丈夫）
      jack.w = lucy;
      lucy.h = jack;
      得到“jack”的妻子的名字
      System.out.println(jack.name+"的妻子名字叫"+jack.w.name);
      }
}
   丈夫类
   public class Husband{
   身份证号码
   String idCard;
   丈夫对象中含有妻子的引用
   Wife w;
   }
   妻子类
   public class Wife{
   身份证号码
   String idCard;
   妻子对象中含有丈夫的引用
   Husband h;
   }
 */