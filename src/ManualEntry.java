/**
 * This class is for manual entry of data, if facial recognition fails to work
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Scanner;

public class ManualEntry extends JFrame implements ActionListener {

    // Stores informaiton for making a date, when storing attendance data for a specific date
    private int year = new Date().getYear() + 1900;
    private int month = new Date().getMonth();
    private int day = new Date().getDate();
    private String date;

    JComboBox name;
    JButton enter;

    boolean addingNew;

    /**
     * @param addingNew
     * @throws IOException
     */
    public ManualEntry(boolean addingNew) throws IOException {

        // Assigns date
        date = String.format("%d-%d-%d", year, month, day);

        // Aspect of function of this class instance
        this.addingNew = addingNew;

        setSize(350, 400);
        setLocation((int)Main.dim.getWidth()/2-500, (int)Main.dim.getHeight()/2-250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Enter your attendance manually");

        setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/background.jpeg")))));

        // getContentPane().setBackground(Color.YELLOW);

        // Makes JLabel title
        JLabel title = new JLabel("Manual Attendance", SwingConstants.CENTER);
        title.setBounds(0, 20, 1000, 50);
        title.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        title.setVisible(true);
        title.setForeground(Color.YELLOW);
        add(title);

        // Makes JLabel askClassName
        JLabel askClassName = new JLabel("Name: ");
        askClassName.setBounds(100, 100, 150, 20);
        askClassName.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
        askClassName.setVisible(true);
        askClassName.setForeground(Color.WHITE);
        add(askClassName);

        // Defines the path to the right place
        String path = "res/studentInformation/" + Main.currentTeacher + ".txt";
        String[][] temp = new String[Main.countLines(path)+1][3];

        int index = 0;

        // Independent from the global objects, since this functionality is built for viewing raw data
        // (This same code was used in Main class as well)
        File file = new File(path);
        String[] people = new String[Main.countLines(path)];

        // Initializes scanner for reading the student list file
        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            // Store all attributes in array
            String[] attribs = reader.nextLine().split(",");

            // Assign it to the index of the previously defined array
            people[index] = attribs[0];
            index++;
        }

        // initializes combobox
        name = new JComboBox(people);
        name.setBounds(100, 130, 150, 40);
        name.setOpaque(false);
        name.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
        name.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 18));
        add(name);

        // Initializes enter button
        enter = new JButton("Enter");
        enter.setBorder(IntermediatePage.blackBorder);
        enter.setBounds(100, 250, 150, 30);
        enter.setBackground(Color.WHITE);
        enter.setForeground(Color.BLACK);
        enter.addActionListener(this);
        enter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                enter.setBackground(Color.BLACK);
                enter.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                enter.setBackground(Color.WHITE);
                enter.setForeground(Color.BLACK);
            }
        });
        add(enter);

        setVisible(true);

    }

    // Checks if person is already admitted for the day
    public boolean alreadyAdmitted(String name, String path) throws FileNotFoundException {

        Scanner reader = new Scanner(new File(path));

        // Checks every person already added
        while (reader.hasNextLine()) {
            String current = reader.nextLine();
            if (current.equals(name))
                return true;
        }
        return false;

    }

    /**
     * Action Listener methods
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Actionlistener for when enter is clicked
        if (e.getSource() == enter) {

            // Keeps track of identity the same way as facescanpage for consistency
            String identity = name.getSelectedItem().toString() + ".txt";
            String pathToAttendanceFile = "res/attendanceData/" + Main.currentTeacher + "/" + Main.currentClass + "/" + date + ".txt";
            File attendanceFile = new File(pathToAttendanceFile);

            try {
                if (!alreadyAdmitted(identity.substring(0, identity.length() - 4), pathToAttendanceFile)) {

                    boolean newline = false;

                    if (attendanceFile.exists())
                        newline = true;

                    attendanceFile.createNewFile();
                    // Take substring to remove extension from file name, which was used as identity
                    if (newline)
                        Files.write(Paths.get(pathToAttendanceFile), ("\n" + identity.substring(0, identity.length() - 4)).getBytes(), StandardOpenOption.APPEND);
                    else
                        Files.write(Paths.get(pathToAttendanceFile), (identity.substring(0, identity.length() - 4)).getBytes(), StandardOpenOption.APPEND);

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            // Closing this page
            Main.manualEntry.dispose();

        }
    }
}
