package com.leo618.hellome.hello.bean;

import com.leo618.hellome.libcore.base.BaseBean;

/**
 * function:
 *
 * <p></p>
 * Created by lzj on 2016/11/10.
 */

@SuppressWarnings("ALL")
public class AuthorInfoBean extends BaseBean {

    private String token;
    private String userid;
    private String nickname;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "AuthorInfoBean{" +
                "token='" + token + '\'' +
                ", userid='" + userid + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
