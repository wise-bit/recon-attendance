import org.bridj.NativeError;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class IntermediatePage extends JFrame implements ActionListener {

    // A JComboBox to choose your class
    JComboBox classChooser;

    // Declaration of all of the buttons
    JButton addNewFace = new JButton("Add New Student");
    JButton moreTraining = new JButton("Train Your Data");
    JButton viewClass = new JButton("View Your Class");
    JButton attendanceHistory = new JButton("View Attendance History");
    JButton attendanceTime = new JButton("Take Attendance");
    JButton addClass = new JButton("Add New Class");
    JButton logout = new JButton("Logout");

    // A border for better visibility of objects
    public static Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 2, true);

    /**
     * Constructor for the intermediate page to take to other pages
     * @throws IOException
     */
    public IntermediatePage() throws IOException {

        setSize(1000, 400);
        setLocation((int)Main.dim.getWidth()/2-500, (int)Main.dim.getHeight()/2-250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Choose your options");

        setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/background.jpeg")))));

        JLabel title = new JLabel("What would you like to do?", SwingConstants.CENTER);
        title.setBounds(0, 20, 1000, 50);
        title.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        title.setVisible(true);
        title.setForeground(Color.YELLOW);
        add(title);

        // THE REASON THIS WAS NOT DONE USING A FOR LOOP WAS TO ENSURE READIBILITY OF THE CODE WHEN EDITING LATER, AND
        // AVOIDING CONFUSION OF WHICH BUTTON IS FOR WHICH FUNCTIONALITY

        /////-----  ADDDING MAIN BUTTONS STARTS HERE  -----/////

        // Adding a button for new face addition
        addNewFace.setFont(new Font("Verdana", Font.BOLD, 18));
        addNewFace.setBorder(blackBorder);
        addNewFace.setBounds(150, 100, 300, 30);
        addNewFace.setBackground(Color.WHITE);
        addNewFace.setForeground(Color.BLACK);
        addNewFace.addActionListener(this);
        addNewFace.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addNewFace.setBackground(Color.BLACK);
                addNewFace.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addNewFace.setBackground(Color.WHITE);
                addNewFace.setForeground(Color.BLACK);
            }
        });
        add(addNewFace);

        // Adding a button for more training of facial features of one student
        moreTraining.setFont(new Font("Verdana", Font.BOLD, 18));
        moreTraining.setBorder(blackBorder);
        moreTraining.setBounds(150, 150, 300, 30);
        moreTraining.setBackground(Color.WHITE);
        moreTraining.setForeground(Color.BLACK);
        moreTraining.addActionListener(this);
        moreTraining.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                moreTraining.setBackground(Color.BLACK);
                moreTraining.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                moreTraining.setBackground(Color.WHITE);
                moreTraining.setForeground(Color.BLACK);
            }
        });
        add(moreTraining);

        // Adding a button for viewing all students of a class
        viewClass.setFont(new Font("Verdana", Font.BOLD, 18));
        viewClass.setBorder(blackBorder);
        viewClass.setBounds(150, 200, 300, 30);
        viewClass.setBackground(Color.WHITE);
        viewClass.setForeground(Color.BLACK);
        viewClass.addActionListener(this);
        viewClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewClass.setBackground(Color.BLACK);
                viewClass.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewClass.setBackground(Color.WHITE);
                viewClass.setForeground(Color.BLACK);
            }
        });
        add(viewClass);

        // Adding a button for attendance history
        attendanceHistory.setFont(new Font("Verdana", Font.BOLD, 18));
        attendanceHistory.setBorder(blackBorder);
        attendanceHistory.setBounds(150, 250, 300, 30);
        attendanceHistory.setBackground(Color.WHITE);
        attendanceHistory.setForeground(Color.BLACK);
        attendanceHistory.addActionListener(this);
        attendanceHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                attendanceHistory.setBackground(Color.BLACK);
                attendanceHistory.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                attendanceHistory.setBackground(Color.WHITE);
                attendanceHistory.setForeground(Color.BLACK);
            }
        });
        add(attendanceHistory);

        // Adding a button for taking the student to the page which allows the taking of attendance
        attendanceTime.setFont(new Font("Verdana", Font.BOLD, 18));
        attendanceTime.setBorder(blackBorder);
        attendanceTime.setBounds(150, 300, 300, 30);
        attendanceTime.setBackground(Color.WHITE);
        attendanceTime.setForeground(Color.BLACK);
        attendanceTime.addActionListener(this);
        attendanceTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                attendanceTime.setBackground(Color.BLACK);
                attendanceTime.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                attendanceTime.setBackground(Color.WHITE);
                attendanceTime.setForeground(Color.BLACK);
            }
        });
        add(attendanceTime);

        // Adding a button for adding a new class to the database of the teacher
        addClass.setFont(new Font("Verdana", Font.BOLD, 18));
        addClass.setBorder(blackBorder);
        addClass.setBounds(600, 200, 300, 30);
        addClass.setBackground(Color.WHITE);
        addClass.setForeground(Color.BLACK);
        addClass.addActionListener(this);
        addClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addClass.setBackground(Color.BLACK);
                addClass.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addClass.setBackground(Color.WHITE);
                addClass.setForeground(Color.BLACK);
            }
        });
        add(addClass);

        /////-----  ADDDING MAIN BUTTONS ENDS HERE  -----/////

        // Adding a JComboBox for choosing active class
        classChooser = new JComboBox(Main.listFilesFolders("res/attendanceData/" + Main.currentTeacher));
        classChooser.addItem("Select");
        classChooser.setSelectedItem("Select");
        classChooser.setBounds(600, 150, 300, 30);
        classChooser.addActionListener(this);
        classChooser.setVisible(true);
        add(classChooser);

        // Button to log out and go back to the login screen, for another teacher to log in
        logout.setFont(new Font("Verdana", Font.BOLD, 18));
        logout.setBorder(blackBorder);
        logout.setBounds(600, 300, 300, 30);
        logout.setBackground(Color.WHITE);
        logout.setForeground(Color.BLACK);
        logout.addActionListener(this);
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logout.setBackground(Color.RED);
                logout.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logout.setBackground(Color.WHITE);
                logout.setForeground(Color.BLACK);
            }
        });
        add(logout);

        // Ensures that current is not a blank item, therefore preventing crashes
        String current = classChooser.getSelectedItem().toString();
        Main.currentClass = current;

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Adds actionlistener to classChooser to choose a class
        if (e.getSource() == classChooser) {

            String current = classChooser.getSelectedItem().toString();
            Main.currentClass = current;

        } else

            // Adds actionlistener to add new class
        if (e.getSource() == addClass) {
            try {
                Main.addClass = new AddClass();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else

            // Adds actionlistener to logout button to go back to login screen
        if (e.getSource() == logout) {

            // Logs out of the current user account
            Main.intermediatePage.dispose();
            try {
                Main.login = new Login();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        else if (Main.currentClass.equals("Select")) // Ensures that the user has chosen a class
        {
            JOptionPane.showMessageDialog(this, "Please choose a class!",
                    "Message", JOptionPane.PLAIN_MESSAGE);
        } else {

            // Takes to the page to add new face/student
            if (e.getSource() == addNewFace) {

                // // Creates an instance of the new face class
                Main.intermediatePage.dispose();
                try {
                    Main.newface = new NewFace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            // Adds actionlistener to training button to add more training data
            if (e.getSource() == moreTraining) {

                // Takes to the training page
                Main.intermediatePage.dispose();
                // Creates an instance of the train face class
                try {
                    Main.training = new TrainFace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

            // Acitonlistener for button for viewClass
            if (e.getSource() == viewClass) {

                // Creates an instance of the student list class
                try {
                    Main.studentlist = new StudentList();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

            // This show the attendance history when clicked
            if (e.getSource() == attendanceHistory) {

                // Creates an instance of the attendance history class
                try {
                    Main.history = new AttendanceHistory();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

            // Adds functionality to attendance time button, which is clicked when it is time to take attendance
            if (e.getSource() == attendanceTime) {

                try {
                    Main.faceScanPage = new FaceScanPage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

        }

    }

}
