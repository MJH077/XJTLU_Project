public class Hero2 extends Hero1{
    public String activePlus;
    public Hero2(String name, String activeSentence, String activePlus){
        super(name, activeSentence);
        this.activePlus = activePlus;
    }
    @Override
    public void active(){
        super.active();
        System.out.println("then " + activePlus);
    }
}
