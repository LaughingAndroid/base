package com.kibey.proxy.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/12
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public interface FragmentBuilderProxy {
    Fragment create(Context context, String pluginName, String page, Bundle bundle);
}
