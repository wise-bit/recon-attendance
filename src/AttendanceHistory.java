import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AttendanceHistory extends JFrame implements ActionListener {

    ArrayList<Student> studentAttendanceRecord = new ArrayList<Student>();

    public JList<String> studentsOfClass;

    public JScrollPane pane = new JScrollPane(studentsOfClass);

    public AttendanceHistory() {

        studentsOfClass = new JList<>();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
