package com.ahnz.headmaster.view.activity_main;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.adapter.FindFragmentRecyclerViewAdapter;
import com.ahnz.headmaster.bean.activity_main.fragment_find.FindClassify;
import com.ahnz.headmaster.utils.URLS;
import com.ahnz.headmaster.view.activity_find_classification_details.FindClassificationDetailsActivity;
import com.ahnz.headmaster.view.base.LazyFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.renj.pagestatuscontroller.annotation.RPageStatus;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;

/**
 * @author xzb
 * @description:发现
 * @date :2020/7/8 9:18
 */
public class FindFragment extends LazyFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getContentViewId() {
        Log.e("Lazy", "发现加载布局");
        return R.layout.fragment_find;
    }

    @Override
    protected void initData() {
        Log.e("Lazy", "发现加载数据");
        EasyHttp.get(URLS.FIND_CLASSIFY)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        //1009   网络未连接
                        if (1009 == e.getCode()) {
                            pageStatusController.changePageStatus(RPageStatus.ERROR);
                        }
                        //1005   连接超时
                        if (1005 == e.getCode()) {
                            pageStatusController.changePageStatus(RPageStatus.NET_WORK);
                        }
                        Log.e("发现", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String string) {
                        if (string != null && !string.equals("")) {
                            //Log.e("发现", string);
                            //Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
                            pageStatusController.changePageStatus(RPageStatus.CONTENT);
                            bindData(string);
                        }
                    }
                });
    }

    /**
     * 绑定数据
     */
    private void bindData(String data) {
        //定义布局管理器为Grid管理器，设置一行放2个
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        //设置布局管理器为网格布局管理器
        recyclerView.setLayoutManager(layoutManager);
        FindClassify findClassify = new Gson().fromJson(data, FindClassify.class);
        //实例化Adapter
        FindFragmentRecyclerViewAdapter findFragmentRecyclerViewAdapter = new FindFragmentRecyclerViewAdapter(this, R.layout.item_find_fragment_recyclerview, findClassify.getData());
        //设置适配器
        recyclerView.setAdapter(findFragmentRecyclerViewAdapter);

        //设置Item点击事件
        findFragmentRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), FindClassificationDetailsActivity.class);
                intent.putExtra(FindClassificationDetailsActivity.DATA_KEY, findClassify.getData().get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).fitsSystemWindows(true).init();
    }
}
