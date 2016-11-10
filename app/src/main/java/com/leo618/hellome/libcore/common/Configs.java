package com.leo618.hellome.libcore.common;


import com.leo618.hellome.libcore.util.UIUtil;

/**
 * function : 通用配置类.
 *
 * <p>Created by lzj on 2015/12/31.<p/>
 */
public final class Configs {

    /** 当前APK模式 0-开发测试模式；1-上线模式 */
    @SuppressWarnings("FieldCanBeLocal")
    private static int STATE = 0;

    /** 调试开关 */
    public static boolean DEBUG = true;

    static {
        //开发测试环境
        if (0 == STATE) {
            DEBUG = true;
        }
        //生产上线环境
        else if (1 == STATE) {
            DEBUG = false;
        }
        //未知
        else {
            throw new RuntimeException("STATE is wrong!!!");
        }
    }

    /** 日志记录目录 */
    public static final String PATH_BASE_LOG = "/.crashLog/";
    /** 临时文件目录 */
    public static final String PATH_BASE_TEMP = "/temp/";
    /** 公共图片文件目录 */
    public static final String PATH_BASE_PICTURE = "/picture/";
    /** 公共图片文件缓存目录 */
    public static final String PATH_BASE_PICTURE_CACHE = PATH_BASE_PICTURE + "/cache";

    /** 用于更新的apk文件名称 */
    public static final String APKNAME = UIUtil.getContext().getPackageName() + ".apk";
    /** 用于更新的apk临时文件名称 */
    public static final String APKNAMETEMP = UIUtil.getContext().getPackageName() + ".tmp";

    /** 用户token */
    public static String token;

    /** AppKey: 新闻 */
    public static final String JUHE_APPKEY_NEWS = "e21dc85e4e5fdd0654276c4320870249";


}
