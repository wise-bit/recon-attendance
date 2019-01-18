import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;

public class FaceScanPage extends JFrame implements ActionListener {

    private int year = new Date().getYear() + 1900;
    private int month = new Date().getMonth();
    private int day = new Date().getDate();

    JButton save = new JButton("Save");
    JButton manualEntry = new JButton(("Manual Entry"));

    public FaceScanPage() {

        String date = String.format("%d-%d-%d", year, month, day);

        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(0, 0, 0));
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        save.addActionListener(this);
        save.setBackground(Color.WHITE);
        save.setForeground(Color.BLACK);
        save.setBorderPainted(false);
        buttons.add(save);

        // buttons.add(Box.createRigidArea(new Dimension(50,0)));

        manualEntry.addActionListener(this);
        manualEntry.setBackground(Color.WHITE);
        manualEntry.setForeground(Color.BLACK);
        manualEntry.setBorderPainted(false);
        buttons.add(manualEntry);

        buttons.setBounds(0, getHeight()-100, getWidth(), 50);

        add(buttons);

    }

    @Override
    public void actionPerformed(ActionEvent e) {



        if (e.getSource() == manualEntry) {
            Main.newface.dispose();
            try {
                Main.manualEntry = new ManualEntry(true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
