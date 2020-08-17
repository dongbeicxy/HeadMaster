package com.ahnz.headmaster.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.ahnz.headmaster.HeadMasterApplication;
import com.ahnz.headmaster.WelcomeActivity;
import com.ahnz.headmaster.widget.dialog.DislikeDialog;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

/**
 * @author xzb
 * @description: 穿山甲广告 工具类
 * @date :2020/7/22 9:56
 */
public class ADUtil {
    //https://www.cnblogs.com/ngy0217/p/9006716.html
    //静态内部类实现模式（线程安全，调用效率高，可以延时加载）
    private static class ADUtilInstance {
        private static final ADUtil instance = new ADUtil();
    }

    private ADUtil() {
    }

    public static ADUtil getInstance() {
        return ADUtilInstance.instance;
    }

    public interface ADViewController {
        public void setViewState(int viewState);

        public void addAdView(View adView);
    }

    private ADViewController adViewController;

    private TTAdNative mTTAdNative;
    //广告数据 集合
    private TTNativeExpressAd mTTAd;
    private boolean mHasShowDownloadActive = false;

    /*
     * 根据手机的分辨率px转换成dp
     * */
    private int pxTodp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取广告
     */
    public void getAd(Activity activity, String adID, ADViewController adViewController) {
        this.adViewController = adViewController;
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = HeadMasterApplication.getTTAdManager().createAdNative(activity);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        //TTAdManagerHolder.get().requestPermissionIfNecessary(context);

        WindowManager wm = activity.getWindowManager(); //this 指代当前的activity
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
        Log.d("广告 屏幕宽度px", widthPixels + "");

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(adID) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(520, 130) //期望模板广告view的size,单位dp
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                //Toast.makeText(context, "load error : " + code + ", " + message, Toast.LENGTH_SHORT).show();
                //ad_layout.setVisibility(View.GONE);
                adViewController.setViewState(View.GONE);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                //ad_layout.setVisibility(View.VISIBLE);
                adViewController.setViewState(View.VISIBLE);
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(2 * 1000);
                bindAdListener(mTTAd, activity);
                mTTAd.render();
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad, Activity activity) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.d("Banner广告", "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.d("Banner广告", "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.d("Banner广告", msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.d("Banner广告", "渲染成功");
                //ad_view.addView(view);
                adViewController.addAdView(view);
            }
        });
        //dislike设置
        bindDislike(ad, false, activity);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }

//        new QMUIDialog.MessageDialogBuilder(activity)
//                .setTitle("温馨提示")
//                .setMessage("点击该页面广告将要下载应用，是否继续操作？")
//                .addAction("取消", new QMUIDialogAction.ActionListener() {
//                    @Override
//                    public void onClick(QMUIDialog dialog, int index) {
//                        dialog.dismiss();
//                    }
//                })
//                .addAction("继续", new QMUIDialogAction.ActionListener() {
//                    @Override
//                    public void onClick(QMUIDialog dialog, int index) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();

        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                Log.d("Banner广告", "点击开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    Log.d("Banner广告", "下载中，点击暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                Log.d("Banner广告", "下载暂停，点击继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                Log.d("Banner广告", "下载失败，点击重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                Log.d("Banner广告", "安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Log.d("Banner广告", "点击安装");
            }
        });
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle, Activity activity) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(activity, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    Log.d("Banner广告", "点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    //ad_layout.setVisibility(View.GONE);
                    adViewController.setViewState(View.GONE);
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(activity, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Log.d("Banner广告", "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                //ad_layout.setVisibility(View.GONE);
                adViewController.setViewState(View.GONE);
            }

            @Override
            public void onCancel() {
                Log.d("Banner广告", "点击取消");
            }

            @Override
            public void onRefuse() {

            }
        });
    }
}
