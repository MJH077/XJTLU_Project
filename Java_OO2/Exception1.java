public class Exception1 {
    public static void main(String[] args){
        try{
            System.out.println(4/0);
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }finally{
            System.out.println("Finally executed.");
        }
        System.out.println("end.");
    }
}
