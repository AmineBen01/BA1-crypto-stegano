package ch.epfl.cs107.utils;

import java.nio.charset.StandardCharsets;

import static ch.epfl.cs107.utils.Bit.*;

/**
 * <b>Task 1.2: </b>Utility class to manipulate texts ({@link String}) objects.
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Text {
    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Text() {
    }
    // ============================================================================================
    // ==================================== STRING MANIPULATION ===================================
    // ============================================================================================
    /**
     * Convert a given <b>String</b> into a <b>byte[]</b> following the <b>UTF-8</b> convention
     *
     * @param str String to convert
     * @return bytes representation of the String in the <b>UTF-8</b> format
     */
    public static byte[] toBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }
    /**
     * Convert a given String into a boolean array representation
     *
     * @param str String to convert
     * @return <b>UTF-8</b> representation of the string in the <b>bit array</b> format
     * @implNote the bit array should be based on an <b>UTF-8</b> representation
     */
    public static boolean[] toBitArray(String str) {
        assert str != null;
        byte[] bytes = toBytes(str); // str convertie en tableau de byte selon UTF-8
        boolean[] result = new boolean[bytes.length * Byte.SIZE];
        int lastIndexOfAByte = Byte.SIZE - 1;
        for (int i = 0; i < bytes.length; i++) { //itération dans le tableau de bytes
            for (int j = 0; j < Byte.SIZE; j++) { //itération dans un byte
                result[Byte.SIZE * i + j] = getXthBit(bytes[i], lastIndexOfAByte - j);
            } //on commence par extraire le premier bit, celui qui est tout à droite (lastIndexOfAByte - j)
        }
        return result;
    }
    /**
     * Convert a given <b>byte[]</b> into a <b>String</b> following the <b>UTF-8</b> convention
     *
     * @param bytes String in the byte array format
     * @return String representation using the {@link String} type
     */
    public static String toString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
    /**
     * Convert a given bit array to a String
     *
     * @param bitArray <b>UTF-8</b> compatible bit array
     * @return <b>UTF-8 String</b> representation of the bit array
     */
    public static String toString(boolean[] bitArray) {
        assert bitArray != null;
        byte[] bytes = new byte[bitArray.length / Byte.SIZE];
        boolean[] oneByteInBool = new boolean[Byte.SIZE];
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < oneByteInBool.length; j++) {
                oneByteInBool[j] = bitArray[Byte.SIZE * i + j]; //on extrait le byte i de bitArray sous sa forme bool
            }
            bytes[i] = toByte(oneByteInBool); //on transforme ce byteInBool en Byte et on le met dans le byte[]
        }
        return toString(bytes);
    }
}
