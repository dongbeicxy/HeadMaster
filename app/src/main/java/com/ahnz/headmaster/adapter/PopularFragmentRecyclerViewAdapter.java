package com.ahnz.headmaster.adapter;

import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.activity_main.fragment_home.Popular;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author xzb
 * @description: 火爆人气列表 适配器
 * @date :2020/7/8 15:03
 */
public class PopularFragmentRecyclerViewAdapter extends BaseQuickAdapter<Popular.DataBean, BaseViewHolder> {
    private Fragment fragment;

    public PopularFragmentRecyclerViewAdapter(Fragment fragment, int layoutResId, List<Popular.DataBean> data) {
        super(layoutResId, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Popular.DataBean dataBean) {
        Glide.with(fragment)
                .load(dataBean.getImage())
                .transform(new RoundedCorners(12)) // 圆角图片
                .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
    }
}
