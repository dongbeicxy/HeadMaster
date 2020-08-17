package com.ahnz.headmaster.view.activity_main;

import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.adapter.PopularFragmentRecyclerViewAdapter;
import com.ahnz.headmaster.bean.activity_find_classification_details.FindClassDetails;
import com.ahnz.headmaster.bean.activity_main.fragment_home.Popular;
import com.ahnz.headmaster.utils.URLS;
import com.ahnz.headmaster.view.activity_crop.CropActivity;
import com.ahnz.headmaster.view.activity_header_details.HeaderDetailsActivity;
import com.ahnz.headmaster.view.base.LazyFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.renj.pagestatuscontroller.annotation.RPageStatus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xzb
 * @description:火爆人气
 * @date :2020/7/8 13:34
 */
public class PopularFragment extends LazyFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    @Override
    protected int getContentViewId() {
        Log.e("Lazy", "火爆人气加载布局");
        return R.layout.fragment_popular;
    }

    @Override
    protected void initData() {
        Log.e("Lazy", "火爆人气加载数据");
        loadData(true);
    }

    private void loadData(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        ///api/avater/v1/hot?page=1&limit=20&type=1
        EasyHttp.get(URLS.POPULAR + page + "&limit=30&type=1")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        //1009   网络未连接
                        if (0 == allList.size() && 1009 == e.getCode()) {
                            pageStatusController.changePageStatus(RPageStatus.ERROR);
                        }
                        //1005   连接超时
                        if (0 == allList.size() && 1005 == e.getCode()) {
                            pageStatusController.changePageStatus(RPageStatus.NET_WORK);
                        }
                        if (1009 == e.getCode() || 1005 == e.getCode()) {
                            smartRefreshLayout.finishRefresh(false);//传入true表示刷新成功
                            smartRefreshLayout.finishLoadMore(false);//传入true表示加载成功
                        }
                        Log.e("火爆人气", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String string) {
                        if (string != null && !string.equals("")) {
                            //Log.e("发现", string);
                            pageStatusController.changePageStatus(RPageStatus.CONTENT);
                            bindData(string, isRefresh);
                        }
                    }
                });
    }

    //实例化Adapter
    private PopularFragmentRecyclerViewAdapter adapter;
    //总数据源
    private List<Popular.DataBean> allList = new ArrayList<>();

    //数据的页码
    private int page = 1;

    private void bindData(String data, boolean isRefresh) {
        Popular popular = new Gson().fromJson(data, Popular.class);
        if (isRefresh) {
            smartRefreshLayout.finishRefresh(true);//传入true表示刷新成功
            allList.clear();
            allList.addAll(popular.getData());
        } else {
            smartRefreshLayout.finishLoadMore(true);//传入true表示加载成功
            allList.addAll(popular.getData());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {
        //定义布局管理器为Grid管理器，设置一行放3个
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        //设置布局管理器为网格布局管理器
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new Decoration());
        adapter = new PopularFragmentRecyclerViewAdapter(this, R.layout.item_popular_fragment_recyclerview, allList);
        //设置适配器
        recyclerView.setAdapter(adapter);

        //设置Item点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Popular.DataBean dataBean = allList.get(position);
                Intent intent = new Intent(getActivity(), CropActivity.class);
                intent.putExtra(HeaderDetailsActivity.PIC_PATH_NET, dataBean.getImage());
                startActivity(intent);
            }
        });

        //是否在刷新的时候禁止内容的一切手势操作（默认false）
        smartRefreshLayout.setDisableContentWhenRefresh(true);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData(true);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadData(false);
            }
        });
    }


    @Override
    public void initImmersionBar() {

    }

    class Decoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            //outRect.set()中的参数分别对应左、上、右、下的间隔
            outRect.set(4, 4, 4, 4);
        }
    }
}
