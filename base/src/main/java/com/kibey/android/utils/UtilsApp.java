package com.kibey.android.utils;

import android.app.Application;


/**
 * @author mchwind
 * @version V6.1
 * @since 17/3/17
 */
public class UtilsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // todo 引用qiniu，设置上传工具
        // UploadUtilsImpl.setsUploadUtils(paths -> UploadUtil.uploadFilesToQiniuRx(UploadUtil.FileType.scope_image, paths));

        // test res
        AppProxy.init(this);
    }
}
