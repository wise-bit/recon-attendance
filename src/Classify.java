import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Classify {

    public Classify(BufferedImage image) throws IOException {

        String filePath = "res/pureTestSets/";
        ImageIO.write(ImageAnalysis.trainingReady(image) , "PNG", new File(filePath + "modifiedImage1.png"));

    }

}
