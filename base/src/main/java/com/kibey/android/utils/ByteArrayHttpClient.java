package com.kibey.android.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ByteArrayHttpClient {
    private static final String TAG = "ByteArrayHttpClient";

    public static byte[] get(final String urlString) {
        InputStream in = null;
        try {
            final String decodedUrl = URLDecoder.decode(urlString, "UTF-8");
            final URL url = new URL(decodedUrl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            in = con.getInputStream();
            return getBytes(in);
        } catch (final MalformedURLException e) {
            Log.d(TAG, "Malformed URL", e);
        } catch (final OutOfMemoryError e) {
            Log.d(TAG, "Out of memory", e);
        } catch (final UnsupportedEncodingException e) {
            Log.d(TAG, "Unsupported encoding", e);
        } catch (final IOException e) {
            Log.d(TAG, "IO exception", e);
        } catch (Exception e) {
            Log.d(TAG, "IO exception", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException ignored) {
                }
            }
        }
        return null;
    }

    public static byte[] getBytes(InputStream is) throws Exception {
        byte[] data = null;

        Collection chunks = new ArrayList();
        byte[] buffer = new byte[1024 * 1000];
        int read = -1;
        int size = 0;

        while ((read = is.read(buffer)) != -1) {
            if (read > 0) {
                byte[] chunk = new byte[read];
                System.arraycopy(buffer, 0, chunk, 0, read);
                chunks.add(chunk);
                size += chunk.length;
            }
        }

        if (size > 0) {
            ByteArrayOutputStream bos = null;
            try {
                bos = new ByteArrayOutputStream(size);
                for (Iterator itr = chunks.iterator(); itr.hasNext(); ) {
                    byte[] chunk = (byte[]) itr.next();
                    bos.write(chunk);
                }
                data = bos.toByteArray();
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        }
        return data;
    }

    public static byte[] getBytes(InputStream is, int count) throws Exception {
        byte[] data = null;

        Collection chunks = new ArrayList();
        byte[] buffer = new byte[1024];
        int read = -1;
        int size = 0;

        while ((read = is.read(buffer)) != -1) {
            if (read > 0) {
                byte[] chunk = new byte[read];
                System.arraycopy(buffer, 0, chunk, 0, read);
                chunks.add(chunk);
                size += chunk.length;
                if (size > count) break;
            }
        }

        if (size > 0) {
            ByteArrayOutputStream bos = null;
            try {
                bos = new ByteArrayOutputStream(size);
                for (Iterator itr = chunks.iterator(); itr.hasNext(); ) {
                    byte[] chunk = (byte[]) itr.next();
                    bos.write(chunk);
                }
                data = bos.toByteArray();
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        }
        return data;
    }
}
