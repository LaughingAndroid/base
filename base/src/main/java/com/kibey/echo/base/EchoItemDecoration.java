package com.kibey.echo.base;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.kibey.android.ui.adapter.BaseRVAdapter;
import com.kibey.android.utils.AppProxy;
import com.kibey.android.utils.EchoColor;
import com.kibey.android.utils.ViewUtils;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mchwind
 * @version V5.7
 * @since 16/9/29
 */
public class EchoItemDecoration {
    public static final int DEFAULT_ITEM_DIVIDER_SIZE = ViewUtils.dp2Px(.5f);

    public static HorizontalDividerItemDecoration.Builder createBuilder() {
        Map<String, Integer> mSizeMap = new HashMap<>();
        Map<String, Integer> marginLeftMap = new HashMap<>();
        Map<String, Integer> marginRightMap = new HashMap<>();
        return new HorizontalDividerItemDecoration.Builder(AppProxy.getApp())
                .colorProvider(new FlexibleDividerDecoration.ColorProvider() {
                    @Override
                    public int dividerColor(int position, RecyclerView parent) {
                        RecyclerView.LayoutManager manager = parent.getLayoutManager();
                        if (null != manager) {
                            View view = manager.findViewByPosition(position);
                            if (null != view && view.getTag() instanceof HeadRV) {
                                return Color.TRANSPARENT;
                            }
                        }
                        return EchoColor.COLOR.LINE;
                    }
                })
                .marginProvider(new HorizontalDividerItemDecoration.MarginProvider() {
                    @Override
                    public int dividerLeftMargin(int position, RecyclerView parent) {
                        return getMarginLeft(position, parent, marginLeftMap);
                    }

                    @Override
                    public int dividerRightMargin(int position, RecyclerView parent) {
                        return getMarginRight(position, parent, marginRightMap);
                    }
                })
                .sizeProvider(new FlexibleDividerDecoration.SizeProvider() {
                    @Override
                    public int dividerSize(int position, RecyclerView parent) {
                        return getSize(position, parent, mSizeMap);
                    }
                });
    }

    public static HorizontalDividerItemDecoration create() {
        return createBuilder().build();
    }

    private static int getSize(int position, RecyclerView parent, Map<String, Integer> sizeMap) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (null != manager) {
            int size = 0;
            View view = manager.findViewByPosition(position);
            if (null != view) {
                if (view.getTag() instanceof ItemSize) {
                    size = ((ItemSize) view.getTag()).itemSize();
                    sizeMap.put("" + position, size);
                } else if (view.getTag() instanceof HeadRV) {
                    size = 0;
                    sizeMap.put("" + position, size);
                }
            } else {
                Integer temp = sizeMap.get("" + position);
                size = null == temp ? 0 : temp;
            }
            return size;
        }
        return 0;
    }

    private static int getMarginLeft(int position, RecyclerView parent, Map<String, Integer> sizeMap) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (null != manager) {
            int size = 0;
            View view = manager.findViewByPosition(position);
            if (null != view) {
                if (view.getTag() instanceof ItemLeftMargin) {
                    size = ((ItemLeftMargin) view.getTag()).marginLeft();
                    sizeMap.put("" + position, size);
                }
            } else {
                Integer temp = sizeMap.get("" + position);
                size = null == temp ? 0 : temp;
            }
            return size;
        }
        return 0;
    }

    private static int getMarginRight(int position, RecyclerView parent, Map<String, Integer> sizeMap) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (null != manager) {
            int size = 0;
            View view = manager.findViewByPosition(position);
            if (null != view) {
                if (view.getTag() instanceof ItemRightMargin) {
                    size = ((ItemRightMargin) view.getTag()).marginRight();
                    sizeMap.put("" + position, size);
                }
            } else {
                Integer temp = sizeMap.get("" + position);
                size = null == temp ? 0 : temp;
            }
            return size;
        }
        return 0;
    }

    public static RecyclerView.ItemDecoration createDefault(BaseRVAdapter baseRVAdapter, int margin) {
        return new HorizontalDividerItemDecoration.Builder(AppProxy.getApp())
                .color(EchoColor.COLOR.LINE)
                .sizeProvider(new FlexibleDividerDecoration.SizeProvider() {
                    @Override
                    public int dividerSize(int position, RecyclerView parent) {
                        if (position == 0 || position > baseRVAdapter.getItemCount()) {
                            return 0;
                        }
                        return DEFAULT_ITEM_DIVIDER_SIZE;
                    }
                })
                .margin(ViewUtils.dp2Px(margin)).build();
    }


    public static class BaseItemSizeHolder<DATA> extends BaseRVAdapter.BaseViewHolder<DATA> implements ItemSize {
        public BaseItemSizeHolder() {
        }

        public BaseItemSizeHolder(View view) {
            super(view);
        }

        public BaseItemSizeHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }

        public BaseItemSizeHolder(Context context, int layoutId, ViewGroup parent) {
            super(context, layoutId, parent);
        }

        @Override
        public int itemSize() {
            return DEFAULT_ITEM_DIVIDER_SIZE;
        }

        @Override
        public int marginLeft() {
            return 0;
        }

        @Override
        public int marginRight() {
            return 0;
        }
    }

    public interface ItemSize extends ItemLeftMargin {
        int itemSize();
    }

    public interface ItemLeftMargin extends ItemRightMargin {
        int marginLeft();
    }

    public interface ItemRightMargin extends Serializable {
        int marginRight();
    }
}
