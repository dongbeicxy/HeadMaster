package com.ahnz.headmaster.bean.activity_main.fragment_find;

import java.io.Serializable;
import java.util.List;

/**
 * @author xzb
 * @description: 发现分类
 * @date :2020/7/8 14:58
 */
public class FindClassify implements Serializable {
    /**
     * status : 200
     * msg : 成功
     * data : [{"id":1,"name":"欧美","image":"http://app1.qianr.com//uploads/banner/20200314154616.png"},{"id":2,"name":"情侣","image":"http://app1.qianr.com//uploads/banner/20200314154629.png"},{"id":3,"name":"治愈","image":"http://app1.qianr.com//uploads/banner/20200314154640.png"},{"id":4,"name":"小清新","image":"http://app1.qianr.com//uploads/banner/20200314154650.png"},{"id":5,"name":"逗比","image":"http://app1.qianr.com//uploads/banner/20200314154701.png"},{"id":6,"name":"超萌宠","image":"http://app1.qianr.com//uploads/banner/20200314154713.png"},{"id":7,"name":"超级英雄","image":"http://app1.qianr.com//uploads/banner/20200314163405.png"},{"id":8,"name":"动漫","image":"http://app1.qianr.com//uploads/banner/20200314154744.png"},{"id":9,"name":"文艺","image":"http://app1.qianr.com//uploads/banner/20200314154754.png"},{"id":10,"name":"唯美文字","image":"http://app1.qianr.com//uploads/banner/20200314163425.png"},{"id":11,"name":"简约","image":"http://app1.qianr.com//uploads/banner/20200314154857.png"},{"id":12,"name":"明星","image":"http://app1.qianr.com//uploads/banner/20200314154911.png"}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * name : 欧美
         * image : http://app1.qianr.com//uploads/banner/20200314154616.png
         */
        private int id;
        private String name;
        private String image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
