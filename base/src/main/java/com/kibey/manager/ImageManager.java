package com.kibey.manager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.kibey.proxy.image.ImageProxy;
import com.kibey.proxy.image.ImgLoadListener;
import com.kibey.lib.PluginInvocationHandler;

import java.io.File;

public class ImageManager implements ImageProxy {
    private static ImageManager instance = null;
    private ImageProxy mImageProxy;

    private ImageManager() {
        mImageProxy = (ImageProxy) new PluginInvocationHandler().bind(ImageProxy.class);
    }

    public ImageProxy getImageProxy() {
        return mImageProxy;
    }

    public static ImageManager getInstance() {
        if (instance == null) {
            synchronized (ImageManager.class) {
                if (instance == null) {
                    instance = new ImageManager();
                }
            }
        }
        return instance;
    }

    @Override
    public Bitmap loadImageSync(String url) {
        return mImageProxy.loadImageSync(url);
    }

    @Override
    public void loadImage(Uri url, ImageView iv) {
        mImageProxy.loadImage(url, iv);
    }

    @Override
    public void loadImage(String url, ImgLoadListener loadingListener) {
        mImageProxy.loadImage(url, loadingListener);
    }

    public void loadImage(String url, ImageView iv) {
        mImageProxy.loadImage(url, iv);
    }

    @Override
    public void loadImage(String url, ImageView imageView, int placeholder) {
        mImageProxy.loadImage(url, imageView, placeholder);
    }

    @Override
    public void loadImage(String url, ImageView imageView, ImgLoadListener listener) {
        mImageProxy.loadImage(url, imageView, listener);
    }

    @Override
    public void loadImage(String url, ImageView imageView, boolean baseWidthOrHeight) {
        mImageProxy.loadImage(url, imageView, baseWidthOrHeight);
    }

    @Override
    public void loadImage(String url, TextView mTextView, int location) {
        mImageProxy.loadImage(url, mTextView, location);
    }

    @Override
    public void loadImage(String url, ImageView imageView, int placeholder, ImgLoadListener listener) {
        mImageProxy.loadImage(url, imageView, placeholder, listener);
    }

    @Override
    public void loadImage(String url, ImageView imageView, ImgLoadListener listener, boolean isShowPlaceHolder) {
        mImageProxy.loadImage(url, imageView, listener, isShowPlaceHolder);
    }

    @Override
    public Object loadImage(String url, ImageView imageView, int w, int h, ImgLoadListener listener) {
        return mImageProxy.loadImage(url, imageView, w, h, listener);
    }

    @Override
    public void loadImageProgress(String url, ImgLoadListener loadingListener, Object progressListener) {
        mImageProxy.loadImageProgress(url, loadingListener, progressListener);
    }

    @Override
    public void loadImage(String url, ImageView imageView, int w, int h) {
        mImageProxy.loadImage(url, imageView, w, h);
    }

    @Override
    public File getFile(String url) {
        return mImageProxy.getFile(url);
    }

    @Override
    public Bitmap _getBitmap(String s) {
        return mImageProxy._getBitmap(s);
    }
}
