public class Student {
    private String name;
    private String gender;
    private int age;
    public Student(String name, String gender, int age){
        this.name = name;
        this.gender = gender;
        this.age = age;
    }
    public String toString(){
        return "Student name= "+ name + ", gender=" + gender + ", age=" + age;
    }
    public String getName(){
        return name;
    }
}
