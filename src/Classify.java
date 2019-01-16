import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Classify {

    /*

    Dataset source

    Mahmoud Afifi and Abdelrahman Abdelhamed, "AFIF4: Deep gender classification based on an AdaBoost-based
    fusion of isolated facial features and foggy faces". arXiv:1706.04277, arXiv 2017.

     */

    public BufferedImage image;

    public int headLength;
    public int headX1;
    public int headX2;
    public int headY;

    public int eyeLength;
    public int eyeX1;
    public int eyeX2;
    public int eyeY;

    public String imageName;

    public Classify(BufferedImage img, String name) throws IOException {

        setImage(img);
        setImageName(name);

        assignHeadHaar();

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

        for (int y = 0; y < image.getHeight()/Classifiers.pixelAccuracy-1; y++) {
            for (int x = 0; x < image.getWidth()/Classifiers.pixelAccuracy-1; x++) {

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

//        Graphics2D g = (Graphics2D) getImage().getGraphics();
//        g.setColor(Color.RED);
//        g.setStroke(new BasicStroke(5));
//        g.drawLine(coords[0], coords[2], coords[1], coords[2]);

        headLength = Math.abs(coords[1] - coords[0]);
        headX1 = coords[0];
        headX2 = coords[1];
        headY = coords[2] + Classifiers.pixelAccuracy;

        // String filePath = "res/trainingSet/teacher1/learner1/";
        // ImageIO.write(getImage() , "PNG", new File(filePath + "Ximage1.png"));

        // assignEyesHaar();
        generatePixelatedHead();

    }

    public void generatePixelatedHead() throws IOException {

        eyeLength = headLength/3;

        BufferedImage pixelatedImage = new BufferedImage(image.getWidth()/eyeLength, image.getHeight()/eyeLength, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < pixelatedImage.getHeight(); y++) {
            for (int x = 0; x < pixelatedImage.getWidth(); x++) {

                int RGBSumRed = 0;
                int RGBSumGreen = 0;
                int RGBSumBlue = 0;

                for (int j = 0; j < eyeLength; j++) {
                    for (int i = 0; i < eyeLength; i++) {
                        int RGB = image.getRGB(x*eyeLength + i, y*eyeLength + j);
                        RGBSumRed+=(RGB >> 16) & 0xFF;
                        RGBSumGreen+=(RGB >> 8) & 0xFF;
                        RGBSumGreen+=(RGB & 0xFF);
                    }
                }

                int RGB = ImageAnalysis.RGBGenerator((int)(RGBSumRed/Math.pow(eyeLength, 2)),
                        (int)(RGBSumGreen/Math.pow(eyeLength, 2)),
                        (int)(RGBSumBlue/Math.pow(eyeLength, 2)));
                
                pixelatedImage.setRGB(x, y, RGB);

            }
        }

        int[] zoneIntensities = new int[2];
        zoneIntensities[0] = 100000;
        zoneIntensities[1] = 100000;

        for (int y = headY/eyeLength; y < pixelatedImage.getHeight()/2+1; y++) {
            for (int x = 0; x < pixelatedImage.getWidth()-2; x++) {
                int zone1 = ImageAnalysis.comprehensiveRGB(pixelatedImage.getRGB(x, y));
                int zone2 = ImageAnalysis.comprehensiveRGB(pixelatedImage.getRGB(x+1, y));
                int zone3 = ImageAnalysis.comprehensiveRGB(pixelatedImage.getRGB(x+2, y));

                if (Math.abs(zone1 - zone3) < 10 && zone1 < zone2 && zone3 < zone2 &&

                        ((zone2 - zone1 < zoneIntensities[0] && zone2 - zone3 < zoneIntensities[1]) ||
                                (zone2 - zone3 < zoneIntensities[0] && zone2 - zone1 < zoneIntensities[1]))) {

                    System.out.println(zone1 + " " + zone2 + " " + zone3);

                    zoneIntensities[0] = zone1;
                    zoneIntensities[1] = zone3;

                    eyeX1 = x * eyeLength;
                    eyeX2 = eyeX1 + eyeLength;
                    eyeY = y * eyeLength;

                }
            }
        }

        Graphics2D g = (Graphics2D) getImage().getGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(5));
        g.drawLine(eyeX1, eyeY, eyeX2, eyeY);
        g.drawLine(eyeX1 + eyeLength*2, eyeY, eyeX2 + eyeLength*2, eyeY);

        String filePath = "res/trainingSet/teacher1/learner2/";
        ImageIO.write(image , "PNG", new File(filePath + "X" + imageName.substring(0, imageName.length()-4) + ".png"));

    }

    public void assignEyesHaar() throws IOException {

        eyeLength = headLength/3;
        int difference = 10;

        boolean found = false;

        int top_length = 0;
        int[] coords = new int[3];

        for (int y = 1; y < image.getHeight()/eyeLength/2; y++) {
            for (int x = 1; x < image.getWidth()/eyeLength-2; x++) {

                // Checks for total area score
                int sector1Score = 0;
                int sector2Score = 0;
                int sector3Score = 0;

                for (int j = 0; j < eyeLength; j++) {
                    for (int i = 0; i < eyeLength; i++) {
                        if ((x) * eyeLength + i + eyeLength*2 >= image.getWidth()) {
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
                                image.getRGB((x) * eyeLength + i + eyeLength*2,
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
                    coords[0] = x*eyeLength;
                    coords[1] = x*eyeLength + eyeLength;
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
        g.drawLine(coords[0] + eyeLength*2, coords[2], coords[1] + eyeLength*2, coords[2]);

        String filePath = "res/trainingSet/teacher1/learner2/";
        ImageIO.write(getImage() , "PNG", new File(filePath + "X"+ imageName +".png"));

    }

}
