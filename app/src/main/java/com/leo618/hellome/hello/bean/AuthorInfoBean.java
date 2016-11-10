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

    //{"status":0,"msg":"","data":{"author":"leo","phone":"18820285271","qq":"619827587"}}

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String author;
        private String phone;
        private String qq;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }
}
