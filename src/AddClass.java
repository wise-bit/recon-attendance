import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class AddClass extends JFrame implements ActionListener {

    JTextField className;
    JButton enter;
            
    public AddClass() throws IOException {

        setSize(350, 400);
        setLocation((int)Main.dim.getWidth()/2-500, (int)Main.dim.getHeight()/2-250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Add new face");

        setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("res/background.jpeg")))));

        // getContentPane().setBackground(Color.YELLOW);

        JLabel title = new JLabel("Add new Class", SwingConstants.CENTER);
        title.setBounds(0, 20, 1000, 50);
        title.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        title.setVisible(true);
        title.setForeground(Color.YELLOW);
        add(title);

        JLabel askClassName = new JLabel("Class name: ", SwingConstants.CENTER);
        askClassName.setBounds(100, 100, 150, 20);
        askClassName.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 18));
        askClassName.setVisible(true);
        askClassName.setForeground(Color.WHITE);
        add(askClassName);

        className = new JTextField();
        className.setBounds(100, 130, 150, 40);
        className.setOpaque(false);
        className.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.WHITE));
        className.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 18));
        add(className);
        
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

            if (!(className.getText().equals("") || className.getText().contains(","))) {

                new File("res/attendanceData/" + Main.currentTeacher + "/" + className.getText()).mkdir();
                Main.addClass.dispose();
                Main.intermediatePage.dispose();
                try {
                    Main.intermediatePage = new IntermediatePage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Invalid class name, please try again!",
                        "Message", JOptionPane.PLAIN_MESSAGE);
            }

        }
    }
}
