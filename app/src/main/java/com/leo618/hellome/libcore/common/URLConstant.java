package com.leo618.hellome.libcore.common;


/**
 * function :  接口地址常量池,动态获取host.
 * <p></p>
 * Created by lzj on 2015/12/31.
 */
@SuppressWarnings("ALL")
public class URLConstant {
    /** Host地址 -- 安全地址 : 用于服务端安全数据交互，如Json请求获取等 */
    public static String URL_BASE_S;
    /** Host地址 -- cdn资源地址 : 用于静态资源请求，如图片加载 */
    public static String URL_BASE_C;

    static {
        // 开发测试环境
        if (Configs.DEBUG) {
            URL_BASE_S = "https://leo618.com";
            URL_BASE_C = "http://leo618.com";
        }
        // 上线生产环境
        else {
            URL_BASE_S = "https://leo618.com";
            URL_BASE_C = "http://leo618.com";
        }
    }

    //-----------------------------------------------------业务接口
    public static String AUTHOR_INFO = "http://dev.baobao.kinimi.com/index.php?m=api&c=user&a=login&";

}