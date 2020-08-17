package com.ahnz.headmaster.utils;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * @作者 xzb
 * @描述: 权限工具类
 * @创建时间 :2020/4/3 11:21
 */
public class PermissionUtil {

    /**
     * 权限组
     */
    public static String[] permissionsGroup = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            //读取日历
            //Manifest.permission.READ_CALENDAR,
            Manifest.permission.CAMERA,
            //读取 通讯录
            //Manifest.permission.READ_CONTACTS,
            //Manifest.permission.ACCESS_FINE_LOCATION,
            //录音
            //Manifest.permission.RECORD_AUDIO,
            //传感器
            //Manifest.permission.BODY_SENSORS,
            //收短信
            //Manifest.permission.RECEIVE_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    /**
     * 电话状态
     */
    public static String[] permissionsPhoneState = new String[]{
            Manifest.permission.READ_PHONE_STATE};

    /**
     * 拍照
     */
    public static String[] permissionsCamera = new String[]{
            Manifest.permission.CAMERA};

    /**
     * 位置
     */
    public static String[] permissionsLocation = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION};

    /**
     * 录音
     */
    public static String[] permissionsAudio = new String[]{
            Manifest.permission.RECORD_AUDIO};

    /**
     * 存储
     */
    public static String[] permissionsStorage = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    //权限 标识
    private boolean flag = false;

    /**
     * 判断 权限是否赋予
     *
     * @param fragmentActivity
     * @param permissions
     * @return
     */
    @SuppressLint("CheckResult")
    public boolean permissionJudge(FragmentActivity fragmentActivity, String[] permissions) {
        RxPermissions rxPermissions = new RxPermissions(fragmentActivity);
        rxPermissions.requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            //Toast.makeText(MainActivity.this, "全部授权", Toast.LENGTH_SHORT).show();
                            flag = true;
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            //Toast.makeText(MainActivity.this, "用户拒绝了" + permission.name + "，没有选中『不再询问』", Toast.LENGTH_SHORT).show();
                            flag = false;
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』那么下次启动时，就不会提示出来了，
                            //Toast.makeText(MainActivity.this, "用户拒绝了" + permission.name + "，而且选中『不再询问』", Toast.LENGTH_SHORT).show();
                            flag = false;
                        }
                    }
                });
        return flag;
    }
}
