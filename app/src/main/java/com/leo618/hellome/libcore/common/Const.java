package com.leo618.hellome.libcore.common;

/**
 * function : 常量池.
 *
 * <p>Created by lzj on 2015/12/31.</p>
 */
public final class Const {

    /** SharedPreferences文件名-->默认 */
    public static final String SP_FILENAME = "AppInfo";

    /** key ：是否已经添加桌面Icon */
    public static final String KEY_HAS_ADD_ICON_ON_DESKTOP = "key_has_add_icon_on_desktop";
    /** Intent跳转 基本数据 */
    public static final String KEY_INTENT_JUMP_DATA = "key_intent_jump_data";
    /** Intent跳转 路径传递 */
    public static final String KEY_INTENT_JUMP_PATH = "key_intent_jump_path";
    /** key ：登录用户信息 */
    public static final String KEY_LOGIN_USER_INFO = "key_login_user_info";
    /** key ：是否已经读取关于我们 */
    public static final String KEY_READ_ABOUT_US = "key_read_about_us";
    /** key-->app更新信息: APP强制更新下载中 */
    public static final String KEY_UPDATE_FORCING_DOWNLOADING = "key_update_forcing_downloading";
    /** key-->是否需要展示引导页 */
    public static final String KEY_NEED_SHOW_GUIDE = "key_need_show_guide";
    /** key-->存储上次使用的APP版本号 */
    public static final String KEY_LAST_VERSION_CODE = "key_last_version_code";

    /** intent result code 直接返回到上一页面 */
    public static final int CODE_BACK = 0x101;
    /** intent result code 要求刷新 */
    public static final int CODE_NEED_REFRESH = CODE_BACK + 1;

    /******************** 视图状态常量 *********************/
    /** 视图状态 ： 加载中 */
    public static final int LAYOUT_LOADING = 0;
    /** 视图状态 ： 无数据空视图 */
    public static final int LAYOUT_EMPTY = LAYOUT_LOADING + 1;
    /** 视图状态 ： 出错 */
    public static final int LAYOUT_ERROR = LAYOUT_LOADING + 2;
    /** 视图状态 ： 数据视图 */
    public static final int LAYOUT_DATA = LAYOUT_LOADING + 3;

    /********************  网络请求服务端状态码常量 *********************/
    /** Reponse code : 100(ok) */
    public static final int RESP_OK = 0;
    /** Reponse code : 4092(reLogin) */
    public static final int RESP_RE_LOGIN = 4092;

}
