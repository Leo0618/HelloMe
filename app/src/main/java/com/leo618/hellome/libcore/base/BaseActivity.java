package com.leo618.hellome.libcore.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.leo618.hellome.libcore.MyApp;
import com.leo618.hellome.libcore.common.Const;
import com.leo618.hellome.libcore.manager.EventManager;
import com.leo618.hellome.libcore.manager.SystemBarTintManager;
import com.leo618.hellome.libcore.util.Logg;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.ButterKnife;


/**
 * function:android 系统中的四大组件之一Activity基类.
 * <p></p>
 * Created by lzj on 2015/12/31.
 */
@SuppressWarnings({"unused", "SameReturnValue"})
public abstract class BaseActivity extends AppCompatActivity {
    /** 日志输出标志,当前类的类名 */
    protected final String TAG = this.getClass().getSimpleName();

    /** 整个应用Applicaiton */
    private MyApp mApplication;
    /** 当前Activity的弱引用，防止内存泄露 */
    private WeakReference<Activity> activityWeakReference = null;
    /** 系统状态栏管理类 */
    private SystemBarTintManager mSysbarTintManager;

    /**
     * 设置activity布局视图的资源id
     */
    protected abstract int setContentViewResId();

    /**
     * 处理业务逻辑
     */
    protected abstract void doBusiness();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logg.d(TAG, TAG + "-->onCreate()");
        int contentViewResId = this.setContentViewResId();
        if (contentViewResId == 0) {
            throw new IllegalArgumentException("must set activity's contentViewResId.");
        }
        setContentView(contentViewResId);
        mApplication = (MyApp) this.getApplication(); // 获取应用Application
        activityWeakReference = new WeakReference<Activity>(this); // 将当前Activity压入栈
        mApplication.pushTask(activityWeakReference);
        EventManager.register(this);//事件注册
        ButterKnife.bind(this);//ButterKnife注入
        this.doBusiness();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (this.enableSystemBarManager()) {
            initSystemBarManager();//初始化状态栏管理器 默认设置了bg_primary颜色
        }
    }

    @Override
    protected void onRestart() {
        Logg.d(TAG, TAG + "-->onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Logg.d(TAG, TAG + "-->onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Logg.d(TAG, TAG + "-->onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Logg.d(TAG, TAG + "-->onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logg.d(TAG, TAG + "-->onStop()");
        super.onStop();
    }

    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        } else {
            return mDestroyed;
        }
    }

    private boolean mDestroyed;

    @Override
    protected void onDestroy() {
        Logg.d(TAG, TAG + "-->onDestroy()");
        EventManager.unregister(this);//事件反注册
        ButterKnife.unbind(this);//ButterKnife注入解绑
        if (mApplication != null && activityWeakReference != null) {
            mApplication.removeTask(activityWeakReference);
        }
        super.onDestroy();
        mDestroyed = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = supportFragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode & 0xffff, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = supportFragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls) {
        this.startActivity(cls, null);
    }

    /**
     * 跳转activity
     */
    protected void startActivity(Class<?> cls, String strParam) {
        Intent intent = new Intent(this, cls);
        if (!TextUtils.isEmpty(strParam)) {
            intent.putExtra(Const.KEY_INTENT_JUMP_DATA, strParam);
        }
        this.startActivity(intent);
    }

    /** 是否需要处理软键盘失去焦点隐藏的事件 */
    protected boolean enableDispatchTouchEventOnSoftKeyboard() {
        return true;
    }

    /** 是否启用SystemBarManager */
    protected boolean enableSystemBarManager() {
        return true;
    }

    //处理失去焦点软键盘隐藏事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!this.enableDispatchTouchEventOnSoftKeyboard()) {
            return super.dispatchTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    /**
     * 初始化状态栏管理器
     */
    private void initSystemBarManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mSysbarTintManager = new SystemBarTintManager(this);
            mSysbarTintManager.setStatusBarTintEnabled(true);
        }
    }

    /** 获取系统状态栏管理器 可用于设置状态栏颜色透明度等 */
    protected SystemBarTintManager getSysbarTintManager() {
        if (mSysbarTintManager == null) {
            initSystemBarManager();
        }
        return mSysbarTintManager;
    }

    /**
     * 点击输入框外需要隐藏键盘
     */
    private boolean isShouldHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop); // 获取输入框当前的location位置
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 点击的是输入框区域，保留点击EditText的事件
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }


    @SuppressWarnings("unchecked")
    protected final <T extends View> T findView(int id) {
        return (T) this.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

    private ProgressDialog mLoading;

    protected void loadingShow() {
        loadingShow(null);
    }

    protected void loadingShow(String msg) {
        if (mLoading == null) {
            mLoading = new ProgressDialog(this);
            mLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        if (!TextUtils.isEmpty(msg)) mLoading.setMessage(msg);
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }

    protected void loadingDismiss() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
    }
}
