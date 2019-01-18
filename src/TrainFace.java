
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

    JLabel title;

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

    Border b = BorderFactory.createLineBorder(Color.WHITE, 5, true);

    public TrainFace() throws IOException {

        setSize(500, 700);

        setLocation((int) (Main.dim.getWidth()/2), 0);

        setTitle("Scan your face");
        setLayout(null);

        Color bg = new Color(194, 220, 25);

        // this.getContentPane().setBackground(bg);

        setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/background.jpeg")))));

        webcam = Webcam.getDefault();
        webcam.open();

        title = new JLabel("Train Your Face Further", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(1000, 50));
        title.setBounds(0, 10, getWidth(), 50);
        title.setVisible(true);
        add(title);
        // JPanel placeHolderPanel1 = new JPanel(new BorderLayout(0,50));

        JLabel askName = new JLabel("Name: ", SwingConstants.CENTER);
        askName.setFont(new Font("Verdana", Font.BOLD, 18));
        askName.setBounds(0, 70, getWidth(), 20);
        add(askName);

        nameChoser = new JComboBox(Main.listFilesFolders("res/trainingSet/" + Main.currentTeacher));
        nameChoser.setVisible(true);
        int nameChoserWidth = 200;
        int nameChoserHeight = 30;
        nameChoser.setBounds((getWidth() - nameChoserWidth)/2, 100, nameChoserWidth, nameChoserHeight);
        add(nameChoser);


        if (webcam.isOpen()) { //if web cam open
            image = webcam.getImage();
            imageLbl = new JLabel();
            // imageLbl.setSize(1024, 720);
            Image dimg = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            imageLbl.setIcon(new ImageIcon(dimg));

//            imageLbl.setHorizontalAlignment(JLabel.CENTER);
//            imageLbl.setVerticalAlignment(JLabel.CENTER);

            imageLbl.setBorder(b);
            imageLbl.setBounds((getWidth() - 400)/2, 160, 400, 400);
            add(imageLbl);

        } else {
            JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam...",
                    "Message", JOptionPane.PLAIN_MESSAGE);
        }

        fpsCountLabel = new JLabel("FPS: " + (int)(1000/delay), SwingConstants.CENTER);
        fpsCountLabel.setBounds(0, 560, getWidth(), 20);
        fpsCountLabel.setVisible(true);
        add(fpsCountLabel);

        slider = new JSlider(10, 60);
        slider.setOpaque(false); //?
        slider.addChangeListener(this);
        slider.setBounds(10, 580, getWidth()-10, 50);
        add(slider);

        // Buttons
        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(0, 0, 0));
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

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

        buttons.setBounds(0, 620, getWidth(), 50);

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
            image = ImageAnalysis.resize(image, 400, 400);

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
