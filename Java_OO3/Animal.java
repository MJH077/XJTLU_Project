public class Animal {
    public String name;
    public boolean canFly;
    public Animal(String name){
        this.canFly = false;
        this.name = name;
    }
    public Animal(String name, boolean canFly){
        this.name = name;
        this.canFly = canFly;
    }
    public void active(){
        if(canFly==false){
            System.out.println(name + " cannot fly");
        }else{
            System.out.println(name + " can fly");
        }
    }
}
