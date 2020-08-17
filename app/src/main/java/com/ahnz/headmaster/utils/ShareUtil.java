package com.ahnz.headmaster.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author xzb
 * @description: 分享 工具类
 * @date :2020/7/20 13:54
 */
public class ShareUtil {
    //https://www.cnblogs.com/ngy0217/p/9006716.html
    //静态内部类实现模式（线程安全，调用效率高，可以延时加载）
    private static class ShareUtilInstance {
        private static final ShareUtil instance = new ShareUtil();
    }

    private ShareUtil() {
    }

    public static ShareUtil getInstance() {
        return ShareUtilInstance.instance;
    }

    public void share(Activity activity, SHARE_MEDIA share_media, boolean isLocal, String picPath) {
        //分享本地图片
        if (isLocal) {
            File file = new File(picPath);
            if (!file.exists()) {
                Log.e("ShareUtil", "图片不存在或路径错误！");
                return;
            }
            FileInputStream fis = null;
            Bitmap bitmap = null;
            try {
                fis = new FileInputStream(picPath);
                bitmap = BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null == bitmap) {
                return;
            }
            UMImage imageFile = new UMImage(activity, bitmap);
            new ShareAction(activity)
                    .setPlatform(share_media)//传入平台
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
        //分享网络图片
        else {
            UMImage imageUrl = new UMImage(activity, picPath);
            new ShareAction(activity)
                    .setPlatform(share_media)//传入平台
                    .withMedia(imageUrl)//分享网络图片
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
    }
}
