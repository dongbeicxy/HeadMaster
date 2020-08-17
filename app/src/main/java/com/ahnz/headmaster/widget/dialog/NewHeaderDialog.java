package com.ahnz.headmaster.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.ahnz.headmaster.MainActivity;
import com.ahnz.headmaster.R;
import com.ahnz.headmaster.utils.ADUtil;
import com.ahnz.headmaster.utils.FileUtils;
import com.ahnz.headmaster.utils.PermissionUtil;
import com.ahnz.headmaster.view.activity_header_details.HeaderDetailsActivity;
import com.ahnz.headmaster.widget.ScaleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.hawk.Hawk;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author xzb
 * @description: 生成新头像
 * @date :2020/7/16 16:19
 */
public class NewHeaderDialog extends Dialog {

    //广告布局
    ScaleLayout ad_layout;

    //广告视图
    RelativeLayout ad_view;

    private FragmentActivity activity;
    private Bitmap bitmap;

    public NewHeaderDialog(@NonNull FragmentActivity activity, Bitmap bitmap) {
        super(activity, R.style.Dialog);
        this.activity = activity;
        this.bitmap = bitmap;
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_header);

        //初始化界面控件
        initView();

        boolean isCleanAD = Hawk.get("CleanADFlag", false);
        if (!isCleanAD) {
            //加载 广告
            ADUtil.getInstance().getAd(activity, "945325317", new ADUtil.ADViewController() {
                @Override
                public void setViewState(int viewState) {
                    ad_layout.setVisibility(viewState);
                }

                @Override
                public void addAdView(View adView) {
                    ad_view.addView(adView);
                }
            });
        }

    }

    private void initView() {
        ad_layout = findViewById(R.id.ad_layout);
        ad_view = findViewById(R.id.ad_view);

        //点击  一键去广告
        findViewById(R.id.clean_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad_layout.setVisibility(View.GONE);
            }
        });

        //点击  继续制作
        findViewById(R.id.continue_make_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //点击  返回首页
        findViewById(R.id.return_home_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }
        });

        //点击  手机相册
        findViewById(R.id.save_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = new PermissionUtil().permissionJudge(activity, PermissionUtil.permissionsStorage);
                if (flag) {
                    new FileUtils().saveBitmap(System.currentTimeMillis() + ".jpg", "hm", bitmap, activity, new FileUtils.PicSaveStateListener() {
                        @Override
                        public void notifyState(String msg) {
                            Toast t = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.TOP, 0, 64);
                            t.show();
                        }
                    });
                } else {
                    Toast.makeText(activity, "请到设置中打开存储权限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //点击  微信
        findViewById(R.id.share_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMImage imageFile = new UMImage(activity, bitmap);
                new ShareAction(activity)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(imageFile)//分享文件
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                //Toast.makeText(activity, "onResult", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                //Toast.makeText(activity, "onError", Toast.LENGTH_SHORT).show();
                                Log.e("share", throwable.getMessage());
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                //Toast.makeText(activity, "onCancel", Toast.LENGTH_SHORT).show();
                            }
                        })//回调监听器
                        .share();
            }
        });

        //点击  QQ
        findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMImage imageFile = new UMImage(activity, bitmap);
                new ShareAction(activity)
                        .setPlatform(SHARE_MEDIA.QQ)//传入平台
                        .withMedia(imageFile)//分享文件
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                //Toast.makeText(activity, "onResult", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                //Toast.makeText(activity, "onError", Toast.LENGTH_SHORT).show();
                                Log.e("share", throwable.getMessage());
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                //Toast.makeText(activity, "onCancel", Toast.LENGTH_SHORT).show();
                            }
                        })//回调监听器
                        .share();
            }
        });

        //加载 方形 图片
        ImageView iv_square = (ImageView) findViewById(R.id.iv_square);
        Glide.with(activity).load(bitmap).into(iv_square);

        CircleImageView iv_circular = (CircleImageView) findViewById(R.id.iv_circular);
        Glide.with(activity).load(bitmap).into(iv_circular);
    }
}
