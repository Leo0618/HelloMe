package com.leo618.hellome.libcore;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Looper;
import android.support.multidex.MultiDex;

import com.leo618.hellome.hello.hotfix.TinkerManager;
import com.leo618.hellome.libcore.manager.net.OkHttpClientWrap;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.lang.ref.WeakReference;
import java.util.Stack;


/**
 * function : 应用程序入口.
 * <p></p>
 * Created by lzj on 2015/12/31.
 */
@SuppressWarnings("ALL")
@DefaultLifeCycle(application = "com.leo618.hellome.libcore.MyApp",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class MyAppLike extends DefaultApplicationLike {
    private static final String TAG = MyAppLike.class.getSimpleName();
    private static MyAppLike mMyAppLike;
    private static Application mContext;
    private static android.os.Handler mMainThreadHandler;
    private static Looper mMainThreadLooper;
    private static Thread mMainThread;
    private static int mMainThreadId;
    /*** 寄存整个应用Activity **/
    private final Stack<WeakReference<Activity>> mActivitys = new Stack<>();

    public MyAppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent, Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        mMyAppLike = this;
        // install tinker
        TinkerManager.sampleInstallTinker(this);
        //my before logic
        initMyLogic();
    }

    private void initMyLogic() {
        mContext = getApplication();
        mMainThreadLooper = getApplication().getMainLooper();
        mMainThreadHandler = new android.os.Handler(mMainThreadLooper);
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();

        OkHttpClientWrap.getInstance().initSSLAccess();//初始化Https访问权限

        new LoadInitDataTask().execute((Void) null);// 异步加载初始数据
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

    public static MyAppLike getAppLike() {
        return mMyAppLike;
    }

    /**
     * 获取全局上下文
     *
     * @return the mContext
     */
    public static Application getApp() {
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
