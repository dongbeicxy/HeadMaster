package com.ahnz.headmaster;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.ahnz.headmaster.utils.database.Migration;
import com.ahnz.headmaster.utils.database.RealmOperationHelper;
import com.ahnz.headmaster.widget.LoadFooterView;
import com.ahnz.headmaster.widget.RefreshHeaderView;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.orhanobut.hawk.Hawk;
import com.renj.pagestatuscontroller.RPageStatusManager;
import com.renj.pagestatuscontroller.annotation.RPageStatus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.cache.model.CacheMode;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author xzb
 * @description:
 * @date :2020/7/8 14:21
 */
public class HeadMasterApplication extends Application {

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new RefreshHeaderView(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new LoadFooterView(context);
            }
        });
    }

    //友盟 分享
    {
        PlatformConfig.setWeixin("wxc42004af9ad26946", "f1a3041e389bf3591e2a13e523c7697a");
        PlatformConfig.setQQZone("101861316", "a210ce8ca221ecd2e67e1a784048cd2b");
        PlatformConfig.setQQFileProvider("com.ahnz.headmaster.fileprovider");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "6d982c0555", true);
        Hawk.init(this).build();
        RPageStatusManager.getInstance()
                .addPageStatusView(RPageStatus.LOADING, R.layout.layout_loading)
                .addPageStatusView(RPageStatus.EMPTY, R.layout.layout_empty)
                .addPageStatusView(RPageStatus.NET_WORK, R.layout.layout_timeout)
                .addPageStatusView(RPageStatus.ERROR, R.layout.layout_error);
        initEasyHttp();
        initRealm();
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口  s2参数为 pushSecret
//        UMConfigure.init(this, "5f110185978eea08cad12707"
//                , AnalyticsConfig.getChannel(this), UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5f110185978eea08cad12707");

        // 选用AUTO页面采集模式
        // 1. MobclickAgent.PageMode.AUTO: 建议大多数用户使用本采集模式，SDK在此模式下自动采集Activity
        // 页面访问路径，开发者不需要针对每一个Activity在onResume/onPause函数中进行手动埋点。在此模式下，
        // 开发者如需针对Fragment、CustomView等自定义页面进行页面统计，直接调用MobclickAgent.onPageStart/
        // MobclickAgent.onPageEnd手动埋点即可。此采集模式简化埋点工作，唯一缺点是在Android 4.0以下设备中
        // 统计不到Activity页面数据和各类基础指标(提示：目前Android 4.0以下设备市场占比已经极小)。
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        initAD();

        /*********************************************************************************/
        /*********************************************************************************/
        /*********************************************************************************/
      /*  //内存泄漏
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);*/
    }

   /* private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        HeadMasterApplication application = (HeadMasterApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }*/

    private void initEasyHttp() {
        EasyHttp.init(this);//默认初始化,必须调用
        //String Url = URLS.TEST_BASE_URL;
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        EasyHttp.getInstance()
                //可以全局统一设置全局URL
                //.setBaseUrl(Url)//设置全局URL  url只能是域名 或者域名+端口号
                // 打开该调试开关并设置TAG,不需要就不要加入该行
                // 最后的true表示是否打印内部异常，一般打开方便调试错误
                .debug("RxEasyHttp", true)
                //如果使用默认的60秒,以下三行也不需要设置
                .setReadTimeOut(3 * 1000)
                .setWriteTimeOut(3 * 1000)
                .setConnectTimeout(3 * 1000)
                //可以全局统一设置超时重连次数,默认为3次,那么最差的情况会请求4次(一次原始请求,三次重连请求),
                //不需要可以设置为0
                .setRetryCount(2)//网络不好自动重试2次
                //可以全局统一设置超时重试间隔时间,默认为500ms,不需要可以设置为0
                .setRetryDelay(100)//每次延时500ms重试
                //可以全局统一设置超时重试间隔叠加时间,默认为0ms不叠加
                .setRetryIncreaseDelay(100)//每次延时叠加500ms
                //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体请看CacheMode
                .setCacheMode(CacheMode.NO_CACHE)
                //可以全局统一设置缓存时间,默认永不过期
                .setCacheTime(-1)//-1表示永久缓存,单位:秒 ，Okhttp和自定义RxCache缓存都起作用
                //全局设置自定义缓存保存转换器，主要针对自定义RxCache缓存
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                //全局设置自定义缓存大小，默认50M
                .setCacheMaxSize(100 * 1024 * 1024)//设置缓存大小为100M
                //设置缓存版本，如果缓存有变化，修改版本后，缓存就不会被加载。特别是用于版本重大升级时缓存不能使用的情况
                .setCacheVersion(1)//缓存版本为1
                //.setHttpCache(new Cache())//设置Okhttp缓存，在缓存模式为DEFAULT才起作用
                //可以设置https的证书,以下几种方案根据需要自己设置
                .setCertificates();                                 //方法一：信任所有证书,不安全有风险
        //.setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
        //配置https的域名匹配规则，不需要就不要加入，使用不当会导致https握手失败
        //.setHostnameVerifier(new SafeHostnameVerifier(Url))//全局访问规则
        //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
        //.addCommonHeaders(headers)//设置全局公共头
        //.addCommonParams(params)//设置全局公共参数
        //.addNetworkInterceptor(new NoCacheInterceptor())//设置网络拦截器
        //.setCallFactory()//全局设置Retrofit对象Factory
        //.setCookieStore()//设置cookie
        //.setOkproxy()//设置全局代理
        //.setOkconnectionPool()//设置请求连接池
        //.setCallbackExecutor()//全局设置Retrofit callbackExecutor
        //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
        //.addInterceptor(new GzipRequestInterceptor())//开启post数据进行gzip后发送给服务器
        //.addInterceptor(new CustomSignInterceptor())//添加参数签名拦截器
        //.addInterceptor(new HeTInterceptor());//处理自己业务的拦截器
    }

    //public static Realm REALM_INSTANCE;
    //public static RealmConfiguration configuration;

    private void initRealm() {
        //https://www.jianshu.com/p/37af717761cc
        //https://www.jianshu.com/p/28912c2f31db
        //https://blog.csdn.net/QDJdeveloper/article/details/77848498?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.nonecase
        //https://blog.csdn.net/wangxw725/article/details/100171404
        //https://blog.csdn.net/u013651026/article/details/96479407
        //https://www.cnblogs.com/endv/p/12229594.html
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(RealmOperationHelper.DB_NAME)//数据库名字
                .schemaVersion(0)//指定数据库的版本号。 初建时 版本号为 0
                .migration(new Migration())//指定迁移操作的迁移类。
                .build();
        //REALM_INSTANCE = Realm.getInstance(configuration);
        Realm.setDefaultConfiguration(configuration);
    }

    public static TTAdManager ttAdManager;

    /**
     * 初始化 穿山甲 广告
     */
    private void initAD() {
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        ttAdManager = TTAdSdk.init(this,
                new TTAdConfig.Builder()
                        .appId("5089189")
                        .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                        .appName("头像制作神器_android")
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
                        .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                        .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                        /*
                         *如何配置下载类广告在点击后二次弹窗确认？
                      开发者可以在初始化的时候调用directDownloadNetworkType方法，不传递任何参数即可在所有网络下均有下载的二次弹窗。
                       * 此方法中传递参数即代表可以在某网络情况下点击直接下载，不用经过二次确认。
                         */
                        .directDownloadNetworkType()
                        .supportMultiProcess(true) //是否支持多进程，true支持
                        //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                        .build());
    }

    public static TTAdManager getTTAdManager() {
        return ttAdManager;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
