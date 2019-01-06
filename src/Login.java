import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {

    JLabel usernamePrompt = new JLabel("Username: ");
    JLabel passwordPrompt = new JLabel("Password: ");
    JTextField usernameBox = new JTextField();
    JPasswordField passwordBox = new JPasswordField();

    public Login() {

        usernameBox.addActionListener(this);
        passwordBox.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
