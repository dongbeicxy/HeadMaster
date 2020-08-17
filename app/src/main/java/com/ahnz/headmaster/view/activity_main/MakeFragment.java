package com.ahnz.headmaster.view.activity_main;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ahnz.headmaster.MainActivity;
import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.CameraData;
import com.ahnz.headmaster.utils.FileProviderUtil;
import com.ahnz.headmaster.utils.FileUtils;
import com.ahnz.headmaster.utils.PermissionUtil;
import com.ahnz.headmaster.view.base.LazyFragment;
import com.gyf.immersionbar.ImmersionBar;
import com.renj.pagestatuscontroller.annotation.RPageStatus;

import java.io.File;
import java.util.List;

import butterknife.OnClick;

/**
 * @author xzb
 * @description:制作
 * @date :2020/7/8 9:19
 */
public class MakeFragment extends LazyFragment {
    @Override
    protected int getContentViewId() {
        Log.e("Lazy", "制作加载布局");
        return R.layout.fragment_make;
    }

    @Override
    protected void initData() {
        Log.e("Lazy", "制作加载数据");
    }

    @Override
    protected void initEvent() {
        pageStatusController.changePageStatus(RPageStatus.CONTENT);
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    private Uri uri;//照片uri
    private File cameraPicSaveFilePath = null;


    @OnClick({R.id.picture, R.id.camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.camera:
                boolean flag = new PermissionUtil().permissionJudge(getActivity(), PermissionUtil.permissionsCamera);
                if (flag) {
                    // 跳转到系统的拍照界面
                    Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (isAndroidQ) {
                        // 适配android 10
                        uri = createImageUri();
                    } else {
                        //拍照照片路径
                        //cameraPicSaveFilePath = new File(FileUtils.getFolderOrFilePath(getActivity(), "camera", true, System.currentTimeMillis() + ".jpg"));
                        cameraPicSaveFilePath = new File(FileUtils.createRootPicsFolderOrFile("hm", true, System.currentTimeMillis() + ".jpg"));
                        if (null != cameraPicSaveFilePath) {
                            //注意：下面打开相机方式拍照后文件会保存在imgUri中，onActivityResult回调不会返回数据，当回调成功后直接拿imgUri就是你拍的照片内容。
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                intentToCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else {
                            }
                            uri = FileProviderUtil.getUriForFile(getActivity(), cameraPicSaveFilePath);
                        }
                    }
                    //下面这句指定调用相机拍照后的照片存储的路径
                    intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    getActivity().startActivityForResult(intentToCamera, 2);
                    ((MainActivity) getActivity()).cameraData = new CameraData();
                    ((MainActivity) getActivity()).cameraData.setFile(cameraPicSaveFilePath);
                    ((MainActivity) getActivity()).cameraData.setUri(uri);

                } else {
                    Toast.makeText(getActivity(), "请到设置中打开相机权限", Toast.LENGTH_SHORT).show();
                }
                break;

            //点击相册
            case R.id.picture:
                boolean picturePermissionFlag = new PermissionUtil().permissionJudge(getActivity(), PermissionUtil.permissionsStorage);
                if (picturePermissionFlag) {
                    //                Intent intent = new Intent();
                    //                intent.setAction(Intent.ACTION_GET_CONTENT);
                    //                intent.setType("image/*");
                    //                getActivity().startActivityForResult(intent, 1);
                    // 参考 Ucrop
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                            .setType("image/*")
                            .addCategory(Intent.CATEGORY_OPENABLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        String[] mimeTypes = {"image/jpeg", "image/png"};
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    }
                    getActivity().startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);
                } else {
                    Toast.makeText(getActivity(), "请到设置中打开存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getActivity().getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }
}
