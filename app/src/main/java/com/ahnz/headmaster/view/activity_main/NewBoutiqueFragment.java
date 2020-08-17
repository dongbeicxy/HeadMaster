package com.ahnz.headmaster.view.activity_main;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.adapter.NewBoutiqueFragmentRecyclerViewAdapter;
import com.ahnz.headmaster.bean.activity_main.fragment_home.NewBoutique;
import com.ahnz.headmaster.bean.activity_main.fragment_home.NewBoutiqueRecyclerViewItem;
import com.ahnz.headmaster.bean.activity_main.fragment_home.Popular;
import com.ahnz.headmaster.utils.URLS;
import com.ahnz.headmaster.view.activity_crop.CropActivity;
import com.ahnz.headmaster.view.activity_header_details.HeaderDetailsActivity;
import com.ahnz.headmaster.view.base.LazyFragment;
import com.ahnz.headmaster.widget.GridSectionAverageGapItemDecoration;
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
 * @description:最新精品
 * @date :2020/7/8 13:33
 */
public class NewBoutiqueFragment extends LazyFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    @Override
    protected int getContentViewId() {
        Log.e("Lazy", "最新精品加载布局");
        return R.layout.fragment_new_boutique;
    }

    @Override
    protected void initData() {
        Log.e("Lazy", "最新精品加载数据");
        loadData(true);
    }

    private void loadData(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        ///api/avater/v1/new?page=1&limit=20
        EasyHttp.get(URLS.NEW_BOUTIQUE + page + "&limit=10")
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
                        Log.e("最新精品", e.getMessage() + e.getCode());
                    }

                    @Override
                    public void onSuccess(String string) {
                        if (string != null && !string.equals("")) {
                            Log.e("最新精品", string);
                            //Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
                            pageStatusController.changePageStatus(RPageStatus.CONTENT);
                            bindData(string, isRefresh);
                        }
                    }
                });
    }

    private NewBoutiqueFragmentRecyclerViewAdapter adapter;
    //总数据源
    private List<NewBoutiqueRecyclerViewItem> allList = new ArrayList<>();
    //数据的页码
    private int page = 1;

    /**
     * 绑定数据
     */
    private void bindData(String data, boolean isRefresh) {
        NewBoutique newBoutique = new Gson().fromJson(data, NewBoutique.class);

        if (isRefresh) {
            smartRefreshLayout.finishRefresh(true);//传入true表示刷新成功
            allList.clear();
            for (int i = 0; i < newBoutique.getData().getInfo().size(); i++) {
                //添加 分组标题
                allList.add(new NewBoutiqueRecyclerViewItem(true, newBoutique.getData().getInfo().get(i).getDate()));
                for (int j = 0; j < newBoutique.getData().getInfo().get(i).getAvatars().size(); j++) {
                    allList.add(new NewBoutiqueRecyclerViewItem(false, newBoutique.getData().getInfo().get(i).getAvatars().get(j)));
                }
            }
        } else {
            smartRefreshLayout.finishLoadMore(true);//传入true表示加载成功
            for (int i = 0; i < newBoutique.getData().getInfo().size(); i++) {
                //添加 分组标题
                allList.add(new NewBoutiqueRecyclerViewItem(true, newBoutique.getData().getInfo().get(i).getDate()));
                for (int j = 0; j < newBoutique.getData().getInfo().get(i).getAvatars().size(); j++) {
                    allList.add(new NewBoutiqueRecyclerViewItem(false, newBoutique.getData().getInfo().get(i).getAvatars().get(j)));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {
        //定义布局管理器为Grid管理器，设置一行放3个
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        //设置布局管理器为网格布局管理器
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSectionAverageGapItemDecoration(1, 1, 8, 1));
        adapter = new NewBoutiqueFragmentRecyclerViewAdapter(this, R.layout.item_new_boutique_fragment_recyclerview, R.layout.item_new_boutique_recyclerview_head, allList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                NewBoutiqueRecyclerViewItem newBoutiqueRecyclerViewItem = allList.get(position);
                if (newBoutiqueRecyclerViewItem.isHeader()) {
                    //Tips.show((String) mySection.getObject());
                } else {
                    NewBoutique.DataBean.InfoBean.AvatarsBean avatarsBean = (NewBoutique.DataBean.InfoBean.AvatarsBean) newBoutiqueRecyclerViewItem.getObject();
                    //Toast.makeText(getActivity(), avatarsBean.getImage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), CropActivity.class);
                    intent.putExtra(HeaderDetailsActivity.PIC_PATH_NET, avatarsBean.getImage());
                    startActivity(intent);
                }
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
}
