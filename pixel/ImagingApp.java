/**
 * package file and printwriter
 */
import java.io.File;
import java.io.PrintWriter;
/**
 * This is the entry point to your executable application. We want to be able to use a command line
argument when we run the application. Execution in the terminal should look like this:
 */
public class ImagingApp {
    static Image inputImage;
    static Image outputImage;
    static CompressedImage compressedImage;
/**
 * 
 * @param img
 * @param filename the name of the PGM or PPM file that will be used as input image
 * @return
 */
    static boolean saveImage(Image img, String filename) {
        try {
            PrintWriter pw = new PrintWriter(new File(filename));
            if (img.grayscale) {
                pw.println("P2");
            } else {
                pw.println("P3");
            }

            pw.println(img.getWidth() + " " + img.getHeight());
            pw.println("255");

            if (img.grayscale) {
                for (int y = 0; y < img.getHeight(); y++) {
                    for (int x = 0; x < img.getWidth(); x++) {
                        int[] pixelValue = img.getPixel(y, x).getValue();
                        pw.print(pixelValue[0] + " ");
                    }
                    pw.println();
                }
            } else {
                for (int y = 0; y < img.getHeight(); y++) {
                    for (int x = 0; x < img.getWidth(); x++) {
                        int[] pixelValue = img.getPixel(y, x).getValue();
                        pw.println(pixelValue[0] + " " + pixelValue[1] + " " + pixelValue[2]);
                    }
                }
            }
            pw.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error occured saving the image to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
/**
 * 
 * @param img The input image that is constructed from the contents of the input_file , must be assigned to
this field.
 * @param filename The output image that is constructed from operation compress, must be assigned to this field.
 * 
 */
    static boolean saveImage(CompressedImage img, String filename) {
        try {
            PrintWriter pw = new PrintWriter(new File(filename));
            if (img.grayscale) {
                pw.println("P2");
            } else {
                pw.println("P3");
            }

            pw.println(img.getWidth() + " " + img.getHeight());
            pw.println("255");

            if (img.grayscale) {
                for (int y = 0; y < img.getHeight(); y++) {
                    for (int x = 0; x < img.getWidth(); x++) {
                        int[] pixelValue = img.getPixel(y, x).getValue();
                        pw.print(pixelValue[0] + " ");
                    }
                    pw.println();
                }
            } else {
                for (int y = 0; y < img.getHeight(); y++) {
                    for (int x = 0; x < img.getWidth(); x++) {
                        int[] pixelValue = img.getPixel(y, x).getValue();
                        pw.println(pixelValue[0] + " " + pixelValue[1] + " " + pixelValue[2]);
                    }
                }
            }
            pw.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error occured saving the image to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
/**
 * 
 * @param args All the handling of the command line arguments as well as the validation of the user input, takes
place in this method
 */
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];
        String operation = args[2];

        inputImage = new Image(inputFile);

        switch (operation) {
            case "scale":
                outputImage = inputImage.scale(Integer.parseInt(args[3]));
                saveImage(outputImage, outputFile);
                break;

            case "crop":
                outputImage = inputImage.crop(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
                saveImage(outputImage, outputFile);
                break;

            case "flip":
                outputImage = inputImage.flip(args[3]);
                saveImage(outputImage, outputFile);
                break;

            case "rotate":
                outputImage = inputImage.rotate(args[3].equals("clockwise"));
                saveImage(outputImage, outputFile);
                break;

            case "compress":
                compressedImage = inputImage.compress(args[3].equals("yes"), args[4].equals("yes"));
                saveImage(compressedImage, outputFile);
                break;
        
            default:
                break;
        }
    }
}