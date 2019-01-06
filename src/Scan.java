import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Scan extends JFrame implements WebcamClass, ActionListener {

    public static final int imageWidth = 450;
    public static final int imageHeight = 350;

    private JLabel title;
//    private JPanel webcamInterface;
//    private JButton enter = new JButton("Let me in!");
//    private JButton train = new JButton("Train");
//    private JButton addFace = new JButton("Add face");
//    private JButton close = new JButton("Close");
    private JButton analyze = new JButton("Analyze");

    Webcam webcam;
    BufferedImage image;
    JLabel imageLbl = new JLabel();;

    private Timer timer=new Timer(10, this);

    public Scan() throws IOException {

        setSize(720, 480);

        setTitle("Scan your face");
        setLayout(new BorderLayout());

        webcam = Webcam.getDefault();
        webcam.open();

        title = new JLabel("Face Recognition", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.ITALIC, 24));
        add(title, BorderLayout.PAGE_START);

        if (webcam.isOpen()) { //if web cam open
            image = webcam.getImage();
            imageLbl = new JLabel();
            // imageLbl.setSize(1024, 720);
            Image dimg = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            imageLbl.setIcon(new ImageIcon(dimg));

            imageLbl.setHorizontalAlignment(JLabel.CENTER);
            imageLbl.setVerticalAlignment(JLabel.CENTER);

            add(imageLbl, BorderLayout.CENTER);


        } else {
            JOptionPane.showMessageDialog(this, "Uh oh, cannot find webcam...",
                    "A plain message", JOptionPane.PLAIN_MESSAGE);
        }

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));

        analyze.addActionListener(this);
        analyze.setForeground(Color.WHITE);
        analyze.setBackground(Color.BLACK);
        analyze.setBorderPainted(false);
        buttons.add(analyze);

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
    public int getTimerTime() {
        return 0;
    }

    @Override
    public void setTimerTime(int timerTime) {

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

        if (e.getSource() == analyze) {

        }

    }
}

