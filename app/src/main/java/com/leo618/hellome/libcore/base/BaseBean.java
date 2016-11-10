package com.leo618.hellome.libcore.base;

/**
 * function: 基础响应状态类
 *
 * <p></p>
 * Created by lzj on 2016/7/12.
 */
@SuppressWarnings("ALL")
public class BaseBean {
    protected int status;

    protected String ret_msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }
}
