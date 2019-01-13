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

    public Classify(BufferedImage img) throws IOException {

        setImage(img);

        assignheadHaar();

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
    public void assignheadHaar() throws IOException {

        int top_length = 0;
        int[] coords = new int[3];

        for (int y = 0; y < image.getHeight()/Classifiers.pixelAccuracy-2; y++) {
            for (int x = 0; x < image.getWidth()/Classifiers.pixelAccuracy-2; x++) {

                System.out.print("round " + x + " - ");

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

                    System.out.println(currentLength);

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

        System.out.println(top_length + ", which is, " + Arrays.toString(coords));

        Graphics2D g = (Graphics2D) getImage().getGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(5));
        g.drawLine(coords[0], coords[2], coords[1], coords[2]);

        String filePath = "res/trainingSet/teacher1/learner1/";
        ImageIO.write(getImage() , "PNG", new File(filePath + "Ximage1.png"));

    }
}
