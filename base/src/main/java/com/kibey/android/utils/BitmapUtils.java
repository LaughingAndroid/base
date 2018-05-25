package com.kibey.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.text.format.Time;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapUtils {
    final static String tag = "bitmaputils";
    private static final int MAX_SIZE = 1080 * 1080;
    private static String imagePath;

    private static final String PREFIX = "image";

    public static void initImagePath(Context context) {
        imagePath = FilePathManager.getDiskCacheDir(context, FilePathManager.PHOTO)
                .getAbsolutePath() + File.separator;
    }

    public static byte[] getBytesFromBitmap(Bitmap bmp) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();

        } catch (Exception ex) {
            return null;
        }
    }

    public static Bitmap cutBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int a = (int) (Math.min(width, height) * 0.5f);
        int centerX = (int) (width * 0.5f);
        int centerY = (int) (height * 0.5f);
        return Bitmap.createBitmap(bitmap
                , Math.max(centerX - a, 0)
                , Math.max(centerY - a, 0)
                , Math.min(2 * a, width)
                , Math.min(2 * a, height));
    }

    public synchronized static Bitmap getBitmap(String filePath, int size) {
        try {
            Bitmap bitmap = null;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inPurgeable = true;
            options.inTempStorage = new byte[12 * 1024];
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Config.RGB_565;
            final int width = options.outWidth;
            final int height = options.outHeight;
            /** Calculation scale **/
            int scale = 0;

            if (size == 0) {
                scale = 1;
                while (width * height / scale / scale > MAX_SIZE) {
                    scale++;
                    Logs.e("to lager");
                }
            } else {
                if (width < height) {
                    scale = width / size;
                } else {
                    scale = height / size;
                }
                if (scale < 1) {
                    scale = 1;
                }
                if (scale < 1) {
                    scale = 1;
                }
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            bitmap = BitmapFactory.decodeFile(filePath, options);
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }

    }

    public synchronized static Bitmap getBitmapEqualRatio(String filePath, double size) {
        try {
            Bitmap bitmap = null;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inPurgeable = true;
            options.inTempStorage = new byte[12 * 1024];
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Config.RGB_565;
            final int width = options.outWidth;
            final int height = options.outHeight;
            /** Calculation scale **/
            int scale = 0;

            if (size == 0) {
                scale = 1;
                while (width * height / scale / scale > MAX_SIZE) {
                    scale++;
                    Logs.e("to lager");
                }
            } else {
                if (size < 1) {
                    scale = 1;
                } else {
                    scale = (int) Math.rint(Math.sqrt(size));
                }
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            bitmap = BitmapFactory.decodeFile(filePath, options);
            int bitmapSize = 0;
            if (null != bitmap)
                bitmapSize = bitmap.getRowBytes() * bitmap.getHeight();
            Logs.i(tag, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>w1:" + width + " h:"
                    + height + " w*h*rgb:" + width * height * 4 + " w*h:"
                    + width * height + " s:" + scale + " bitmapSize:"
                    + bitmapSize);
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }

    }


    public static Bitmap getBitmap(byte[] bytes, int size) {
        Bitmap bitmap = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPurgeable = true;
        options.inTempStorage = new byte[12 * 1024];
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Config.RGB_565;
        final int width = options.outWidth;
        final int height = options.outHeight;
        Log.i("img", "width:" + width + "height" + height);
        /** Calculation scale **/
        int scale = 1;
        if (0 == size) {
            while (width * height / scale / scale > MAX_SIZE) {
                scale++;
                Logs.e("to lager");
            }
        } else {
            if (width < height) {
                scale = width / size;
            } else {
                scale = height / size;
            }
        }
        if (scale < 1) {
            scale = 1;
        }
        if (scale < 1) {
            scale = 1;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        Logs.i(tag, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>w3:" + width + "h:" + height
                + "s:" + scale);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        return bitmap;
    }

    /**
     * 保存bitmap到文件
     *
     * @param bm     bitmap
     * @param file   文件
     * @param format 压缩格式 {@link Bitmap.CompressFormat}
     */
    public static void saveBmpToSdcard(Bitmap bm, File file, Bitmap.CompressFormat format) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            long start = System.currentTimeMillis();
            try {
                final OutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file));
                bm.compress(format, 100, fileOut);
                fileOut.flush();
                fileOut.close();

            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            Logs.d(tag, "save" + (System.currentTimeMillis() - start));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存bitmap到文件
     *
     * @param bmp
     * @param imageFile
     * @param isRoundedCorner 是否是圆角
     * @return
     */
    public static String saveBmpToSdcard(Bitmap bmp, File imageFile, boolean isRoundedCorner) {

        try {
            if (!imageFile.exists()) {
                imageFile.createNewFile();
            }
            long start = System.currentTimeMillis();
            try {
                Bitmap bitmap = bmp;
                if (isRoundedCorner) {
                    bitmap = getRoundedCornerBitmap(bmp, 1, 10,
                            Config.ARGB_8888);
                }
                final OutputStream fileOut = new BufferedOutputStream(new FileOutputStream(imageFile));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
                fileOut.flush();
                fileOut.close();

            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            Logs.d(tag, "save" + (System.currentTimeMillis() - start));
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 保存bitmap到sdcard
     *
     * @param bmp
     * @param imageFile
     * @return
     */
    public static String saveBmpToSdcard(Bitmap bmp, File imageFile) {
        try {
            if (!imageFile.exists()) {
                imageFile.createNewFile();
            }
            long start = System.currentTimeMillis();
            try {
                Bitmap bitmap = bmp;
                final OutputStream fileOut = new BufferedOutputStream(new FileOutputStream(imageFile));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
                fileOut.flush();
                fileOut.close();

            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            Logs.d(tag, "save" + (System.currentTimeMillis() - start));
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取圆角bitmap
     *
     * @param bitmap
     * @param scale
     * @param roundPx
     * @param config
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float scale,
                                                float roundPx, Config config) {

        final int width = (int) (bitmap.getWidth() * scale);
        final int height = (int) (bitmap.getHeight() * scale);

        Bitmap output = null;
        output = Bitmap.createBitmap(width, height, config);
        final Canvas canvas = new Canvas(output);

        final int color = 0xff000000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rectF, paint);

        return output;
    }

    /**
     * 旋转bitmapc
     *
     * @param mBitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateImage(Bitmap mBitmap, float degrees) {
        // 加载要操作的图片
        Bitmap bitmap = mBitmap;
        // 得到需要加载图片的高度与宽度
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        // 创建Matrix对象,Matrix是在Android中用于操作图像的类
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        // 创建旋转后的图片
        mBitmap = Bitmap
                .createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return mBitmap;
    }


    public static void disposeBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    private static String cacheImageInMemory(Bitmap bitmap, String pathName) {
        File dir = new File(imagePath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(pathName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (null != bitmap) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fos) {
                    fos.flush();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathName;
    }

    public static String cacheImageInMemoryTemp(Context context, Bitmap bitmap) {
        return cacheImageInMemory(bitmap,
                locateImageCacheFilePathName(context, "temp"));
    }

    public static String cacheImageInMemory(Context context, Bitmap bitmap) {
        return cacheImageInMemory(bitmap,
                locateImageCacheFilePathName(context, ""));
    }

    public static String cacheImageInMemory(Context context, byte[] _data) {
        return cacheImageInMemory(context, getBitmap(_data, 0));
    }

    public static String locateImageCacheFilePathName(Context context,
                                                      String string) {
        String imageName = null;
        Time t = new Time();
        t.setToNow();
        imageName = PREFIX + t.year + "_" + t.month + "_"
                + t.monthDay + "_" + t.hour + "_" + t.minute + "_" + t.second
                + string + ".jpg";
        initImagePath(context);
        File imageDir = new File(imagePath);
        if (!imageDir.isDirectory()) {
            if (!imageDir.mkdirs()) {
                Logs.d(tag, imagePath + " create failed");
            }
        }
        return imagePath + imageName;
    }


    public static Bitmap getBitmap(View layContent) {
        layContent.setDrawingCacheEnabled(true);
        layContent.layout(0, 0, layContent.getMeasuredWidth(),
                layContent.getMeasuredHeight());
        layContent.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        layContent.buildDrawingCache();
        Bitmap bitmap = layContent.getDrawingCache();
        return bitmap;
    }

    /**
     * 截取圆形bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap bitmapRound(Bitmap bitmap, OOMListener listener) {

        try {
            int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
            Bitmap newbmp = Bitmap.createBitmap(width, width, Config.ARGB_8888);

            Canvas canvas = new Canvas(newbmp);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            final int color = 0xff000000;
            final Paint paint = new Paint();
            final RectF rectF = new RectF(0, 0, width, width);
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, width, width, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, null, rectF, paint);

            // save all clip
            canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
            // store
            canvas.restore();// 存储

            bitmap.recycle();
            return newbmp;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            listener.outOfMemory();
        }
        return null;
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            Logs.d("TakePictureActivity", "orientation=" + orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        Log.d(tag, "degree=" + degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    public interface OOMListener {
        void outOfMemory();
    }

    /**
     * @param bm
     * @param isWx
     * @return
     */
    public static Bitmap createBitmapForShare(Bitmap bm, boolean isWx) {
        if (null == bm) {
            return null;
        }
        final float maxWidth = isWx ? 480.f : 640.f;
        int width = bm.getWidth();
        if (width <= maxWidth) {
            return bm;
        }

        float scale = 640.f / bm.getWidth();
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
        return newBitmap;
    }
}
