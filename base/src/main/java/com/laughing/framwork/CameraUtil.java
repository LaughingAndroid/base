package com.laughing.framwork;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import com.kibey.android.utils.FilePathManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fengguangyu1 on 15/6/26.
 */
public class CameraUtil {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Check if this device has a camera */
    public boolean checkCameraHardware(Context context) {
        // this device has a camera
// no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().toString()
                + File.separator + "kibey_echo" + File.separator + FilePathManager.PATH_PHOTO);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public static File saveBitmap(Bitmap bitmap) {
        File mediaFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

        if (mediaFile == null) {
            return null;
        }

        // Saving the bitmap
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return mediaFile;
    }


    public static File saveSquarePicture(Context context, Bitmap bitmap) {
        int cropHeight;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            cropHeight = bitmap.getWidth();
        } else {
            cropHeight = bitmap.getHeight();
        }

//        bitmap = ThumbnailUtils.extractThumbnail(bitmap, cropHeight, cropHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, cropHeight, cropHeight);

        File mediaFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

        if (mediaFile == null) {
            return null;
        }

        // Saving the bitmap
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return mediaFile;
    }


    public static File thumbnailPicture(Context context, Bitmap bitmap) {
        return thumbnailPicture(context, bitmap, 0);
    }


    public static File thumbnailPicture(Context context, Bitmap bitmap, int max) {
        int cropHeight;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            cropHeight = bitmap.getWidth();
        } else {
            cropHeight = bitmap.getHeight();
        }

        if (max > 0) {
            cropHeight = Math.min(cropHeight, max);
        }

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, cropHeight, cropHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        File mediaFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

        if (mediaFile == null) {
            return null;
        }

        // Saving the bitmap
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return mediaFile;
    }

}
