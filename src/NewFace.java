import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewFace extends JFrame implements AdditionServices, ActionListener {

    JLabel title = new JLabel("New Profile");

    JLabel askName = new JLabel("Name: ");
    JLabel askEmail = new JLabel("Email");
    JLabel askTeacherPassword = new JLabel("Teacher's authorization");

    JTextField nameBox = new JTextField();
    JTextField emailBox = new JTextField();
    JTextField teacherPasswordBox = new JTextField();

    JButton register = new JButton("Take Snapshot");
    JButton save = new JButton("Save");

    public NewFace() {

    }

    @Override
    public void saveNewState() {

    }

    @Override
    public void modifyState() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
