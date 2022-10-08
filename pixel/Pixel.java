/**
 * This class represents a single pixel in an image.
 */
class Pixel {
    private int[] value;
/**
 * 
 * @param grayvalue Creates and initializes a grayscale pixel
 */
    Pixel(int grayvalue) {
        this.value = new int[1];
        this.value[0] = grayvalue;
    }
/**
 * 
 * @param red
 * @param green
 * @param blue
 * Creates and initializes a color pixel
 */
    Pixel(int red, int green, int blue) {
        this.value = new int[3];
        this.value[0] = red;
        this.value[1] = green;
        this.value[2] = blue;
    }

    public int[] getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Pixel)) {
            return false;
        }
        Pixel _other = (Pixel) other;
        if (this.value.length != _other.value.length) {
            return false;
        }
        for (int i = 0; i < this.value.length; i++) {
            if (this.value[i] != _other.value[i]) {
                return false;
            }
        }
        return true;
    }
/**
 * Returns a string representation of the pixel
 */
    @Override
    public String toString() {
        if (this.value.length == 1) {
            return this.value[0] + "";
        }
        return "R" + this.value[0] + "$G" + this.value[1] + "#B" + this.value[2];
    }

    @Override
    public Pixel clone() {
        if (this.value.length == 1) {
            return new Pixel(this.value[0]);
        }
        return new Pixel(this.value[0], this.value[1], this.value[2]);
    }
}