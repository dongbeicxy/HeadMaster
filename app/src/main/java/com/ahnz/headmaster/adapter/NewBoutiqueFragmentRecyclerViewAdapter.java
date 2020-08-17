package com.ahnz.headmaster.adapter;

import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.activity_main.fragment_home.NewBoutique;
import com.ahnz.headmaster.bean.activity_main.fragment_home.NewBoutiqueRecyclerViewItem;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author xzb
 * @description: 最新精品 列表适配器
 * @date :2020/7/8 16:25
 */
public class NewBoutiqueFragmentRecyclerViewAdapter extends BaseSectionQuickAdapter<NewBoutiqueRecyclerViewItem, BaseViewHolder> {

    private Fragment fragment;

    public NewBoutiqueFragmentRecyclerViewAdapter(Fragment fragment, int layoutResId, int sectionHeadResId, List<NewBoutiqueRecyclerViewItem> data) {
        super(sectionHeadResId, data);
        setNormalLayout(layoutResId);
        this.fragment = fragment;
    }

    @Override
    protected void convertHeader(BaseViewHolder baseViewHolder, NewBoutiqueRecyclerViewItem bean) {
        if (bean.getObject() instanceof String) {
            baseViewHolder.setText(R.id.head_tv, (String) bean.getObject());
        }
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NewBoutiqueRecyclerViewItem bean) {
        NewBoutique.DataBean.InfoBean.AvatarsBean listBean = (NewBoutique.DataBean.InfoBean.AvatarsBean) bean.getObject();
        Glide.with(fragment)
                .load(listBean.getImage())
                .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
    }
}
