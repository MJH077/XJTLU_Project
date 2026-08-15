public class Loop1 {
    public static void main(String[] args){

        int topScore = 100;
        int thirdScore = 80;
        if((topScore > thirdScore) && (topScore>100)){
            System.out.println("Great!");
        }else{
            System.out.println("Bad!");
        }
        if((topScore>90) || (thirdScore<=90)){
            System.out.println("Right!");
        }else{
            System.out.println("Wrong!");
        }
        int newValue = 50;
        if(newValue == 50){
            System.out.println("True!");
        }else{
            System.out.println("False!");
        }
        boolean isCar = false;
        if(isCar){
            System.out.println("This is not supposed to happen");
        }else{
            System.out.println("This is supposed to happen");
        }
        isCar = true;
        boolean wasCar = isCar ? true : false;
        if(wasCar){
            System.out.println("wasCar is true");
        }else{
            System.out.println("wasCar if false");
        }
           int value = 1;
           if(value == 1){
               System.out.println("Value is 1");
           }else if(value == 2){
               System.out.println("Value is 2");
           }else{
               System.out.println("Value is not 1 or 2");
           }
           int switchValue = 5;
           switch(switchValue){
               case 1:
                   System.out.println("1");
                   break;
               case 2:
                   System.out.println("2");
                   break;
               case 3: case 4: case 5:
                   System.out.println("None");
                   break;
           }
           char charValue = 'A';
           switch(charValue){
               case 'A':
                   System.out.println("A was found");
                   break;
               case 'B':
                   System.out.println("B was found");
                   break;
               case 'C': case 'D': case 'E':
                   System.out.println("C D E were found");
                   break;
               default:
                   System.out.println("Not found");
                   break;
           }
           for(int i = 0; i<5; i++){
               System.out.println(i);
           }
           int f = 0;
           while(f<5){
               System.out.println(f);
               f++;
           }
           int k =0;
           do{
               System.out.println(k);
               k++;
           }while(k<=5);
    }
}
