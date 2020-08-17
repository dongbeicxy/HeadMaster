package com.ahnz.headmaster.utils;

/**
 * @author xzb
 * @description:接口 网址
 * @date :2020/7/8 14:29
 */
public class URLS {
    //线上
    public static final String BASE_URL = "https://app1-api.qianr.com";

    //测试
    //public static final String BASE_URL = "http://app1.qianr.com";

    //发现Fragment 分类
    public static final String FIND_CLASSIFY = BASE_URL + "/api/avater/v1/cat";

    //最新精品
    public static final String NEW_BOUTIQUE = BASE_URL + "/api/avater/v1/new?page=";

    //火爆人气
    public static final String POPULAR = BASE_URL + "/api/avater/v1/hot?page=";

    //热门搜索
    public static final String HOT_SEARCH = BASE_URL + "/api/avater/v1/hotSearch";

    //获取 搜索结果
    public static final String GET_SEARCH_RESULT = BASE_URL + "/api/avater/v1/result?name=";

    //发现 分类详情
    public static final String FIND_CLASS_DETAILS = BASE_URL + "/api/avater/v1/catDetail?cat_id=";

    //发现 分类详情
    public static final String FRAME = BASE_URL + "/api/avater/v1/frame?type=";
}
