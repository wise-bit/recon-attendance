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

public class TrainFace extends JFrame implements AdditionServices, ActionListener, WebcamClass, ChangeListener {

    // initial ratio is 450 by 350
    public static final int imageWidth = 300;
    public static final int imageHeight = 240;

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

        setSize(720, 480);
        setTitle("Scan your face");
        setLayout(new BorderLayout());

        webcam = Webcam.getDefault();
        webcam.open();

        title = new JLabel("Train four face further", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 24));
        add(title, BorderLayout.PAGE_START);

        // Main panel

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        JPanel webcamPanel = new JPanel(new GridLayout(2, 1));

        if (webcam.isOpen()) { //if web cam open
            image = webcam.getImage();
            imageLbl = new JLabel();
            // imageLbl.setSize(1024, 720);
            Image dimg = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            imageLbl.setIcon(new ImageIcon(dimg));

            imageLbl.setHorizontalAlignment(JLabel.CENTER);
            imageLbl.setVerticalAlignment(JLabel.CENTER);

            imageLbl.setBorder(b);

            webcamPanel.add(imageLbl);

        } else {
            JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam...",
                    "A plain message", JOptionPane.PLAIN_MESSAGE);
        }

        JPanel fpsPanel = new JPanel(new GridLayout(2, 1));
        fpsCountLabel = new JLabel("FPS: " + (int)(1000/delay), SwingConstants.CENTER);
        fpsPanel.add(fpsCountLabel);
        slider = new JSlider(10, 60);
        slider.addChangeListener(this);
        fpsPanel.add(slider, BorderLayout.SOUTH);

        webcamPanel.add(fpsPanel);
        mainPanel.add(webcamPanel);

        // Components

        JPanel componentsMegaHolder = new JPanel(new GridLayout(2, 1));

        JPanel components = new JPanel(new GridLayout(4, 1, 0, 20));

        JPanel placeHolderPanel1 = new JPanel(new BorderLayout(0,50));

        nameChoser = new JComboBox(Main.listFilesFolders("res/trainingSet/" + Main.currentTeacher));

        components.add(placeHolderPanel1);
        components.add(nameChoser);
        components.add(new JPanel());

        componentsMegaHolder.add(components);

        mainPanel.add(componentsMegaHolder);
        add(mainPanel, BorderLayout.CENTER);


        // Buttons

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));

        // TODO: Add buttons

        register.addActionListener(this);
        save.addActionListener(this);

        buttons.add(register);
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

            imageLbl.setVisible(false);
            image = webcam.getImage();

            // FUNCTION: FLIP

            for (int i=0;i<image.getWidth()/2;i++)
                for (int j=0;j<image.getHeight();j++)
                {
                    int tmp = image.getRGB(i, j);

                    image.setRGB(i, j, image.getRGB(image.getWidth()-i-1, j));
                    image.setRGB(image.getWidth()-i-1, j, tmp);

                }

            Image dimg = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            imageIcon.getImage().flush();
            imageLbl.setIcon(imageIcon);
            imageLbl.setVisible(true);

        }

        if (e.getSource() == register) {

            String filePath = "res/trainingSet/" + Main.currentTeacher + "/" + nameChoser.getSelectedItem().toString() + "/";
            File dir = new File(filePath);
            dir.mkdir();
            try {
                ImageIO.write(image, "PNG", new File(filePath + "image" + (Main.countFolders(filePath)+1) + ".png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        if (e.getSource() == save) {
            Main.newface.dispose();
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
