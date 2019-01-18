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
import java.io.IOException;
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

    @Override
    public void saveNewState() throws FileNotFoundException {

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
        } else if (tempID.equals("Student ID") || tempID.length() != 9 || tempID.contains(",")) {
            JOptionPane.showMessageDialog(null, "Please enter valid ID", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            tempName = "";
            tempID = "";
        } else {

            if (!studentExists()) {

                String filePath = "res/trainingSet/" + Main.currentTeacher + "/" + tempName + "/";
                File dir = new File(filePath);
                dir.mkdir();
                try {
                    ImageIO.write(image, "PNG", new File(filePath + "image" + (Main.countFolders(filePath) + 1) + ".png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                // This prevents the user from editing this data on this form anymore, therefore only adding images for one user at a time
                nameField.setEditable(false);
                studentIDField.setEditable(false);

                JOptionPane.showMessageDialog(null, "Success! Account Created. \n" +
                        "You may now continue adding images for training.", "Info", JOptionPane.INFORMATION_MESSAGE);

            } else {

                // Error message if the user already exists in the database

                JOptionPane.showMessageDialog(null, "Student already exists in database. " +
                        "Please try again...", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }

    public boolean studentExists() throws FileNotFoundException {

        Scanner reader = new Scanner(new File("res/studentInformation"));

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
        // Reserved method for eigenfaces
    }

    /**
     * Action listener methods
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // This is used to change the image to make it look like a video based on triggers from the timer
        if (e.getSource() == timer) {

            image = webcam.getImage();

            imageLbl.setVisible(false);

            // FUNCTION: FLIP
            for (int i = 0; i < image.getWidth() / 2; i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    int tmp = image.getRGB(i, j);

                    image.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
                    image.setRGB(image.getWidth() - i - 1, j, tmp);

                }
            }

            image = ImageAnalysis.cropImage(image, (image.getWidth() - image.getHeight()) / 2, 0, image.getHeight(), image.getHeight());
            image = ImageAnalysis.resize(image, imageHeight, imageWidth);

            ImageIcon imageIcon = new ImageIcon(image);
            imageIcon.getImage().flush();
            imageLbl.setIcon(imageIcon);
            imageLbl.setVisible(true);

        }


        if (e.getSource() == register) {

            try {
                saveNewState();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

        }

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

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == slider) {
            delay = slider.getValue();

            timer.stop();
            timer = new Timer(delay, this);
            timer.start();

            fpsCountLabel.setText("FPS: " + (int) (1000 / delay));
        }
    }
}
