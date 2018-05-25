package com.kibey.manager;

import android.net.Uri;
import android.os.Bundle;

import com.kibey.android.data.model.Model;
import com.kibey.android.utils.AppProxy;
import com.kibey.android.utils.FileMd5;
import com.kibey.android.utils.FilePathManager;
import com.kibey.android.utils.FileUtils;
import com.kibey.android.utils.JsonUtils;
import com.kibey.android.utils.Md5Util;
import com.kibey.android.utils.StringUtils;
import com.kibey.android.utils.WebUtils;
import com.kibey.lib.PluginApkManager;
import com.kibey.lib.PluginConfig;

import java.net.URLDecoder;

import rx.Subscriber;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/13
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public class PluginApk extends Model {
    String id;
    String name;
    String package_name;
    String url;
    String md5;
    String uri_list;

    private String _path;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public String getUrl() {
        return url;
    }

    public String getUri_list() {
        return uri_list;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setUri_list(String uri_list) {
        this.uri_list = uri_list;
    }

    public String dexOutPath() {
        return package_name + "_dex";
    }

    public boolean isLoad() {
        return PluginApkManager.getInstance().getHelper(package_name) != null;
    }

    public String getPath() {
        _path = FilePathManager.getFilepath() + "/plugin/" + Md5Util.makeMd5Sum(package_name);
        return _path;
    }

    boolean isInstalling;

    public boolean isInstalling() {
        return isInstalling;
    }

    public rx.Observable<Boolean> install() {
        if (isLoad()) {
            return rx.Observable.just(true);
        } else {
            isInstalling = true;
            return rx.Observable.create(new rx.Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    String path = getPath();
                    String localMd5 = FileMd5.getMD5(path);
                    boolean downloaded = false;
                    if (!localMd5.equals(md5)) {
                        downloaded = WebUtils.download(url, getPath());
                    } else {
                        downloaded = true;
                    }
                    if (downloaded) {
                        PluginApkManager.getInstance().loadApk(package_name, getPath(), dexOutPath(), AppProxy.getApp());
                    }
                    subscriber.onNext(downloaded);
                    subscriber.onCompleted();
                    isInstalling = false;
                }
            });
        }
    }

    public void uninstall() {
        FileUtils.delete(getPath());
        PluginApkManager.getInstance().uninstall(package_name);
    }


}
