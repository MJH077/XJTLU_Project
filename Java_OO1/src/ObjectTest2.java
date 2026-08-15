public class ObjectTest2 {
    public static void main(String[] args){
        Object2 p = new Object2();
        p.setBrand("iPhone");
        p.setPrice(6000);
        System.out.println(p.getBrand());
        System.out.println(p.getPrice());

        Object2 r = new Object2();
        r.call();
        r.pay();
    }
}
