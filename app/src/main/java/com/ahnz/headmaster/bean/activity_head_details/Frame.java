package com.ahnz.headmaster.bean.activity_head_details;

import java.io.Serializable;
import java.util.List;

/**
 * @author xzb
 * @description: 边框 实体
 * @date :2020/7/15 15:32
 */
public class Frame implements Serializable {
    /**
     * status : 200
     * msg : 成功
     * data : [{"material":"https://app1-api.qianr.com//static/head/frame/20200326092110531.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200326092101921.png","is_vip":2},{"material":"https://app1-api.qianr.com//static/head/frame/20200326092039178.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200326092045254.png","is_vip":2},{"material":"https://app1-api.qianr.com//static/head/frame/20200326091754721.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200326091802978.png","is_vip":2},{"material":"https://app1-api.qianr.com//static/head/frame/20200319094813313.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200319094817341.png","is_vip":1},{"material":"https://app1-api.qianr.com//static/head/frame/20200319094759746.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200319094803596.png","is_vip":1},{"material":"https://app1-api.qianr.com//static/head/frame/20200521152410496.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200521152418288.png","is_vip":1},{"material":"https://app1-api.qianr.com//static/head/frame/20200521152525797.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200521152533839.png","is_vip":1},{"material":"https://app1-api.qianr.com//static/head/frame/20200521152608873.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200521152559553.png","is_vip":1},{"material":"https://app1-api.qianr.com//static/head/frame/20200521152626131.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200521152635111.png","is_vip":1},{"material":"https://app1-api.qianr.com//static/head/frame/20200521152849819.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200521152814488.png","is_vip":1},{"material":"https://app1-api.qianr.com//static/head/frame/20200521152920561.png","thumb":"https://app1-api.qianr.com//static/head/frame/20200521152927303.png","is_vip":1}]
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
         * material : https://app1-api.qianr.com//static/head/frame/20200326092110531.png
         * thumb : https://app1-api.qianr.com//static/head/frame/20200326092101921.png
         * is_vip : 2
         */
        private String material;
        private String thumb;
        private int is_vip;
        private int resourceId;

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
        }
    }
}
