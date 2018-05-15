package com.kibey.android.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;

/**
 * @author seven
 * @version V1.0
 * @since 2017/10/16
 */
public class DrawableBuilder {
    private GradientDrawable drawable;

    private float radius;
    private float[] radiusArray;
    private Drawable selectDrawable;
    private Drawable checkDrawable;

    public static DrawableBuilder get() {
        return new DrawableBuilder();
    }

    public static DrawableBuilder get(GradientDrawable drawable) {
        return new DrawableBuilder(drawable);
    }

    private DrawableBuilder() {
        drawable = new GradientDrawable();
    }

    private DrawableBuilder(GradientDrawable drawable) {
        this.drawable = drawable;
    }

    /**
     * Set shape
     *
     * @param shape The desired shape for this drawable: {@link GradientDrawable#LINE},
     *              {@link GradientDrawable#OVAL}, {@link GradientDrawable#RECTANGLE} or {@link GradientDrawable#RING}
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public DrawableBuilder shape(int shape) {
        drawable.setShape(shape);
        return this;
    }

    /**
     * Set gradient
     *
     * @param gradientType The type of the gradient: {@link GradientDrawable#LINEAR_GRADIENT},
     *                     {@link GradientDrawable#RADIAL_GRADIENT} or {@link GradientDrawable#SWEEP_GRADIENT}
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public DrawableBuilder gradient(int gradientType) {
        drawable.setGradientType(gradientType);
        return this;
    }

    /**
     * [1] color
     * [2] startColor endColor
     * [3] startColor centerColor endColor
     *
     * @param colors
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public DrawableBuilder color(int... colors) {
        drawable.mutate();
        if (colors.length > 1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        	drawable.setColors(colors);
		} else {
            drawable.setColor(colors[0]);
        }
        return this;
    }

    /**
     * Set orientation
     *
     * @param orientation one of {@link GradientDrawable.Orientation}
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public DrawableBuilder orientation(GradientDrawable.Orientation orientation) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			drawable.setOrientation(orientation);
		}
		return this;
    }

    /**
     * Set alpha
     *
     * @param alpha 1-255
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public DrawableBuilder alpha(int alpha) {
        drawable.setAlpha(alpha);
        return this;
    }

    /**
     * Set stroke
     *
     * @param width stroke width dp
     * @param color stroke color
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public DrawableBuilder stroke(int width, int color) {
        drawable.setStroke(ViewUtils.dp2Px(width), color);
        return this;
    }

    /**
     * Set corner radius
     *
     * @param radius radius dp
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public DrawableBuilder corner(float radius) {
        int rd = ViewUtils.dp2Px(radius);
        this.radius = rd;
        drawable.setCornerRadius(rd);
        return this;
    }

    public DrawableBuilder corner(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        float[] radius = {topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft};
        float[] rds = new float[8];
        for (int i = 0; i < 8; i++) {
            int rd = ViewUtils.dp2Px(radius[i]);
            rds[i] = rd;
        }
        this.radiusArray = rds;
        drawable.setCornerRadii(this.radiusArray);
        return this;
    }

    /**
     * Build gradient drawable
     *
     * @return Drawable
     */
    public Drawable build() {
        Drawable result = drawable;
        if (null != selectDrawable || null != checkDrawable) {
            result = DrawableUtils.getStateDrawable(result, selectDrawable, checkDrawable);
        }
        return result;
    }

    /**
     * Build ripple drawable
     *
     * @param rippleColor ripple color
     * @return Drawable
     */
    public Drawable ripple(int rippleColor) {
        GradientDrawable mask = drawable;
        if (radius != 0) {
            mask = new GradientDrawable();
            if (null != radiusArray) {
                mask.setCornerRadii(radiusArray);
            } else {
                mask.setCornerRadius(radius);
            }
            mask.setColor(Color.WHITE);
        }
        return ripple(rippleColor, mask);
    }

    /**
     * Build ripple drawable
     *
     * @param rippleColor ripple color
     * @param mask        mask drawable
     * @return Drawable
     */
    public Drawable ripple(int rippleColor, Drawable mask) {
        Drawable result = drawable;
        if (SDKUtils.hasLollipop()) {
            RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(rippleColor), drawable, mask);
            result = rippleDrawable;
        }
        if (null != selectDrawable || null != checkDrawable) {
            result = DrawableUtils.getStateDrawable(result, selectDrawable, checkDrawable);
        }
        return result;
    }

    public DrawableBuilder select(Drawable select) {
        selectDrawable = select;
        return this;
    }

    public DrawableBuilder check(Drawable check) {
        checkDrawable = check;
        return this;
    }
}
