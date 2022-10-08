
/**
 * class CompressedImage represents an image that has a reduced memory footprint thanks to exploiting the fact
that adjacent pixels may have the same value and therefore it’s unnecessary to keep full copies of
them in memory
 */
class CompressedImage{
    public Tile[] tileArray;
    private int width;
    private int height;
    public boolean grayscale;
/**
 * 
 * @param height The height of the image measured in pixels
 * @param width The width of the image measured in pixels
 * @param grayscale Indicates the type of the image
 */
    CompressedImage(int height, int width, boolean grayscale) {
        this.height = height;
        this.width = width;
        this.grayscale = grayscale;
        int tilesCount = (int)Math.ceil(height * width / 16);
        this.tileArray = new Tile[tilesCount];
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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tile)) {
            return false;
        }
        CompressedImage _other = (CompressedImage) other;
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