package ch.epfl.cs107.utils;

import ch.epfl.cs107.Helper;

import static ch.epfl.cs107.utils.Bit.*;

/**
 * <b>Task 1.3: </b>Utility class to manipulate ARGB images
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Image {
    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Image() {
    }
    // ============================================================================================
    // ==================================== PIXEL MANIPULATION ====================================
    // ============================================================================================
    /**
     * Build a given pixel value from its respective components
     *
     * @param alpha alpha component of the pixel
     * @param red   red component of the pixel
     * @param green green component of the pixel
     * @param blue  blue component of the pixel
     * @return packed value of the pixel
     */
    public static int argb(byte alpha, byte red, byte green, byte blue) {
        int result = 0;
        result |= (blue & 0xFF); //transforms blue into unsigned and put it in result
        result |= (green & 0xFF) << 8; //same + shifts green unsigned 8 bits to the left
        result |= (red & 0xFF) << 16;
        result |= (alpha & 0xFF) << 24;
        return result;
    }
    /**
     * Extract a component of a given pixel
     *
     * @param pixel     packed value of the pixel
     * @param component name of the component ('a', 'r', 'g', 'b')
     * @return the component of the pixel
     */
    public static byte argbComponent(int pixel, char component) {
        /*byte result = 0;
        int start = switch (component) {
            case 'a' -> Byte.SIZE * 3;
            case 'r' -> Byte.SIZE * 2;
            case 'g' -> Byte.SIZE;
            default -> 0;
        };
        for (int i = 0; i < Byte.SIZE; ++i) {
            if (getXthBit(pixel, i + start)) {
                result += (byte) Math.pow(2, i);
            }
        }
        return result;*/
        return switch (component) {
            case 'b' -> (byte) ((pixel >> 0) & 0xFF);
            case 'g' -> (byte) ((pixel >> 8) & 0xFF);
            case 'r' -> (byte) ((pixel >> 16) & 0xFF);
            case 'a' -> (byte) ((pixel >> 24) & 0xFF);
            default -> 0;
        };
    }
    /**
     * Extract the alpha component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the alpha component of the pixel
     */
    public static byte alpha(int pixel) {
        return argbComponent(pixel, 'a');
    }
    /**
     * Extract the red component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the red component of the pixel
     */
    public static byte red(int pixel) {
        return argbComponent(pixel, 'r');
    }
    /**
     * Extract the green component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the green component of the pixel
     */
    public static byte green(int pixel) {
        return argbComponent(pixel, 'g');
    }
    /**
     * Extract the blue component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the blue component of the pixel
     */
    public static byte blue(int pixel) {
        return argbComponent(pixel, 'b');
    }
    /**
     * Compute the gray scale of the given pixel
     *
     * @param pixel packed value of the pixel
     * @return gray scaling of the given pixel
     */
    public static int gray(int pixel) {
        int result = (Image.green(pixel) & 0xFF) + (Image.red(pixel) & 0xFF) + (Image.blue(pixel) & 0xFF);
        result /= 3;
        return result;
    }
    /**
     * Compute the binary representation of a given pixel.
     *
     * @param gray      gray scale value of the given pixel
     * @param threshold when to consider a pixel white
     * @return binary representation of a pixel
     */
    public static boolean binary(int gray, int threshold) {
        assert (gray <= 255) && (gray >= 0);
        return gray >= threshold;
    }
    // ============================================================================================
    // =================================== IMAGE MANIPULATION =====================================
    // ============================================================================================
    /**
     * Build the gray scale version of an ARGB image
     *
     * @param image image in ARGB format
     * @return the gray scale version of the image
     * @throws AssertionError if the input image is null or invalid
     */
    /**
     * Convert a 2D ARGB image array to grayscale.
     * @param image 2D array representing an ARGB image.
     * @return 2D array representing the grayscale image.
     */
    public static int[][] toGray(int[][] image) {
        assert image != null;
        assert image.length == 0 || (image[0] != null && image[0].length >= 0);

        // Handle empty image by returning an empty array
        if (image.length == 0 || image[0].length == 0) {
            return new int[0][0];
        }

        // Initialize result array with the same dimensions as the input
        int[][] result = new int[image.length][];
        for (int i = 0; i < image.length; i++) {
            // Handle null rows by setting empty rows in the result
            if (image[i] == null) {
                result[i] = new int[0];
                continue;
            }

            // Initialize each row of result and convert each pixel to grayscale
            result[i] = new int[image[i].length];
            for (int j = 0; j < image[i].length; j++) {
                result[i][j] = gray(image[i][j]);  // Convert pixel to grayscale
            }
        }
        return result;
    }

    /**
     * Build the binary representation of an image from the gray scale version
     *
     * @param image     Image in gray scale representation
     * @param threshold Threshold to consider
     * @return binary representation of the image
     */
    public static boolean[][] toBinary(int[][] image, int threshold) {
        // Assertions for debugging
        assert image != null;
        assert image.length == 0 || (image[0] != null && image[0].length >= 0);

        // Handle empty image by returning an empty boolean array
        if (image.length == 0 || image[0].length == 0) {
            return new boolean[0][0];
        }

        // Initialize result array with the same dimensions as the input
        boolean[][] result = new boolean[image.length][];
        for (int i = 0; i < image.length; i++) {
            // Handle null rows by setting empty rows in the result
            if (image[i] == null) {
                result[i] = new boolean[0];
                continue;
            }

            // Initialize each row of result and apply threshold for binary conversion
            result[i] = new boolean[image[i].length];
            for (int j = 0; j < image[i].length; j++) {
                result[i][j] = binary(image[i][j], threshold);  // Convert pixel to binary based on threshold
            }
        }
        return result;
    }
    /**
     * Build an ARGB image from the gray-scaled image
     *
     * @param image grayscale image representation
     * @return <b>gray ARGB</b> representation
     * @implNote The result of this method will a gray image, not the original image
     */
    public static int[][] fromGray(int[][] image) {
        assert image != null;
        assert image.length == 0 || (image[0] != null && image[0].length >= 0);

        // Handle empty image by returning an empty array
        if (image.length == 0 || image[0].length == 0) {
            return new int[0][0];
        }

        // Initialize result array with the same dimensions as the input
        int[][] result = new int[image.length][];
        for (int i = 0; i < image.length; i++) {
            // Handle null rows by setting empty rows in the result
            if (image[i] == null) {
                result[i] = new int[0];
                continue;
            }

            // Initialize each row of result and convert grayscale values to ARGB
            result[i] = new int[image[i].length];
            for (int j = 0; j < image[i].length; j++) {
                int gray = image[i][j];
                result[i][j] = argb((byte) 0xFF, (byte) gray, (byte) gray, (byte) gray);  // Convert grayscale to ARGB
            }
        }
        return result;
    }
    /**
     * Build an ARGB image from the binary image
     *
     * @param image binary image representation
     * @return <b>black and white ARGB</b> representation
     * @implNote The result of this method will a black and white image, not the original image
     */
    public static int[][] fromBinary(boolean[][] image) {
        assert image != null;
        assert image.length == 0 || (image[0] != null && image[0].length >= 0);

        // Handle empty image by returning an empty array
        if (image.length == 0 || image[0].length == 0) {
            return new int[0][0];
        }

        // Initialize result array with the same dimensions as the input
        int[][] result = new int[image.length][];
        for (int i = 0; i < image.length; i++) {
            // Handle null rows by setting empty rows in the result
            if (image[i] == null) {
                result[i] = new int[0];
                continue;
            }

            // Initialize each row of result and convert binary values to ARGB
            result[i] = new int[image[i].length];
            for (int j = 0; j < image[i].length; j++) {
                int gray = image[i][j] ? 255 : 0;  // 255 for true, 0 for false
                result[i][j] = argb((byte) 0xFF, (byte) gray, (byte) gray, (byte) gray);  // Convert to ARGB format
            }
        }
        return result;
    }
}
