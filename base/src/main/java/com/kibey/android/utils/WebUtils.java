package com.kibey.android.utils;

import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author
 * @version V1.0
 * @date 2011-8-13
 * @description
 */
public class WebUtils {
    public static boolean download(String source, String file) {
       return download(source, file, null);
    }

    public static <T> Observable<T> rxDownload(final String source, final String file, final IDownloadUrl callback, final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                boolean success = download(source, file, callback);
                if (!success) {
                    subscriber.onError(new Exception(source + " download failed!"));
                    return;
                }
                subscriber.onNext(t);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }


    public static boolean download(String source, String file, final IDownloadUrl callback) {
        long totalSize;

        File tmpFolder = new File(FilePathManager.getFilepath() + "/temp");

        if (!tmpFolder.exists()) {
            tmpFolder.mkdirs();
        }

        File tmp = new File(tmpFolder, Md5Util.makeMd5Sum(file));
        if (tmp.exists()) {
            tmp.delete();
        }

        URL mUrl;
        OutputStream ouput = null;

        try {
            int index = source.lastIndexOf("/");
            String host = source.substring(0, index);
            String name = source.substring(index + 1, source.length());
            String[] str = name.split("\\?");
            mUrl = new URL(host + "/" + Uri.encode(str[0])
                    + (str.length == 2 ? ("?" + str[1]) : ""));
            ouput = new FileOutputStream(tmp);

            HttpURLConnection con = (HttpURLConnection) mUrl.openConnection();
            InputStream is = con.getInputStream();
            totalSize = con.getContentLength();
            byte[] buffer = new byte[1024];
            int length = 0;
            int size = 0;
            while ((size = is.read(buffer)) != -1) {
                ouput.write(buffer, 0, size);
                length += size;
                if (null != callback) {
                    long result = callback.progress(Math.min(totalSize - 1, length), totalSize);
                    if (result < 0) {
                        return false;
                    }

                }
            }
            // move to download file;
            FileUtils.copyFile(tmp.getAbsolutePath(), file);
            tmp.delete();
            if (callback != null) {
                callback.progress(totalSize, totalSize);
            }
        } catch (Exception e) {
            if (null != e)
                e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != ouput)
                    ouput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public interface IDownloadUrl {
        long progress(long progress, long total);
    }

    public static void downloadPic(final String neturl, final String file_, final IDownload iDownload) {
        downloadPic(neturl, file_, FilePathManager.FILE_COVER_PATH, iDownload);
    }

    public static void downloadPic(final String neturl, final String file_, final String filePath, final IDownload iDownload) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = null;
                    File folder = new File(filePath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    url = new URL(neturl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setRequestProperty("Accept-Encoding", "identity");
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String file_path;
                        File file;
                        file_path = filePath + File.separator + file_ + ".jpg";
                        file = new File(file_path);
                        if (file.exists()) {
                            file.delete();
                        }
                        InputStream is = conn.getInputStream();

                        byte[] buffer = new byte[1024];
                        int length = 0;

                        OutputStream os = new FileOutputStream(file);

                        while ((length = is.read(buffer)) != -1) {
                            os.write(buffer, 0, length);
                        }
                        os.close();
                        is.close();
                        if (iDownload != null) {
                            APPConfig.post(new Runnable() {
                                @Override
                                public void run() {
                                    iDownload.success();

                                }
                            });
                        }
//
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    APPConfig.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AppProxy.getApp(), "Download error!", Toast.LENGTH_SHORT).show();
                            if (iDownload != null) {
                                iDownload.fail();
                            }
                        }
                    });

                }

            }
        };
        Thread thread = new Thread(run);
        thread.start();

    }

    public static boolean is_file_exists(String file_) {
        File file = new File(FilePathManager.FILE_COVER_PATH + File.separator + file_ + ".jpg");
        return file.exists();
    }

    public interface IDownload {
        void success();

        void fail();
    }

}
