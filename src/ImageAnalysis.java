/**
 * This is the most unique and most important class of the program, since it uses image manipulation to create a new image
 * which can be used to train machine learning models with, or general template matching as well.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.util.ArrayList;

public class ImageAnalysis {

    // These two colors are used a lot of production of new imafes
    public static final int BLACK = RGBGenerator(0, 0, 0);
    public static final int WHITE = RGBGenerator(255, 255, 255);

    // These guide the general functionality of the program

    // How sensetive the pixel sensing should be
    public static final int sensitivity = -1000000;

    // Radius of landmarks being placed
    public static final int landmarkSize = 5;

    // Size of each segment of an image when being divided, in pixels
    public static final int segmentation = 200;


    public static BufferedImage furtherModifcation(BufferedImage img) {
        // Ready for training
        img = trainingReady(img);

        BufferedImage boldedImage = bold(img);
        // Creates a glitched image to enhance features of face
        BufferedImage glitchedImage = createGlitch(boldedImage);
        BufferedImage landmarkedImage = visualLandmarkAssignment(glitchedImage);

        BufferedImage enhancedImage = enhanceGlitch(glitchedImage);

        return enhancedImage;
    }

    public static BufferedImage furtherModifcationClassification(BufferedImage img) {

        BufferedImage boldedImage = bold(img);

        // Creates a glitched image to enhance features of face
        BufferedImage glitchedImage = createGlitch(boldedImage);

        BufferedImage enhancedImage = enhanceGlitch(glitchedImage);

        BufferedImage resizedAgain = resize(enhancedImage, 200, 200);

        return resizedAgain;
    }

    // Prepares image to be stored for training for the facial recognition
    public static BufferedImage trainingReady(BufferedImage image) {

        BufferedImage croppedImage;
        // Crops image based on orientation
        if (image.getHeight() <= image.getWidth())
            croppedImage = cropImage(image, (image.getWidth() - image.getHeight()) / 2, 0, image.getHeight(), image.getHeight());
        else
            croppedImage = cropImage(image, 0, (image.getHeight() - image.getWidth()) / 2, image.getWidth(), image.getWidth());

        // Greyscales image for analysis purposes
        BufferedImage greyScaledImage = toGreyScalePixelInefficient(croppedImage);

        // uses the ratio of 450
        BufferedImage resizedImage = resize(greyScaledImage, 200, 200);

        // returns the new image
        return resizedImage;

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
    public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        // Image tmp = img.getScaledInstance(width, height, Image.SCALE_FAST);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    // Bolds the image by using only white and black
    public static BufferedImage bold(BufferedImage image) {

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
        for (int i = 0; i < image.getWidth() / 2; i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int tmp = image.getRGB(i, j);

                image.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
                image.setRGB(image.getWidth() - i - 1, j, tmp);

            }
        }
        return image;
    }

    // Creates glitch style image for future comparison
    // This uses the principle of assigning vectors to replace color
    public static BufferedImage createGlitch(BufferedImage image) {

        BufferedImage newImage = new BufferedImage(image.getWidth() * 3, image.getHeight() * 3, BufferedImage.TYPE_INT_RGB);

        for (int i = 1; i < image.getWidth() - 1; i++) {
            for (int j = 1; j < image.getHeight() - 1; j++) {

                // These are the default values in case the differences are the same
                int maxDiff = sensitivity;
                int posOfPix = 4;

                if (image.getRGB(i - 1, j - 1) - image.getRGB(i, j) < maxDiff) {
                    maxDiff = image.getRGB(i - 1, j - 1) - image.getRGB(i, j);
                    posOfPix = 0;
                }
                if (image.getRGB(i, j - 1) - image.getRGB(i, j) < maxDiff) {
                    maxDiff = image.getRGB(i, j - 1) - image.getRGB(i, j);
                    posOfPix = 1;
                }
                if (image.getRGB(i + 1, j - 1) - image.getRGB(i, j) < maxDiff) {
                    maxDiff = image.getRGB(i + 1, j - 1) - image.getRGB(i, j);
                    posOfPix = 2;
                }

                if (image.getRGB(i - 1, j) - image.getRGB(i, j) < maxDiff) {
                    maxDiff = image.getRGB(i - 1, j) - image.getRGB(i, j);
                    posOfPix = 3;
                }
                if (image.getRGB(i, j) - image.getRGB(i, j) < maxDiff) {
                    maxDiff = image.getRGB(i, j) - image.getRGB(i, j);
                    posOfPix = 4;
                }
                if (image.getRGB(i + 1, j) - image.getRGB(i, j) < maxDiff) {
                    maxDiff = image.getRGB(i + 1, j) - image.getRGB(i, j);
                    posOfPix = 5;
                }

                if (image.getRGB(i - 1, j + 1) - image.getRGB(i, j) < maxDiff) {
                    maxDiff = image.getRGB(i - 1, j + 1) - image.getRGB(i, j);
                    posOfPix = 6;
                }
                if (image.getRGB(i, j + 1) - image.getRGB(i, j) < maxDiff) {
                    maxDiff = image.getRGB(i, j + 1) - image.getRGB(i, j);
                    posOfPix = 7;
                }
                if (image.getRGB(i + 1, j + 1) - image.getRGB(i, j) < maxDiff) {
                    maxDiff = image.getRGB(i + 1, j + 1) - image.getRGB(i, j);
                    posOfPix = 8;
                }

                // Loops through a 3x3 matrix, making new pixel blocks
                for (int x = 0; x < 3; x++) {
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

    public static BufferedImage enhanceGlitch(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < newImage.getHeight(); y++) {
            for (int x = 0; x < newImage.getWidth(); x++) {
                newImage.setRGB(x, y, WHITE);
            }
        }

        // Places landmarks
        for (int y = 10; y < image.getHeight(); y++) {
            for (int x = 10; x < image.getWidth(); x++) {
                if (comprehensiveRGB(image.getRGB(x, y)) == 0){
                    Graphics2D g = (Graphics2D) newImage.getGraphics();
                    g.setColor(Color.BLACK);
                    g.setStroke(new BasicStroke(5));

                    int newLandmarkDiameter = 10;
                    g.fillOval(x, y, newLandmarkDiameter, newLandmarkDiameter);
                }
            }
        }

        return newImage;
    }

    // Crop an image, courtesy of bufferedImage
    public static BufferedImage cropImage(BufferedImage originalImage, int x, int y, int w, int h) {
        BufferedImage subImgage = originalImage.getSubimage(x, y, w, h);
        return subImgage;
    }

    /**
     * Creates a mask on the image, later depreciated
     * @param image
     * @param x
     * @param y
     * @param length
     * @param width
     * @return
     */
    public static BufferedImage mask(BufferedImage image, int x, int y, int length, int width) // boolean?
    {
        BufferedImage imageCopy = new BufferedImage(length, width, BufferedImage.TYPE_INT_RGB);

        return imageCopy;

    }

    /**
     * Assigns landmarks
     * @param image
     * @return
     */
    public static BufferedImage visualLandmarkAssignment(BufferedImage image) {

        // BufferedImage imageCopy = image;
        BufferedImage imageCopy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        int segmentCount = segmentation; // To be changed for more --> less precision for landmark points (multiple of 20)
        int distingushingThreshold = 0;

        int segmentLength = image.getHeight() / segmentCount;
        // System.out.println(segmentLength);

        ArrayList<Point> pointsPlaced = new ArrayList<Point>();

        for (int y = 0; y < segmentCount; y++) {
            for (int x = 0; x < segmentCount; x++) {

                int RGBSum = 0;

                for (int i = 0; i < segmentLength; i++) {
                    for (int j = 0; j < segmentLength; j++) {
                        RGBSum += comprehensiveRGB(image.getRGB(x * segmentLength + j, y * segmentLength + i));
                    }
                }

                if (RGBSum > distingushingThreshold) {

                    // FUNCTION: ADD LANDMARK

                    int pointX = segmentLength * x + segmentLength / 2;
                    int pointY = segmentLength * y + segmentLength / 2;

                    // image.setRGB(pointX, pointY, RGBGenerator(255, 0, 0));
                    Graphics2D g = (Graphics2D) imageCopy.getGraphics();
                    g.setColor(Color.RED);
                    g.setStroke(new BasicStroke(5));

                    g.fillOval(segmentLength * x + segmentLength / 2, segmentLength * y + segmentLength / 2, landmarkSize, landmarkSize);
                    // TODO: Store these points somewhere
                    // System.out.println(segmentLength*x + segmentLength/2 + ":" + (segmentLength*y + segmentLength/2) + " --> " + RGBSum);
                }


            }
        }

        return imageCopy;

    }

    // Prints out the actual values of RGB blocks for better human understanding
    public static void printReverseRGB(int RGB) {
        int red = (RGB >> 16) & 0xFF;
        int green = (RGB >> 8) & 0xFF;
        int blue = (RGB & 0xFF);
        System.out.printf("%d --> %d,%d,%d\n", RGB, red, green, blue);
    }

    // Converts to a version which is understandable to both humans and machine to a certain extent when it comes to comparing
    public static int comprehensiveRGB(int RGB) {
        int red = (RGB >> 16) & 0xFF;
        int green = (RGB >> 8) & 0xFF;
        int blue = (RGB & 0xFF);
        return red + green + blue;
    }

}
