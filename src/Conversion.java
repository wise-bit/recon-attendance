//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//
//public class Conversion {
//
//    public static void convertFromJpegToPng() throws IOException {
//
//        // read a jpeg from a inputFile
//        BufferedImage bufferedImage = ImageIO.read(new File(inputFile));
//
//        // write the bufferedImage back to outputFile
//        ImageIO.write(bufferedImage, "png", new File(outputFile));
//
//        // this writes the bufferedImage into a byte array called resultingBytes
//        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//        ImageIO.write(bufferedImage, "png", byteArrayOut);
//        byte[] resultingBytes = byteArrayOut.toByteArray();
//
//    }
//
//}
