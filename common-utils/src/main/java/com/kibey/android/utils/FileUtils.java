package com.kibey.android.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author seven
 * @version V1.0
 * @since 16/6/2
 */
public final class FileUtils {
    private static Context mContext = AppProxy.getApp();

    private FileUtils() {
    }

    /**
     * read file
     *
     * @param filePath
     * @return if file not exist, return null, else return content of file
     * @throws IOException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
		StringBuilder fileContent = new StringBuilder();

		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append   is append, if true, write to the end of file, else clear content of file and write into it
     * @return return true
     * @throws IOException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        FileWriter fileWriter = null;
        try {
            File file = new File(filePath);
            if (!new File(file.getParent()).exists()) {
                new File(file.getParent()).mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
			return false;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public static double copyFile(String sourceFile, String targetFile) {
        return copyFile(new File(sourceFile), new File(targetFile));
    }

    /**
     * 文件拷贝
     *
     * @param sourceFile
     * @param targetFile
     * @return void
     * @throws IOException kibey 2014-2-15 下午7:15:51
     */
    public static double copyFile(File sourceFile, File targetFile) {
        if (sourceFile != null && sourceFile.equals(targetFile)) return 1;

        long start = System.currentTimeMillis();
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;

        File targetFolder = new File(targetFile.getParent());
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }
        try {
            targetFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1 && !isStopCopy(sourceFile.getAbsolutePath())) {
                outBuff.write(b, 0, len);
            }
            if (isStopCopy(sourceFile.getAbsolutePath())) {
                stopCopy.remove(sourceFile.getAbsolutePath());
                return -1;
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
            start = (System.currentTimeMillis() - start);
            double speed = targetFile.length() / 1000.0f / (start / 1000.0f);
            stopCopy.remove(sourceFile.getAbsolutePath());
            return speed;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            } catch (IOException e) {
            }
        }
        return -1;
    }

    /**
     * 停止copy
     */
    public static HashMap<String, Boolean> stopCopy = new HashMap<>();

    private static boolean isStopCopy(String key) {
        return null != stopCopy.get(key) && stopCopy.get(key);
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context  Context 使用CopyFiles类的Activity
     * @param srcPath  String  原文件路径  如：/aa
     * @param distPath String  复制后路径  如：xx:/bb/cc
     */
    public static void copyAssetsToDir(Context context, String srcPath, String distPath) throws Exception {
        String fileNames[] = context.getAssets().list(srcPath);//获取assets目录下的所有文件及目录名
        if (fileNames.length > 0) {//如果是目录
            File file = new File(distPath);
            file.mkdirs();//如果文件夹不存在，则递归
            for (String fileName : fileNames) {
                copyAssetsToDir(context, srcPath + "/" + fileName, distPath + "/" + fileName);
            }
        } else {//如果是文件
            InputStream is = context.getAssets().open(srcPath);
            FileOutputStream fos = new FileOutputStream(new File(distPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        }
    }

    /**
     * 判断文件是否存在，并且长度>0
     *
     * @param path
     * @return
     */
    public static boolean isValid(String path) {
        try {
            File f = new File(path);
            if (!f.exists() || f.length() <= 0) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 判断是否有SD卡
     *
     * @return
     */
    public static boolean isSdCardEnable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 保存图片到系统 pictures下
     *
     * @param source
     * @param filename
     * @return
     * @throws Exception
     */
    public static String saveToGallery(File source, String filename) throws Exception {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        dir = new File(dir, "echo");
        dir.mkdirs();
        File file = File.createTempFile(filename, ".jpg", dir);
        Logs.i("common", file.getAbsolutePath());
        copyFile(source, file);
        String path = file.getAbsolutePath();
        MediaScannerConnection.scanFile(mContext, new String[]{path}, null, null);
        return path;
    }

    /**
     * 获取SD卡剩余空间
     *
     * @param type 返回单位：KB、MB、GB
     * @return
     */
    public static long getSDAvailableSize(String... type) {
        if (isSdCardEnable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            if (null != type
                    && type.length > 0
                    && !StringUtils.isEmpty(type[0])) {
                if (type.equals("KB")) {
                    return (availableBlocks * blockSize) / 1024;
                } else if (type.equals("MB")) {
                    return (availableBlocks * blockSize) / 1024 / 1024;
                } else if (type.equals("GB")) {
                    return (availableBlocks * blockSize) / 1024 / 1024 / 1024;
                }
            }
            return availableBlocks * blockSize;
        }
        return -1;
    }

    /**
     * 获取SD卡空间
     *
     * @param type 返回单位：MB、GB
     * @return
     */
    public static long getAllSize(String... type) {
        if (isSdCardEnable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            if (null != type
                    && type.length > 0
                    && !StringUtils.isEmpty(type[0])) {
                if (type.equals("MB")) {
                    return (availableBlocks * blockSize) / 1024;
                } else if (type.equals("GB")) {
                    return (availableBlocks * blockSize) / 1024 / 1024;
                }
            }
            return availableBlocks * blockSize;
        }
        return -1;
    }

    /**
     * 获取文件夹大小
     *
     * @param folderPath 文件夹路径
     * @param type       返回单位：B、KB、MB、GB
     * @return
     */
    public static double getFolderSize(String folderPath, String type) {
        long totalSize = getFolderLength(folderPath);
        if (!StringUtils.isEmpty(type)) {
            if (type.equals("B")) {
                return totalSize;
            } else if (type.equals("KB")) {
                return (double) totalSize / 1024;
            } else if (type.equals("MB")) {
                return (double) totalSize / 1024 / 1024;
            } else if (type.equals("GB")) {
                return (double) totalSize / 1024 / 1024 / 1024;
            }
        }
        return totalSize;
    }

    /**
     * 合成文件
     *
     * @param file1
     * @param file2
     * @param resultFile
     * @throws IOException
     */
    public static void join(InputStream file1, InputStream file2,
                            String resultFile) throws IOException {
        DataInputStream fis1 = null;
        DataInputStream fis2 = null;
        DataOutputStream fos = null;
        SequenceInputStream sis = null;
        try {
            File file = new File(resultFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fis1 = new DataInputStream(new BufferedInputStream(file1));
            fis2 = new DataInputStream(new BufferedInputStream(file2));
            sis = new SequenceInputStream(fis1, fis2);
            fos = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(file)));
            byte[] buff = new byte[1024];
            while (true) {
                int i = sis.read(buff);
                if (i == -1)
                    break;
                fos.write(buff);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fis1)
                fis1.close();
            if (null != fis2)
                fis2.close();
            if (null != fos)
                fos.close();
            if (null != sis)
                sis.close();
        }
    }

    public static void join(String file1, String file2, String resultFile)
            throws IOException {
        join(new FileInputStream(file1), new FileInputStream(file2), resultFile);
    }

    /**
     * 删除文件夹
     *
     * @param dir
     * @return
     */
    public static boolean delete(String dir) {
        deleteEmptyDir(dir);
        return deleteDir(new File(dir));
    }

    /**
     * 删除空目录
     *
     * @param dir 将要删除的目录路径
     */
    private static void deleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            Logs.d("Successfully deleted empty directory: " + dir);
        } else {
            Logs.d("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 获取文件夹大小
     *
     * @param folderPath
     * @return
     */
    private static long getFolderLength(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists())
            return 0;
        if (!file.isDirectory())
            return 0;

        String[] tempList = file.list();
        File temp = null;
        long totalLen = 0;
        for (int i = 0; i < tempList.length; i++) {
            if (folderPath.endsWith(File.separator)) {
                temp = new File(folderPath + tempList[i]);
            } else {
                temp = new File(folderPath + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                totalLen += temp.length();
            }
            if (temp.isDirectory()) {
                totalLen += getFolderLength(folderPath + "/" + tempList[i]);
            }
        }
        return totalLen;
    }


    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(AppProxy.getApp().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                Result += line + "\r\n";
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 截取文件
     *
     * @param origin 原始文件
     * @param result 结果文件
     * @param start
     * @param end
     */
    public static boolean cutoutFile(String origin, String result, long start, long end) {
        long newLength = end - start;
        if (newLength < 0) {
            throw new IllegalArgumentException();
        }
        RandomAccessFile randomFile = null;
        BufferedOutputStream outBuff = null;
        try {
            byte[] buffer = new byte[1024];
            int sum = 0;
            int bytesRead = 0;
            randomFile = new RandomAccessFile(origin, "r");
            randomFile.seek(start);

            outBuff = new BufferedOutputStream(new FileOutputStream(result));

            while (sum < newLength) {
                bytesRead = randomFile.read(buffer);
                long left = newLength - sum;
                if (left >= bytesRead) {
                    outBuff.write(buffer);
                    sum += bytesRead;
                } else {
                    byte[] bytes = Arrays.copyOf(buffer, (int) left);
                    outBuff.write(bytes);
                    break;
                }
            }
            outBuff.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                randomFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outBuff.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 获取指定文件大小
     * @param file
     * @return
     * @throws Exception 　　
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }
}
