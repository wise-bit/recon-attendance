/**
 * This is one of the most crucial classes of the program, since it scans the face and identifies it using algorithms
 * defined elsewhere in other classes.
 */

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Scanner;

/**
 * @extends JFrame
 * @implements ActionListener
 */
public class FaceScanPage extends JFrame implements ActionListener {

    // Stores informaiton for making a date, when storing attendance data for a specific date
    private int year = new Date().getYear() + 1900;
    private int month = new Date().getMonth();
    private int day = new Date().getDate();
    private String date;

    // Keeps track of further components such as the title and the webcam
    JLabel title;
    Webcam webcam;
    BufferedImage image;
    JLabel imageLbl = new JLabel();

    // Specifies the width and height of the image
    public static final int imageWidth = 400;
    public static final int imageHeight = 400;

    // Delay of timer, and timer initializaiton
    int delay = 30;
    private Timer timer = new Timer(delay, this);

    Border b = BorderFactory.createLineBorder(Color.WHITE, 5, true);

    JButton enter = new JButton("Let me in!");
    JButton manualEntry = new JButton(("Manual Entry"));
    JButton back = new JButton("Back");

    public FaceScanPage() throws IOException {

        date = String.format("%d-%d-%d", year, month, day);


        setSize(500, 700);

        setLocation((int) (Main.dim.getWidth()/2), 0);

        setTitle("Scan your face");
        setLayout(null);

        setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/background.jpeg")))));

        webcam = Webcam.getDefault();
        webcam.open();

        title = new JLabel("Scan Your Face", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(1000, 50));
        title.setBounds(0, 60, getWidth(), 50);
        title.setVisible(true);
        add(title);
        // JPanel placeHolderPanel1 = new JPanel(new BorderLayout(0,50));

        if (webcam.isOpen()) { //if web cam open
            image = webcam.getImage();
            imageLbl = new JLabel();
            Image dimg = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            imageLbl.setIcon(new ImageIcon(dimg));
            imageLbl.setBorder(b);
            imageLbl.setBounds((getWidth() - 400)/2, 160, 400, 400);
            add(imageLbl);

        } else {
            JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam...",
                    "Message", JOptionPane.PLAIN_MESSAGE);
        }


        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(0, 0, 0));
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        // Adds the enter button for checking face validity
        enter.addActionListener(this);
        enter.setBackground(Color.WHITE);
        enter.setForeground(Color.BLACK);
        enter.setBorderPainted(false);
        buttons.add(enter);

        // buttons.add(Box.createRigidArea(new Dimension(50,0)));

        manualEntry.addActionListener(this);
        manualEntry.setBackground(Color.WHITE);
        manualEntry.setForeground(Color.BLACK);
        manualEntry.setBorderPainted(false);
        buttons.add(manualEntry);

        back.addActionListener(this);
        back.setBackground(Color.WHITE);
        back.setForeground(Color.BLACK);
        back.setBorderPainted(false);
        buttons.add(back);

        buttons.setBounds(0, getHeight()-100, getWidth(), 50);

        // Adds buttons
        add(buttons);

        // Starts timer
        timer.start();
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

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timer) {

            // Gets new image from webcam

            image = webcam.getImage();

            //Sets the image to invisible

            imageLbl.setVisible(false);

            // FUNCTION: FLIP
            for (int i = 0; i < image.getWidth() / 2; i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    int tmp = image.getRGB(i, j);

                    // FLIPS pixels over the y-axis, making the video output more comprehensible to the human brain
                    image.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
                    image.setRGB(image.getWidth() - i - 1, j, tmp);

                }
            }

            // Crops the image to the right size

            image = ImageAnalysis.cropImage(image, (image.getWidth() - image.getHeight()) / 2, 0, image.getHeight(), image.getHeight());
            image = ImageAnalysis.resize(image, imageHeight, imageWidth);

            // Creates new image icon for the image

            ImageIcon imageIcon = new ImageIcon(image);
            imageIcon.getImage().flush();
            imageLbl.setIcon(imageIcon);
            imageLbl.setVisible(true);

        }

        if (e.getSource() == manualEntry) {
            Main.faceScanPage.dispose();
            try {
                Main.manualEntry = new ManualEntry(true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (e.getSource() == enter) {

            try {

                // Creates new instance of classifier

                Classify c = new Classify(image);

                // Only attempts to add a face if a face is detected

                if (c.isFace) {

                    int headtoEye = c.eyeY - c.headY;
                    int eyetoMouth = c.mouthY - c.eyeY;
                    int headtoMouth = c.mouthY - c.headY;
                    int headMouthRatio = (int) (Math.abs(c.headX2 - c.headX1) / Math.abs(c.mouthX1 - c.mouthX2));

                    Face f = new Face(headtoEye, eyetoMouth, headtoMouth, headMouthRatio);

                    String identity = "";
                    int differences = Integer.MAX_VALUE;

                    String[] studentsList = Main.listFiles("res/studentFaceData/" + Main.currentTeacher);

                    // Loop through all of the files of the specific teacher
                    for (String name : studentsList) {

                        String filePathForFaceData = "res/studentFaceData/" + Main.currentTeacher + "/" + name;

                        // Extract all attributes from the file
                        String[] attribs = new Scanner(new File(filePathForFaceData)).nextLine().split(",");

                        // Makes new face object using these attributes
                        Face f2 = new Face(Integer.parseInt(attribs[0]),
                                Integer.parseInt(attribs[1]),
                                Integer.parseInt(attribs[2]),
                                Integer.parseInt(attribs[3]));

                        // Generates a score based on the comparison
                        // The lower the score, the better
                        int score = f2.compareTo(f);

                        // Compares score with past results to find closest face
                        if (score < differences) {
                            differences = score;
                            identity = name;
                        }

                    }

                    // Check if an face was recognized
                    if (identity.equals("")) {
                        JOptionPane.showMessageDialog(null, "Face not recognized... please try again!",
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Show welcome message
                        JOptionPane.showMessageDialog(null, "Welcome, " + identity.substring(0, identity.length()-4),
                                "Info", JOptionPane.INFORMATION_MESSAGE);

                        // Write to the attendance folder
                        String pathToAttendanceFile = "res/attendanceData/" + Main.currentTeacher + "/" + Main.currentClass + "/" + date + ".txt";
                        File attendanceFile = new File(pathToAttendanceFile);

                        // Checks if the person was already admitted to the class for that day or not
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

                    }

                } else {

                    // In case no face is detected, show this

                    JOptionPane.showMessageDialog(null, "No face detected, please try again...",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception e0) {
                System.out.println(e0.getMessage());
            }

        }

        // To exit back to the previous page
        if (e.getSource() == back) {
            Main.faceScanPage.dispose();
        }

    }
}

