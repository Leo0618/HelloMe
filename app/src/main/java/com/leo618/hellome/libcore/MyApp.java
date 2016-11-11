package com.leo618.hellome.libcore;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.support.multidex.MultiDex;

import com.leo618.hellome.libcore.manager.net.OkHttpClientWrap;

import java.lang.ref.WeakReference;
import java.util.Stack;


/**
 * function : 应用程序入口.
 * <p></p>
 * Created by lzj on 2015/12/31.
 */
@SuppressWarnings("unused")
public class MyApp extends Application {
    private static final String TAG = MyApp.class.getSimpleName();
    private static MyApp mContext;
    private static android.os.Handler mMainThreadHandler;
    private static Looper mMainThreadLooper;
    private static Thread mMainThread;
    private static int mMainThreadId;
    /*** 寄存整个应用Activity **/
    private final Stack<WeakReference<Activity>> mActivitys = new Stack<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        mMainThreadLooper = getMainLooper();
        mMainThreadHandler = new android.os.Handler(mMainThreadLooper);
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();

        OkHttpClientWrap.getInstance().initSSLAccess();//初始化Https访问权限

        new LoadInitDataTask().execute((Void) null);// 异步加载初始数据
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /** 使用异步加载初始配置数据 */
    private class LoadInitDataTask extends android.os.AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // 在这里加载一些不及时使用或者耗时的初始化数据.比如初始化UIL、各类SDK等等,如果涉及到需要请求权限放入Splash去处理
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }
    }

    /**
     * 获取全局上下文
     *
     * @return the mContext
     */
    public static MyApp getApplication() {
        return mContext;
    }

    /**
     * 获取主线程Handler
     *
     * @return the mMainThreadHandler
     */
    public static android.os.Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程轮询器
     *
     * @return the mMainThreadLooper
     */
    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    /**
     * 获取主线程
     *
     * @return the mMainThread
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程ID
     *
     * @return the mMainThreadId
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /************ Application中存放的Activity操作（压栈/出栈）API（开始） ***********************/
    /**
     * 将Activity压入Application栈
     *
     * @param task 将要压入栈的Activity对象
     */
    public void pushTask(WeakReference<Activity> task) {
        mActivitys.push(task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     *
     * @param task 将要移除栈的Activity对象
     */
    public void removeTask(WeakReference<Activity> task) {
        mActivitys.remove(task);
    }

    /**
     * 关闭某个activity
     *
     * @param activityCls 指定activity的类 eg：MainActivity.class
     */
    public void finishActivity(Class<? extends Activity> activityCls) {
        int end = mActivitys.size();
        for (int i = end - 1; i >= 0; i--) {
            Activity cacheActivity = mActivitys.get(i).get();
            if (cacheActivity.getClass().getSimpleName().equals(activityCls.getSimpleName()) && !cacheActivity.isFinishing()) {
                cacheActivity.finish();
                removeTask(i);
            }
        }
    }

    /**
     * 根据指定位置从栈中移除Activity
     *
     * @param taskIndex Activity栈索引
     */
    public void removeTask(int taskIndex) {
        if (mActivitys.size() > taskIndex)
            mActivitys.remove(taskIndex);
    }

    /** 获取顶层activity */
    public Activity getTopActivity() {
        if (mActivitys.size() > 0) {
            return mActivitys.get(mActivitys.size() - 1).get();
        }
        return null;
    }

    /** 移除全部（用于整个应用退出） */
    public void removeAll() {
        int end = mActivitys.size();
        for (int i = end - 1; i >= 0; i--) {
            Activity activity = mActivitys.get(i).get();
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        mActivitys.clear();
    }

    /** 移除除第一个MainActivity之外的全部（主要用于回到MainActivity） */
    public void removeAllExceptFirst() {
        int end = mActivitys.size();
        for (int i = end - 1; i >= 1; i--) {
            Activity activity = mActivitys.get(i).get();
            if (!activity.isFinishing()) {
                activity.finish();
            }
            removeTask(i);
        }
    }
    /*************** Application中存放的Activity操作（压栈/出栈）API（结束） ************************/

}
