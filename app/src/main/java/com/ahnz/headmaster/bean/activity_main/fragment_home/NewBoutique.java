package com.ahnz.headmaster.bean.activity_main.fragment_home;

import java.io.Serializable;
import java.util.List;

/**
 * @author xzb
 * @description:最新精品 列表数据集
 * @date :2020/7/8 16:23
 */
public class NewBoutique implements Serializable {

    /**
     * status : 200
     * msg : 成功
     * data : {"total":22,"info":[{"date":"03-20","avatars":[{"id":20,"image":"http://app1.qianr.com/static/head/fetch/1339295e75a89154fa9.jpg"},{"id":185,"image":"http://app1.qianr.com/static/head/fetch/1340005e75a8b090994.jpg"}]},{"date":"03-05","avatars":[{"id":1,"image":"http://app1.qianr.com/static/head/fetch/1339265e75a88ed5071.jpg"},{"id":2,"image":"http://app1.qianr.com/static/head/fetch/1339265e75a88eec339.jpg"},{"id":3,"image":"http://app1.qianr.com/static/head/fetch/1339275e75a88f11a8f.jpg"},{"id":4,"image":"http://app1.qianr.com/static/head/fetch/1339275e75a88f2730c.jpg"},{"id":45,"image":"http://app1.qianr.com/static/head/fetch/1339365e75a8984281d.jpg"},{"id":123,"image":"http://app1.qianr.com/static/head/fetch/1339505e75a8a6968ca.jpg"}]},{"date":"03-05","avatars":[{"id":10,"image":"http://app1.qianr.com/static/head/fetch/1339275e75a88fa6baf.jpg"},{"id":20,"image":"http://app1.qianr.com/static/head/fetch/1339295e75a89154fa9.jpg"},{"id":30,"image":"http://app1.qianr.com/static/head/fetch/1339325e75a894d86f5.jpg"}]},{"date":"03-05","avatars":[{"id":100,"image":"http://app1.qianr.com/static/head/fetch/1339435e75a89fab142.jpg"},{"id":200,"image":"http://app1.qianr.com/static/head/fetch/1339595e75a8af80bb1.jpg"},{"id":300,"image":"http://app1.qianr.com/static/head/fetch/1340105e75a8ba33e90.jpg"}]},{"date":"03-05","avatars":[{"id":1000,"image":"http://app1.qianr.com/static/head/fetch/1341585e75a926c84b9.jpg"},{"id":2000,"image":"http://app1.qianr.com/static/head/fetch/1344095e75a9a9b85c1.jpg"},{"id":3000,"image":"http://app1.qianr.com/static/head/fetch/1346185e75aa2ab4c10.jpg"}]}]}
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
         * total : 22
         * info : [{"date":"03-20","avatars":[{"id":20,"image":"http://app1.qianr.com/static/head/fetch/1339295e75a89154fa9.jpg"},{"id":185,"image":"http://app1.qianr.com/static/head/fetch/1340005e75a8b090994.jpg"}]},{"date":"03-05","avatars":[{"id":1,"image":"http://app1.qianr.com/static/head/fetch/1339265e75a88ed5071.jpg"},{"id":2,"image":"http://app1.qianr.com/static/head/fetch/1339265e75a88eec339.jpg"},{"id":3,"image":"http://app1.qianr.com/static/head/fetch/1339275e75a88f11a8f.jpg"},{"id":4,"image":"http://app1.qianr.com/static/head/fetch/1339275e75a88f2730c.jpg"},{"id":45,"image":"http://app1.qianr.com/static/head/fetch/1339365e75a8984281d.jpg"},{"id":123,"image":"http://app1.qianr.com/static/head/fetch/1339505e75a8a6968ca.jpg"}]},{"date":"03-05","avatars":[{"id":10,"image":"http://app1.qianr.com/static/head/fetch/1339275e75a88fa6baf.jpg"},{"id":20,"image":"http://app1.qianr.com/static/head/fetch/1339295e75a89154fa9.jpg"},{"id":30,"image":"http://app1.qianr.com/static/head/fetch/1339325e75a894d86f5.jpg"}]},{"date":"03-05","avatars":[{"id":100,"image":"http://app1.qianr.com/static/head/fetch/1339435e75a89fab142.jpg"},{"id":200,"image":"http://app1.qianr.com/static/head/fetch/1339595e75a8af80bb1.jpg"},{"id":300,"image":"http://app1.qianr.com/static/head/fetch/1340105e75a8ba33e90.jpg"}]},{"date":"03-05","avatars":[{"id":1000,"image":"http://app1.qianr.com/static/head/fetch/1341585e75a926c84b9.jpg"},{"id":2000,"image":"http://app1.qianr.com/static/head/fetch/1344095e75a9a9b85c1.jpg"},{"id":3000,"image":"http://app1.qianr.com/static/head/fetch/1346185e75aa2ab4c10.jpg"}]}]
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
             * date : 03-20
             * avatars : [{"id":20,"image":"http://app1.qianr.com/static/head/fetch/1339295e75a89154fa9.jpg"},{"id":185,"image":"http://app1.qianr.com/static/head/fetch/1340005e75a8b090994.jpg"}]
             */

            private String date;
            private List<AvatarsBean> avatars;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public List<AvatarsBean> getAvatars() {
                return avatars;
            }

            public void setAvatars(List<AvatarsBean> avatars) {
                this.avatars = avatars;
            }

            public static class AvatarsBean {
                /**
                 * id : 20
                 * image : http://app1.qianr.com/static/head/fetch/1339295e75a89154fa9.jpg
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
}
