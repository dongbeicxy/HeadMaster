package com.ahnz.headmaster.bean.activity_head_details;

import java.util.List;

/**
 * @author xzb
 * @description: 边框和贴图 总数据 包含首个 初始化 Icon
 * @date :2020/7/15 16:15
 */
public class FrameMappingAllData {
    private int resourceId;
    private List<Frame.DataBean> dataBean;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public List<Frame.DataBean> getDataBean() {
        return dataBean;
    }

    public void setDataBean(List<Frame.DataBean> dataBean) {
        this.dataBean = dataBean;
    }
}
