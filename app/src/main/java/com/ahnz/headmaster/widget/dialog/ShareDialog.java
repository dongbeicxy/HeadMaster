package com.ahnz.headmaster.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.SharePicPath;
import com.ahnz.headmaster.utils.ShareUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author xzb
 * @description: 分享Dialog
 * @date :2020/7/10 13:56
 */
public class ShareDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    private SharePicPath sharePicPath;
    private Bitmap bitmap;

    public ShareDialog(@NonNull Context context, Activity activity, SharePicPath sharePicPath, Bitmap bitmap) {
        super(context, R.style.Dialog);
        this.activity = activity;
        this.sharePicPath = sharePicPath;
        this.bitmap = bitmap;
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        getWindow().setLayout(
                getWindow().getContext().getResources().getDisplayMetrics().widthPixels,
                (int) (getWindow().getContext().getResources().getDisplayMetrics().widthPixels * 1.00));
        //        //居于底部
        getWindow().setGravity(Gravity.BOTTOM);

        //初始化界面控件
        initView();
    }

    private TextView share_weixin, share_qq;

    private void initView() {

        share_weixin = findViewById(R.id.share_weixin);
        share_qq = findViewById(R.id.share_qq);
        share_weixin.setOnClickListener(this);
        share_qq.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_weixin:
                //ShareUtil.getInstance().share(activity, SHARE_MEDIA.WEIXIN, sharePicPath.isLocal(), sharePicPath.getPicPath());
                UMImage wx = new UMImage(activity, bitmap);
                UMImage thumb = new UMImage(activity, bitmap);
                wx.setThumb(thumb);//设置缩略图

                wx.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
                wx.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
                //压缩格式设置
                wx.compressFormat = Bitmap.CompressFormat.JPEG;//用户分享透明背景的图片可以设置 PNG 这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色

                new ShareAction(activity)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(wx)//分享图片
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
                break;

            case R.id.share_qq:
                //ShareUtil.getInstance().share(activity, SHARE_MEDIA.QQ, sharePicPath.isLocal(), sharePicPath.getPicPath());
                UMImage qq = new UMImage(activity, bitmap);
                new ShareAction(activity)
                        .setPlatform(SHARE_MEDIA.QQ)//传入平台
                        .withMedia(qq)//分享图片
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
                break;
        }
        dismiss();
    }
}
