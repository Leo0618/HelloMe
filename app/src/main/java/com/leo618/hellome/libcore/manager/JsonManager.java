package com.leo618.hellome.libcore.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.lang.reflect.Type;

/**
 * function:JSON序列化和反序列化帮助类.
 *
 * <p>Created by lzj on 2016/2/19.</p>
 */
@SuppressWarnings("unused")
public class JsonManager {

    /**
     * 解析Json字符串为JavaBean对象
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        T t = null;
        try {
            t = JSON.parseObject(text, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> T parseType(String text, Type clazz) {
        T t = null;
        try {
            t = JSON.parseObject(text, clazz, new Feature[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将JavaBean对象转换成json字符串
     */
    public static String toJSONString(Object javaObject) {
        String result = null;
        try {
            result = JSON.toJSONString(javaObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
