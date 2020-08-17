package com.ahnz.headmaster.view.activity_crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.utils.database.RealmOperationHelper;
import com.ahnz.headmaster.utils.pic_mapping.ImageWrapper;
import com.ahnz.headmaster.view.activity_header_details.HeaderDetailsActivity;
import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zhouyou.http.EasyHttp;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xzb
 * @description: 图片 裁剪 Activity
 * @date :2020/7/27 13:35
 */
public class CropActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.cropImageView)
    CropImageView cropImageView;

    //下一步
    @BindView(R.id.next_tv)
    TextView next_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.black).navigationBarColor(R.color.black).fitsSystemWindows(true).init();
        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        toolbar.setTitle("");
        //设置导航图标要在setSupportActionBar方法之后
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        if (null != getIntent().getStringExtra(HeaderDetailsActivity.PIC_PATH_NET)) {
            next_tv.setVisibility(View.GONE);
            //根据url 获得 Bitmap
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = getBitmap(getIntent().getStringExtra(HeaderDetailsActivity.PIC_PATH_NET));
                    handler.sendMessage(message);
                }
            }).start();
        }
        if (null != getIntent().getStringExtra(HeaderDetailsActivity.PIC_PATH_LOCAL)) {
            cropImageView.setImageUriAsync(Uri.parse(getIntent().getStringExtra(HeaderDetailsActivity.PIC_PATH_LOCAL)));
        }

        if (null != getIntent().getParcelableExtra(HeaderDetailsActivity.PIC_PATH_URI)) {
            cropImageView.setImageUriAsync((Uri) getIntent().getParcelableExtra(HeaderDetailsActivity.PIC_PATH_URI));
        }
        //设置纵横比是否固定  1:1
        cropImageView.setFixedAspectRatio(true);
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
                cropImageView.setImageBitmap((Bitmap) msg.obj);
                next_tv.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 点击下一步
     */
    public void clickNext(View view) {
        //旋转 90 度
        //cropImageView.rotateImage(90);
        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
//                if (result.getError() == null) {
//                    Bitmap bitmap = cropImageView.getCropShape() == CropImageView.CropShape.OVAL
//                            ? CropImage.toOvalBitmap(result.getBitmap())
//                            : result.getBitmap();
//                }
                Bitmap cropped = view.getCroppedImage();
                if (null != cropped) {
                    Intent intent = new Intent(CropActivity.this, HeaderDetailsActivity.class);
                    //ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //cropped.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    //byte[] bitmapByte = baos.toByteArray();
                    //intent.putExtra(HeaderDetailsActivity.PIC_BITMAP_BYTES, bitmapByte);
                    CropActivity.this.startActivity(intent);
                    EventBus.getDefault().postSticky(cropped);
//                    try {
//                        baos.flush();
//                        baos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
        //裁剪 图片 请求数据
        cropImageView.getCroppedImageAsync();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
