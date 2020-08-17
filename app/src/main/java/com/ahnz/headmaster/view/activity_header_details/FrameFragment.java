package com.ahnz.headmaster.view.activity_header_details;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.adapter.FrameRecyclerViewAdapter;
import com.ahnz.headmaster.bean.activity_head_details.Frame;
import com.ahnz.headmaster.bean.activity_head_details.FrameMappingAllData;
import com.ahnz.headmaster.bean.activity_main.fragment_find.FindClassify;
import com.ahnz.headmaster.utils.URLS;
import com.ahnz.headmaster.view.base.LazyFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.renj.pagestatuscontroller.annotation.RPageStatus;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import butterknife.BindView;

/**
 * @author xzb
 * @description: 边框
 * @date :2020/7/10 14:51
 */
public class FrameFragment extends LazyFragment {

    @Override
    public void onAttach(@NonNull Context context) {
        picMakeListener = (PicMakeListener) context;
        super.onAttach(context);
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_filter;
    }

    @Override
    protected void initData() {
        EasyHttp.get(URLS.FRAME + "1")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("发现", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String string) {
                        if (string != null && !string.equals("")) {
                            //Log.e("边框", string);
                            //Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
                            pageStatusController.changePageStatus(RPageStatus.CONTENT);
                            bindData(string);
                        }
                    }
                });
    }

    private void bindData(String data) {
        Frame frame = new Gson().fromJson(data, Frame.class);
        if (200 == frame.getStatus()) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new FilterFragment.Decoration());

            Frame.DataBean dataBean = new Frame.DataBean();
            dataBean.setResourceId(R.mipmap.pic_init);
            List<Frame.DataBean> list = frame.getData();
            list.add(0, dataBean);
            //https://blog.csdn.net/tanpeiqi/article/details/88556225?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-4.edu_weight&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-4.edu_weight
            /**
             * 二.设置FrameLayout中的某个View透明
             * 由于FrameLayout层层叠加的特性，使得下层View被上层View遮蔽，有时为了让下层View可见，就不得不让上层View透明：
             *
             * View.getBackground().setAlpha(100);
             */
            FrameRecyclerViewAdapter adapter = new FrameRecyclerViewAdapter(this, R.layout.item_filter_fragment_recyclerview, list);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    picMakeListener.setFrame(list.get(position).getMaterial());
                }
            });
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void initImmersionBar() {

    }
}
