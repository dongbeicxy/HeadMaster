package com.ahnz.headmaster.bean.activity_search;

import java.io.Serializable;
import java.util.List;

/**
 * @author xzb
 * @description: 搜索结果
 * @date :2020/7/15 9:27
 */
public class SearchResult implements Serializable {
    /**
     * status : 200
     * msg : 成功
     * data : {"total":31,"info":[{"id":9285,"image":"https://app1-api.qianr.com/static/head/fetch/1400475e75ad8f64630.jpg"},{"id":9241,"image":"https://app1-api.qianr.com/static/head/fetch/1400445e75ad8c6af12.jpg"}]}
     */
    private int status;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total : 31
         * info : [{"id":9285,"image":"https://app1-api.qianr.com/static/head/fetch/1400475e75ad8f64630.jpg"},{"id":9241,"image":"https://app1-api.qianr.com/static/head/fetch/1400445e75ad8c6af12.jpg"}]
         */

        private int total;
        private List<InfoBean> info;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 9285
             * image : https://app1-api.qianr.com/static/head/fetch/1400475e75ad8f64630.jpg
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
}
