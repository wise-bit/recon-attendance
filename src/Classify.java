import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Classify {

    /*

    Dataset source

    Mahmoud Afifi and Abdelrahman Abdelhamed, "AFIF4: Deep gender classification based on an AdaBoost-based
    fusion of isolated facial features and foggy faces". arXiv:1706.04277, arXiv 2017.

     */

    public BufferedImage image;

    public Classify(BufferedImage img) throws IOException {

        setImage(img);

        String filePath = "res/trainingSet/teacher1/learner1/";
        ImageIO.write(ImageAnalysis.trainingReady(getImage()) , "PNG", new File(filePath + "Ximage1.png"));

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
    public void assignheadHaar() {

        for (int y = 0; y < image.getHeight() - Classifiers.pixelAccuracy; y+=Classifiers.pixelAccuracy) {
            for (int x = 0; x < image.getWidth() - Classifiers.pixelAccuracy; x+=Classifiers.pixelAccuracy) {

                // Checks for total area score
                int sectorScore = 0;

                for (int j = 0; j < Classifiers.pixelAccuracy; j++) {
                    for (int i = 0; i < Classifiers.pixelAccuracy; i++) {
                        sectorScore += ImageAnalysis.comprehensiveRGB(image.getRGB(x*Classifiers.pixelAccuracy+i, y*Classifiers.pixelAccuracy+j));
                    }
                }

                // NOTE: higher score means lighter color

                // TODO: COMPLETE THIS METHOD

            }
        }

    }
}
