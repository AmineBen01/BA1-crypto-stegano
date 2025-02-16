package ch.epfl.cs107.crypto;

/**
 * <b>Task 2: </b>Utility class to decrypt a given cipher text.
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Decrypt {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Decrypt(){}

    // ============================================================================================
    // ================================== CAESAR'S ENCRYPTION =====================================
    // ============================================================================================

    /**
     * Method to decode a byte array message using a single character key
     * <p>
     * @param cipher Cipher message to decode
     * @param key Key to decode with
     * @return decoded message
     */
    public static byte[] caesar(byte[] cipher, byte key) {
        assert cipher != null;
        byte[] plainText = new byte[cipher.length];

        for (int i = 0; i < cipher.length; i++) {
            plainText[i] = (byte) ((cipher[i] - key + 256) % 256);
        }

        return plainText;
    }


    // ============================================================================================
    // =============================== VIGENERE'S ENCRYPTION ======================================
    // ============================================================================================

    /**
     * Method to decode a byte array using a byte array keyword
     * @param cipher Cipher message to decode
     * @param keyword Key to decode with
     * @return decoded message
     */
    public static byte[] vigenere(byte[] cipher, byte[] keyword) {
        assert cipher != null && keyword != null && keyword.length > 0;
        byte[] plainText = new byte[cipher.length];

        for (int i = 0; i < cipher.length; i++) {
            plainText[i] = (byte) ((cipher[i] - keyword[i % keyword.length] + 256) % 256);
        }

        return plainText;
    }



    // ============================================================================================
    // =================================== CBC'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method to decode a CBC-encrypted cipher message
     * @param cipher message to decode
     * @param iv the initialization vector used to start the chain decoding
     * @return decoded message
     */
    public static byte[] cbc(byte[] cipher, byte[] iv) {
        // Check for null values and ensure the IV length matches the block size
        assert cipher != null && cipher.length > 0;
        assert iv != null && iv.length > 0;

        int blockSize = iv.length; // Block size derived from IV length
        assert blockSize > 0 && cipher.length % blockSize == 0; // Cipher text must be a multiple of block size

        byte[] plainText = new byte[cipher.length];
        byte[] previousBlock = new byte[blockSize];

        // Copy IV into previousBlock for the initial XOR operation
        System.arraycopy(iv, 0, previousBlock, 0, blockSize);

        for (int i = 0; i < cipher.length; i += blockSize) {
            int blockLength = Math.min(blockSize, cipher.length - i); // Get length of current block

            // Temporary array to hold the current cipher block before updating previousBlock
            byte[] currentCipherBlock = new byte[blockLength];
            System.arraycopy(cipher, i, currentCipherBlock, 0, blockLength);

            // XOR the cipher block with previousBlock to get the plaintext block
            for (int j = 0; j < blockLength; j++) {
                plainText[i + j] = (byte) (cipher[i + j] ^ previousBlock[j]);
            }

            // Update previousBlock with the current cipher block for the next iteration
            System.arraycopy(currentCipherBlock, 0, previousBlock, 0, blockLength);
        }

        return plainText;
    }




    // ============================================================================================
    // =================================== XOR'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method to decode xor-encrypted ciphers
     * @param cipher text to decode
     * @param key the byte we will use to XOR
     * @return decoded message
     */
    public static byte[] xor(byte[] cipher, byte key) {
        assert cipher != null;
        byte[] plainText;
        plainText = Encrypt.xor(cipher, key);

        return plainText;
    }

    // ============================================================================================
    // =================================== ONETIME'S PAD ENCRYPTION ===============================
    // ============================================================================================

    /**
     * Method to decode otp-encrypted ciphers
     * @param cipher text to decode
     * @param pad the one-time pad to use
     * @return decoded message
     */
    public static byte[] oneTimePad(byte[] cipher, byte[] pad) {
        // Check that pad is not null and has a valid length
        assert pad != null && pad.length > 0;

        // Assert that cipher is not null
        assert cipher != null;

        // If the cipher is empty, return an empty plaintext array
        if (cipher.length == 0) {
            return new byte[0]; // Return an empty array if cipher is empty
        }

        // Check that the length of the pad matches the length of the cipher
        assert pad.length == cipher.length;

        // Create an array for the plaintext
        byte[] plainText = new byte[cipher.length];

        // XOR operation for decryption
        for (int i = 0; i < cipher.length; i++) {
            plainText[i] = (byte) (cipher[i] ^ pad[i]); // XOR each byte of cipher with corresponding pad byte
        }

        return plainText; // Return the decrypted plaintext
    }

}
