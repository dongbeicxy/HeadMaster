package com.ahnz.headmaster.adapter;

import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.activity_main.fragment_find.FindClassify;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author xzb
 * @description: 发现列表 适配器
 * @date :2020/7/8 15:03
 */
public class FindFragmentRecyclerViewAdapter extends BaseQuickAdapter<FindClassify.DataBean, BaseViewHolder> {
    private Fragment fragment;

    public FindFragmentRecyclerViewAdapter(Fragment fragment, int layoutResId, List<FindClassify.DataBean> data) {
        super(layoutResId, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FindClassify.DataBean dataBean) {
        Glide.with(fragment)
                .load(dataBean.getImage())
                .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
    }
}
