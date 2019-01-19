import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class NewFace extends JFrame implements AdditionServices, ActionListener, WebcamClass, ChangeListener {

    public static String tempName = "";
    public static String tempID = "";

    // initial ratio is 450 by 350
    public static final int imageWidth = 400;
    public static final int imageHeight = 400;

    JLabel title;

    JLabel askName = new JLabel("Name: ");
    JLabel askEmail = new JLabel("Email: ");
    JLabel askTeacherPassword = new JLabel("Authorization");

    JTextField nameField = new JTextField();
    JTextField studentIDField  =new JTextField();
    JTextField teacherPasswordBox = new JTextField();

    JButton register = new JButton("Take Snapshot");
    JButton save = new JButton("Save");
    JButton manualEntry = new JButton(("Manual Entry"));

    JLabel fpsCountLabel;
    JSlider slider;

    Webcam webcam;
    BufferedImage image;
    JLabel imageLbl = new JLabel();

    int delay = 30;

    private Timer timer = new Timer(delay, this);

    Border b = BorderFactory.createLineBorder(Color.BLACK, 5, true);

    public NewFace() throws IOException {

        setSize(1000, 600);
        setLocation((int) Main.dim.getWidth() / 2 - 500, (int) Main.dim.getHeight() / 2 - 250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login to Reconattendance");

        setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/background.jpeg")))));


        title = new JLabel("Add New Face", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Verdana", Font.BOLD, 24));
        title.setBounds(0, 20, 1000, 50);
        add(title);

        // Main panel

        webcam = Webcam.getDefault();
        webcam.open();

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
                imageLbl.setBounds(500, 75, imageWidth, imageHeight);
                add(imageLbl);

            } else {
                JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam... please contact administrator.",
                        "Fatal Error", JOptionPane.PLAIN_MESSAGE);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam... please contact administrator.",
                    "Fatal Error", JOptionPane.PLAIN_MESSAGE);

        }

        fpsCountLabel = new JLabel("FPS: " + (int) (1000 / delay), SwingConstants.CENTER);
        add(fpsCountLabel);

        slider = new JSlider(10, 60);
        slider.addChangeListener(this);
        add(slider);

        // Components

        // Form information

        // Questions asked

        // Asked names
        askName.setBounds(100, 150, 150, 20);
        askName.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
        askName.setVisible(true);
        askName.setForeground(Color.WHITE);
        add(askName);

        // Asked student ID
        askEmail.setBounds(100, 200, 150, 20);
        askEmail.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
        askEmail.setVisible(true);
        askEmail.setForeground(Color.WHITE);
        add(askEmail);

        askTeacherPassword.setBounds(100, 350, 150, 20);
        askTeacherPassword.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
        askTeacherPassword.setVisible(true);
        askTeacherPassword.setForeground(Color.WHITE);
        add(askTeacherPassword);


        // Input field for name
        nameField.setBounds(250, 150 - 10, 175, 40);
        nameField.setOpaque(false);
        nameField.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
        nameField.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 18));
        add(nameField);

        // Input field for studentID
        studentIDField.setBounds(250, 200 - 10, 175, 40);
        studentIDField.setOpaque(false);
        studentIDField.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
        studentIDField.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 18));
        add(studentIDField);

        teacherPasswordBox.setBounds(250, 350 - 10, 175, 40);
        teacherPasswordBox.setOpaque(false);
        teacherPasswordBox.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
        teacherPasswordBox.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 18));
        add(teacherPasswordBox);


        // Buttons

        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(0, 0, 0));
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        register.addActionListener(this);
        register.setBackground(Color.WHITE);
        register.setForeground(Color.BLACK);
        register.setBorderPainted(false);
        buttons.add(register);

        save.addActionListener(this);
        save.setBackground(Color.WHITE);
        save.setForeground(Color.BLACK);
        save.setBorderPainted(false);
        buttons.add(save);

        buttons.add(register);
        buttons.add(save);

        buttons.setBounds(0, getHeight()-100, getWidth(), 50);

        add(buttons);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit the program?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        JLabel faceFrame = new JLabel("Place your face within this box", SwingConstants.CENTER);
        faceFrame.setForeground(Color.WHITE);
        faceFrame.setFont(new Font("Tahoma", Font.BOLD + Font.ITALIC, 18));
        faceFrame.setBounds(50, 50, imageLbl.getWidth() - 100, imageLbl.getHeight() - 100);
        faceFrame.setBorder(BorderFactory.createLineBorder(Color.RED, 2, true));
        imageLbl.add(faceFrame);

        timer.start();
        setVisible(true);

    }

    /**
     * Saves a new image state
     * @throws IOException
     */
    @Override
    public void saveNewState() throws IOException {

        // Ensures the strings are not empty
        if (tempName.equals("")) {
            tempName = nameField.getText();
        }
        if (tempID.equals("")) {
            tempID = studentIDField.getText();
        }

        // Error checks to ensure user has inputted something, while also to prevent commas in name
        if (tempName.equals("Name") || tempID.contains(",")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid name", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            tempName = "";
            tempID = "";
        }

        // Ensures that the length of the student ID is 9
        else if (tempID.equals("Student ID") || tempID.length() != 9 || tempID.contains(",")) {
            JOptionPane.showMessageDialog(null, "Please enter valid ID", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            tempName = "";
            tempID = "";
        } else {

            // Checks that the password is the same by running the custom hashing algorithm
            if (!Encryption.encrypt(teacherPasswordBox.getText()).equals(Main.currentPassword)) {

                JOptionPane.showMessageDialog(null, "Please enter correct authorization password",
                        "Info", JOptionPane.INFORMATION_MESSAGE);

            }

            // Checks that the student does not already exist in the database
            else if (!studentExists() && nameField.isEditable()) {

                String filePathForFaceData = "res/studentFaceData/" + Main.currentTeacher + "/" + tempName + ".txt";
                String filePathForInformation = "res/studentInformation/" + Main.currentTeacher + ".txt";

                // Creates a file from the filename for data manipulation
                File faceDir = new File(filePathForFaceData);
                faceDir.createNewFile();

                // This keeps track of the last line

                Classify c = new Classify(image);

                // Only attempts to add a face if a face is detected

                if (c.isFace) {

                    // Keeps track of old data to be read from file
                    int prevHeadtoEye = 0;
                    int prevEyetoMouth = 0;
                    int prevHeadtoMouth = 0;
                    int prevHeadMouthRatio = 0;

                    // Creates the variables necessary to get all the details of the face, from the key features
                    int headtoEye = c.eyeY - c.headY;
                    int eyetoMouth = c.mouthY - c.eyeY;
                    int headtoMouth = c.mouthY - c.headY;
                    int headMouthRatio = (int) (Math.abs(c.headX2 - c.headX1) / Math.abs(c.mouthX1 - c.mouthX2));

                    String data = "";
                    Scanner readOldFaceData = new Scanner(new File(filePathForFaceData));

                    if (Main.countLines(filePathForFaceData) > 0) {

                        // Averaging with old data, with old data prioritized slightly more in order to prevent drastic changes to data

                        String[] line = readOldFaceData.nextLine().split(",");

                        // Stores all of the old parameters by parsing them to integers

                        prevHeadtoEye = Integer.parseInt(line[0]);
                        prevEyetoMouth = Integer.parseInt(line[1]);
                        prevHeadtoMouth = Integer.parseInt(line[2]);
                        prevHeadMouthRatio = Integer.parseInt(line[0]);

                        // A ratio which reasonably doesn't change too much

                        // This is the ratio of how much is allowed to be changed at once of the parameters (20%)

                        double changeRatio = 0.2;

                        // Modifying each of the past results to the updated ones

                        headtoEye = (int) (headtoEye * changeRatio + prevHeadtoEye * (1 - changeRatio));
                        eyetoMouth = (int) (eyetoMouth * changeRatio + prevEyetoMouth * (1 - changeRatio));
                        headtoMouth = (int) (headtoMouth * changeRatio + prevHeadtoMouth * (1 - changeRatio));
                        headMouthRatio = (int) (headMouthRatio * changeRatio + prevHeadMouthRatio * (1 - changeRatio));

                    }

                    // Formats the string in a way so that it can be read later in the future

                    data = String.format("%d,%d,%d,%d", headtoEye, eyetoMouth, headtoMouth, headMouthRatio);

                    // Stores the formatted string as a byte stream

                    byte[] bytes = data.getBytes();
                    FileOutputStream stream = new FileOutputStream(filePathForFaceData, false);
                    stream.write(bytes);
                    stream.close();

                    // Only allows this to show up the first time
                    if (nameField.isEditable()) {
                        JOptionPane.showMessageDialog(null, "Success! Account Created. \n" +
                                "You may now continue adding images for training.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }

                    // This prevents the user from editing this data on this form anymore, therefore only adding images for one user at a time
                    nameField.setEditable(false);
                    studentIDField.setEditable(false);

                    // Add new information about the student to the studentInformation file

                    // This string is formatted to store all information in a separated way, for the details
                    String formatted = String.format("\n%s,%s", nameField.getText(), studentIDField.getText());

                    // Stores the student information for future references

                    try {
                        Files.write(Paths.get(filePathForInformation), formatted.getBytes(), StandardOpenOption.APPEND);
                    }catch (IOException e) {
                        System.out.println(e.getMessage());
                    }


                } else {

                    // In case no face is detected, show this

                    JOptionPane.showMessageDialog(null, "No face detected, please try again...",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }

            } else {

                // Error message if the user already exists in the database

                JOptionPane.showMessageDialog(null, "Student already exists in database. " +
                        "Please try again...", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }

    // Checks if the student already exists in the database

    public boolean studentExists() throws FileNotFoundException {

        Scanner reader = new Scanner(new File("res/studentInformation/" + Main.currentTeacher + ".txt"));

        // Reads all of the lines of the file

        while (reader.hasNextLine()) {
            String[] line = reader.nextLine().split(",");
            if (line[0].equals(tempName)) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;

    }

    @Override
    public void modifyState() {
        // Reserved method for eigenfaces in future versions
    }

    /**
     * Action listener methods
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // This is used to change the image to make it look like a video based on triggers from the timer
        if (e.getSource() == timer) {

            // Gets new image from webcam

            image = webcam.getImage();

            //Sets the image to invisible

            imageLbl.setVisible(false);

            // FUNCTION: FLIP
            for (int i = 0; i < image.getWidth() / 2; i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    int tmp = image.getRGB(i, j);

                    image.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
                    image.setRGB(image.getWidth() - i - 1, j, tmp);

                }
            }

            //

            image = ImageAnalysis.cropImage(image, (image.getWidth() - image.getHeight()) / 2, 0, image.getHeight(), image.getHeight());
            image = ImageAnalysis.resize(image, imageHeight, imageWidth);

            ImageIcon imageIcon = new ImageIcon(image);
            imageIcon.getImage().flush();
            imageLbl.setIcon(imageIcon);
            imageLbl.setVisible(true);

        }

        // This enables the storage of data by calling the method saveNewState() when register is clicked

        if (e.getSource() == register) {

            try {
                saveNewState();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        // Disposes of the screen, returning to the old screen

        if (e.getSource() == save) {
            Main.newface.dispose();
            try {
                Main.intermediatePage = new IntermediatePage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            // System.exit(0); // TODO: REMOVE LATER
        }

    }

    @Override
    public int getTimerTime() {
        return 0;
    }

    @Override
    public void setTimerTime(int timerTime) {
        timer.setDelay(timerTime);
    }

    /**
     * Slider code (removed in current version for this screen)
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == slider) {
            delay = slider.getValue();

            // Restarts the timer with the new delay
            timer.stop();
            timer = new Timer(delay, this);
            timer.start();

            fpsCountLabel.setText("FPS: " + (int) (1000 / delay));
        }
    }
}
