package com.kibey.proxy.image;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;
import android.widget.TextView;

import com.kibey.android.data.model.IKeepProguard;

import java.io.File;

/**
 * by liyihang
 * blog http://sijienet.com/
 */
public interface ImageProxy extends IKeepProguard {
    Bitmap loadImageSync(String url);

    void loadImage(Uri url, ImageView iv);

    void loadImage(String url, ImgLoadListener loadingListener);

    void loadImage(String url, ImageView imageView);

    void loadImage(String url, ImageView imageView, @DrawableRes int placeholder);

    void loadImage(String url, ImageView imageView, ImgLoadListener listener);

    /**
     * 加载图片,完成后image view按图片比例显示
     *
     * @param url
     * @param imageView
     * @param baseWidthOrHeight 以宽或者高为基准
     */
    void loadImage(String url, ImageView imageView, final boolean baseWidthOrHeight);

    /**
     * 加载TextView前的小图标
     *
     * @param url       图标URL
     * @param mTextView 文本控件
     * @param location  图标的位置 0左，1上，2右，3下
     */
    void loadImage(String url, final TextView mTextView, final int location);

    void loadImage(String url, ImageView imageView, @DrawableRes int placeholder, ImgLoadListener listener);

    /**
     * 第一次送礼的时候全屏动画不会出现PLACEHOLDER默认图片
     *
     * @param url
     * @param imageView
     * @param listener
     */
    void loadImage(String url, ImageView imageView, ImgLoadListener listener, boolean isShowPlaceHolder);

    /**
     * Loading bitmap for image aware
     *
     * @param url      bitmap url
     * @param listener loading listener
     */
    Object loadImage(String url, ImageView imageView, int w, int h, ImgLoadListener listener);

    void loadImageProgress(String url, ImgLoadListener loadingListener, Object progressListener);


    /**
     * Load image with size
     *
     * @param url
     * @param imageView
     * @param w         size width
     * @param h         size height
     */
    void loadImage(String url, ImageView imageView, int w, int h);

    File getFile(String url);

    Bitmap _getBitmap(String s);
}
