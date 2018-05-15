package com.kibey.android.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;

/**
 * Created by mchwind on 15/6/29.
 */
public class DrawableUtils {

    public static GradientDrawable getRoundDrawable(int radius, String colorStr) {
        int color = Color.parseColor(colorStr);
        return getRoundDrawable(radius, color);
    }

    public static GradientDrawable getRoundDrawable(int radius, @ColorInt int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    public static GradientDrawable getRoundDrawable(int radius, int color, int strokeWith, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
		if (radius > 0) {
			drawable.setCornerRadius(radius);
		}
        if (strokeWith > 0) {
            drawable.setStroke(strokeWith, strokeColor);
        }
        return drawable;
    }

    public static StateListDrawable getStateDrawable(int idNormal, int idPressed, int idFocused) {
        Drawable normal = idNormal == -1 ? null : AppProxy.getApp().getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : AppProxy.getApp().getResources().getDrawable(idPressed);
        Drawable focus = idFocused == -1 ? null : AppProxy.getApp().getResources().getDrawable(idFocused);
        return getStateDrawable(normal, pressed, focus);
    }

    public static StateListDrawable getStateDrawable(Drawable normal, Drawable pressed, Drawable focus) {
        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        StateListDrawable sd = new StateListDrawable();
        sd.addState(new int[]{android.R.attr.state_checked}, pressed);
        sd.addState(new int[]{android.R.attr.state_selected}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }

    /**
     * 获取一个有触碰效果的drawable
     *
     * @param radius   半径
     * @param norColor 正常状态颜色
     * @param selColor 按下，checked，focused颜色
     * @return
     */
    public static Drawable getStateDrawable(int radius, String norColor, String selColor) {
        Drawable normal = getRoundDrawable(radius, norColor);
        Drawable select = getRoundDrawable(radius, selColor);
        return getStateDrawable(normal, select, select);
    }

    public static Drawable getRoundStateDrawable(int radius, int norColor, int selColor) {
        Drawable normal = getRoundDrawable(radius, norColor);
        Drawable select = getRoundDrawable(radius, selColor);
        return getStateDrawable(normal, select, select);
    }

    /**
     * @param radius
     * @param norColor
     * @param selColor
     * @param stokeColor
     * @return
     */
    public static Drawable getStrokeStateDrawable(int radius, int norColor, int selColor, int stokeColor) {
        int stokeWidth = DeviceUtils.d2p(1f);
        Drawable normal = getRoundDrawable(radius, norColor, stokeWidth, stokeColor);
        Drawable select = getRoundDrawable(radius, selColor, stokeWidth, stokeColor);
        return getStateDrawable(normal, select, select);
    }


    public static Drawable getDrawableFromUri(Context context, @DrawableRes int id, boolean setBounds) {
        return getDrawableFromUri(context, getUri(id), setBounds);
    }

    public static Drawable getDrawableFromUri(Context context, Uri mContentUri, boolean setBounds) {
        Drawable drawable = null;
        Bitmap bitmap;
        try {
            InputStream is = context.getContentResolver().openInputStream(
                    mContentUri);
            bitmap = BitmapFactory.decodeStream(is);
            drawable = new BitmapDrawable(context.getResources(), bitmap);
            if (setBounds) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            is.close();
        } catch (Exception e) {
            Logs.e("sms Failed to loaded content " + mContentUri + e);
            // TODO: 17/3/31
            if (new File(mContentUri.toString()).exists()) {
                Bitmap b = BitmapUtils.getBitmap(mContentUri.toString(), 0);
                if (null != b) {
                    drawable = new BitmapDrawable(b);
                }
            }
        }

        return drawable;
    }

    public static void setBackground(View view, Uri uri) {
        Drawable drawable = DrawableUtils.getDrawableFromUri(AppProxy.getApp(), uri, false);
        DrawableUtils.setBackground(view, drawable);
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static Uri getUri(int resId) {
        Resources r = AppProxy.getApp().getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));
        return uri;
    }

    public static void clear(View view) {
        if (view instanceof ImageView) {
            ImageView bgIv = (ImageView) view;
            bgIv.setImageDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                bgIv.setBackground(null);
            } else {
                bgIv.setBackgroundDrawable(null);
            }
        }
    }
}
