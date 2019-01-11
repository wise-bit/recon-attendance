import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.URL;
import java.util.HashMap;

public class ImageAnalysis {

    // This main is to be deleted later

    public static final int BLACK = RGBGenerator(0,0,0);
    public static final int WHITE = RGBGenerator(255,255,255);

    public static void main(String[] args) throws IOException {

        BufferedImage img = img = ImageIO.read(new File("res/pureTestSets/face1.png"));

        String filePath = "res/pureTestSets/";

        int width = img.getWidth();
        int height = img.getHeight();

        ImageIO.write( trainingReady(img) , "PNG", new File(filePath + "modifiedImage1.png"));

    }

    public static BufferedImage toGreyScale(BufferedImage image) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        image = op.filter(image, null);
        return image;
    }

    public static BufferedImage toGreyScalePixelInefficient(BufferedImage image) {

        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        // BufferedImage newImage = image;

        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                // fetches rgb of a specific pixel
                int rgb = image.getRGB(x, y);

                // Alternate image generation based on vector values
                newImage.setRGB(x, y, RGBtoGREYpixel(rgb));
            }
        }
        return newImage;

    }

    // Convert an RGB pixel to a grey-scale pixel
    public static int RGBtoGREYpixel(int rgb) {

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = (rgb & 0xFF);

        // Gets the RGB code and gets each of the codes, averages them, therefore calculating the intensity of the pixel
        int grayLevel = (red + green + blue) / 3;
        int grey = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
        return grey;
    }

    // Resize an image using given dimensions
    public static BufferedImage resize (BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        // Image tmp = img.getScaledInstance(width, height, Image.SCALE_FAST);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    // Bolds the image by using only white and black
    public static BufferedImage bold (BufferedImage image) {

        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                int rgb = image.getRGB(x, y);
                if (rgb < -10000000) {
                    newImage.setRGB(x, y, BLACK);
                } else {
                    newImage.setRGB(x, y, WHITE);
                }
            }
        }
        return newImage;

    }

    // Returns RGB code from the each integer value of red, green and blue
    public static int RGBGenerator(int r, int g, int b) {

        int grayLevel = (r + g + b) / 3;
        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
        return gray;

    }

    // Flips the image over the X axis
    public static BufferedImage flipImageX(BufferedImage image) {
        for (int i=0;i<image.getWidth()/2;i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int tmp = image.getRGB(i, j);

                image.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
                image.setRGB(image.getWidth() - i - 1, j, tmp);

            }
        }
        return image;
    }

    // Creates glitch style image for future comparison
    public static BufferedImage createGlitch (BufferedImage image) {

        BufferedImage newImage = new BufferedImage(image.getWidth()*3, image.getHeight()*3, BufferedImage.TYPE_INT_RGB);

        for (int i = 1; i < image.getWidth()-1; i++) {
            for (int j = 1; j < image.getHeight()-1; j++) {

                // These are the default values in case the differences are the same
                int maxDiff = -1500000;
                int posOfPix = 4;

                if (image.getRGB(i-1, j-1) - image.getRGB(i, j) < maxDiff) { maxDiff = image.getRGB(i-1, j-1) - image.getRGB(i, j); posOfPix = 0; }
                if (image.getRGB(i, j-1) - image.getRGB(i, j) < maxDiff) { maxDiff = image.getRGB(i, j-1) - image.getRGB(i, j); posOfPix = 1; }
                if (image.getRGB(i+1, j-1) - image.getRGB(i, j) < maxDiff) { maxDiff = image.getRGB(i+1, j-1) - image.getRGB(i, j); posOfPix = 2; }

                if (image.getRGB(i-1, j) - image.getRGB(i, j) < maxDiff) { maxDiff = image.getRGB(i-1, j) - image.getRGB(i, j); posOfPix = 3; }
                if (image.getRGB(i, j) - image.getRGB(i, j) < maxDiff) { maxDiff = image.getRGB(i, j) - image.getRGB(i, j); posOfPix = 4; }
                if (image.getRGB(i+1, j) - image.getRGB(i, j) < maxDiff) { maxDiff = image.getRGB(i+1, j) - image.getRGB(i, j); posOfPix = 5; }

                if (image.getRGB(i-1, j+1) - image.getRGB(i, j) < maxDiff) { maxDiff = image.getRGB(i-1, j+1) - image.getRGB(i, j); posOfPix = 6; }
                if (image.getRGB(i, j+1) - image.getRGB(i, j) < maxDiff) { maxDiff = image.getRGB(i, j+1) - image.getRGB(i, j); posOfPix = 7; }
                if (image.getRGB(i+1, j+1) - image.getRGB(i, j) < maxDiff) { maxDiff = image.getRGB(i+1, j+1) - image.getRGB(i, j); posOfPix = 8; }

                for (int x = 0; x < 3 ; x++) {
                    for (int y = 0; y < 3; y++) {
                        try {
                            newImage.setRGB(i * 3 + x, j * 3 + y, WHITE);
                        } catch (Exception e) {
                            System.out.println(i * 3 + j + " " + j * 3 + y);
                        }
                    }
                }

                if (posOfPix != 4) {
                    newImage.setRGB(i * 3 + posOfPix % 3, j * 3 + posOfPix / 3, BLACK);
                }

            }
        }

        return newImage;

    }

    // Crop an image, courtesy of bufferedImage
    public static BufferedImage cropImage(BufferedImage originalImage, int x, int y, int w, int h){
        BufferedImage subImgage = originalImage.getSubimage(x, y, w, h);
        return subImgage;
    }

    public static BufferedImage visualLandmarkAssignment (BufferedImage image) {

        int segmentCount = 10; // To be changed for more --> less precision for landmark points (multiple of 20)

        int segmentLength = image.getHeight() / segmentCount;

        for (int x = 0; x < segmentCount; x++) {
            for (int y = 0; y < segmentCount; y++) {



            }
        }

        return image;

    }

    // Prepares image to be stored for training for the facial recognition
    public static BufferedImage trainingReady(BufferedImage image) {

        BufferedImage croppedImage = cropImage(image, (image.getWidth() - image.getHeight())/2, 0, image.getHeight(), image.getHeight());

        BufferedImage greyScaledImage = toGreyScalePixelInefficient(croppedImage);

        // uses the ratio of 450
        // BufferedImage resizedImage = resize( greyScaledImage , 100, 129);
        BufferedImage resizedImage = resize( greyScaledImage , 200, 200);

        // BufferedImage boldedImage = bold(resizedImage);

        BufferedImage glitchedImage = createGlitch(resizedImage);

        // BufferedImage resizedAgain = resize( glitchedImage , 800, 1040);
        // BufferedImage resizedAgain = resize( glitchedImage , 200, 258);

        return glitchedImage;

    }

    // Kept in case of cloud storage requirements (not implemented in actual program)
    public static void downloadOnlineImage(String websiteURL, String filename, String outputPath) {
        try{
            URL url = new URL(websiteURL);
            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(outputPath + "/" + filename);
            byte[] buffer = new byte[2048];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
