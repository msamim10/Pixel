/*
 * This class represents a square block of 4x4 pixels. The pixels are not stored in a 2D array though, but in a
1D array of size 16 (the ordering of the pixels is row by row from top to bottom and within each row is
from left to right).
 */
class Tile {
    private Pixel[] block;

    Tile() {
        this.block = new Pixel[16];
    }

    Pixel getPixel(int y, int x) {
        return this.block[y * 4 + x];
    }

    void setPixel(int y, int x, Pixel p) {
        this.block[y * 4 + x] = p;
    }
// Indicates whether some other tile is equal to this one. All pixels must be equal for the two tiles to be considered equal.
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tile)) {
            return false;
        }
        Tile _other = (Tile) other;
        for (int i = 0; i < 16; i++) {
            if (!this.block[i].equals(_other.block[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String formattedBlock = "";
        for (int i = 0; i < 15; i++) {
            formattedBlock += this.block[i].toString() + ",";
        }
        formattedBlock += this.block[15].toString();
        return formattedBlock;
    }
/*Creates and returns a copy of this Tile object. The meaning of "copy" is that, for any Tile x, the
expressions x.clone()!=x and x.clone().equals(x) will both be true. In other words,
the clone is a deep copy of the original and, therefore, no memory areas are shared between the
original and clone
 */
    @Override
    public Tile clone() {
        Tile _clone = new Tile();
        for (int i = 0; i < 16; i++) {
            _clone.block[i] = this.block[i].clone();
        }
        return  _clone;
    }
}
