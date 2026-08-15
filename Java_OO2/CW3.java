public class CW3 {
    private int num1;
    private int num2;
    public CW3(int num1, int num2){
        this.num1 = num1;
        this.num2 = num2;
    }
    public int returnFirst(){
        return num1;
    }
    public int returnSecond(){
        return num2;
    }
    public int addResult(){
        return num1+num2;
    }
    public int subResult(){
        return num1-num2;
    }
    public int mulResult(){
        return num1*num2;
    }
    public int divResult() {
        try {
            if (num2 == 0) {
                throw new ArithmeticException("The divisor num2 cannot be zero!");
            } else {
                return num1 / num2;
            }
        }catch(ArithmeticException e){
            System.out.println(e.toString());
            return -1;
        }
    }
}
