package ch.epfl.cs107.stegano;

import ch.epfl.cs107.Helper;
import ch.epfl.cs107.utils.Bit;
import ch.epfl.cs107.utils.Text;

import java.util.Arrays;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 3.2: </b>Utility class to perform Text Steganography
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public class TextSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private TextSteganography(){}

    // ============================================================================================
    // =================================== EMBEDDING BIT ARRAY ====================================
    // ============================================================================================

    /**
     * Embed a bitmap message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedBitArray(int[][] cover, boolean[] message) {
        // Validate inputs
        assert cover != null && cover.length > 0;
        assert cover[0] != null && cover[0].length > 0;
        assert message != null; // Can be empty

        // Prepare result array with the same dimensions as cover
        int[][] result = new int[cover.length][cover[0].length];

        // Copy cover array into result
        for (int i = 0; i < cover.length; i++) {
            assert cover[i] != null;
            System.arraycopy(cover[i], 0, result[i], 0, cover[i].length);
        }

        // If message is empty, return the result as a copy of cover
        if (message.length == 0) {
            return result;
        }

        // Embed message bits
        int maxCapacity = cover.length * cover[0].length;
        for (int k = 0; k < Math.min(message.length, maxCapacity); k++) {
            int i = k / cover[0].length;
            int j = k % cover[0].length;
            result[i][j] = Bit.embedInLSB(cover[i][j], message[k]);
        }

        return result;
    }




    /**
     * Extract a bitmap from an image
     * @param image Image to extract from
     * @return extracted message
     */
    public static boolean[] revealBitArray(int[][] image) {
        assert image != null;

        // Check for empty image
        if (image.length == 0) {
            return new boolean[0]; // return empty boolean array for empty image
        }

        // Ensure all rows have the same length
        int numCols = image[0].length;
        assert numCols > 0;

        for (int i = 1; i < image.length; i++) {
            assert image[i] != null;
            assert image[i].length == numCols;
        }

        // Prepare the result array based on the total number of elements in the image
        int totalElements = image.length * numCols;
        boolean[] result = new boolean[totalElements];

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < numCols; j++) {
                result[i * numCols + j] = Bit.getLSB(image[i][j]);
            }
        }

        return result;
    }






    // ============================================================================================
    // ===================================== EMBEDDING STRING =====================================
    // ============================================================================================

    /**
     * Embed a String message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedText(int[][] cover, byte[] message) {
        // Validate inputs
        assert cover != null && cover.length > 0;
        assert cover[0] != null;
        assert cover[0].length > 0;

        // Ensure that all rows are of the same length to guarantee the image is rectangular
        int numCols = cover[0].length;
        for (int i = 1; i < cover.length; i++) {
            assert cover[i] != null;
            assert cover[i].length == numCols;
        }

        assert message != null;
        // Optional: Allow empty messages by commenting out the following line
        assert message.length > 0;

        // Convert the message directly to a bit array
        boolean[] messageToBitArray = Text.toBitArray(Text.toString(message));

        // Calculate maximum capacity for embedding based on cover dimensions
        int coverCapacity = cover.length * cover[0].length;

        // If the message is too long, use only as much of it as will fit in the cover
        boolean[] truncatedMessage = messageToBitArray;
        if (messageToBitArray.length > coverCapacity) {
            truncatedMessage = new boolean[coverCapacity];
            System.arraycopy(messageToBitArray, 0, truncatedMessage, 0, coverCapacity);
        }

        // Embed the truncated bit array into the cover image
        return embedBitArray(cover, truncatedMessage);
    }



    /**
     * Extract a String from an image.
     * @param image Image to extract from
     * @return extracted message as a byte array
     */
    public static byte[] revealText(int[][] image) {
        // Validate inputs
        assert image != null && image.length > 0;
        assert image[0] != null && image[0].length > 0;

        // Ensure that all rows are of the same length to guarantee the image is rectangular
        int numCols = image[0].length;
        for (int i = 1; i < image.length; i++) {
            assert image[i] != null;
            assert image[i].length == numCols;
        }

        // Extract bits from the image
        boolean[] revealedBitArray = revealBitArray(image);

        // Convert the extracted bits to a String, then to a byte array
        String message = Text.toString(revealedBitArray);
        return Text.toBytes(message);
    }
}
