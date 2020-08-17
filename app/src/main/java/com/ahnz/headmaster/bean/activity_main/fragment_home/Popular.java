package com.ahnz.headmaster.bean.activity_main.fragment_home;

import java.io.Serializable;
import java.util.List;

/**
 * @author xzb
 * @description: 火爆人气 实体Bean
 * @date :2020/7/9 10:30
 */
public class Popular implements Serializable {
    /**
     * status : 200
     * msg : 成功
     * data : [{"id":4206,"image":"http://app1.qianr.com/static/head/fetch/1349065e75aad2a0db2.jpg"},{"id":4205,"image":"http://app1.qianr.com/static/head/fetch/1349065e75aad206df7.jpg"},{"id":4204,"image":"http://app1.qianr.com/static/head/fetch/1349075e75aad3929a8.jpg"},{"id":4203,"image":"http://app1.qianr.com/static/head/fetch/1349065e75aad26274a.jpg"}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 4206
         * image : http://app1.qianr.com/static/head/fetch/1349065e75aad2a0db2.jpg
         */
        private int id;
        private String image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
