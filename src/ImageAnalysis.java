import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.URL;

public class ImageAnalysis {

    // This main is to be deleted later

    public static void main(String[] args) throws IOException {

        BufferedImage img = img = ImageIO.read(new File("res/pureTestSets/testimage1.png"));

        String filePath = "res/pureTestSets/";

        int width = img.getWidth();
        int height = img.getHeight();

        System.out.println(width + " " + height);

        ImageIO.write(toGreyScalePixelInefficient(img), "PNG", new File(filePath + "modifiedImage1.png"));

    }

    public static BufferedImage toGreyScale(BufferedImage image) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        image = op.filter(image, null);
        return image;
    }

    public static BufferedImage toGreyScalePixelInefficient(BufferedImage image) {

        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                int grayLevel = (r + g + b) / 3;
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                // System.out.println(rgb + " : " + r + " " + g + " " + b + " " + gray);
                newImage.setRGB(x, y, gray);
            }
        }
        return newImage;
    }

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

    public static void downloadOnlineImage() {
        try{
            String fileName = "digital_image_processing.jpg";
            String website = "http://tutorialspoint.com/java_dip/images/"+fileName;

            System.out.println("Downloading File From: " + website);

            URL url = new URL(website);
            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(fileName);
            byte[] buffer = new byte[2048];

            int length = 0;

            while ((length = inputStream.read(buffer)) != -1) {
                System.out.println("Buffer Read of length: " + length);
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

}
