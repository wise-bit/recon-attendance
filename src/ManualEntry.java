/**
 * This class is for manual entry of data, if facial recognition fails to work
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ManualEntry extends JFrame implements ActionListener {

    JComboBox name;
    JButton enter;

    boolean addingNew;

    /**
     * @param addingNew
     * @throws IOException
     */
    public ManualEntry(boolean addingNew) throws IOException {

        this.addingNew = addingNew;

        setSize(350, 400);
        setLocation((int)Main.dim.getWidth()/2-500, (int)Main.dim.getHeight()/2-250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Enter your attendance manually");

        setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/background.jpeg")))));

        // getContentPane().setBackground(Color.YELLOW);

        JLabel title = new JLabel("Manual Attendance", SwingConstants.CENTER);
        title.setBounds(0, 20, 1000, 50);
        title.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        title.setVisible(true);
        title.setForeground(Color.YELLOW);
        add(title);

        JLabel askClassName = new JLabel("Name: ");
        askClassName.setBounds(100, 100, 150, 20);
        askClassName.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
        askClassName.setVisible(true);
        askClassName.setForeground(Color.WHITE);
        add(askClassName);

        name = new JComboBox();
        name.setBounds(100, 130, 150, 40);
        name.setOpaque(false);
        name.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
        name.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 18));
        add(name);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enter) {

            File f = new File("res/attendanceData/" + Main.currentTeacher + "/" + name.getSelectedItem().toString() + ".txt");

            Main.addClass.dispose();
            Main.intermediatePage.dispose();
            try {
                Main.intermediatePage = new IntermediatePage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
