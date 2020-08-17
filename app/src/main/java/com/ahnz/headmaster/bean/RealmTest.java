package com.ahnz.headmaster.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * @author xzb
 * @description: Realm 测试类
 * @date :2020/7/17 11:01
 */
public class RealmTest extends RealmObject {
    /**
     * @PrimaryKey 表示该字段是主键
     * 字段必须是String、 integer、byte、short、 int、long 以及它们的封装类Byte, Short, Integer, and Long
     * 使用了该注解之后可以使用copyToRealmOrUpdate()方法，通过主键查询它的对象，如果查询到了，则更新它，否则新建一个对象来代替。
     * 使用了该注解之后，创建和更新数据将会慢一点，查询数据会快一点。
     * @Ignore 忽略，即该字段不被存储到本地
     * @Required —— 表示该字段非空
     */
    @PrimaryKey
    private String picPath;
    @Required
    private String dataID = "";

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
