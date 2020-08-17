package com.ahnz.headmaster.view.activity_find_classification_details;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.adapter.FindClassDetailsRecyclerViewAdapter;
import com.ahnz.headmaster.bean.activity_find_classification_details.FindClassDetails;
import com.ahnz.headmaster.bean.activity_main.fragment_find.FindClassify;
import com.ahnz.headmaster.utils.URLS;
import com.ahnz.headmaster.view.activity_crop.CropActivity;
import com.ahnz.headmaster.view.activity_header_details.HeaderDetailsActivity;
import com.ahnz.headmaster.view.activity_main.PopularFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.renj.pagestatuscontroller.IRPageStatusController;
import com.renj.pagestatuscontroller.RPageStatusController;
import com.renj.pagestatuscontroller.annotation.RPageStatus;
import com.renj.pagestatuscontroller.listener.OnRPageEventListener;
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
import butterknife.ButterKnife;

/**
 * @author xzb
 * @description: 发现分类详情 页面
 * @date :2020/7/10 17:24
 */
public class FindClassificationDetailsActivity extends AppCompatActivity {

    public static final String DATA_KEY = "data_key";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.details_rv)
    RecyclerView details_rv;

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private RPageStatusController pageStatusController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).fitsSystemWindows(true).init();
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        setContentView(R.layout.activity_find_classification_details);
        ButterKnife.bind(this);
        initViews();
        initData(true);
    }


    private void initViews() {
        toolbar.setTitle("");
        //设置导航图标要在setSupportActionBar方法之后
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_dark_24dp);

        pageStatusController = RPageStatusController.get();
        pageStatusController.bind(this);
        pageStatusController.changePageStatus(RPageStatus.LOADING);

        //未 联网时 错误页面 点击回调
        pageStatusController.registerOnRPageEventListener(RPageStatus.ERROR, R.id.ll_error, new OnRPageEventListener() {
            @Override
            public void onViewClick(final @NonNull IRPageStatusController iRPageStatusController, @RPageStatus int pageStatus, @NonNull Object object, @NonNull View view, int viewId) {
                initData(true);
            }
        });
        //请求超时  时 页面 点击回调
        pageStatusController.registerOnRPageEventListener(RPageStatus.NET_WORK, R.id.ll_timeout, new OnRPageEventListener() {
            @Override
            public void onViewClick(final @NonNull IRPageStatusController iRPageStatusController, @RPageStatus int pageStatus, @NonNull Object object, @NonNull View view, int viewId) {
                initData(true);
            }
        });


        //定义布局管理器为Grid管理器，设置一行放3个
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        //设置布局管理器为网格布局管理器
        details_rv.setLayoutManager(layoutManager);
        details_rv.addItemDecoration(new Decoration());
        findClassDetailsRecyclerViewAdapter = new FindClassDetailsRecyclerViewAdapter(this, R.layout.item_new_boutique_fragment_recyclerview, allList);
        //设置适配器
        details_rv.setAdapter(findClassDetailsRecyclerViewAdapter);
        //设置Item点击事件
        findClassDetailsRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(FindClassificationDetailsActivity.this, CropActivity.class);
                intent.putExtra(HeaderDetailsActivity.PIC_PATH_NET, allList.get(position).getImage());
                startActivity(intent);
            }
        });

        //是否在刷新的时候禁止内容的一切手势操作（默认false）
        smartRefreshLayout.setDisableContentWhenRefresh(true);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(true);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                initData(false);
            }
        });
    }

    //数据的页码
    private int page = 1;

    private FindClassDetailsRecyclerViewAdapter findClassDetailsRecyclerViewAdapter;

    //总数据源
    private List<FindClassDetails.DataBean> allList = new ArrayList<>();

    private void initData(boolean isRefresh) {
        FindClassify.DataBean dataBean = (FindClassify.DataBean) getIntent().getSerializableExtra(DATA_KEY);
        //设置标题
        title_tv.setText(dataBean.getName());
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        EasyHttp.get(URLS.FIND_CLASS_DETAILS + dataBean.getId() + "&page=" + page + "&limit=30")
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
                        Log.e("发现分类详情", e.getMessage());
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

    /**
     * 绑定数据
     *
     * @param data
     * @param isRefresh
     */
    private void bindData(String data, boolean isRefresh) {
        FindClassDetails findClassDetails = new Gson().fromJson(data, FindClassDetails.class);
        if (isRefresh) {
            smartRefreshLayout.finishRefresh(true);//传入true表示刷新成功
            allList.clear();
            allList.addAll(findClassDetails.getData());
        } else {
            smartRefreshLayout.finishLoadMore(true);//传入true表示加载成功
            allList.addAll(findClassDetails.getData());
        }
        findClassDetailsRecyclerViewAdapter.notifyDataSetChanged();
    }


    public static class Decoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            //outRect.set()中的参数分别对应左、上、右、下的间隔
            outRect.set(4, 4, 4, 4);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
