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
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

public class StudentList extends JFrame implements ActionListener {

    JComboBox classroomSelection;
    JTable dataTable;
    String[] columnNames = { "Index", "Student Name" };

    JLabel title;

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

        // Makes a list for adding all of the exercises available to it, using the exercise object ArrayList
        String[] classesList = new String[Main.allClasses.size()+1];
        classesList[0] = "-- Select --";
        for (int i = 0; i < Main.allClasses.size(); i++) {
            classesList[i+1] = Main.allClasses.get(i).getName();
        }

        // Makes sure the list is easily lexicographically comprehensible
        Arrays.sort(classesList);

        // Makes a combobox using the exerciseList, which has an actionListener
        // Instead of depending on yet another button
        classroomSelection = new JComboBox(classesList);
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

        // TODO: Add to this method for dataset creation

        if (classroomSelection.getSelectedItem().toString().equals("-- Select --")) {
            String[][] temp = {{"", ""}};
            return temp;
        }

        // Defines the path to the right place
        String path = "res/attendanceData/" + Main.currentTeacher + "/" + classroomSelection.getSelectedItem().toString();
        String[][] temp = new String[Main.countFolders(path)+1][3];

        int index = 0;

        // Independent from the global objects, since this functionality is built for viewing raw data
        File file = new File(path);
        String[] directoriesOfClasses = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        for (int i = 0; i < directoriesOfClasses.length; i++) {
            temp[i][0] = String.valueOf(i+1);
            temp[i][1] = directoriesOfClasses[i];
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
                title.setText("Students List for class " + Main.currentClass + " of teacher " + Main.currentTeacher);
            } else {
                title.setText("Students List");
            }

        }

    }

}
