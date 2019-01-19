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

public class AttendanceHistory extends JFrame implements ActionListener {

    JComboBox classroomSelection;
    JTable dataTable;
    String[] columnNames = { "Index", "Student Name" };

    JLabel title;

    HashMap<String, String> students = new HashMap<String, String>();

    /**
     * Constructor method
     * @throws IOException
     */

    public AttendanceHistory() throws IOException {

        // Sets the layout for the program
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 243, 160));
        setSize(550, 450);
        setTitle("Attendance History");

        // Adds the title, which must strike out from the other features
        title = new JLabel("Students List", SwingConstants.CENTER);
        title.setFont(new Font(title.getFont().toString(), Font.BOLD,18));
        add(title, BorderLayout.PAGE_START);

        String[] classesList = Main.listFiles("res/attendanceData/" + Main.currentTeacher + "/" + Main.currentClass + "/");

        for (int i = 0; i < classesList.length; i++) {
            classesList[i] = classesList[i].substring(0, classesList[i].length()-4);
        }

        // Makes sure the list is easily lexicographically comprehensible
        Arrays.sort(classesList);

        // Makes a combobox using the exerciseList, which has an actionListener
        // Instead of depending on yet another button
        classroomSelection = new JComboBox(classesList);
        classroomSelection.setSelectedItem(classesList.length-1);
        System.out.println(classroomSelection.getSelectedItem());
        classroomSelection.addActionListener(this);

        // Initializes the JTable using a method described later
        dataTable = new JTable(makeData(), columnNames);
        JScrollPane scrollableTable = new JScrollPane(dataTable);
        dataTable.setDefaultEditor(Object.class, null);

        // Makes a panel, making placement easier
        JPanel comps = new JPanel();
        // Uses BoxLayout
        comps.setLayout(new BoxLayout(comps, BoxLayout.PAGE_AXIS));

        // Adds all components
        comps.add(classroomSelection);
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

        // If user selects blank option, return blank table
        if (classroomSelection.getSelectedItem().toString().equals("-- Select --")) {
            String[][] temp = {{"", ""}};
            return temp;
        }

        // Defines the path to the right place
        String path = "res/attendanceData/" + Main.currentTeacher + "/" + Main.currentClass + "/" + classroomSelection.getSelectedItem().toString() + ".txt";
        String[][] temp = new String[Main.countLines(path)+1][3];

        int index = 0;

        // Independent from the global objects, since this functionality is built for viewing raw data
        // (This same code was used in Main class as well)
        File file = new File(path);
        String[] people = new String[Main.countLines(path)];

        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            // Store all attributes in array
            String attribs = reader.nextLine();

            // Assign it to the index of the previously defined array
            people[index] = attribs;

            // increments index
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

        // If the classroomSelection undergoes any changes
        if (e.getSource() == classroomSelection) {

            // Makes a new model, which is able to modify the data for the table
            DefaultTableModel model = null;

            // Makes a new model for the table
            try {
                model = new DefaultTableModel(makeData(), columnNames);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            // Puts the model in the table
            dataTable.setModel(model);

            if (!classroomSelection.getSelectedItem().toString().equals("-- Select --")) {
                Main.currentClass = classroomSelection.getSelectedItem().toString();
                title.setText("Attendance of " + Main.currentClass + " of teacher " + Main.currentTeacher);
            } else {
                title.setText("Students List");
            }

        }

    }

}
