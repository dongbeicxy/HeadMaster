package com.ahnz.headmaster.view.activity_header_details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.adapter.HeaderDetailsMenuViewPagerAdapter;
import com.ahnz.headmaster.bean.PictureCollection;
import com.ahnz.headmaster.bean.SharePicPath;
import com.ahnz.headmaster.utils.BitmapUtil;
import com.ahnz.headmaster.utils.FileUtils;
import com.ahnz.headmaster.utils.PathUtils;
import com.ahnz.headmaster.utils.PermissionUtil;
import com.ahnz.headmaster.utils.database.RealmOperationHelper;
import com.ahnz.headmaster.utils.pic_mapping.ImageWrapper;
import com.ahnz.headmaster.view.base.LazyFragment;
import com.ahnz.headmaster.widget.PicsGestureLayout;
import com.ahnz.headmaster.widget.ScaleLayout;
import com.ahnz.headmaster.widget.ViewPagerForScrollView;
import com.ahnz.headmaster.widget.dialog.DownLoadPicDialog;
import com.ahnz.headmaster.widget.dialog.NewHeaderDialog;
import com.ahnz.headmaster.widget.dialog.ShareDialog;
import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.umeng.socialize.UMShareAPI;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.utils.HttpLog;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import jp.wasabeef.glide.transformations.BitmapTransformation;

/**
 * @author xzb
 * @description:头像详情页面
 * @date :2020/7/10 13:21
 */
public class HeaderDetailsActivity extends AppCompatActivity implements LazyFragment.PicMakeListener {

    //网络图片
    public static final String PIC_PATH_NET = "pic_path_net";

    //本地图片
    public static final String PIC_PATH_LOCAL = "pic_path_local";

    //本地图片 Uri (拍照)
    public static final String PIC_PATH_URI = "pic_path_uri";

    //本地图片 Bitmap (裁剪)
    public static final String PIC_BITMAP_BYTES = "pic_bitmap_bytes";

    private static final String[] CHANNELS = new String[]{"滤镜", "边框", "贴图"};

    private List<String> mDataList = Arrays.asList(CHANNELS);

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //图片编辑区 布局
    @BindView(R.id.pic_imageview_layout)
    ScaleLayout pic_imageview_layout;

    //底部 图片
    @BindView(R.id.pic_imageview_bottom)
    ImageView pic_imageview_bottom;

    //边框
    @BindView(R.id.pic_imageview_frame)
    ImageView pic_imageview_frame;

    //贴图 容器
    @BindView(R.id.mapping_layout)
    PicsGestureLayout mapping_layout;

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    @BindView(R.id.viewPager)
    ViewPagerForScrollView viewPager;

    //美化 等菜单项 布局
    @BindView(R.id.menu_layout)
    LinearLayout menuLayout;

    //生成新头像 布局
    @BindView(R.id.new_header_layout)
    ScrollView new_header_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).fitsSystemWindows(true).init();
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        setContentView(R.layout.activity_header_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initViews();
    }

    private void initViews() {
        toolbar.setTitle("");
        //设置导航图标要在setSupportActionBar方法之后
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_dark_24dp);
    }

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    //函数名可以自己定义，只要在其上面注解@Subscribe并设置好线程
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(Bitmap bitmap) {
        this.bitmap = bitmap;
        Glide.with(this)
                .load(bitmap)
                .into(pic_imageview_bottom);
    }

    //点击 分享
    public void clickShare(View view) {
        SharePicPath sharePicPath = new SharePicPath();
        new ShareDialog(this, this, sharePicPath, bitmap).show();
    }

    //按 物理返回键
//    @Override
//    public void onBackPressed() {
//        showExitDialog();
//    }

    private void showExitDialog() {
        if (View.VISIBLE != new_header_layout.getVisibility()) {
            finish();
            return;
        }
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("温馨提示")
                .setMessage("确定退出？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        new_header_layout.setVisibility(View.GONE);
                        menuLayout.setVisibility(View.VISIBLE);
                        Glide.with(HeaderDetailsActivity.this)
                                .load(bitmap)
                                .into(pic_imageview_bottom);
                        //还原原图 去掉边框
                        pic_imageview_frame.setVisibility(View.GONE);
                        //清除所有贴图
                        mapping_layout.clearChildImage();
                    }
                })
                .show();
    }

    //点击 美化
    public void clickBeautify(View view) {
        menuLayout.setVisibility(View.GONE);
        new_header_layout.setVisibility(View.VISIBLE);
        if (null != viewPager.getAdapter()) {
            Log.e("Head", "使用老布局");
            return;
        }
        Log.e("Head", "生成布局");
        //页卡对应内容
        viewPager.setAdapter(new HeaderDetailsMenuViewPagerAdapter(getSupportFragmentManager(), mDataList.size()));
        //指示器 白色背景
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(Color.RED);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setLineHeight(6.0f);
                indicator.setRoundRadius(0f);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.RED);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private Disposable disposable;

    //点击 保存
    public void clickSave(View view) {
        boolean flag = new PermissionUtil().permissionJudge(this, PermissionUtil.permissionsStorage);
        if (flag) {
            new FileUtils().saveBitmap(System.currentTimeMillis() + ".jpg", "hm", bitmap, this, new FileUtils.PicSaveStateListener() {
                @Override
                public void notifyState(String msg) {
                    Toast t = Toast.makeText(HeaderDetailsActivity.this, msg, Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.TOP, 0, 64);
                    t.show();
                }
            });
//            if (null != getIntent().getStringExtra(PIC_PATH_NET)) {
//                DownLoadPicDialog downLoadPicDialog = new DownLoadPicDialog(this);
//                //下载 网络图片
//                //String savePath = FileUtils.getFolderOrFilePath(this, "hm_pics", true, FileUtils.getFileName(getIntent().getStringExtra(PIC_PATH_NET)));
//                String savePath = FileUtils.createRootPicsFolderOrFile("hm", false, "");
//                disposable = EasyHttp.downLoad(getIntent().getStringExtra(PIC_PATH_NET))
//                        .savePath(savePath)
//                        .saveName(FileUtils.getFileName(getIntent().getStringExtra(PIC_PATH_NET)))//不设置默认名字是时间戳生成的
//                        .execute(new DownloadProgressCallBack<String>() {
//                            @Override
//                            public void update(long bytesRead, long contentLength, boolean done) {
//                                int progress = (int) (bytesRead * 100 / contentLength);
//                                HttpLog.e(progress + "% ");
//                                if (done) {//下载完成
//                                    //Toast.makeText(HeaderDetailsActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onStart() {
//                                downLoadPicDialog.show();
//                                //开始下载
//                                //Toast.makeText(HeaderDetailsActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onComplete(String path) {
//                                //下载完成，path：下载文件保存的完整路径
//                                //Toast.makeText(NetActivity.this, "下载完成:" + path, Toast.LENGTH_SHORT).show();
//                                downLoadPicDialog.dismiss();
//                                Toast t = Toast.makeText(HeaderDetailsActivity.this, "头像已保存到系统相册中", Toast.LENGTH_SHORT);
//                                t.setGravity(Gravity.TOP, 0, 64);
//                                t.show();
//                                try {
//                                    //通知系统相册刷新
//                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                    intent.setData(Uri.fromFile(new File(path)));
//                                    sendBroadcast(intent);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onError(ApiException e) {
//                                downLoadPicDialog.dismiss();
//                                //下载失败
//                                //Toast.makeText(HeaderDetailsActivity.this, "下载失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                Toast t = Toast.makeText(HeaderDetailsActivity.this, "下载失败", Toast.LENGTH_SHORT);
//                                t.setGravity(Gravity.TOP, 0, 64);
//                                t.show();
//                            }
//                        });
//            } else {
//                Toast t = Toast.makeText(HeaderDetailsActivity.this, "头像已保存到系统相册中", Toast.LENGTH_SHORT);
//                t.setGravity(Gravity.TOP, 0, 64);
//                t.show();
//            }
        } else {
            Toast.makeText(this, "请到设置中打开存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    //点击 生成新头像
    public void clickNewHeader(View view) {
        Bitmap bitmap = BitmapUtil.getViewBitmap(pic_imageview_layout);
        new NewHeaderDialog(this, bitmap).show();
    }

    //点击 收藏
    public void clickCollection(View view) {
        PictureCollection pictureCollection = new PictureCollection();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        pictureCollection.setBitmapBytes(bitmapByte);
        pictureCollection.setCollectionTime(System.currentTimeMillis());
        RealmOperationHelper.getInstance().addOrUpdate(pictureCollection);
        Toast t = Toast.makeText(HeaderDetailsActivity.this, "头像已添加至收藏", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.TOP, 0, 64);
        t.show();
    }

    //设置 滤镜
    @Override
    public void setFilter(BitmapTransformation bitmapTransformation, int position, Uri picUri, String picPath) {
        //正常
        if (0 == position) {
            Glide.with(this)
                    .load(bitmap)
                    .into(pic_imageview_bottom);
        } else {
            Glide.with(this)
                    .load(bitmap)
                    .transform(bitmapTransformation)
                    .into(pic_imageview_bottom);
        }
    }

    //设置边框
    @Override
    public void setFrame(String material) {
        //还原原图 去掉边框
        if (null == material) {
            pic_imageview_frame.setVisibility(View.GONE);
        } else {
            pic_imageview_frame.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(material)
                    .into(pic_imageview_frame);
        }
    }

    //https://blog.csdn.net/xiaanming/article/details/42833893
    //设置 贴图
    @Override
    public void setMapping(String thumb) {
        //还原原图 去掉贴图
        if (null == thumb) {
            //重置 画布 清空所有贴图
            mapping_layout.clearChildImage();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                message.obj = getBitmap(thumb);
                handler.sendMessage(message);
            }
        }).start();
    }

    private Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    //返回应用主线程中的 Looper
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && null != msg.obj) {
                //https://www.cnblogs.com/andlp/p/matrix.html
                //初始化时,图片坐标(布局正中间)
                ImageWrapper imageWrapper = new ImageWrapper(
                        mapping_layout.getWidth() / 2,
                        mapping_layout.getHeight() / 2, (Bitmap) msg.obj);
                mapping_layout.clearSelectedImage();
                imageWrapper.setChecked(true);//是否选中
                mapping_layout.addChildImage(imageWrapper);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中，添加：
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //showExitDialog();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != disposable) {
            EasyHttp.cancelSubscription(disposable);
        }

        if (null != RealmOperationHelper.mRealm) {
            RealmOperationHelper.mRealm.close();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
