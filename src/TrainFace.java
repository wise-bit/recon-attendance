
// Import from https://github.com/sarxos/webcam-capture
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
import java.io.IOException;
import java.util.concurrent.Flow;

public class TrainFace extends JFrame implements AdditionServices, ActionListener, WebcamClass, ChangeListener {

    // initial ratio is 450 by 350
    public static final int imageWidth = 450;
    public static final int imageHeight = 350;

    JLabel title = new JLabel("Add more phases of faces");

    JLabel askName = new JLabel("Name: ");
    JComboBox nameBox = new JComboBox();

    JButton deleteAll = new JButton("Start from scratch");
    JButton register = new JButton("Take Snapshot");
    JButton save = new JButton("Save");

    Webcam webcam;
    BufferedImage image;
    JLabel imageLbl = new JLabel();

    JLabel fpsCountLabel;
    JSlider slider;
    int delay = 30;
    private Timer timer=new Timer(delay, this);

    JComboBox nameChoser;

    Border b = BorderFactory.createLineBorder(Color.BLACK, 5, true);

    public TrainFace() {

        setSize(1000, 700);
        setTitle("Scan your face");
        setLayout(new BorderLayout());

        Color bg = new Color(194, 220, 25);

        this.getContentPane().setBackground(bg);

        webcam = Webcam.getDefault();
        webcam.open();

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(135, 156, 21));

        title = new JLabel("Train Your Face Further", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(1000, 50));
        titlePanel.add(title);

        add(titlePanel, BorderLayout.PAGE_START);


        // Main panel

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBackground(bg);

        JPanel webcamPanel = new JPanel();
        webcamPanel.setBackground(bg);

        // JPanel placeHolderPanel1 = new JPanel(new BorderLayout(0,50));

        mainPanel.add(Box.createRigidArea(new Dimension(5,50)));

        JPanel askforname = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        askforname.setBackground(bg);

        JLabel askName = new JLabel("Name: ");
        askName.setFont(new Font("Ariel", Font.ITALIC + Font.BOLD, 18));
        askforname.add(askName);

        nameChoser = new JComboBox(Main.listFilesFolders("res/trainingSet/" + Main.currentTeacher));
        nameChoser.setVisible(true);
        int nameChoserWidth = 200;
        int nameChoserHeight = 30;
        nameChoser.setPreferredSize(new Dimension(nameChoserWidth, nameChoserHeight));
        nameChoser.setMinimumSize(new Dimension(nameChoserWidth, nameChoserHeight));
        nameChoser.setMaximumSize(new Dimension(nameChoserWidth, nameChoserHeight));
        askforname.add(nameChoser);

        // Components - name chooser combo box
        mainPanel.add(askforname);

        mainPanel.add(Box.createRigidArea(new Dimension(5,30)));

        if (webcam.isOpen()) { //if web cam open
            image = webcam.getImage();
            imageLbl = new JLabel();
            // imageLbl.setSize(1024, 720);
            Image dimg = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            imageLbl.setIcon(new ImageIcon(dimg));

//            imageLbl.setHorizontalAlignment(JLabel.CENTER);
//            imageLbl.setVerticalAlignment(JLabel.CENTER);

            imageLbl.setBorder(b);
            webcamPanel.add(imageLbl);

        } else {
            JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam...",
                    "Message", JOptionPane.PLAIN_MESSAGE);
        }

        // webcamPanel.add(fpsPanel);
        mainPanel.add(webcamPanel);

        JPanel fpsPanel = new JPanel(new GridLayout(2, 1));
        fpsPanel.setBackground(new Color(226, 255, 28));

        fpsCountLabel = new JLabel("FPS: " + (int)(1000/delay), SwingConstants.CENTER);
        fpsPanel.add(fpsCountLabel);
        slider = new JSlider(10, 60);
        slider.setBackground(new Color(226, 255, 28));
        slider.addChangeListener(this);
        fpsPanel.add(slider, BorderLayout.SOUTH);

        mainPanel.add(fpsPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(0, 0, 0));
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));

        // TODO: Add buttons

        register.addActionListener(this);
        register.setBackground(Color.WHITE);
        register.setForeground(Color.BLACK);
        register.setBorderPainted(false);
        buttons.add(register);

        save.addActionListener(this);
        save.setBackground(Color.WHITE);
        save.setForeground(Color.BLACK);
        // save.setBorderPainted(false);
        buttons.add(save);

        add(buttons, BorderLayout.PAGE_END);

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

        timer.start();
        this.setVisible(true);

    }

    @Override
    public void saveNewState() {

    }

    @Override
    public void modifyState() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
            image = ImageAnalysis.resize(image, 200, 200);

            // Image dimg = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(image);
            imageIcon.getImage().flush();
            imageLbl.setIcon(imageIcon);
            imageLbl.setVisible(true);

        }

        if (e.getSource() == register) {

            String filePath = "res/trainingSet/" + Main.currentTeacher + "/" + nameChoser.getSelectedItem().toString() + "/";
            File dir = new File(filePath);
            dir.mkdir();
            try {
                ImageIO.write(ImageAnalysis.trainingReady(image), "PNG", new File(filePath + "image" + (Main.countFolders(filePath)+1) + ".png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        if (e.getSource() == save) {
            Main.training.dispose();
            System.exit(0); // TODO: REMOVE LATER
        }

    }



    @Override
    public int getTimerTime() {
        return 0;
    }

    @Override
    public void setTimerTime(int timerTime) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == slider) {
            delay = slider.getValue();

            timer.stop();
            timer = new Timer(delay, this);
            timer.start();

            fpsCountLabel.setText("FPS: " + (int)(1000/delay));
        }
    }
}
