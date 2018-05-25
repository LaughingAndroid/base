package com.kibey.proxy.api;

import com.kibey.android.data.model.IKeepProguard;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/10
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public interface ApiProxy extends IKeepProguard{
    <T> T getApi(final Class<T> service);
}
