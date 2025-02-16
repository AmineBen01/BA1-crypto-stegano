package ch.epfl.cs107.utils;

/**
 * <b>Task 1.1: </b>Utility class to manipulate bits
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Bit {
    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Bit() {
    }
    // ============================================================================================
    // ==================================== BIT MANIPULATION ======================================
    // ============================================================================================
    /**
     * Embed a bit in a given integer bits
     * <p>
     *
     * @param value value to embed in
     * @param m     <code>true</code> to embed 1, <code>false</code> to embed 0
     * @param pos   position of the bit to change
     * @return embedded value
     */
    public static int embedInXthBit(int value, boolean m, int pos) {
        assert pos >= 0 && pos < 32;
        if (getXthBit(value, pos) != m) { //si le bit est différent de m, on le modifie
            if (m) {
                value = (int) ((int) value + Math.pow(2, pos));
            } else {
                value = (int) ((int) value - Math.pow(2, pos));
            } //on ajoute ou on retire 2^pos au nombre en fonction de si 0 ou 1 présent à cette position
        }
        return value;
    }
    /**
     * Embed a bit in the "least significant bit" (LSB)
     * <p>
     *
     * @param value value to embed in
     * @param m     <code>true</code> to embed 1, <code>false</code> to embed 0
     * @return embedded value
     */
    public static int embedInLSB(int value, boolean m) {
        return embedInXthBit(value, m, 0);
    }
    /**
     * Extract a bit in a given position from a given value
     * <p>
     *
     * @param value value to extract from
     * @param pos   position of the bit to extract
     * @return <code>true</code> if the bit is '1' and <code>false</code> otherwise
     */
    public static boolean getXthBit(int value, int pos) {
        assert pos >= 0 && pos < 32;
        return (value & (1 << pos)) != 0; //On fait AND entre value et 1 décalé vers la gauche de pos
    }
    /**
     * Extract the 'least significant bit' from a given value
     * <p>
     *
     * @param value value to extract from
     * @return <code>true</code> if the bit is '1' and <code>false</code> otherwise
     */
    public static boolean getLSB(int value) {
        return getXthBit(value, 0);
    }
    // ============================================================================================
    // ==================================== BYTE MANIPULATION =====================================
    // ============================================================================================
    /**
     * Convert a byte value to the bit array representation.
     * <p>
     * Suppose we have the following input <b><code>0b00_11_01_10</code></b>. We can expect this function
     * to return the following array :
     * <b>[<code>false</code>,
     * <code>false</code>,
     * <code>true</code>,
     * <code>true</code>,
     * <code>false</code>,
     * <code>true</code>,
     * <code>true</code>,
     * <code>false</code>]</b>
     *
     * @param value byte representation of the value
     * @return bit array representation of the value
     */
    // 1 byte contient 8 bits, donc tu peux representer 2^8 valeurs, (-128 à 127)
    public static boolean[] toBitArray(byte value) {
        boolean[] result = new boolean[Byte.SIZE];
        for (int i = 0; i < result.length; i++) {
            result[Byte.SIZE - 1 - i] = ((value >> i) & 1) == 1;
        }
        return result;
    }
    /**
     * Convert a boolean array to a byte
     * <p>
     * Suppose we have the following input :
     * <b>[<code>false</code>,
     * <code>false</code>,
     * <code>true</code>,
     * <code>true</code>,
     * <code>false</code>,
     * <code>true</code>,
     * <code>true</code>,
     * <code>false</code>]</b>
     * We can expect this function to return the following byte : <b><code>0b00_11_01_10</code></b>.
     *
     * @param bitArray bit array representation to convert, must be 8 elements
     * @return the byte representation of the bit array
     */
    public static byte toByte(boolean[] bitArray) {
        assert bitArray != null && bitArray.length == Byte.SIZE;

        byte result = 0;
        for (int i = 0; i < Byte.SIZE; i++) {
            if (bitArray[i]) {
                result |= (byte) (1 << (Byte.SIZE - 1 - i));
            }
        }
        return result;
    }
}
