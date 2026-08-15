import java.io.BufferedReader;
import java.io.FileReader;
public class Text {
    public static void main(String[] args) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        String line;
        for(int i = 0; i < 3; i++){
            line = reader.readLine();
            System.out.println(line);
        }
        reader.close();
    }
}
