package com.ahnz.headmaster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.ahnz.headmaster.adapter.MainActivityViewPagerAdapter;
import com.ahnz.headmaster.bean.CameraData;
import com.ahnz.headmaster.bean.RealmTest;
import com.ahnz.headmaster.utils.PathUtils;
import com.ahnz.headmaster.utils.PermissionUtil;
import com.ahnz.headmaster.utils.PhotoFromPhotoAlbum;
import com.ahnz.headmaster.utils.database.RealmOperationHelper;
import com.ahnz.headmaster.view.activity_crop.CropActivity;
import com.ahnz.headmaster.view.activity_header_details.HeaderDetailsActivity;
import com.ahnz.headmaster.view.activity_search.SearchActivity;
import com.ahnz.headmaster.widget.NoTouchViewPager;
import com.ahnz.headmaster.widget.dialog.PolicyAgreementDialog;
import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhouyou.http.EasyHttp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import io.realm.RealmResults;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

public class MainActivity extends FragmentActivity {

    //@BindView(R.id.view_pager)
    public static NoTouchViewPager noTouchViewPager;

    @BindView(R.id.tab)
    PageNavigationView pageNavigationView;

    //是否 同意协议  标识
    private String agreeFlag = "AgreeFlag";

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        boolean isAgree = Hawk.get(agreeFlag, false);
        if (!isAgree) {
            new PolicyAgreementDialog(this).show();
        }

        noTouchViewPager = (NoTouchViewPager) findViewById(R.id.view_pager);

        ButterKnife.bind(this);

        NavigationController navigationController = pageNavigationView.custom()
                .addItem(newItem(R.mipmap.home_no_select, R.mipmap.home_select, "首页"))
                .addItem(newItem(R.mipmap.find_no_select, R.mipmap.find_select, "发现"))
                .addItem(newItem(R.mipmap.make_no_select, R.mipmap.make_select, "制作"))
                .addItem(newItem(R.mipmap.my_no_select, R.mipmap.my_select, "我的"))
                .build();

        noTouchViewPager.setAdapter(new MainActivityViewPagerAdapter(getSupportFragmentManager(), navigationController.getItemCount()));

        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(noTouchViewPager);

        //设置消息数
        //navigationController.setMessageNumber(1, 8);

        //navigationController.setMessageNumber(2, 89);

        //navigationController.setMessageNumber(3, 0);

        //navigationController.setMessageNumber(4, 125);

        //设置显示小圆点
        //navigationController.setHasMessage(0, true);
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable, checkedDrawable, text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(0xff000000);
        return normalItemView;
    }

    public CameraData cameraData;
    private String picPath = "";

    /**
     * 跳转 搜索页面
     */
    public void clickSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            //Toast.makeText(this, PhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData()), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CropActivity.class);
            try {
                //intent.putExtra(HeaderDetailsActivity.PIC_PATH_LOCAL, "file://" + PhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData()));
                intent.putExtra(HeaderDetailsActivity.PIC_PATH_LOCAL, "file://" + PathUtils.getPath(this, data.getData()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }

        //拍照
        if (requestCode == 2 && resultCode == RESULT_OK) {
            //安卓 7 到 安卓 9
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (null != cameraData) {
                    picPath = String.valueOf(cameraData.getFile());
                    //Toast.makeText(this, picPath, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, CropActivity.class);
                    intent.putExtra(HeaderDetailsActivity.PIC_PATH_LOCAL, "file://" + picPath);
                    startActivity(intent);

                    //通知系统相册刷新
                    Intent intentFile = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intentFile.setData(Uri.fromFile(cameraData.getFile()));
                    sendBroadcast(intentFile);
                }
            }
            // 安卓 10 及 以上
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (null != cameraData) {
                    Uri uri = cameraData.getUri();
                    Intent intent = new Intent(this, CropActivity.class);
                    intent.putExtra(HeaderDetailsActivity.PIC_PATH_URI, uri);
                    startActivity(intent);

                    //通知系统相册刷新
                    //                    Intent intentFile = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    //                    intentFile.setData(uri);
                    //                    sendBroadcast(intentFile);
                }
            } else {
                if (null != cameraData) {
                    Uri uri = cameraData.getUri();
                    picPath = uri.getEncodedPath();
                    //Toast.makeText(this, picPath, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, CropActivity.class);
                    intent.putExtra(HeaderDetailsActivity.PIC_PATH_LOCAL, "file://" + picPath);
                    startActivity(intent);

                    //通知系统相册刷新
                    Intent intentFile = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intentFile.setData(uri);
                    sendBroadcast(intentFile);
                }
            }
        }
    }

    private long mExitTime;       //实现“再按一次退出”的记录时间变量

    @Override //再按一次退出程序
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime < 2000) {
            super.onBackPressed();
        } else {
            mExitTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
        }
    }

    //函数名可以自己定义，只要在其上面注解@Subscribe并设置好线程
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String flag) {
        if ("no_agree".equals(flag)) {
            finish();
        }
        if ("agree".equals(flag)) {
            Hawk.put(agreeFlag, true);
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.requestEach(PermissionUtil.permissionsGroup)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) throws Exception {
                            if (permission.granted) {
                                // 用户已经同意该权限
                                //Toast.makeText(MainActivity.this, "全部授权", Toast.LENGTH_SHORT).show();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                                //Toast.makeText(MainActivity.this, "用户拒绝了" + permission.name + "，没有选中『不再询问』", Toast.LENGTH_SHORT).show();
                            } else {
                                // 用户拒绝了该权限，而且选中『不再询问』那么下次启动时，就不会提示出来了，
                                //Toast.makeText(MainActivity.this, "用户拒绝了" + permission.name + "，而且选中『不再询问』", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}