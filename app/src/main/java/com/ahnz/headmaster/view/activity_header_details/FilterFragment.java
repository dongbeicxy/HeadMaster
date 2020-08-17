package com.ahnz.headmaster.view.activity_header_details;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.view.base.LazyFragment;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.renj.pagestatuscontroller.annotation.RPageStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSoftLightBlendFilter;
import jp.wasabeef.glide.transformations.BitmapTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.GPUFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

/**
 * @author xzb
 * @description: 滤镜
 * @date :2020/7/10 14:51
 */
public class FilterFragment extends LazyFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String picPath;
    private Uri picUri;
    private Bitmap bitmap;

    //Fragment中的onAttach方法
    @Override
    public void onAttach(Context context) {
        picMakeListener = (PicMakeListener) context;
        super.onAttach(context);
        HeaderDetailsActivity headerDetailsActivity = (HeaderDetailsActivity) context;
        bitmap = headerDetailsActivity.getBitmap();
        if (null != headerDetailsActivity.getIntent().getStringExtra(HeaderDetailsActivity.PIC_PATH_NET)) {
            picPath = headerDetailsActivity.getIntent().getStringExtra(HeaderDetailsActivity.PIC_PATH_NET);
        }
        if (null != headerDetailsActivity.getIntent().getStringExtra(HeaderDetailsActivity.PIC_PATH_LOCAL)) {
            picUri = (Uri.parse(headerDetailsActivity.getIntent().getStringExtra(HeaderDetailsActivity.PIC_PATH_LOCAL)));
        }

        if (null != headerDetailsActivity.getIntent().getParcelableExtra(HeaderDetailsActivity.PIC_PATH_URI)) {
            picUri = (Uri) headerDetailsActivity.getIntent().getParcelableExtra(HeaderDetailsActivity.PIC_PATH_URI);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_filter;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        pageStatusController.changePageStatus(RPageStatus.CONTENT);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new Decoration());

        List<BitmapTransformation> list = new ArrayList<>();
        list.add(0, null);
        list.add(new GrayscaleTransformation());//黑白
        list.add(new GPUFilterTransformation(new GPUImageSoftLightBlendFilter()));//暖色
        list.add(new ColorFilterTransformation(0x7900CCCC));//颜色过滤
        //list.add(new BlurTransformation(8, 2));//模糊
        //list.add(new MaskTransformation(R.mipmap.welcome));//图片遮罩
        list.add(new ToonFilterTransformation(0.2f, 10f));//卡通
        list.add(new SepiaFilterTransformation(1f));//乌墨色
        list.add(new ContrastFilterTransformation(3f));//对比度
        list.add(new InvertFilterTransformation());//反转
        //list.add(new PixelationFilterTransformation(20f));//像素化
        list.add(new SketchFilterTransformation());//素描
        //list.add(new SwirlFilterTransformation(1.0F, 0.4F, new PointF(0.5F, 0.5F)));//旋转
        list.add(new BrightnessFilterTransformation(0.5f));//亮度
        list.add(new KuwaharaFilterTransformation(10));//Kuwahara
        list.add(new VignetteFilterTransformation(new PointF(0.5F, 0.5F), new float[]{0.0f, 0.0f, 0.0f}, 0.0F, 0.5F));//装饰图

        FilterFragmentRecyclerViewAdapter adapter = new FilterFragmentRecyclerViewAdapter(this, R.layout.item_filter_fragment_recyclerview, list);
        //设置适配器
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                picMakeListener.setFilter(list.get(position), position, picUri, picPath);
            }
        });
    }


    class FilterFragmentRecyclerViewAdapter extends BaseQuickAdapter<BitmapTransformation, BaseViewHolder> {
        private Fragment fragment;

        public FilterFragmentRecyclerViewAdapter(Fragment fragment, int layoutResId, List<BitmapTransformation> data) {
            super(layoutResId, data);
            this.fragment = fragment;
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, BitmapTransformation dataBean) {
            ImageView imageView = (ImageView) baseViewHolder.getView(R.id.item_imageview);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //正常
//            if (0 == baseViewHolder.getAdapterPosition()) {
//                Glide.with(fragment)
//                        .load(null == picPath ? picUri : picPath)
//                        .into(imageView);
//            } else {
//                if (null != picPath) {
//                    Glide.with(fragment)
//                            .load(picPath)
//                            .transform(dataBean)
//                            .into(imageView);
//                }
//                if (null != picUri) {
//                    Glide.with(fragment)
//                            .load(picUri)
//                            .transform(dataBean)
//                            .into(imageView);
//                }
//            }
            //bitmap
            //正常
            if (0 == baseViewHolder.getAdapterPosition()) {
                Glide.with(fragment)
                        .load(bitmap)
                        .into(imageView);
            } else {
                Glide.with(fragment)
                        .load(bitmap)
                        .transform(dataBean)
                        .into(imageView);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    public static class Decoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            //outRect.set()中的参数分别对应左、上、右、下的间隔
            outRect.set(0, 0, 16, 0);
        }
    }

    @Override
    public void initImmersionBar() {

    }
}
