package com.ahnz.headmaster.adapter;

import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.activity_find_classification_details.FindClassDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author xzb
 * @description: 发现 分类详情 列表 适配器
 * @date :2020/7/8 15:03
 */
public class FindClassDetailsRecyclerViewAdapter extends BaseQuickAdapter<FindClassDetails.DataBean, BaseViewHolder> {
    private FragmentActivity fragmentActivity;

    public FindClassDetailsRecyclerViewAdapter(FragmentActivity fragmentActivity, int layoutResId, List<FindClassDetails.DataBean> data) {
        super(layoutResId, data);
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FindClassDetails.DataBean dataBean) {
        Glide.with(fragmentActivity)
                .load(dataBean.getImage())
                //.transform(new RoundedCorners(12)) // 圆角图片
                .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
    }
}
