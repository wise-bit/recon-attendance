/**
 *
 * This method displays a table filled with raw data for each individual user
 * @author Satrajit
 *
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class StudentList extends JFrame implements ActionListener, MouseListener {

    JTable dataTable;
    String[] columnNames = { "Index", "Student Name" };

    JLabel title;

    HashMap<String, String> students = new HashMap<String, String>();

    /**
     * Constructor method
     * @throws IOException
     */

    public StudentList() throws IOException {

        // Sets the layout for the program
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 243, 160));
        setSize(550, 450);
        setTitle("Students List");

        // Adds the title, which must strike out from the other features
        title = new JLabel("Students List", SwingConstants.CENTER);
        title.setFont(new Font(title.getFont().toString(), Font.BOLD,18));
        add(title, BorderLayout.PAGE_START);

        // Initializes the JTable using a method described later
        dataTable = new JTable(makeData(), columnNames);
        JScrollPane scrollableTable = new JScrollPane(dataTable);
        dataTable.setDefaultEditor(Object.class, null);

        dataTable.addMouseListener(this);

        // Makes a panel, making placement easier
        JPanel comps = new JPanel();
        // Uses BoxLayout
        comps.setLayout(new BoxLayout(comps, BoxLayout.PAGE_AXIS));

        // Adds all components
        comps.add(scrollableTable);
        add(comps);

        // Not resizable, but visible
        setResizable(false);
        setVisible(true);

    }

    /**
     *
     * @return data which is to be used by the table
     * @throws IOException
     */


    public String[][] makeData() throws IOException {

        // Defines the path to the right place
        String path = "res/studentInformation/" + Main.currentTeacher + ".txt";
        String[][] temp = new String[Main.countLines(path)+1][3];

        int index = 0;

        // Independent from the global objects, since this functionality is built for viewing raw data
        // (This same code was used in Main class as well)
        File file = new File(path);
        String[] people = new String[Main.countLines(path)];

        // Initializes scanner for reading the student list file
        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            // Store all attributes in array
            String[] attribs = reader.nextLine().split(",");

            // Assign it to the index of the previously defined array
            people[index] = attribs[0];

            // Adds student info to hashmap for future reference as well
            students.put(people[index], attribs[1]);
            index++;
        }

        // Reassign all imported data into the table
        for (int i = 0; i < people.length; i++) {
            temp[i][0] = String.valueOf(i+1);
            temp[i][1] = people[i];
        }

        return temp;

    }

    /**
     * Action performed
     * @param e
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        // Saved for extra features in the future
    }

    public void mouseClicked(java.awt.event.MouseEvent e) {
        int row = dataTable.rowAtPoint(e.getPoint());
        int col = dataTable.columnAtPoint(e.getPoint());
        if (col == 1)
            JOptionPane.showMessageDialog(null,"Student ID: " + students.get(dataTable.getValueAt(row, col).toString()));
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
