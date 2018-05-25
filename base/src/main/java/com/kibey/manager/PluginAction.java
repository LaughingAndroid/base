package com.kibey.manager;

import android.net.Uri;
import android.os.Bundle;

import com.kibey.android.utils.JsonUtils;
import com.kibey.android.utils.StringUtils;
import com.kibey.lib.PluginConfig;

import java.net.URLDecoder;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/13
 * 具体的action，打开activity，获取fragment，获取Class
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public class PluginAction {
    public int type;
    public String page;
    public Object result;
    public Bundle bundle;
    private PluginApk pluginApk;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setPluginApk(PluginApk pluginApk) {
        pluginApk = pluginApk;
    }

    public PluginApk getPluginApk() {
        return pluginApk;
    }

    public static class Builder {
        public static PluginAction build(String url) {
            Uri uri = Uri.parse(URLDecoder.decode(url));
            String s = uri.getHost();
            if (PluginConfig.SCHEME.equals(s)) {
                PluginApk pluginApk = new PluginApk();
                // todo check plugin installed
                int type = StringUtils.parseInt(uri.getQueryParameter("type"));
                String page = uri.getQueryParameter("page");
                String package_name = uri.getQueryParameter("package_name");
                pluginApk.setPackage_name(package_name);
                PluginAction action = new PluginAction();
                action.setPage(page);
                action.setType(type);
                String bundleString = uri.getQueryParameter("bundle");
                Bundle bundle = JsonUtils.getBundleForJson(bundleString);
                action.setBundle(bundle);
                action.setPluginApk(pluginApk);

                return action;
            }
            return null;
        }
    }
}
