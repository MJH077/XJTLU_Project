public class Hero {
    public String name;
    public String activeSentence;
    public Hero(String name){
        this.name = name;
        this.activeSentence = "Active";
    }
    public Hero(String name, String activeSentence){
        this.name = name;
        this.activeSentence = activeSentence;
    }
    public void active(){
        System.out.println(name + "said" + activeSentence);
    }
}
