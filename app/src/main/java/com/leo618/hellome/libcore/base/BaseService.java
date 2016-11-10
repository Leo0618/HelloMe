package com.leo618.hellome.libcore.base;

import android.app.Service;
import android.content.Intent;

import com.leo618.hellome.libcore.util.Logg;


/**
 * function : android 系统中的四大组件之一Service基类.
 * <p></p>
 * Created by lzj on 2015/12/31.
 */
public abstract class BaseService extends Service {

    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        Logg.d(TAG, TAG + "-->onCreate()");
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        Logg.d(TAG, TAG + "-->onStart()");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Logg.d(TAG, TAG + "-->onDestroy()");
        super.onDestroy();
    }

}
