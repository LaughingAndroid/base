package com.kibey.android.utils;

import android.text.TextUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Usage:
 * <p/>
 * <pre>
 * String crypto = SimpleCrypto.encrypt(masterpassword, cleartext)
 * ...
 * String cleartext = SimpleCrypto.decrypt(masterpassword, crypto)
 * </pre>
 *
 * @author ferenc.hechler
 */
public class AESUtils {
    static final String tag = "aes_";

    /**
     * 加密
     *
     * @param seed
     * @param cleartext
     * @return
     * @throws Exception
     */
    public static String encrypt(String seed, String cleartext)
            throws Exception {
        if (TextUtils.isEmpty(seed))
            return "";
        if (TextUtils.isEmpty(cleartext))
            return "";
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, cleartext.getBytes());
//        Logs.d(tag + " length:" + result.length);
//        Logs.d(tag + " base64:" + Base64.encodeToString(result, Base64.NO_WRAP));
//        Logs.d(tag + " jinzhi:" + toHex(result));
        return toHex(result);
    }

    public static byte[] encrypt(String key, byte[] data) throws Exception {
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] result = encrypt(rawKey, data);
        return result;
    }


    /**
     * 解密
     *
     * @param seed
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static String decrypt(String seed, String encrypted)
            throws Exception {
        if (TextUtils.isEmpty(seed))
            return "";
        if (TextUtils.isEmpty(encrypted))
            return "";
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    public static byte[] decrypt(String key, byte[] encrypted)
            throws Exception {
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] result = decrypt(rawKey, encrypted);
        return result;
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
//        Logs.e("aes_origin_seed:" + new String(seed));
//        Logs.e("aes_useed_key:" + new String(raw));
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    //php服务器端加密解密
    public static String encode(String key, String input) {
        byte[] result = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            result = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
//        Logs.d(tag + " base64:" + Base64.encodeToString(result, Base64.NO_WRAP));
        return toHex(result);
    }

    public static String decode(String key, byte[] data) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(data);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(output);
    }

    public static String decode(String key, String input) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(toByte(input));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
		if (null == output) {
			return null;
		}

		return new String(output);
    }
    // end php服务器端加密解密
}
