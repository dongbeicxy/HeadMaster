package com.ahnz.headmaster.bean.activity_search;

import java.io.Serializable;
import java.util.List;

/**
 * @author xzb
 * @description: 大家都在搜
 * @date :2020/7/9 17:11
 */
public class HotSearch implements Serializable {

    /**
     * status : 200
     * msg : 成功
     * data : [{"name":"青春"},{"name":"逗比"},{"name":"天气"},{"name":"美女"},{"name":"篮球"},{"name":"帅哥"}]
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
         * name : 青春
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
