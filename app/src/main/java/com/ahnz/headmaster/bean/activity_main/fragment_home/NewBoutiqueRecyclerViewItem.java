package com.ahnz.headmaster.bean.activity_main.fragment_home;

import com.chad.library.adapter.base.entity.JSectionEntity;

/**
 * @author xzb
 * @description: 最新精品 列表 Item 实体
 * @date :2020/7/8 16:55
 */
public class NewBoutiqueRecyclerViewItem extends JSectionEntity {

    private boolean isHeader;
    private Object object;

    public NewBoutiqueRecyclerViewItem(boolean isHeader, Object object) {
        this.isHeader = isHeader;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }
}
