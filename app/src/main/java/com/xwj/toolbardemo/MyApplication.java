package com.xwj.toolbardemo;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * 应用初始入口
 * Created by xuwj on 2016/4/24.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 根据build.gradle来配置是否显示Log
        if (BuildConfig.LOG_DEBUG) {
            Logger.init("toolbar_log").setLogLevel(LogLevel.FULL);
        } else {
            Logger.init("toolbar_log").setLogLevel(LogLevel.NONE);
        }
    }
}
