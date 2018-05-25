package com.kibey.proxy.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.kibey.android.data.model.IKeepProguard;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/12
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public interface FragmentBuilderProxy extends IKeepProguard {
    Fragment create(Context context, String pluginName, String page, Bundle bundle);
}
