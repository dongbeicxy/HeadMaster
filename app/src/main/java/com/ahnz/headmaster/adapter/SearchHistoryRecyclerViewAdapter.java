package com.ahnz.headmaster.adapter;


import com.ahnz.headmaster.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author xzb
 * @description: 搜索历史 列表 适配器
 * @date :2020/7/9 17:30
 */
public class SearchHistoryRecyclerViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchHistoryRecyclerViewAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String string) {
        baseViewHolder.setText(R.id.head_tv, string);
    }
}
