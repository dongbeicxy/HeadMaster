package com.ahnz.headmaster.view.activity_search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.adapter.HotSearchAdapterNew;
import com.ahnz.headmaster.adapter.SearchHistoryRecyclerViewAdapter;
import com.ahnz.headmaster.adapter.SearchResultRecyclerViewAdapter;
import com.ahnz.headmaster.bean.activity_search.HotSearch;
import com.ahnz.headmaster.bean.activity_search.SearchResult;
import com.ahnz.headmaster.utils.SearchHistoryUtils;
import com.ahnz.headmaster.utils.URLS;
import com.ahnz.headmaster.view.activity_crop.CropActivity;
import com.ahnz.headmaster.view.activity_find_classification_details.FindClassificationDetailsActivity;
import com.ahnz.headmaster.view.activity_header_details.HeaderDetailsActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
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
import io.reactivex.disposables.Disposable;

/**
 * @author xzb
 * @description: 搜索
 * @date :2020/7/9 15:42
 */
public class SearchActivity extends Activity {

    //大家都在搜 列表
    @BindView(R.id.hot_search_recyclerview)
    RecyclerView hot_search_recyclerview;

    private HotSearchAdapterNew hotSearchAdapterNew;

    //搜索历史 列表
    @BindView(R.id.search_his_rv)
    RecyclerView search_his_rv;

    //搜索框
    @BindView(R.id.edit_text)
    EditText edit_text;

    //删除文本 图标
    @BindView(R.id.delete_icon)
    ImageView delete_icon;

    //清空文本
    @BindView(R.id.clean_tv)
    TextView clean_tv;

    @BindView(R.id.hot_his_layout)
    ScrollView hot_his_layout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.no_data_hint_layout)
    LinearLayout no_data_hint_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).fitsSystemWindows(true).init();
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initData();
    }

    private Disposable hotSearchdisposable;

    private void initData() {
        initSearchHistory();

        //定义布局管理器为Grid管理器，设置一行放3个
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        //设置布局管理器为网格布局管理器
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new FindClassificationDetailsActivity.Decoration());
        searchResultRecyclerViewAdapter = new SearchResultRecyclerViewAdapter(this, R.layout.item_new_boutique_fragment_recyclerview, allList);
        //设置适配器
        recyclerView.setAdapter(searchResultRecyclerViewAdapter);
        //设置Item点击事件
        searchResultRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchActivity.this, CropActivity.class);
                intent.putExtra(HeaderDetailsActivity.PIC_PATH_NET, allList.get(position).getImage());
                startActivity(intent);
            }
        });

        //是否在刷新的时候禁止内容的一切手势操作（默认false）
        smartRefreshLayout.setDisableContentWhenRefresh(true);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getSearchResult(true, inputContent);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getSearchResult(false, inputContent);
            }
        });

        hotSearchdisposable = EasyHttp.get(URLS.HOT_SEARCH)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("热门搜索", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String string) {
                        if (string != null && !string.equals("")) {
                            bindData(string);
                        }
                    }
                });

        edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“SEARCH”键
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if ("".equals(v.getText().toString().trim())) {
                        Toast.makeText(SearchActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
                    //如果开启
                    if (imm.isActive()) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                    }
                    SearchHistoryUtils.saveSearchHistory(v.getText().toString().trim());
                    inputContent = v.getText().toString().trim();
                    getSearchResult(true, inputContent);
                    return true;
                }
                return false;
            }
        });

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String content = charSequence.toString().trim();
                if (content.equals("")) {
                    delete_icon.setVisibility(View.GONE);
                } else {
                    delete_icon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    //输入的内容
    private String inputContent;


    /**
     * 初始化 搜索历史
     */
    private void initSearchHistory() {
        List<String> list = SearchHistoryUtils.getSearchHistory();
        if (0 == list.size()) {
            clean_tv.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        search_his_rv.setLayoutManager(layoutManager);
        //添加Android自带的分割线
        search_his_rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        SearchHistoryRecyclerViewAdapter searchHistoryRecyclerViewAdapter = new SearchHistoryRecyclerViewAdapter(R.layout.item_search_history_recyclerview, list);
        search_his_rv.setAdapter(searchHistoryRecyclerViewAdapter);

        //设置Item点击事件
        searchHistoryRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                edit_text.setText(list.get(position));
                SearchHistoryUtils.saveSearchHistory(list.get(position));
                inputContent = list.get(position);
                getSearchResult(true, inputContent);
            }
        });
    }

    /**
     * 绑定 大家都在搜  数据
     */
    private void bindData(String data) {
        HotSearch hotSearch = new Gson().fromJson(data, HotSearch.class);
        hotSearchAdapterNew = new HotSearchAdapterNew(R.layout.item_hot_search, hotSearch.getData());
        hot_search_recyclerview.setAdapter(hotSearchAdapterNew);
        hot_search_recyclerview.setLayoutManager(new FlexboxLayoutManager(this));

        hotSearchAdapterNew.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                edit_text.setText(hotSearch.getData().get(position).getName());
                SearchHistoryUtils.saveSearchHistory(hotSearch.getData().get(position).getName());
                inputContent = hotSearch.getData().get(position).getName();
                getSearchResult(true, inputContent);
            }
        });
    }

    /**
     * 点击 取消
     */
    public void clickCancle(View view) {
        finish();
    }

    /**
     * 点击 删除 搜索内容 Icon
     */
    public void clickDelete(View view) {
        edit_text.setText("");
        hot_his_layout.setVisibility(View.VISIBLE);
        smartRefreshLayout.setVisibility(View.GONE);
        no_data_hint_layout.setVisibility(View.GONE);
        allList.clear();
        searchResultRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * 点击 清空
     */
    public void clickCleanHistory(View view) {
        SearchHistoryUtils.cleanSearchHistory();
        SearchHistoryRecyclerViewAdapter searchHistoryRecyclerViewAdapter = new SearchHistoryRecyclerViewAdapter(R.layout.item_search_history_recyclerview, SearchHistoryUtils.getSearchHistory());
        search_his_rv.setAdapter(searchHistoryRecyclerViewAdapter);
        //清空文本  隐藏
        view.setVisibility(View.GONE);
    }

    //数据的页码
    private int page = 1;

    private Disposable searchResultdisposable;

    /**
     * 获取 搜索结果
     */
    private void getSearchResult(boolean isRefresh, String inputContent) {
        //    /api/avater/v1/result?name=青春&page=2&limit=10
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        searchResultdisposable = EasyHttp.get(URLS.GET_SEARCH_RESULT + inputContent + "&page=" + page + "&limit=30")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("热门搜索", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String string) {
                        if (string != null && !string.equals("")) {
                            Log.e("热门搜索", string);
                            bindSearchResult(string, isRefresh);
                        }
                    }
                });
    }

    //搜索结果 适配器
    private SearchResultRecyclerViewAdapter searchResultRecyclerViewAdapter;

    //搜索结果 总数据源
    private List<SearchResult.DataBean.InfoBean> allList = new ArrayList<>();

    /**
     * 绑定 搜索结果
     */
    private void bindSearchResult(String string, boolean isRefresh) {
        SearchResult searchResult = new Gson().fromJson(string, SearchResult.class);
        if (200 == searchResult.getStatus()) {
            if (0 == searchResult.getData().getTotal()) {
                hot_his_layout.setVisibility(View.GONE);
                no_data_hint_layout.setVisibility(View.VISIBLE);
                return;
            }
            hot_his_layout.setVisibility(View.GONE);
            no_data_hint_layout.setVisibility(View.GONE);
            smartRefreshLayout.setVisibility(View.VISIBLE);
            if (isRefresh) {
                smartRefreshLayout.finishRefresh(true);//传入true表示刷新成功
                allList.clear();
                allList.addAll(searchResult.getData().getInfo());
            } else {
                smartRefreshLayout.finishLoadMore(true);//传入true表示加载成功
                allList.addAll(searchResult.getData().getInfo());
            }
            searchResultRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != hotSearchdisposable) {
            EasyHttp.cancelSubscription(hotSearchdisposable);
        }
        if (null != searchResultdisposable) {
            EasyHttp.cancelSubscription(searchResultdisposable);
        }
    }
}
