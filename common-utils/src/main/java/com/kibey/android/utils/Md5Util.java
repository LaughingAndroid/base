package com.kibey.android.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Laughing Hu
 * @version V1.0
 * @date 2012-5-28
 * @description MD5校验码生成工具
 */
public class Md5Util {
    /**
     * 生成md5校验码
     *
     * @param srcContent
     * @return
     */
    public static String makeMd5Sum(byte[] srcContent) {
        if (srcContent == null) {
            return null;
        }

        String strDes = null;

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(srcContent);
            strDes = bytes2Hex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String makeMd5Sum(String str) {
        return makeMd5Sum(str.getBytes());
    }

    private static String bytes2Hex(byte[] byteArray) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (byteArray[i] >= 0 && byteArray[i] < 16) {
                strBuf.append("0");
            }
            strBuf.append(Integer.toHexString(byteArray[i] & 0xFF));
        }
        return strBuf.toString();
    }

    public static String encryptToSHA(byte[] str) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            alga.update(str);
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String rs = bytes2Hex(digesta);
        return rs;
    }

    public static String makeMd5SumTwo(byte[] bytes) {
        String result = makeMd5Sum(bytes);
        result = Md5Util.makeMd5Sum("d&3j^" + result);
        return result;
    }
}
