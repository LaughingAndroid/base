package com.kibey.proxy.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/10
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public interface IToolbar {
    /**
     * Toolbar标志，没有toolbar
     */
    int FLAG_NONE = -1;
    /**
     * Toolbar标志，一个空的Toolbar
     */
    int FLAG_EMPTY = 0;
    /**
     * Toolbar标志，显示返回键
     */
    int FLAG_BACK = 1;
    /**
     * Toolbar标志，显示碟
     */
    int FLAG_DISK = 1 << 1;
    /**
     * Toolbar标志，显示线
     */
    int FLAG_LINE = 1 << 2;
    /**
     * Toolbar标志，使用暗色风格（图标白色）
     */
    int FLAG_DARK = 8;

    /**
     * Set the title of this toolbar.
     * <p>
     * <p>A title should be used as the anchor for a section of content. It should
     * describe or name the content being viewed.</p>
     *
     * @param resId Resource ID of a string to set as the title
     */
    void setTitle(@StringRes int resId);

    /**
     * Set the title of this toolbar.
     * <p>
     * <p>A title should be used as the anchor for a section of content. It should
     * describe or name the content being viewed.</p>
     *
     * @param title Title to set
     */
    void setTitle(CharSequence title);

    void setSubTitle(CharSequence text);


    /**
     * Sets the text color of the title, if present.
     *
     * @param color The new text color in 0xAARRGGBB format
     */
    void setTitleTextColor(@ColorInt int color);

    /**
     * Sets the text color of sub title
     *
     * @param color The new text color in 0xAARRGGBB format
     */
    void setSubTitleTextColor(@ColorInt int color);

    /**
     * Get title TextView
     *
     * @return title TextView
     */
    TextView getTitleView();

    /**
     * Get sub title TextView
     *
     * @return sub title TextView
     */
    TextView getSubTitleView();

    /**
     * Set the icon to use for the toolbar's navigation button.
     *
     * @param resId Resource ID of a drawable to set
     */
    void setNavigationIcon(@DrawableRes int resId);

    ImageView getNavButtonView();

    /**
     * Set the icon to use for the toolbar's navigation button.
     *
     * @param drawable Resource ID of a drawable to set
     */
    void setNavigationIcon(Drawable drawable);

    /**
     * Set a listener to respond to navigation events.
     * <p>
     * <p>This listener will be called whenever the user clicks the navigation button
     * at the start of the toolbar. An icon must be set for the navigation button to appear.</p>
     *
     * @param listener Listener to set
     * @see #setNavigationIcon(Drawable)
     */
    void setNavigationOnClickListener(View.OnClickListener listener);

    /**
     * 添加一个text item
     *
     * @param title    item title
     * @param listener View.OnClickListener
     * @return TextView
     */
    TextView addTextMenuItem(@StringRes int title, View.OnClickListener listener);

    /**
     * 添加一个text item
     *
     * @param title    item title
     * @param listener View.OnClickListener
     * @return TextView
     */
    TextView addTextMenuItem(CharSequence title, View.OnClickListener listener);

    /**
     * 添加一个icon item
     *
     * @param icon     icon drawable
     * @param listener View.OnClickListener
     * @return TextView
     */
    ImageView addIconMenuItem(@DrawableRes int icon, View.OnClickListener listener);

    /**
     * 添加一个icon item
     *
     * @param icon       icon drawable
     * @param listener   View.OnClickListener
     * @param addToFirst 是否添加到前面
     * @return TextView
     */
    ImageView addIconMenuItem(@DrawableRes int icon, boolean addToFirst, View.OnClickListener listener);

    /**
     * 添加一个View item
     *
     * @param view item title
     * @return TextView
     */
    void addMenuItem(@NonNull View view);

    /**
     * 添加一个View item
     *
     * @param view       item title
     * @param addToFirst 是否添加到第一个
     * @return TextView
     */
    void addMenuItem(@NonNull View view, boolean addToFirst);

    /**
     * 添加一个tab
     *
     * @param tabView View
     */
    void addTab(View tabView);

    LinearLayout getTabLayout();

    void setViewPager(ViewPager viewPager);

    /**
     * 设置底部线条是否可见
     *
     * @param visibility {@link View#VISIBLE}
     */
    void setLineVisibility(int visibility);

    /**
     * 设置线条颜色
     *
     * @param color
     */
    void setLineColor(@ColorInt int color);

    /**
     * 设置线条高度
     *
     * @param height 高度
     */
    void setLineHeight(int height);

    void setBackgroundColor(int color);

    void setBackgroundResource(int resId);
}
