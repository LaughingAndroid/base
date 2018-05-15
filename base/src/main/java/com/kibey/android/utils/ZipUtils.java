package com.kibey.android.utils;


import net.lingala.zip4j.core.ZipFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Java utils 实现的Zip工具
 *
 * @author once
 */
public class ZipUtils {
    /**
     * 解压缩功能.
     * 将zipFile文件解压到folderPath目录下.
     *
     * @throws Exception
     */
    public static boolean unZipFile(String zipSourceFile, String folderPath, String password) {
        ZipFile zipFile;
        try {
            // zip
            zipFile = new ZipFile(zipSourceFile);
            if (password != null) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(folderPath);
            return true;
        } catch (Exception e) {
            if (Logs.IS_DEBUG) {
                e.printStackTrace();
            }
            try {
                // try gzip
                unGzipFile(zipSourceFile, folderPath);
                return true;
            } catch (Exception e1) {
                if (Logs.IS_DEBUG) {
                    e1.printStackTrace();
                }
            }
        } finally {
        }
        return false;
    }


    public static String unGzipFile(String srcFile, String folderPath) throws Exception {
        FileInputStream is = null;
        FileOutputStream os = null;
        InputStream gzis = null;
        final int MAX_BYTE = 1024 * 1000;
        int len = 0;
        byte[] b = new byte[MAX_BYTE];
        try {
            if (!new File(folderPath).exists()) {
                new File(folderPath).mkdirs();

            }
            is = new FileInputStream(srcFile);
            String resultFile = folderPath + "/" + Md5Util.makeMd5Sum(srcFile);
            os = new FileOutputStream(resultFile);
            gzis = new GZIPInputStream(is);
            while ((len = gzis.read(b)) != -1) {
                os.write(b, 0, len);
            }
            os.flush();
            return resultFile;
        } finally {
            if (gzis != null) {
                gzis.close();
            }
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    public static void gzipFile(File srcFile, File desFile) {

        FileInputStream fis;
        FileOutputStream fos;
        GZIPOutputStream gzos;

        final int MAX_BYTE = 1024 * 1000;
        int len = 0;
        byte[] b = new byte[MAX_BYTE];

        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(desFile);
            gzos = new GZIPOutputStream(fos);
            while ((len = fis.read(b)) != -1)
                gzos.write(b, 0, len);
            gzos.flush();
            gzos.close();
            fos.close();
            fis.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}