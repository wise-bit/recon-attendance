import java.util.ArrayList;

public class Student implements Person {

    String name;
    ArrayList<Integer> attendanceSinceDayOne = new ArrayList<Integer>();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
