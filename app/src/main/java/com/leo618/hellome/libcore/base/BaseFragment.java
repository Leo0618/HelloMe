package com.leo618.hellome.libcore.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo618.hellome.libcore.common.Const;
import com.leo618.hellome.libcore.manager.EventManager;
import com.leo618.hellome.libcore.util.Logg;

import butterknife.ButterKnife;


/**
 * function : Fragment基类(兼容低版本).
 * <p></p>
 * Created by lzj on 2015/12/31.
 */
@SuppressWarnings({"unused", "deprecation"})
public abstract class BaseFragment extends Fragment {
    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();
    /** fragment根布局 */
    protected View mRootView;

    @Override
    public void onAttach(Activity activity) {
        Logg.d(TAG, TAG + "-->onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logg.d(TAG, TAG + "-->onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logg.d(TAG, TAG + "-->onCreate()");
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置fragment布局视图的资源id
     */
    protected abstract int setContentViewResId();

    /**
     * 处理业务逻辑
     */
    protected abstract void doBusiness();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int contentViewResId = this.setContentViewResId();
        if (contentViewResId == 0) {
            throw new IllegalArgumentException("must set activity's contentViewResId.");
        }
        if (null != mRootView) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        mRootView = inflater.inflate(contentViewResId, container, false);
        EventManager.register(this);//事件注册
        ButterKnife.bind(this, mRootView);//ButterKnife注入绑定
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.doBusiness();
    }

    @Override
    public void onResume() {
        Logg.d(TAG, TAG + "-->onResume()");
        super.onResume();
    }

    @Override
    public void onStop() {
        Logg.d(TAG, TAG + "-->onStop()");
        super.onStop();
    }

    @Override
    public void onPause() {
        Logg.d(TAG, TAG + "-->onPause()");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Logg.d(TAG, TAG + "-->onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Logg.d(TAG, TAG + "-->onDestroy()");
        EventManager.unregister(this);
        ButterKnife.unbind(this);//ButterKnife注入解绑
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Logg.d(TAG, TAG + "-->onDetach()");
        super.onDetach();
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls) {
        this.startActivity(cls, null);
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls, String strParam) {
        Intent intent = new Intent(this.getActivity(), cls);
        if (!TextUtils.isEmpty(strParam)) {
            intent.putExtra(Const.KEY_INTENT_JUMP_DATA, strParam);
        }
        this.getActivity().startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T findView(int id) {
        return (T) mRootView.findViewById(id);
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
            mLoading = new ProgressDialog(getActivity());
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
