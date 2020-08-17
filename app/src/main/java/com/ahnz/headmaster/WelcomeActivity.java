package com.ahnz.headmaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.fragment.app.FragmentActivity;

import com.ahnz.headmaster.utils.TTAdManagerHolder;
import com.ahnz.headmaster.utils.UIUtils;
import com.ahnz.headmaster.view.activity_header_details.HeaderDetailsActivity;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author xzb
 * @description: 启动欢迎页
 * @date :2020/7/9 15:07                  历时14个工作日开发完毕  14个模块
 */
public class WelcomeActivity extends FragmentActivity {
    //根布局
    @BindView(R.id.content_frame)
    RelativeLayout content_frame;

    private TTAdNative mTTAdNative;

    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 3100;

    //开屏广告ID
    private static final String AD_ID = "887351996";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideBottomUIMenu();
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        //step2:创建TTAdNative对象
        //mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        mTTAdNative = HeadMasterApplication.getTTAdManager().createAdNative(this);
        loadSplashAd();
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 2000);
    }

    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = null;
        //个性化模板广告需要传入期望广告view的宽、高，单位dp，请传入实际需要的大小，
        //比如：广告下方拼接logo、适配刘海屏等，需要考虑实际广告大小
        float expressViewWidth = UIUtils.getScreenWidthDp(this);
        float expressViewHeight = UIUtils.getHeight(this);
        adSlot = new AdSlot.Builder()
                .setCodeId(AD_ID)
                .setSupportDeepLink(true)
                // b=(int)(Math.round(a));
                .setImageAcceptedSize(1080, 1920)
                //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
                //.setExpressViewAcceptedSize(expressViewWidth, expressViewHeight)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @MainThread
            @Override
            public void onError(int code, String message) {
                Log.d("广告", String.valueOf(message));
                goToMainActivity();
            }

            @MainThread
            @Override
            public void onTimeout() {
                Log.d("广告", String.valueOf("开屏广告加载超时"));
                goToMainActivity();
            }

            @MainThread
            @Override
            public void onSplashAdLoad(TTSplashAd ttSplashAd) {
                Log.d("广告", "开屏广告请求成功");
                if (ttSplashAd == null) {
                    return;
                }
                //获取SplashView
                View view = ttSplashAd.getSplashView();
                if (view != null && content_frame != null && !WelcomeActivity.this.isFinishing()) {
                    content_frame.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    content_frame.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ttSplashAd.setNotAllowSdkCountdown();
                } else {
                    goToMainActivity();
                }
                //设置SplashView的交互监听器
                ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @MainThread
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d("广告", "开屏广告点击" + type);
                    }

                    @MainThread
                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d("广告", "开屏广告展示");
                    }

                    @MainThread
                    @Override
                    public void onAdSkip() {
                        Log.d("广告", "开屏广告跳过");
                        goToMainActivity();
                    }

                    @MainThread
                    @Override
                    public void onAdTimeOver() {
                        Log.d("广告", "开屏广告倒计时结束");
                        goToMainActivity();
                    }
                });
                if (ttSplashAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
//                    new QMUIDialog.MessageDialogBuilder(WelcomeActivity.this)
//                            .setTitle("温馨提示")
//                            .setMessage("点击该页面广告将要下载应用，是否继续操作？")
//                            .addAction("取消", new QMUIDialogAction.ActionListener() {
//                                @Override
//                                public void onClick(QMUIDialog dialog, int index) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .addAction("继续", new QMUIDialogAction.ActionListener() {
//                                @Override
//                                public void onClick(QMUIDialog dialog, int index) {
//                                    dialog.dismiss();
//
//                                }
//                            })
//                            .show();
                    ttSplashAd.setDownloadListener(new TTAppDownloadListener() {
                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            Log.d("广告", "下载中...");
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            Log.d("广告", "下载暂停...");
                        }


                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            Log.d("广告", "下载失败...");
                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                            Log.d("广告", "下载完成...");
                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
                            Log.d("广告", "安装完成...");
                        }
                    });
                }
            }
        }, AD_TIME_OUT);
    }

    //隐藏虚拟按键
    private void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        content_frame.removeAllViews();
        finish();
    }

    //禁用返回键  相比于onBackPressed和onKeyDown方法有时候没有效果，这个方法能保证禁用手机的返回键。
    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
