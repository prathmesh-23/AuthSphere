package io.securepath.authsphere.cryptography;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESEncryption {

    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int IV_LENGTH = 12;

    private static final String AES_KEY = "A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6";// bytes

    // ---------------- AES-256/GCM ----------------

    public static String encrypt(String plainText) {
        try {
            byte[] iv = generateIV();
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), AES);

            Cipher cipher = Cipher.getInstance(AES_GCM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes());

            // Store IV + ciphertext together (IV is needed for decryption)
            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    public static String decrypt(String encryptedText) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encryptedText);

        // Extract IV and ciphertext
        byte[] iv = new byte[IV_LENGTH];
        byte[] cipherText = new byte[decoded.length - IV_LENGTH];
        System.arraycopy(decoded, 0, iv, 0, IV_LENGTH);
        System.arraycopy(decoded, IV_LENGTH, cipherText, 0, cipherText.length);

        SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), AES);

        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }


    public static String generateStrongKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // AES-256
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    public static String hashPasswordWithKey(String password, String userKey) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        try {
            // Combine password + userKey before hashing
            String combined = password + userKey;

            // Argon2id hashing (iterations=3, memory=64MB, parallelism=1)
            return argon2.hash(3, 65536, 1, combined);
        } finally {
            argon2.wipeArray(password.toCharArray()); // clear sensitive data
            argon2.wipeArray(userKey.toCharArray());  // clear sensitive data
        }
    }

    public static boolean verifyPasswordWithKey(String storedHash, String password, String userKey) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String combined = password + userKey;
        return argon2.verify(storedHash, combined);
    }

}
