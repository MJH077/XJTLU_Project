public class Object2 {
    private String brand;
    private double price;
    // attribute


    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getBrand(){
        return brand;
    }
    public void setPrice(double price){
        if(price >= 10 && price <= 100){
            this.price = price;
        }else{
            System.out.println("非法");
        }
    }
    public double getPrice(){
        return price;
    }
    /*
    set方法：给成员变量赋值
    get方法：对外提供成员变量的值
     */

    public void call(){
        System.out.println("Calling");
    }
    public void pay(){
        System.out.println("Paying");
    }
    // action
}
/*
private关键字是一个权限修饰符，可以修饰成员变量和成员方法
被修饰的成员只能在本类中访问，若要被其他类使用需提供set,get等方法
这些方法用于给于或获取成员变量的值，且方法用public修饰
 */