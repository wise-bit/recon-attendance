import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class IntermediatePage extends JFrame implements ActionListener {

    JComboBox classChooser;

    JButton addNewFace = new JButton("Add New Face");
    JButton moreTraining = new JButton("Train Your Data");
    JButton viewClass = new JButton("View your class");
    JButton attendanceHistory = new JButton("View attendance history");
    JButton attendanceTime = new JButton("Take attendance");

    JButton chooseClass = new JButton("Select Class");

    public IntermediatePage() throws IOException {

        setSize(1000, 500);
        setLocation((int)Main.dim.getWidth()/2-500, (int)Main.dim.getHeight()/2-250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login to Reconattendance");

        setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/background.jpeg")))));



    }

    @Override
    public void actionPerformed(ActionEvent e) {



    }

}
