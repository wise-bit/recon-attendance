import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Login extends JFrame implements ActionListener {

    JLabel usernamePrompt = new JLabel("Username: ");
    JLabel passwordPrompt = new JLabel("Password: ");
    JTextField usernameBox = new JTextField();
    JPasswordField passwordBox = new JPasswordField();

    JButton login = new JButton("Login");

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


        // Adds Button



        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
