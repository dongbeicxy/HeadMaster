package com.ahnz.headmaster.bean;

import android.graphics.Bitmap;
import android.net.Uri;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author xzb
 * @description: 图片收藏
 * @date :2020/7/17 14:02
 */
public class PictureCollection extends RealmObject {
    public static final String NET = "net";
    public static final String LOCAL = "local";
    public static final String URI = "uri";
    /**
     * @PrimaryKey 表示该字段是主键
     * 字段必须是String、 integer、byte、short、 int、long 以及它们的封装类Byte, Short, Integer, and Long
     * 使用了该注解之后可以使用copyToRealmOrUpdate()方法，通过主键查询它的对象，如果查询到了，则更新它，否则新建一个对象来代替。
     * 使用了该注解之后，创建和更新数据将会慢一点，查询数据会快一点。
     * @Ignore 忽略，即该字段不被存储到本地
     * @Required —— 表示该字段非空
     */
    //图片路径  网络路径 和 本机路径
    private String picPath;

    //图片 路径 类型
    private String pathType;

    @PrimaryKey
    //收藏时间  用于排序
    private long collectionTime;

    private byte[] bitmapBytes;

    public byte[] getBitmapBytes() {
        return bitmapBytes;
    }

    public void setBitmapBytes(byte[] bitmapBytes) {
        this.bitmapBytes = bitmapBytes;
    }

    public long getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(long collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getPathType() {
        return pathType;
    }

    public void setPathType(String pathType) {
        this.pathType = pathType;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

}
