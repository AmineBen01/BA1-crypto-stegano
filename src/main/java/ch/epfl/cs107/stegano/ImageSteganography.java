package ch.epfl.cs107.stegano;

import ch.epfl.cs107.Helper;
import ch.epfl.cs107.utils.Bit;
import ch.epfl.cs107.utils.Image;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 3.1: </b>Utility class to perform Image Steganography
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ImageSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private ImageSteganography(){}

    // ============================================================================================
    // ================================== EMBEDDING METHODS =======================================
    // ============================================================================================

    /**
     * Embed an ARGB image on another ARGB image (the cover)
     * @param cover Cover image
     * @param argbImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedARGB(int[][] cover, int[][] argbImage, int threshold) {
        assert cover != null && cover.length > 0 && argbImage != null && argbImage.length > 0;
        int[][] grayImage = Image.toGray(argbImage);
        return embedGray(cover, grayImage, threshold);
    }

    /**
     * Embed a Gray scaled image on another ARGB image (the cover)
     * @param cover Cover image
     * @param grayImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedGray(int[][] cover, int[][] grayImage, int threshold) {
        // Check that cover and grayImage are non-null and contain at least one row and one column
        assert cover != null && cover.length > 0 && cover[0].length > 0 : "Cover image must be non-null and non-empty";
        assert grayImage != null && grayImage.length > 0 && grayImage[0].length > 0 : "Gray image must be non-null and non-empty";

        // Check that each row in grayImage has consistent lengths (image should be rectangular)
        int grayWidth = grayImage[0].length;
        for (int[] row : grayImage) {
            assert row.length == grayWidth : "All rows in gray image must have the same length";
        }

        // Convert grayImage to binary based on the threshold
        boolean[][] booleans = Image.toBinary(grayImage, threshold);

        // Embed binary image into the cover image
        return embedBW(cover, booleans);
    }



    /**
     * Embed a binary image on another ARGB image (the cover).
     *
     * @param cover Cover image represented as a 2D array of integers (ARGB format).
     * @param load  Embedded image represented as a 2D array of booleans.
     * @return ARGB image with the image embedded on the cover.
     */
    public static int[][] embedBW(int[][] cover, boolean[][] load) {
        // Assert that cover and load are non-null and have positive dimensions
        assert cover != null;
        assert cover.length > 0;
        assert cover[0] != null;
        assert cover[0].length > 0;

        assert load != null;
        assert load.length > 0;
        assert load[0] != null;
        assert load[0].length > 0;

        // Assert that cover dimensions are at least as large as load dimensions
        assert cover.length >= load.length;
        assert cover[0].length >= load[0].length;

        int[][] result = new int[cover.length][cover[0].length];

        for (int i = 0; i < cover.length; i++) {
            for (int j = 0; j < cover[i].length; j++) {
                if (i < load.length && j < load[i].length) {
                    // Embed the LSB only if within the bounds of the load image
                    result[i][j] = Bit.embedInLSB(cover[i][j], load[i][j]);
                } else {
                    // Copy the original pixel if it's outside the load dimensions
                    result[i][j] = cover[i][j];
                }
            }
        }

        return result;
    }




    // ============================================================================================
    // =================================== REVEALING METHODS ======================================
    // ============================================================================================

    /**
     * Reveal a binary image from a given image
     * @param image Image to reveal from
     * @return binary representation of the hidden image
     */
    public static boolean[][] revealBW(int[][] image) {
        assert image != null && image.length > 0 && image[0] != null && image.length == image[0].length;
        boolean[][] result = new boolean[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                result[i][j] = Bit.getLSB(image[i][j]);
            }
        }
        return result;
    }

}
