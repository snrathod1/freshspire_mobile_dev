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

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new Random().nextBytes(salt);
        return salt;
    }

    public static String generateApiKey() {
        return byteToString(generateSalt());
    }

    public static String generateSaltString() {
        return byteToString(generateSalt());
    }

    public static String byteToString(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    public static byte[] stringToByte(String input) {
        if (Base64.isBase64(input)) {
            return Base64.decodeBase64(input);
        } else {
            return Base64.encodeBase64(input.getBytes());
        }
    }

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
     * @param user
     * @param password
     * @return If the password is correct for the user
     */
    public static boolean isCorrectPasswordForUser(User user, String password) {
        return PasswordUtil.encryptString(password, user.getSalt()).equals(user.getPassword());
    }
}
