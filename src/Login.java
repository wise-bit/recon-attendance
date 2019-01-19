import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Login extends JFrame implements ActionListener {

    JLabel usernamePrompt = new JLabel("Username: ");
    JLabel passwordPrompt = new JLabel("Password: ");
    JTextField usernameBox = new JTextField();
    JPasswordField passwordBox = new JPasswordField();

    JLabel informationLine = new JLabel();

    JButton login = new JButton("Login");
    JButton signup = new JButton("Sign Up");

    Webcam webcam = Webcam.getDefault();
    BufferedImage image;
    JLabel imageLbl = new JLabel();
    Border b = BorderFactory.createLineBorder(Color.WHITE, 5, true);
    int delay = 30;
    private Timer timer=new Timer(delay, this);

    public static final int imageWidth = 300;
    public static final int imageHeight = 300;

    boolean accessGranted = false;

    public Login() throws IOException {

        setSize(1000, 500);
        setLocation((int)Main.dim.getWidth()/2-500, (int)Main.dim.getHeight()/2-250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login to Reconattendance");

        setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/background.jpeg")))));

        // getContentPane().setBackground(Color.YELLOW);

        JLabel title = new JLabel("Reconattendance: Login", SwingConstants.CENTER);
        title.setBounds(0, 20, 1000, 50);
        title.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        title.setVisible(true);
        title.setForeground(Color.YELLOW);
        add(title);

        informationLine = new JLabel("<HTML><div style='text-align: center;'>The sign up button works using this form as well for convenience, " +
                "so please enter your information before pressing sign up!</div></HTML>");
        informationLine.setForeground(Color.WHITE);
        informationLine.setBounds(100, 250, 325, 70);
        add(informationLine);


        // Asks the questions

        usernamePrompt.setBounds(100, 150, 150, 20);
        usernamePrompt.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
        usernamePrompt.setVisible(true);
        usernamePrompt.setForeground(Color.WHITE);
        add(usernamePrompt);

        passwordPrompt.setBounds(100, 200, 150, 20);
        passwordPrompt.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
        passwordPrompt.setVisible(true);
        passwordPrompt.setForeground(Color.WHITE);
        add(passwordPrompt);


        // Sets up the text fields

        usernameBox.setBounds(250, 150 - 10, 175, 40);
        usernameBox.setOpaque(false);
        usernameBox.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
        usernameBox.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 18));
        add(usernameBox);

        passwordBox.setBounds(250, 200 - 10, 175, 40);
        passwordBox.setOpaque(false);
        passwordBox.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
        passwordBox.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 18));
        add(passwordBox);

        usernameBox.setText(Main.lastUsername);
        passwordBox.setText(Main.lastPassword);

        // Adds Button

        login.setBounds(this.getWidth()/2 - 50 - 70, this.getHeight() - 100, 100, 50);
        login.setBackground(new Color(154, 224, 247));
        login.setForeground(Color.WHITE);
        //login.setFocusPainted(false);
        login.setFont(new Font("Tahoma", Font.BOLD, 14));
        login.addActionListener(this);
        add(login);
        
        signup.setBounds(this.getWidth()/2 - 50 + 70, this.getHeight() - 100, 100, 50);
        signup.setBackground(new Color(154, 224, 247));
        signup.setForeground(Color.WHITE);
        //signup.setFocusPainted(false);
        signup.setFont(new Font("Tahoma", Font.BOLD, 14));
        signup.addActionListener(this);
        add(signup);

        try {

            webcam.open();

            if (webcam.isOpen()) { //if web cam open
                image = webcam.getImage();
                imageLbl = new JLabel();
                // imageLbl.setSize(1024, 720);
                Image dimg = image.getScaledInstance(450, 350, Image.SCALE_SMOOTH);
                imageLbl.setIcon(new ImageIcon(dimg));

//            imageLbl.setHorizontalAlignment(JLabel.CENTER);
//            imageLbl.setVerticalAlignment(JLabel.CENTER);

                imageLbl.setBorder(b);
                imageLbl.setBounds(600, 75, imageWidth, imageHeight);
                add(imageLbl);

            } else {
                JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam... please contact administrator.",
                        "Fatal Error", JOptionPane.PLAIN_MESSAGE);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam... please contact administrator.",
                    "Fatal Error", JOptionPane.PLAIN_MESSAGE);

        }

        JLabel faceFrame = new JLabel("Say Cheese!", SwingConstants.CENTER);
        faceFrame.setForeground(Color.WHITE);
        faceFrame.setFont(new Font("Tahoma", Font.BOLD + Font.ITALIC, 18));
        faceFrame.setBounds(50, 50, imageLbl.getWidth()-100, imageLbl.getHeight()-100);
        faceFrame.setBorder(BorderFactory.createLineBorder(Color.RED, 2, true));
        imageLbl.add(faceFrame);

        timer.start();
        setVisible(true);

    }

    public boolean unique() {

        for (Teacher t : Main.teacherAccounts) {
            if (t.getName().equals(usernameBox.getText())) {
                return false;
            }
        }
        return true;

    }

    /**
     * Action Listener methods
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == login) {

            if (usernameBox.getText().contains(",") || usernameBox.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "Invalid username", "Error", JOptionPane.INFORMATION_MESSAGE);
            } else if (String.valueOf(passwordBox.getPassword()).contains(",") || String.valueOf(passwordBox.getPassword()).length() == 0) {
                JOptionPane.showMessageDialog(null, "Invalid password", "Error", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String currentUserInput = usernameBox.getText();
                String currentPasswordInput = Encryption.encrypt(String.valueOf(passwordBox.getPassword()));

                for (Teacher t : Main.teacherAccounts) {
                    if (t.getName().equals(currentUserInput) && t.getEncodedPassword().equals(currentPasswordInput)) {
                        JOptionPane.showMessageDialog(null, "Welcome " + t.getName() + "!", "Message", JOptionPane.INFORMATION_MESSAGE);
                        Main.currentTeacher = t.getName();
                        Main.currentPassword = t.getEncodedPassword();
                        accessGranted = true;
                        break;
                    }
                }

                // Doesn't say if it is the username or the password that is wrong, for the sake of security which
                // prevents people from guessing any details
                if (!accessGranted) {
                    JOptionPane.showMessageDialog(null, "Invalid details, please try again ", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Main.login.dispose();
                    try {
                        Main.intermediatePage = new IntermediatePage();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }

        // If user presses the signup button
        if (e.getSource() == signup) {

            try {
                if (usernameBox.getText().contains(",") || usernameBox.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Invalid username", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else if (String.valueOf(passwordBox.getPassword()).contains(",") || String.valueOf(passwordBox.getPassword()).length() == 0) {
                    JOptionPane.showMessageDialog(null, "Invalid password", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else {

                    if (unique()) {

                        Main.lastUsername = usernameBox.getText();
                        Main.lastPassword = String.valueOf(passwordBox.getPassword());
                        String stringToWrite = "\n" + usernameBox.getText() + "," + Encryption.encrypt(String.valueOf(passwordBox.getPassword()));
                        Files.write(Paths.get("res/accounts.txt"), stringToWrite.getBytes(), StandardOpenOption.APPEND);

                        // Makes a new folder in the attendance data folder to keep track of each day's attendance for each student
                        new File("res/attendanceData/" + usernameBox.getText()).mkdir();

                        // Makes a new file in the student information folder to keep track of a student's name and studentID
                        new File("res/studentInformation/" + usernameBox.getText() + ".txt").createNewFile();


                        // Makes new directory to store face data of students
                        new File("res/studentFaceData/" + usernameBox.getText() + "/").mkdir();

                        Main.login.dispose();
                        Main.init();
                        Main.login = new Login();

                    } else {

                        JOptionPane.showMessageDialog(null, "Username already exists, plase try again!\n" +
                                "If you have forgotten your password, please contact the administrator.", "Error", JOptionPane.INFORMATION_MESSAGE);

                    }
                }
            }catch (IOException error) {
                System.out.println(error.getMessage());
            }

        }

        if (e.getSource() == timer) {

            image = webcam.getImage();

            imageLbl.setVisible(false);

            // FUNCTION: FLIP

            for (int i=0;i<image.getWidth()/2;i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    int tmp = image.getRGB(i, j);

                    image.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
                    image.setRGB(image.getWidth() - i - 1, j, tmp);

                }
            }

            image = ImageAnalysis.cropImage(image, (image.getWidth() - image.getHeight())/2, 0, image.getHeight(), image.getHeight());
            image = ImageAnalysis.resize(image, imageHeight, imageWidth);

            // Image dimg = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(image);
            imageIcon.getImage().flush();
            imageLbl.setIcon(imageIcon);
            imageLbl.setVisible(true);

        }

    }
}
