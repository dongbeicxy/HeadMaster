package com.ahnz.headmaster.view.activity_header_details;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.adapter.FrameRecyclerViewAdapter;
import com.ahnz.headmaster.bean.activity_head_details.Frame;
import com.ahnz.headmaster.bean.activity_head_details.FrameMappingAllData;
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
 * @description: 贴图
 * @date :2020/7/10 14:51
 */
public class MappingFragment extends LazyFragment {
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
        EasyHttp.get(URLS.FRAME + "2")
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
            FrameRecyclerViewAdapter adapter = new FrameRecyclerViewAdapter(this, R.layout.item_filter_fragment_recyclerview, list);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    picMakeListener.setMapping(list.get(position).getThumb());
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
