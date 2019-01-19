/**
 * This class is not part of the application, but is used to test training data
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ClassifyTest {

    /*

    Facial imagery dataset source:
    Mahmoud Afifi and Abdelrahman Abdelhamed, "AFIF4: Deep gender classification based on an AdaBoost-based
    fusion of isolated facial features and foggy faces". arXiv:1706.04277, arXiv 2017.

     */

    public BufferedImage image;

    public BufferedImage pixelatedImage;

    public int headLength;
    public int headX1;
    public int headX2;
    public int headY;

    public int eyeLength;
    public int eyeX1;
    public int eyeX2;
    public int eyeY;

    public int mouthLength;
    public int mouthX1;
    public int mouthX2;
    public int mouthY;

    public String imageName;

    public ClassifyTest(BufferedImage img, String name) throws IOException {

        setImage(img);
        setImageName(name);

        assignHeadHaar();
        assignEyebrowsHaar();
        assignMouthHaar();

        String filePath = "res/trainingSet/teacher1/learner2/todelete/";
        ImageIO.write(pixelatedImage, "PNG", new File(filePath + "X" + imageName.substring(0, imageName.length() - 4) + ".png"));
        String filePath2 = "res/trainingSet/teacher1/learner2/todelete2/";
        ImageIO.write(image, "PNG", new File(filePath2 + "X" + imageName.substring(0, imageName.length() - 4) + ".png"));

    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    // gets image
    public BufferedImage getImage() {
        return image;
    }

    // Sets the image (potentially allows error-checking)
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    // FIND HEAD
    public void assignHeadHaar() throws IOException {

        int top_length = 0;
        int[] coords = new int[3];

        for (int y = 0; y < image.getHeight() / Classifiers.pixelAccuracy - 1; y++) {
            for (int x = 0; x < image.getWidth() / Classifiers.pixelAccuracy - 1; x++) {

                int currentLength = 0;

                while (true) {

                    // Checks for total area score
                    int sector1Score = 0;
                    int sector2Score = 0;

                    for (int j = 0; j < Classifiers.pixelAccuracy; j++) {
                        for (int i = 0; i < Classifiers.pixelAccuracy; i++) {
                            if ((x + currentLength) * Classifiers.pixelAccuracy + i >= image.getWidth()) {
                                break;
                            }
                            sector1Score += ImageAnalysis.comprehensiveRGB(
                                    image.getRGB((x + currentLength) * Classifiers.pixelAccuracy + i,
                                            y * Classifiers.pixelAccuracy + j));
                            sector2Score += ImageAnalysis.comprehensiveRGB(
                                    image.getRGB((x + currentLength) * Classifiers.pixelAccuracy + i,
                                            y * Classifiers.pixelAccuracy + j + Classifiers.pixelAccuracy));
                        }
                    }

                    currentLength++;

                    // NOTE: higher score means lighter color

                    if (sector2Score - sector1Score > 10000) {
                        continue;
                    } else {
                        if (currentLength > top_length) {
                            top_length = currentLength;
                            coords[0] = x * Classifiers.pixelAccuracy;
                            coords[1] = (x + currentLength) * Classifiers.pixelAccuracy;
                            coords[2] = y * Classifiers.pixelAccuracy;
                            currentLength = 0;
                        }
                        break;
                    }
                }

            }
        }

        Graphics2D g = (Graphics2D) getImage().getGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(5));
        g.drawLine(coords[0], coords[2], coords[1], coords[2]);

        headLength = Math.abs(coords[1] - coords[0]);
        headX1 = coords[0];
        headX2 = coords[1];
        headY = coords[2] + Classifiers.pixelAccuracy;

        // assignEyesHaar();

    }

    public void assignEyebrowsHaar() throws IOException {

        // These ratios were calculated from tweaking parameters based on dataset images from approximately 50 images

//        BufferedImage imageBackup = image;
//        image = ImageAnalysis.furtherModifcationClassification(image);

        eyeLength = headLength / 3;
        int eyeLengthY = eyeLength / 5;

        pixelatedImage = new BufferedImage(image.getWidth() / eyeLength, image.getHeight() / eyeLengthY, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < pixelatedImage.getHeight(); y++) {
            for (int x = 0; x < pixelatedImage.getWidth(); x++) {

                int RGBSumRed = 0;
                int RGBSumGreen = 0;
                int RGBSumBlue = 0;

                for (int j = 0; j < eyeLengthY; j++) {
                    for (int i = 0; i < eyeLength; i++) {
                        int RGB = image.getRGB(x * eyeLength + i, y * eyeLengthY + j);
                        RGBSumRed += (RGB >> 16) & 0xFF;
                        RGBSumGreen += (RGB >> 8) & 0xFF;
                        RGBSumGreen += (RGB & 0xFF);
                    }
                }

                int RGB = ImageAnalysis.RGBGenerator((int) (RGBSumRed / (eyeLength * eyeLengthY)),
                        (int) (RGBSumGreen / (eyeLength * eyeLengthY)),
                        (int) (RGBSumBlue / (eyeLength * eyeLengthY)));

                pixelatedImage.setRGB(x, y, RGB);

            }
        }

        // Store max intensities to store the rest of the values in if they are darker than the RGB values defined
        // Enabling storing the darkest spots on the image, for the eyebrows
        int[] zoneIntensities = new int[2];
        zoneIntensities[0] = 100000;
        zoneIntensities[1] = 100000;

        // System.out.println(headX2/eyeLength + " " + pixelatedImage.getWidth());

        for (int y = headY / eyeLengthY+1; y < pixelatedImage.getHeight() / 2 + 1; y++) {
            for (int x = 0; x < headX2 / eyeLength; x++) {

                int zone1 = ImageAnalysis.comprehensiveRGB(pixelatedImage.getRGB(x, y));
                int zone2 = ImageAnalysis.comprehensiveRGB(pixelatedImage.getRGB(x + 1, y));
                int zone3 = ImageAnalysis.comprehensiveRGB(pixelatedImage.getRGB(x + 2, y));

                // System.out.println(zone1 + " " + zone2 + " " + zone3);

                if (Math.abs(zone1 - zone3) < 10 && zone1 < zone2 && zone3 < zone2 &&

                        ((zone2 - zone1 < zoneIntensities[0] && zone2 - zone3 < zoneIntensities[1]) ||
                                (zone2 - zone3 < zoneIntensities[0] && zone2 - zone1 < zoneIntensities[1]))) {

                    // System.out.println(zone1 + " " + zone2 + " " + zone3);
                    // System.out.println("Thats a three pointer!");

                    zoneIntensities[0] = zone1;
                    zoneIntensities[1] = zone3;

                    eyeX1 = x * eyeLength;
                    eyeX2 = eyeX1 + eyeLength;
                    eyeY = y * eyeLengthY - eyeLengthY;

                }
            }
        }

        // image = imageBackup;

        Graphics2D g = (Graphics2D) getImage().getGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(5));
        g.drawLine(eyeX1, eyeY, eyeX2, eyeY);
        g.drawLine(eyeX1 + eyeLength * 2, eyeY, eyeX2 + eyeLength * 2, eyeY);

        if (zoneIntensities[0] == 100000 && zoneIntensities[1] == 100000) {
            System.out.println("not a face");
        }

    }

    public void assignEyesHaar() throws IOException {

        eyeLength = headLength / 3;
        int difference = 10;

        boolean found = false;

        int top_length = 0;
        int[] coords = new int[3];

        for (int y = 1; y < image.getHeight() / eyeLength / 2; y++) {
            for (int x = 1; x < image.getWidth() / eyeLength - 2; x++) {

                // Checks for total area score
                int sector1Score = 0;
                int sector2Score = 0;
                int sector3Score = 0;

                for (int j = 0; j < eyeLength; j++) {
                    for (int i = 0; i < eyeLength; i++) {
                        if ((x) * eyeLength + i + eyeLength * 2 >= image.getWidth()) {
                            System.out.println(x);

                            break;
                        }
                        sector1Score += ImageAnalysis.comprehensiveRGB(
                                image.getRGB((x) * eyeLength + i,
                                        y * eyeLength + j));
                        sector2Score += ImageAnalysis.comprehensiveRGB(
                                image.getRGB((x) * eyeLength + i + eyeLength,
                                        y * eyeLength + j));
                        sector3Score += ImageAnalysis.comprehensiveRGB(
                                image.getRGB((x) * eyeLength + i + eyeLength * 2,
                                        y * eyeLength + j));
                    }
                }

                // NOTE: higher score means lighter color

                int currentDifference;

                if (sector2Score - sector1Score < sector2Score - sector3Score)
                    currentDifference = sector2Score - sector1Score;
                else
                    currentDifference = sector2Score - sector3Score;

                // TODO: DO SOMETHING TO PREVENT THE EYES BEING LOCATED IN WEIRD PLACES

                boolean bigJump = currentDifference > 1000;

                if (currentDifference > difference)
                    difference = currentDifference;

                if (sector2Score - sector1Score >= difference && sector2Score - sector3Score >= difference) {
                    coords[0] = x * eyeLength;
                    coords[1] = x * eyeLength + eyeLength;
                    coords[2] = y * eyeLength;
                }

            }
        }

        System.out.println("\n" + top_length + ", which is, " + Arrays.toString(coords) + ". Difference: " + difference);

        System.out.println(eyeLength);

        Graphics2D g = (Graphics2D) getImage().getGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(5));
        g.drawLine(coords[0], coords[2], coords[1], coords[2]);
        g.drawLine(coords[0] + eyeLength * 2, coords[2], coords[1] + eyeLength * 2, coords[2]);

        String filePath = "res/trainingSet/teacher1/learner2/";
        ImageIO.write(getImage(), "PNG", new File(filePath + "X" + imageName + ".png"));

    }

    public void assignMouthHaar() throws IOException {

        int highestRGBinROW = Integer.MAX_VALUE;
        int highestRGBinROW_Y = 0;

        int mouthSensetivity = 1;

        BufferedImage readyImage = ImageAnalysis.furtherModifcationClassification(image);

        System.out.println(readyImage.getHeight());

        BufferedImage newImage = new BufferedImage(1, image.getHeight() / mouthSensetivity, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < readyImage.getHeight() / mouthSensetivity; y++) {

            int RGBSumRed = 0;
            int RGBSumGreen = 0;
            int RGBSumBlue = 0;

            for (int x = 20; x < readyImage.getWidth() - 20; x++) {

                int RGB = readyImage.getRGB(x, y);
                RGBSumRed += (RGB >> 16) & 0xFF;
                RGBSumGreen += (RGB >> 8) & 0xFF;
                RGBSumGreen += (RGB & 0xFF);

            }

            int RGBInterval = (readyImage.getWidth() - 40);
            int RGB = ImageAnalysis.RGBGenerator((int) (RGBSumRed / RGBInterval), (int) (RGBSumGreen / RGBInterval), (int) (RGBSumBlue / RGBInterval));

            if (RGB < highestRGBinROW && y > 120 && y < 175) {
                highestRGBinROW = RGB;
                highestRGBinROW_Y = y;
            }

            newImage.setRGB(0, y, RGB);

        }

        mouthY = highestRGBinROW_Y;

        int maxLength = 0;

        ///////////////////////////////


        for (int x = 0; x < readyImage.getWidth() / Classifiers.pixelAccuracy - 1; x++) {

            int currentLength = 0;

            while (true) {

                // Checks for total area score
                int sector1Score = 0;
                int sector2Score = 0;

                for (int j = 0; j < Classifiers.pixelAccuracy; j++) {
                    for (int i = 0; i < Classifiers.pixelAccuracy; i++) {
                        if ((x + currentLength) * Classifiers.pixelAccuracy + i >= readyImage.getWidth()) {
                            break;
                        }
                        sector1Score += ImageAnalysis.comprehensiveRGB(
                                readyImage.getRGB((x + currentLength) * Classifiers.pixelAccuracy + i,
                                        mouthY + j));
                        sector2Score += ImageAnalysis.comprehensiveRGB(
                                readyImage.getRGB((x + currentLength) * Classifiers.pixelAccuracy + i,
                                        mouthY + j + Classifiers.pixelAccuracy));
                    }
                }

                currentLength++;

                // NOTE: higher score means lighter color

                if (sector2Score - sector1Score > 10000) {
                    continue;
                } else {
                    if (currentLength > maxLength) {
                        maxLength = currentLength;
                        mouthX1 = x * Classifiers.pixelAccuracy;
                        mouthX2 = (x + currentLength) * Classifiers.pixelAccuracy;
                        currentLength = 0;
                    }
                    break;
                }
            }

        }

        ///////////////////////////////

        Graphics2D g = (Graphics2D) getImage().getGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(5));
        g.drawLine(mouthX1, highestRGBinROW_Y, mouthX2, highestRGBinROW_Y);

//        mouthLength = Math.abs(coords[1] - coords[0]);
//        mouthX1 = coords[0];
//        mouthX2 = coords[1];
//        mouthY = coords[2] + Classifiers.pixelAccuracy;

        System.out.println(highestRGBinROW_Y + " --> " + highestRGBinROW);

        pixelatedImage = newImage;

    }

}
