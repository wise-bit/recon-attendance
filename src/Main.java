/**
 * Author: Satrajit
 */

/**
 *
 * @author: Satrajit Chatterjee
 *
 ********************************************
 * Reconattendance - ICS4U1 - final project *
 ********************************************
 *
 * Name:
 *      Satrajit Chatterjee
 *
 * Date:
 *      January 18, 2019
 *
 * Course Code:
 *      ICS4U1 - Period 5 - Semester 1
 *
 * Title:
 *      Reconattendance (A combination of reconnaissance and attendance)
 *
 * Description:
 *
 *
 * Features:
 *
 *
 * Major Skills:
 *
 *
 * Area of concern:
 *      (i) The program uses a Webcam library, which was impossible to be coded natively in Java, hence it is based on C
 *      code to access hardware. Although tested thoroughly, the webcam interface may fail in specific situations such
 *      as unsupported webcams, which is still fixable through minor tweakings
 *
 *      (ii) The algorithm used in this program is built from the ground up, from scratch. Therefore, it has very minimal
 *      training concerning machine learning, and the mathematical capabilities of the models are incomparable to other projects
 *      such as OpenCV. Therefore, the results may often be false.
 *
 *
 *
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;
import java.awt.GraphicsEnvironment;

/**
 * Main class of the program, which initializes all of the elements, and then calls the login screen.
 *
 * This class also contains important methods used throughout the project such as the method to count the lines of a
 *      program or the method to count the number of folders in another folder.
 */
public class Main {

    public static Login login;
    public static Scan scan;
    public static TrainFace training;
    public static AttendanceHistory history;
    public static NewFace newface;
    public static TableData tabledata;
    public static StudentList studentlist;
    public static Classify classify;
    public static IntermediatePage intermediatePage;
    public static AddClass addClass;
    public static ManualEntry manualEntry;

    public static ClassifyTest classifyTest;

    // Default strings assigned to prevent any form of NullPointerException
    public static String currentTeacher  = "crashProof";
    public static String currentPassword  = "crashProof";
    public static String currentClass  = "crashProof";

    public static ArrayList<Classroom> allClasses;
    public static ArrayList<Teacher> teacherAccounts;

    public static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    public static String lastUsername = "";
    public static String lastPassword = "";

    public static void main(String[] args) throws IOException {

        init();

        // listFonts();

        // Testing purposes
        //trainTestDataset();

        login = new Login();

        // training = new TrainFace(); // move this to login sreen
        // newface = new NewFace();

    }

    /**
     * Initializes the elements of the program such as the list of teachers
     * @throws IOException
     */
    public static void init() throws IOException {

        // Initializing allClasses
        allClasses = new ArrayList<Classroom>();

        // Add classes to arraylist based on which files exist in the folder
        // where each folder is assigned for one class
        File file = new File("res/attendanceData/teacher1/");
        String[] directoriesOfClasses = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        for (String filename : directoriesOfClasses) {
            allClasses.add(new Classroom(filename, countFolders("res/attendanceData/teacher1/" + filename)));
        }


        // Adding all teacher accounts to memory

        teacherAccounts = new ArrayList<Teacher>();

        File accountsFile = new File("res/accounts.txt");
        Scanner fileReader = new Scanner(accountsFile);

        while (fileReader.hasNextLine()) {
            String[] line = fileReader.nextLine().split(",");
            teacherAccounts.add(new Teacher(line[0], line[1]));
        }

    }

    /**
     * Lists all fles in a folder
     *
     * @param path
     * @return namesArray
     */
    public static String[] listFiles(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile())
                names.add(listOfFiles[i].getName());
        }

        String[] namesArray = new String[names.size()];
        for (int i = 0; i < names.size(); i++) {
            namesArray[i] = names.get(i);
        }

        return namesArray;
    }

    /**
     * @param path
     * @return directories
     */
    public static String[] listFilesFolders(String path) {
        File file = new File(path);

        // Lambda expression source: http://www.javased.com/index.php?api=java.io.FilenameFilter
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return directories;
    }

    /**
     * Counts lines in a txt/csv file
     *
     * @param filename
     * @return lines
     * @throws IOException
     */
    public static int countLines(String filename) throws IOException {

        int lines = 0;

        Scanner scanner = new Scanner(new File(filename));

        while (scanner.hasNextLine()) {
            scanner.nextLine();
            lines++;
        }

        return lines;

    }

    /**
     * Counts folders in a folder
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static int countFolders(String path) throws IOException {

        long count;

        // Stream code: https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
        try (Stream<Path> files = Files.list(Paths.get(path))) {
            count = files.count();
        }

        return (int) count;

    }

    /**
     *Used to refresh any jframe
     *
     * @param frame
     */
    public void refresh(JFrame frame) {
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    /**
     * Used to test by using images from downloaded dataset
     *
     * @throws IOException
     */
    public static void trainTestDataset() throws IOException {
        String imagesPath = "res/trainingSet/teacher1/learner2/";
        String[] imagesToCheck = listFiles(imagesPath);
        for (String image : imagesToCheck) {
            BufferedImage img = ImageIO.read(new File("res/trainingSet/teacher1/learner2/" + image));

            String filePath = "res/trainingSet/teacher1/learner2/glitchartstorage/";
            ImageIO.write(ImageAnalysis.furtherModifcation(img), "PNG", new File(filePath + "X" + image.substring(0, image.length()-4) + ".png"));

            classifyTest = new ClassifyTest(ImageAnalysis.trainingReady(img), image);
        }
    }

    /**
     * Lists fonts
     */
    public static void listFonts() {
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for ( int i = 0; i < fonts.length; i++ ) {
            System.out.println(fonts[i]);
        }
    }


}
