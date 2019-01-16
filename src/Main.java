import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class Main {

    public static Login login;
    public static Scan scan;
    public static TrainFace training;
    public static AttendanceHistory history;
    public static NewFace newface;
    public static TableData tabledata;
    public static StudentList studentlist;
    public static Classify classify;

    public static String currentTeacher;
    public static String currentClass;

    public static ArrayList<Classroom> allClasses;

    public static void main(String[] args) throws IOException {

        init();

        // login = new Login();
        // training = new TrainFace();


        // Testing purposes

        String imagesPath = "res/trainingSet/teacher1/learner2/";
        String[] imagesToCheck = listFiles(imagesPath);
        for (String image : imagesToCheck) {
            BufferedImage img = ImageIO.read(new File("res/trainingSet/teacher1/learner2/" + image));
            classify = new Classify(ImageAnalysis.trainingReady(img), image);
        }

    }

    public static void init() throws IOException {

        // Sample data to be removed later (maybe keep for failsafe?)
        currentTeacher = "teacher1";
        currentClass = "ICS4U1";

        // Initializing allClasses

        allClasses = new ArrayList<Classroom>();

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

    }

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

    public static String[] listFilesFolders(String path) {
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return directories;
    }

    // Counts lines in a txt/csv file
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    // Counts folders in a folder
    public static int countFolders(String path) throws IOException {

        long count;

        try (Stream<Path> files = Files.list(Paths.get(path))) {
            count = files.count();
        }

        return (int) count;

    }

    // Used to refresh any jframe
    public void refresh(JFrame frame) {
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

}
