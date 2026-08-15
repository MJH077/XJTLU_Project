public class Hero1 {
    public String name;
    public String activeSentence;
    public Hero1(String name){
        this.name = name;
        this.activeSentence = "Active";
    }
    public Hero1(String name, String activeSentence){
        this.name = name;
        this.activeSentence = activeSentence;
    }
    public void active(){
        System.out.println(name + "said" + activeSentence);
    }
}
