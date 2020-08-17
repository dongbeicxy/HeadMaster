package com.ahnz.headmaster.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.activity_find_classification_details.FindClassDetails;
import com.ahnz.headmaster.bean.activity_head_details.Frame;
import com.ahnz.headmaster.bean.activity_head_details.FrameMappingAllData;
import com.ahnz.headmaster.widget.ScaleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author xzb
 * @description: 边框 贴图 列表 适配器
 * @date :2020/7/15 15:54
 */
public class FrameRecyclerViewAdapter extends BaseQuickAdapter<Frame.DataBean, BaseViewHolder> {
    private Fragment fragment;

    public FrameRecyclerViewAdapter(Fragment fragment, int layoutResId, List<Frame.DataBean> data) {
        super(layoutResId, data);
        this.fragment = fragment;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder baseViewHolder, Frame.DataBean dataBean) {
        //ScaleLayout scaleLayout = (ScaleLayout) baseViewHolder.getView(R.id.content_layout);
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.item_imageview);
        if (null == dataBean.getThumb()) {
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            //scaleLayout.setBackgroundColor(R.color.gray);
//            Glide.with(fragment)
//                    .load(dataBean.getResourceId())
//                    .transform(new RoundedCorners(12)) // 圆角图片
//                    .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
        } else {
            //scaleLayout.setBackgroundColor(R.color.white);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(fragment)
                    .load(dataBean.getThumb())
                    .apply(new RequestOptions()
                            .transform(new CenterCrop(), new RoundedCorners(16)
                            ))
                    .into(imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
