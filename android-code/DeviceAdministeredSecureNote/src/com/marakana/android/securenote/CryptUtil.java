
package com.marakana.android.securenote;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class CryptUtil {
    private static final String ENCRYPTION_ALGORITHM = "AES";

    private static final int KEY_SIZE = 256;

    private static final String RANDOM_ALGORITHM = "SHA1PRNG";

    private static final String CHARSET = "UTF-8";

    public static Cipher getCipher(int mode, byte[] secret) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
        // generate an encryption/decryption key from random data seeded with
        // our secret (i.e. password)
        SecureRandom secureRandom = SecureRandom.getInstance(RANDOM_ALGORITHM);
        secureRandom.setSeed(secret);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyGenerator.init(KEY_SIZE, secureRandom);
        Key key = new SecretKeySpec(keyGenerator.generateKey().getEncoded(), ENCRYPTION_ALGORITHM);
        // get a cipher based on the specified encryption algorithm
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        // tell the cipher if it will be used for encryption or decryption
        // (i.e. cipher mode) and give it our key
        cipher.init(mode, key);
        return cipher;
    }

    public static byte[] encrypt(byte[] input, byte[] secret) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException {
        return getCipher(Cipher.ENCRYPT_MODE, secret).doFinal(input);
    }

    public static byte[] decrypt(byte[] input, byte[] secret) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException {
        return getCipher(Cipher.DECRYPT_MODE, secret).doFinal(input);
    }

    public static String encrypt(String input, String secret) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException {
        return Base64.encodeToString(encrypt(input.getBytes(CHARSET), secret.getBytes(CHARSET)),
                Base64.DEFAULT);
    }

    public static String decrypt(String input, String secret) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException {
        return new String(decrypt(Base64.decode(input.getBytes(), Base64.DEFAULT), secret
                .getBytes(CHARSET)), CHARSET);
    }

    public static OutputStream encrypt(OutputStream out, byte[] secret) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        return new CipherOutputStream(out, getCipher(Cipher.ENCRYPT_MODE, secret));
    }

    public static InputStream decrypt(InputStream in, byte[] secret) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        return new CipherInputStream(in, CryptUtil.getCipher(Cipher.DECRYPT_MODE, secret));
    }
}
