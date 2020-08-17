package com.ahnz.headmaster.view.activity_main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahnz.headmaster.HeadMasterApplication;
import com.ahnz.headmaster.MainActivity;
import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.CameraData;
import com.ahnz.headmaster.utils.ADUtil;
import com.ahnz.headmaster.utils.CleanDataUtils;
import com.ahnz.headmaster.utils.FileUtils;
import com.ahnz.headmaster.utils.UIUtils;
import com.ahnz.headmaster.view.activity_collection.CollectionActivity;
import com.ahnz.headmaster.view.activity_webview.WebViewActivity;
import com.ahnz.headmaster.view.base.LazyFragment;
import com.ahnz.headmaster.widget.ScaleLayout;
import com.ahnz.headmaster.widget.dialog.DisclaimerDialog;
import com.ahnz.headmaster.widget.dialog.DislikeDialog;
import com.ahnz.headmaster.widget.dialog.FeedBackDialog;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.gyf.immersionbar.ImmersionBar;
import com.orhanobut.hawk.Hawk;
import com.renj.pagestatuscontroller.annotation.RPageStatus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xzb
 * @description: 我的
 * @date :2020/7/8 9:20
 */
public class MyFragment extends LazyFragment {

    @BindView(R.id.version_name_tv)
    TextView version_name_tv;

    //缓存大小 文本
    @BindView(R.id.cache_size_tv)
    TextView cache_size_tv;

    //广告布局
    @BindView(R.id.ad_layout)
    ScaleLayout ad_layout;

    //广告视图
    @BindView(R.id.ad_view)
    RelativeLayout ad_view;

    @Override
    protected int getContentViewId() {
        Log.e("Lazy", "我的加载布局");
        return R.layout.fragment_my;
    }

    //好评 去广告 存储 标识
    private String cleanADFlag = "CleanADFlag";

    @Override
    protected void initData() {
        Log.e("Lazy", "我的加载数据");
        boolean isCleanAD = Hawk.get(cleanADFlag, false);
        if (!isCleanAD)
            ADUtil.getInstance().getAd(getActivity(), "945325317", new ADUtil.ADViewController() {
                @Override
                public void setViewState(int viewState) {
                    ad_layout.setVisibility(viewState);
                }

                @Override
                public void addAdView(View adView) {
                    if (null != ad_view && null != adView) {
                        ad_view.addView(adView);
                    }
                }
            });
    }


    @Override
    protected void initEvent() {
        pageStatusController.changePageStatus(RPageStatus.CONTENT);
        version_name_tv.setText("v" + getAppVersionName(getActivity()));
        try {
            cache_size_tv.setText(CleanDataUtils.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.statement, R.id.cache_layout, R.id.comment, R.id.collection, R.id.clean_ad, R.id.policy, R.id.use_agreement, R.id.feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //免责声明
            case R.id.statement:
                new DisclaimerDialog(getActivity()).show();
                break;

            //点击清理缓存
            case R.id.cache_layout:
                try {
                    CleanDataUtils.clearAllCache(getContext());
                    String size = CleanDataUtils.getTotalCacheSize(getContext());
                    cache_size_tv.setText(size);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            //好评
            case R.id.comment:
                goAppShop(getActivity(), getActivity().getPackageName(), "");
                break;

            //我的收藏
            case R.id.collection:
                startActivity(new Intent(getActivity(), CollectionActivity.class));
                break;
            //一键去广告
            case R.id.clean_ad:
                ad_layout.setVisibility(View.GONE);
                break;

            //隐私政策
            case R.id.policy:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.TITLE, "隐私政策");
                intent.putExtra(WebViewActivity.URL, "http://hd.qianr.com/xieyi/yszc.html");
                startActivity(intent);
                break;

            //使用协议
            case R.id.use_agreement:
                Intent use_agreement = new Intent(getActivity(), WebViewActivity.class);
                use_agreement.putExtra(WebViewActivity.TITLE, "使用协议");
                use_agreement.putExtra(WebViewActivity.URL, "http://hd.qianr.com/xieyi/syxy.html");
                startActivity(use_agreement);
                break;

            //意见反馈
            case R.id.feedback:
                new FeedBackDialog(getActivity()).show();
                break;
        }
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 跳转到应用商店评分
     *
     * @param context
     * @param myAppPkg
     * @param shopPkg
     */
    private void goAppShop(Context context, String myAppPkg, String shopPkg) {
        if (TextUtils.isEmpty(myAppPkg)) {
            return;
        }
        try {
            Uri uri = Uri.parse("market://details?id=" + myAppPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(shopPkg)) {
                intent.setPackage(shopPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // 如果没有该应用商店，则显示系统弹出的应用商店列表供用户选择
            goAppShop(context, myAppPkg, "");
        }
        Hawk.put(cleanADFlag, true);
    }

    //重新打开页面 和 页面 刚初始化时 执行
    @Override
    public void onResume() {
        super.onResume();
        Log.e("Fra", "onResume");
        boolean isCleanAD = Hawk.get(cleanADFlag, false);
        if (isCleanAD) {
            ad_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).fitsSystemWindows(true).init();
    }
}
