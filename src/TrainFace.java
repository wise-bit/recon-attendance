import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrainFace extends JFrame implements AdditionServices, ActionListener {

    JLabel title = new JLabel("Add more phases of faces");

    JLabel askName = new JLabel("Name: ");
    JComboBox nameBox = new JComboBox();

    JButton deleteAll = new JButton("Start from scratch");
    JButton register = new JButton("Take Snapshot");
    JButton save = new JButton("Save");

    public TrainFace() {

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
