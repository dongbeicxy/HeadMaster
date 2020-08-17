package com.ahnz.headmaster.view.base;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ahnz.headmaster.HeadMasterApplication;
import com.ahnz.headmaster.R;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.SimpleImmersionFragment;
import com.renj.pagestatuscontroller.IRPageStatusController;
import com.renj.pagestatuscontroller.RPageStatusController;
import com.renj.pagestatuscontroller.annotation.RPageStatus;
import com.renj.pagestatuscontroller.listener.OnRPageEventListener;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.BitmapTransformation;

/**
 * @作者 xzb
 * @描述: 懒加载 Fragment
 * @创建时间 :2020/3/27 9:52
 */
public abstract class LazyFragment extends SimpleImmersionFragment {
    private Unbinder unbinder;
    //private Context mContext;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mContext = getActivity();
//    }

    //图片编辑 接口
    public interface PicMakeListener {
        //设置 滤镜效果
        void setFilter(BitmapTransformation bitmapTransformation, int position, Uri picUri, String picPath);

        //设置边框
        void setFrame(String material);

        //设置贴图
        void setMapping(String thumb);
    }

    protected PicMakeListener picMakeListener;

    protected View rootView;
    private boolean isInitView = false;
    private boolean isVisible = false;
    //https://github.com/itrenjunhua/RPageStatusController
    protected RPageStatusController pageStatusController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImmersionBar.with(this).statusBarDarkFont(true);
        rootView = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        pageStatusController = RPageStatusController.get();
        //未 联网时 错误页面 点击回调
        pageStatusController.registerOnRPageEventListener(RPageStatus.ERROR, R.id.ll_error, new OnRPageEventListener() {
            @Override
            public void onViewClick(final @NonNull IRPageStatusController iRPageStatusController, @RPageStatus int pageStatus, @NonNull Object object, @NonNull View view, int viewId) {
                initData();
            }
        });
        //请求超时  时 页面 点击回调
        pageStatusController.registerOnRPageEventListener(RPageStatus.NET_WORK, R.id.ll_timeout, new OnRPageEventListener() {
            @Override
            public void onViewClick(final @NonNull IRPageStatusController iRPageStatusController, @RPageStatus int pageStatus, @NonNull Object object, @NonNull View view, int viewId) {
                initData();
            }
        });
        View fragmentView = pageStatusController.bind(this, rootView);
        pageStatusController.changePageStatus(RPageStatus.LOADING);
        isInitView = true;
        isCanLoadData();
        return fragmentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见，获取该标志记录下来
        if (isVisibleToUser) {
            isVisible = true;
            isCanLoadData();
        } else {
            isVisible = false;
        }
    }

    private void isCanLoadData() {
        //所以条件是view初始化完成并且对用户可见
        if (isInitView && isVisible) {
            initEvent();
            initData();
            //防止重复加载数据
            isInitView = false;
            isVisible = false;
        }
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        Log.e("Lazy", "fragment销毁视图");
        super.onDestroyView();
      /*  //内存泄漏
        RefWatcher refWatcher = HeadMasterApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);*/
    }


    /**
     * 加载页面布局文件
     *
     * @return
     */
    protected abstract int getContentViewId();

    /**
     * 加载要显示的数据
     */
    protected abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();
}
