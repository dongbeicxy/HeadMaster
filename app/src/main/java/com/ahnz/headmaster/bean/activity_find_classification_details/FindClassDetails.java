package com.ahnz.headmaster.bean.activity_find_classification_details;

import java.io.Serializable;
import java.util.List;

/**
 * @author xzb
 * @description:发现 分类详情
 * @date :2020/7/10 17:42
 */
public class FindClassDetails implements Serializable {

    /**
     * status : 200
     * msg : 成功
     * data : [{"id":480,"image":"http://app1.qianr.com/static/head/fetch/1340325e75a8d035f91.jpg"},{"id":479,"image":"http://app1.qianr.com/static/head/fetch/1340355e75a8d330388.jpg"},{"id":478,"image":"http://app1.qianr.com/static/head/fetch/1340335e75a8d18ad35.jpg"},{"id":477,"image":"http://app1.qianr.com/static/head/fetch/1340345e75a8d251769.jpg"},{"id":476,"image":"http://app1.qianr.com/static/head/fetch/1340385e75a8d609854.jpg"},{"id":475,"image":"http://app1.qianr.com/static/head/fetch/1340335e75a8d170885.jpg"},{"id":474,"image":"http://app1.qianr.com/static/head/fetch/1340345e75a8d289631.jpg"},{"id":473,"image":"http://app1.qianr.com/static/head/fetch/1340345e75a8d2b85cd.jpg"}]
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
         * id : 480
         * image : http://app1.qianr.com/static/head/fetch/1340325e75a8d035f91.jpg
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
