/**
 * package scanner and for file 
 */
import java.io.File;
import java.util.Scanner;
/**
 * This class is used to represent a whole image. The width and the height of the image are
guaranteed to be multiples of 4 so that the splitting in tiles is exact.
 */
class Image{
    public Tile[] tileArray;
    private int width;
    private int height;
    public boolean grayscale;
/**
 * 
 * @param height The height of the image measured in pixels
 * @param width The width of the image measured in pixels
 * @param grayscale  Indicates the type of the image
 */
    Image(int height, int width, boolean grayscale) {
        this.height = height;
        this.width = width;
        this.grayscale = grayscale;
        int tilesCount = (int)Math.ceil(height * width / 16);
        this.tileArray = new Tile[tilesCount];
        for (int i = 0; i < tilesCount; i++) {
            this.tileArray[i] = new Tile();
        }
    }

    Image(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));
    
            // read first line and check if it's P2 (grayscale) or (color) otherwise
            this.grayscale = sc.nextLine().equals("P2");
            // read second line containing "width height"
            this.width = sc.nextInt();
            this.height = sc.nextInt();
            sc.nextInt(); // ignore maximum color value
    
            int tilesCount = (int)Math.ceil(this.height * this.width / 16);
            this.tileArray = new Tile[tilesCount];
            for (int i = 0; i < tilesCount; i++) {
                this.tileArray[i] = new Tile();
            }
    
            int horizontalTilesCount = this.width / 4;
            if (this.grayscale) {
                for(int row = 0; row < this.height; row++) {
                    for (int col = 0; col < this.width; col++) {
                        int tileIndex = ((row / 4) * horizontalTilesCount) + col / 4;
                        Pixel pixel = new Pixel(sc.nextInt());
                        this.tileArray[tileIndex].setPixel(row % 4, col % 4, pixel);
                    }
                }
            } else {
                int tileIndex = 0;
                int tileRowIndex = 0;
                int tileColIndex = 0;
                for (int i = 0; i < height * width; i++) {
                    // String[] line = sc.nextLine().split(" ");
                    int r = sc.nextInt();
                    int g = sc.nextInt();
                    int b = sc.nextInt();
                    Pixel pixel = new Pixel(r, g, b);
                    this.tileArray[tileIndex].setPixel(tileRowIndex % 4, tileColIndex++ % 4, pixel);
    
                    if (tileColIndex % 4 == 0) {
                        tileIndex++;
                    }

                    if (tileColIndex == this.width) {
                        tileRowIndex++;
                        tileIndex = (tileRowIndex / 4) * horizontalTilesCount;
                        tileColIndex = 0;
                    }
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error occured reading the image from file: " + e.getMessage());
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Pixel getPixel(int y, int x) {
        int horizontalTilesCount = this.width / 4;
        int tileIndex = ((y / 4) * horizontalTilesCount) + x / 4;
        return this.tileArray[tileIndex].getPixel(y % 4, x % 4);
    }

    public void setPixel(int y, int x, Pixel p) {
        int horizontalTilesCount = this.width / 4;
        int tileIndex = ((y / 4) * horizontalTilesCount) + x / 4;
        this.tileArray[tileIndex].setPixel(y % 4, x % 4, p);
    }
/**
 * 
 * @param factor If the factor is positive it creates a new magnified image where the newly
created pixels are copies of the top-left neighbor.
 * 
 */

    public Image scale(int factor) {
        int newWidth, newHeight;
        double _factor = 1.0 * factor;
        if (factor < 0) {
            _factor = -1.0 / factor;
        }
        newWidth = (int)(this.width * _factor);
        newHeight = (int)(this.height * _factor);

        Image scaledImage = new Image(newHeight, newWidth, this.grayscale);
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                scaledImage.setPixel(y, x, this.getPixel((int)(y / _factor), (int)(x / _factor)));
            }
        }
        return scaledImage;
    }

    public Image crop(int topY, int topX, int height, int width) {
        Image croppedImage = new Image(height, width, this.grayscale);
        for (int y = topY; y < topY + height; y++) {
            for (int x = topX; x < topX + width; x++) {
                croppedImage.setPixel(y - topY, x - topX, this.getPixel(y, x));
            }
        }
        return croppedImage;
    }

    public Image flip(String direction) {
        Image flippedImage = new Image(this.height, this.width, this.grayscale);
        if (direction.equals("horizontal")) {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    flippedImage.setPixel(y, x, this.getPixel(y, this.width - x - 1));
                }
            }
        } else if (direction.equals("vertical")) {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    flippedImage.setPixel(y, x, this.getPixel(this.height - y - 1, x));
                }
            }
        }
        return flippedImage;
    }

    public Image rotate(boolean clockwise) {
        Image rotatedImage = new Image(this.width, this.height, this.grayscale);
        if (clockwise) {
            for (int y = 0; y < this.width; y++) {
                for (int x = 0; x < this.height; x++) {
                    rotatedImage.setPixel(y, x, this.getPixel(this.height - x - 1, y));
                }
            }
        } else {
            for (int y = 0; y < this.width; y++) {
                for (int x = 0; x < this.height; x++) {
                    rotatedImage.setPixel(y, x, this.getPixel(x, this.width - y - 1));
                }
            }
        }
        return rotatedImage;
    }

    public CompressedImage compress(boolean tileCompression, boolean pixelCompression) {
        CompressedImage compressedImage = new CompressedImage(this.height, this.width, this.grayscale);
        int tilesCount = compressedImage.tileArray.length;

        if (tileCompression) {
            for (int i = 0; i < tilesCount; i++) {
                if (compressedImage.tileArray[i] == null) {
                    compressedImage.tileArray[i] = this.tileArray[i].clone();
                    for (int j = i + 1; j < tilesCount; j++) {
                        if (compressedImage.tileArray[i].equals(this.tileArray[j])) {
                            compressedImage.tileArray[j] = compressedImage.tileArray[i];
                        }
                    }
                }
            }
        }
        if (pixelCompression) {
            for (int i = 0; i < tilesCount; i++) {
                if (compressedImage.tileArray[i] == null) {
                    compressedImage.tileArray[i] = this.tileArray[i].clone();
                }
                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        Pixel p = compressedImage.tileArray[i].getPixel(y, x);
                        for (int y1 = 0; y1 < 4; y1++) {
                            for (int x1 = 0; x1 < 4; x1++) {
                                if (p.equals(compressedImage.tileArray[i].getPixel(y1, x1))) {
                                    compressedImage.tileArray[i].setPixel(y1, x1, p);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < tilesCount; i++) {
            if (compressedImage.tileArray[i] == null) {
                compressedImage.tileArray[i] = this.tileArray[i].clone();
            }
        }
        return compressedImage;
    }


    @Override
    public Image clone() {
        Image _clone = new Image(this.height, this.width, this.grayscale);
        for (int i = 0; i < this.height * this.width / 16; i++) {
            _clone.tileArray[i] = this.tileArray[i].clone();
        }
        return _clone;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tile)) {
            return false;
        }
        Image _other = (Image) other;
        if (this.width != _other.width || this.height != _other.height || this.grayscale != _other.grayscale) {
            return false;
        }
        for (int i = 0; i < this.width * this.height / 16; i++) {
            if (!this.tileArray[i].equals(_other.tileArray[i])) {
                return false;
            }
        }
        return true;
    }
}