import java.io.IOException;

public interface AdditionServices {

    public void saveNewState() throws IOException;

    public void modifyState(); // for debugging

}
