public class Teacher implements Person {

    String name;
    String encodedPassword;

    public Teacher(String name, String encodedPassword) {
        this.name = name;
        this.encodedPassword = encodedPassword;
    }

    public String getEncodedPassword() { return encodedPassword; }

    public void setEncodedPassword(String encodedPassword) { this.encodedPassword = encodedPassword; }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
