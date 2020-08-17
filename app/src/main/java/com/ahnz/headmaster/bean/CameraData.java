package com.ahnz.headmaster.bean;

import android.net.Uri;

import java.io.File;

/**
 * @author xzb
 * @description: 拍照 图片数据
 * @date :2020/7/10 17:02
 */
public class CameraData {
    private File file;
    private Uri uri;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
