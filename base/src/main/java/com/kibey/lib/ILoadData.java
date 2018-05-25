package com.kibey.lib;

import com.kibey.android.data.model.SuperHolderData;
import com.kibey.android.ui.adapter.BaseRVAdapter;

import java.util.List;

import rx.Observable;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/25
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public interface ILoadData {
    BaseRVAdapter getAdapter();

    /**
     * 正常模式，返回data list
     * buildHolder自己添加
     *
     * @return
     */
    Observable<List> loadData();

    /**
     * 服务器配置模式
     * holder_info配置buildHolder
     * data_info对应loadData，返回一个data list
     * @return
     */
    Observable<SuperHolderData> loadSuperHolderData();
}
