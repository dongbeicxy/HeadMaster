package com.ahnz.headmaster.adapter;

import android.app.Activity;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.activity_find_classification_details.FindClassDetails;
import com.ahnz.headmaster.bean.activity_search.SearchResult;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author xzb
 * @description: 搜索结果 列表 适配器
 * @date :2020/7/15 9:36
 */
public class SearchResultRecyclerViewAdapter extends BaseQuickAdapter<SearchResult.DataBean.InfoBean, BaseViewHolder> {
    private Activity activity;

    public SearchResultRecyclerViewAdapter(Activity activity, int layoutResId, List<SearchResult.DataBean.InfoBean> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SearchResult.DataBean.InfoBean dataBean) {
        Glide.with(activity)
                .load(dataBean.getImage())
                //.transform(new RoundedCorners(12)) // 圆角图片
                .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
    }
}
