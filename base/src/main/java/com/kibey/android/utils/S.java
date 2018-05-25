package com.kibey.android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Hashtable;

/**
 * 声音加密类
 * <p/>
 * Created by mchwind on 15/1/31.
 */
public class S {
    /**
     * 密钥
     */
    public static final String DEFAULT__SECRET = "000102030405060708090a0b0c0d0e0f";


    public static final String tag = "decrypt_time";
    private static final int DEFAULT_SIZE = 2000;
    private static final int DEFAULT_SECRET = 255;


    private static Hashtable<String, Integer> sStatus = new Hashtable<>();

    /**
     * echo 加密
     *
     * @param f
     */
    public static boolean echoE(String f) {
        Logs.d(tag + " encode file:" + f);
        return s(f);
//        boolean flag = aes(f, true);
    }

    /**
     * echo 解密
     *
     * @param f
     */
    public static boolean echoD(String f) {
        Logs.d(tag + " decode file:" + f);

//        boolean flag = aes(f, false);
        s(f);
        return true;
    }


    public static boolean s(String f) {
        return s(f, DEFAULT_SIZE, DEFAULT_SECRET);
    }

    public static boolean aes(String f, boolean eOrD) {
        return aes(f, DEFAULT_SIZE, DEFAULT__SECRET, eOrD);
    }


    /**
     * @param f      本地文件地址
     * @param size   加密尺寸，单位k
     * @param secret 密钥
     * @param eOrD   true加密;false解密
     */
    public static boolean aes(String f, int size, String secret, boolean eOrD) {

        long start = System.currentTimeMillis();
        try {
            boolean isEncrypt = true;
            File file = new File(f);
            File data16File = new File(file.getParent() + File.separator + Md5Util.makeMd5Sum(f));
            if (!data16File.exists()) {
                try {
                    if (!eOrD) {
                        file.delete();
                        return false;// 加密文件丢失
                    }
                    isEncrypt = false;
                    data16File.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            RandomAccessFile dataAccessFile = new RandomAccessFile(data16File, "rw");
            RandomAccessFile accessFile = new RandomAccessFile(f, "rw");
            byte[] b = new byte[1024];
            byte[] eb = new byte[1024 + 16];
            if (!isEncrypt) { // 没有加密过，设置加密文件长度
                dataAccessFile.setLength(size * eb.length);
            }
            int n;
            int count = 0;
            while ((n = accessFile.read(b)) != -1) {
                if (count < size) {
                    try {
                        if (eOrD) {
                            eb = AESUtils.encrypt(secret, b);
                            dataAccessFile.seek(count * eb.length);
                            dataAccessFile.write(eb, 0, eb.length);

                            accessFile.seek(count * b.length);
                            accessFile.write(eb, 0, n);
                        } else {
                            int n16 = dataAccessFile.read(eb);
                            b = AESUtils.decrypt(secret, eb);
                            accessFile.seek(count * b.length);
                            accessFile.write(b, 0, n);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    break;
                }

                count++;
            }
            dataAccessFile.close();
            accessFile.close();
            Logs.i("decrypt_time___aes2:" + (System.currentTimeMillis() - start));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean s(String f, int size, int secret) {
        long start = System.currentTimeMillis();
        try {
            File file = new File(f);
            RandomAccessFile accessFile = new RandomAccessFile(f, "rw");
            byte[] b = new byte[1024];
            int n;
            int count = 0;
            while ((n = accessFile.read(b)) != -1) {
                if (count < size) {
                    for (int i = 0; i < b.length; i++) {//历遍字符数组
                        b[i] = (byte) (b[i] ^ secret);//对数组每个元素进行异或运算
                    }
                    accessFile.seek(count * 1024);
                    accessFile.write(b, 0, n);
                } else {
                    break;
                }

                count++;
            }
            accessFile.close();
            Logs.i("decrypt_time___2:" + (System.currentTimeMillis() - start));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
