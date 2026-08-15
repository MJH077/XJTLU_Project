public class HeroPlus extends Hero{
    public String activePlus;
    public HeroPlus(String name, String activeSentence, String activePlus){
        super(name, activeSentence);
        this.activePlus = activePlus;
    }
    @Override
    public void active(){
        super.active();
        System.out.println("then " + activePlus);
    }
}
