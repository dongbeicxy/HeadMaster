package com.ahnz.headmaster.adapter;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.activity_search.HotSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author xzb
 * @description: 热门搜索 数据 适配器
 * @date :2020/7/9 17:06
 */
public class HotSearchAdapterNew extends BaseQuickAdapter<HotSearch.DataBean, BaseViewHolder> {

    public HotSearchAdapterNew(int layoutResId, List<HotSearch.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HotSearch.DataBean dataBean) {
        baseViewHolder.setText(R.id.flow_tv, dataBean.getName());
    }
}