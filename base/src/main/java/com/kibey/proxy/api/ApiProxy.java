package com.kibey.proxy.api;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/10
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public interface ApiProxy {
    <T> T getApi(final Class<T> service);
}
