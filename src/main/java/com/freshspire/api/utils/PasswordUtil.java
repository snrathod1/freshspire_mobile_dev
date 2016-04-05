package com.freshspire.api.utils;

import com.freshspire.api.model.User;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Random;

/**
 * Created by aadisriram on 2/20/16.
 */
public class PasswordUtil {

    private static int ITERATIONS = 500;

    private static int KEY_LENGTH = 32;

    /**
     * Generates a random 16-bit byte array for hashing user passwords.
     *
     * TODO does it still have equals signs in it?
     * @return The 16-bit salt
     */
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new Random().nextBytes(salt);
        return salt;
    }

    /**
     * Generates a random API key for a User object.
     *
     * TODO make API key only contain letters (currently contains '=' characters)
     * @return The API key
     */
    public static String generateApiKey() {
        return byteToString(generateSalt());
    }

    /**
     * Generates a random 24-character salt string of alphanumberic characters for User password hashing.
     *
     * @return The salt string
     */
    public static String generateSaltString() {
        return byteToString(generateSalt());
    }

    /**
     * Transforms a byte array into a string.
     * @param input The byte array to transform
     * @return The transformed string
     */
    public static String byteToString(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    /**
     * Transforms a string into a byte array.
     * @param input The string to transform
     * @return The transformed byte array
     */
    public static byte[] stringToByte(String input) {
        if (Base64.isBase64(input)) {
            return Base64.decodeBase64(input);
        } else {
            return Base64.encodeBase64(input.getBytes());
        }
    }

    /**
     * Generates the salted hash of an input string. Returns the original string if the hashing fails.
     *
     * @param input The input string to salt and hash
     * @param salt The salt to use when hashing
     * @return The hash of the input string, or the original string if the hash failed
     */
    public static String encryptString(String input, String salt) {
        KeySpec spec = new PBEKeySpec(input.toCharArray(), stringToByte(salt), ITERATIONS, KEY_LENGTH*8);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return byteToString(f.generateSecret(spec).getEncoded());
        } catch (Exception ex) {
            return input;
        }
    }

    /**
     * Checks if a given password string is the correct password for a user
     * @param user The user
     * @param password The password to test
     * @return If the password is correct for the user
     */
    public static boolean isCorrectPasswordForUser(User user, String password) {
        return PasswordUtil.encryptString(password, user.getSalt()).equals(user.getPassword());
    }
}
