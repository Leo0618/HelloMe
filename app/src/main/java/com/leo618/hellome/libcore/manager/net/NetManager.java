package com.leo618.hellome.libcore.manager.net;

import com.leo618.hellome.libcore.base.BaseBean;
import com.leo618.hellome.libcore.common.Const;
import com.leo618.hellome.libcore.interf.IRequestCallback;
import com.leo618.hellome.libcore.manager.JsonManager;
import com.leo618.hellome.libcore.util.Logg;
import com.leo618.hellome.libcore.util.NetworkUtil;
import com.leo618.hellome.libcore.util.UIUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


/**
 * function : 网络请求类（单例模式）.所有请求均是异步操作，回调接口运行在UI线程.
 *
 * <p>Created by lzj on 2015/11/3.</p>
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class NetManager {
    private static final String TAG = NetManager.class.getSimpleName();
    private static final AtomicReference<NetManager> INSTANCE = new AtomicReference<>();
    private final Map<String, IRequestCallback> mCallbackMap;

    public static NetManager getInstance() {
        for (; ; ) {
            NetManager netManager = INSTANCE.get();
            if (netManager != null) return netManager;
            netManager = new NetManager();
            if (INSTANCE.compareAndSet(null, netManager)) return netManager;
        }
    }

    //--------------------static method-----start

    /** GET 全路径请求 */
    public static void getByAllPath(String url, IRequestCallback iRequestCallback) {
        getInstance().getByAllPath(NET_TAG(), url, iRequestCallback);
    }

    /** GET-Silence  全路径请求 */
    public static void getSilenceByAllPath(String url, IRequestCallback iRequestCallback) {
        getInstance().getSilenceByAllPath(NET_TAG(), url, iRequestCallback);
    }

    /** GET */
    public static void get(String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        getInstance().get(NET_TAG(), url, paramMap, iRequestCallback);
    }

    /** GET-Silence */
    public static void getSilence(String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        getInstance().getSilence(NET_TAG(), url, paramMap, iRequestCallback);
    }

    /** POST */
    public static void post(String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        getInstance().post(NET_TAG(), url, paramMap, iRequestCallback);
    }

    /** POST-Silence */
    public static void postSilence(String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        getInstance().postSilence(NET_TAG(), url, paramMap, iRequestCallback);
    }

    /** download */
    public static void download(String url, String filePath, IRequestCallback iRequestCallback) {
        getInstance().download(NET_TAG(), url, filePath, iRequestCallback);
    }

    /** download-Silence */
    public static void downloadSilence(String url, String filePath, IRequestCallback iRequestCallback) {
        getInstance().downloadSilence(NET_TAG(), url, filePath, iRequestCallback);
    }

    /** upload */
    public static void upload(String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        getInstance().upload(NET_TAG(), url, paramMap, fileMap, iRequestCallback);
    }

    /** upload-Silence */
    public static void uploadSilence(String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        getInstance().uploadSilence(NET_TAG(), url, paramMap, fileMap, iRequestCallback);
    }

    /** 清除用户持久化的cookie，用于退出登录 */
    public static void clearUserCookie() {
        OkHttpClientWrap.getInstance().clearUserCookie();
    }
    //--------------------static method-----end

    /**
     * 统一httpGet请求入口
     *
     * @param tag              请求类型标记,用于用户处理网络请求比如取消请求
     * @param url              请求完整地址url
     * @param iRequestCallback 回调接口
     */
    public void getByAllPath(String tag, String url, IRequestCallback iRequestCallback) {
        if (filterBeforeRequest(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        Logg.e(TAG, "get--> " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().get(url, new NetResponseCallback(tag), tag);
    }

    /**
     * 静默访问，未作任何处理,需要自己做一些处理动作，比如网络是否可用、数据异常等等,多用于不考虑服务器返回结果的情况下
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param iRequestCallback 请求回调
     */
    public void getSilenceByAllPath(String tag, String url, IRequestCallback iRequestCallback) {
        if (filterBeforeRequestSilence(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        Logg.e(TAG, "getSilenceByAllPath : url = " + url);
        OkHttpClientWrap.getInstance().get(url, new SilenceNetResponseCallback(tag), tag);
    }

    /**
     * 统一httpGet请求入口
     *
     * @param tag              请求类型标记,用于用户处理网络请求比如取消请求
     * @param url              请求完整地址url
     * @param paramMap         参数集合 key=value键值对
     * @param iRequestCallback 回调接口
     */
    public void get(String tag, String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequest(tag, url, iRequestCallback)) return;
        if (paramMap == null) return;
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            url += entry.getKey() + "=" + entry.getValue() + "&";
        }
        url = url.substring(0, url.length() - 1);
        Logg.e(TAG, "get--> " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().get(url, new NetResponseCallback(tag), tag);
    }

    /**
     * 静默访问，未作任何处理,需要自己做一些处理动作，比如网络是否可用、数据异常等等,多用于不考虑服务器返回结果的情况下
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param paramMap         参数集合 key=value键值对
     * @param iRequestCallback 请求回调
     */
    public void getSilence(String tag, String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequestSilence(tag, url, iRequestCallback)) return;
        if (paramMap == null) paramMap = new HashMap<>();
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        if (paramMap.size() > 0) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
            url = url.substring(0, url.length() - 1);
        }
        Logg.e(TAG, "getSilenceByAllPath : url = " + url);
        OkHttpClientWrap.getInstance().get(url, new SilenceNetResponseCallback(tag), tag);
    }

    /**
     * 统一httpPost请求入口
     *
     * @param tag              请求类型标记
     * @param url              请求完整地址url
     * @param iRequestCallback 回调接口
     * @param paramMap         参数集合
     */
    public void post(String tag, String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequest(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        Logg.e(TAG, "post--> " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().post(url, paramMap, new NetResponseCallback(tag), tag);
    }

    /**
     * POST静默访问，未作任何处理,需要自己做一些处理动作，比如网络是否可用、数据异常等等,多用于不考虑服务器返回结果的情况下
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param iRequestCallback 请求回调
     */
    public void postSilence(String tag, String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequestSilence(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        Logg.e(TAG, "postSilence : url = " + url);
        OkHttpClientWrap.getInstance().post(url, paramMap, new SilenceNetResponseCallback(tag), tag);
    }

    /**
     * 统一download请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param filePath         下载完成后文件保存的文件路径
     * @param iRequestCallback 请求回调
     */
    public void download(String tag, String url, String filePath, IRequestCallback iRequestCallback) {
        if (filterBeforeRequest(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        OkHttpClientWrap.getInstance().download(url, filePath, new FileDownloadCallback(tag), tag);
    }

    /**
     * 静默 统一download请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param filePath         下载完成后文件保存的文件路径
     * @param iRequestCallback 请求回调
     */
    public void downloadSilence(String tag, String url, String filePath, IRequestCallback iRequestCallback) {
        if (filterBeforeRequestSilence(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        OkHttpClientWrap.getInstance().download(url, filePath, new FileDownloadCallback(tag), tag);
    }

    /**
     * 统一upload请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param paramMap         上传携带的请求参数
     * @param fileMap          上传文件集合
     * @param iRequestCallback 请求回调
     */
    public void upload(String tag, String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequest(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        OkHttpClientWrap.getInstance().upload(url, paramMap, fileMap, new UploadResponseCallback(tag), tag);
    }

    /**
     * 静默 统一upload请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param paramMap         上传携带的请求参数
     * @param fileMap          上传文件集合
     * @param iRequestCallback 请求回调
     */
    public void uploadSilence(String tag, String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequestSilence(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        OkHttpClientWrap.getInstance().upload(url, paramMap, fileMap, new UploadResponseCallback(tag), tag);
    }

    /** 文件下载拦截回调  ,UI Thread */
    private class FileDownloadCallback extends IRequestCallback<File> {
        private final String tag;

        public FileDownloadCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            Logg.e(TAG, "request----> onStart");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onProgressUpdate(long writedSize, long totalSize, boolean completed) {
            IRequestCallback callback = mCallbackMap.get(tag);
            if (callback != null) {
                callback.onProgressUpdate(writedSize, totalSize, completed);
            }
        }

        @Override
        public void onSuccess(File file) {
            if (null == file) {
                Logg.e(TAG, "onSuccess : file is null.");
                this.onFailure(null);
                return;
            }

            IRequestCallback callback = mCallbackMap.get(tag);
            if (callback == null) {
                return;
            }
            callback.onSuccess(file);
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            Logg.e(TAG, "request----> onFailure:" + e.getMessage());
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(null);
                removeRequestCallback(tag);
            }
        }
    }

    /** 网络请求拦截回调  ,UI Thread */
    private class NetResponseCallback extends IRequestCallback<String> {
        private final String tag;

        public NetResponseCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            Logg.e(TAG, "request----> onStart");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onSuccess(String response) {
            Logg.e(TAG, "request----> onSuccess : " + response);
            if (null == response) {
                Logg.e(TAG, "onSuccess : response is null.");
                this.onFailure(null);
                return;
            }

            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) {
                return;
            }
            if (callBack.mClassType == String.class) {
                callBack.onSuccess(response);
            } else {
                BaseBean baseBean = JsonManager.parseObject(response, BaseBean.class);
                if (baseBean == null) {
                    Logg.e(TAG, "onSuccess : BaseBean is null.");
                    callBack.onFailure(null);
                    return;
                }
                if (baseBean.getStatus() == Const.RESP_OK) { // ok
                    final Object parseObject = JsonManager.parseType(response, callBack.mClassType);
                    if (parseObject == null) {
                        Logg.e(TAG, "onSuccess : parseObject is null.");
                        callBack.onFailure(new RuntimeException("数据解析错误"));
                    } else {
                        callBack.onSuccess(parseObject);
                    }
                } else if (baseBean.getStatus() == Const.RESP_RE_LOGIN) { // reLogin
                    Logg.e(TAG, "onSuccess : need reLogin.");
                    callBack.onFailure(new RuntimeException("请重新登录"));
                    // go to login here
                } else {
                    Logg.e(TAG, "onSuccess : status = " + baseBean.getStatus());
                    callBack.onFailure(new RuntimeException(baseBean.getMsg()));
                }
            }
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            Logg.e(TAG, "request----> onError " + e.getMessage());
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(null);
                removeRequestCallback(tag);
            }
        }
    }

    /** 静默网络请求拦截回调  ,UI Thread */
    private class SilenceNetResponseCallback extends IRequestCallback<String> {
        private final String tag;

        public SilenceNetResponseCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            Logg.e(TAG, "SilenceNetResponseCallback: request----> onStart");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onSuccess(String response) {
            Logg.e(TAG, "SilenceNetResponseCallback: request----> onSuccess : " + response);
            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) {
                return;
            }
            if (callBack.mClassType == String.class) {
                callBack.onSuccess(response);
            } else {
                Object parseObject = JsonManager.parseType(response, callBack.mClassType);
                if (parseObject == null) {
                    Logg.e(TAG, "SilenceNetResponseCallback : onSuccess : parseObject is null.");
                    callBack.onFailure(new RuntimeException("数据解析错误"));
                } else {
                    callBack.onSuccess(parseObject);
                }
            }
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            Logg.e(TAG, "SilenceNetResponseCallback: request----> onError " + e.getMessage());
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(null);
                removeRequestCallback(tag);
            }
        }
    }

    /** 上传文件拦截回调  ,UI Thread */
    private class UploadResponseCallback extends IRequestCallback<String> {
        private final String tag;

        public UploadResponseCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            Logg.e(TAG, "UploadResponseCallback: request----> onStart");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onProgressUpdate(long writedSize, long totalSize, boolean completed) {
            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) {
                return;
            }
            callBack.onProgressUpdate(writedSize, totalSize, completed);
        }

        @Override
        public void onSuccess(String response) {
            Logg.e(TAG, "UploadResponseCallback: request----> onSuccess : " + response);
            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) {
                return;
            }
            if (callBack.mClassType == String.class) {
                callBack.onSuccess(response);
            } else {
                Object parseObject = JsonManager.parseType(response, callBack.mClassType);
                if (parseObject == null) {
                    Logg.e(TAG, "UploadResponseCallback : onSuccess : parseObject is null.");
                    callBack.onFailure(new RuntimeException("数据解析错误"));
                } else {
                    callBack.onSuccess(parseObject);
                }
            }
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            Logg.e(TAG, "SilenceNetResponseCallback: request----> onError " + e.getMessage());
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(null);
                removeRequestCallback(tag);
            }
        }

    }

    /** 请求前进行过滤检测处理 */
    private boolean filterBeforeRequest(String tag, String url, IRequestCallback iRequestCallback) {
        if (!NetworkUtil.isNetworkConnected(UIUtil.getContext())) {
            UIUtil.showToastShort("网络似乎有问题");
            iRequestCallback.onFailure(null);
            return true;
        }
        return false;
    }

    /** 请求前进行过滤检测处理(for 静默) */
    private boolean filterBeforeRequestSilence(String tag, String url, IRequestCallback iRequestCallback) {
        if (!NetworkUtil.isNetworkConnected(UIUtil.getContext())) {
            iRequestCallback.onFailure(null);
            return true;
        }
        return false;
    }


    /** 移除指定类型的回调接口 */
    public void removeRequestCallback(String tag) {
        IRequestCallback iRequestCallBack = mCallbackMap.get(tag);
        if (null != iRequestCallBack) {
            mCallbackMap.remove(tag);
        }
    }

    private NetManager() {
        mCallbackMap = new ConcurrentHashMap<>();
    }

    /** 产生唯一标记tag */
    private static String NET_TAG() {
        return String.valueOf(System.currentTimeMillis());
    }

}
