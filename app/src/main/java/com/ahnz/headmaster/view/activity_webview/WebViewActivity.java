package com.ahnz.headmaster.view.activity_webview;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ahnz.headmaster.R;
import com.gyf.immersionbar.ImmersionBar;
import com.just.agentweb.AgentWeb;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xzb
 * @description: 网页展示 Activity
 * @date :2020/7/30 17:12
 */
public class WebViewActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //标题
    @BindView(R.id.title_tv)
    TextView title_tv;

    private AgentWeb mAgentWeb;

    public static final String URL = "url";
    public static final String TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).fitsSystemWindows(true).init();
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        toolbar.setTitle("");
        //设置导航图标要在setSupportActionBar方法之后
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_dark_24dp);

        title_tv.setText(getIntent().getStringExtra(TITLE));
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) findViewById(R.id.content_layout), new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(getIntent().getStringExtra(URL));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (null != mAgentWeb && null != mAgentWeb.getWebLifeCycle()) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (null != mAgentWeb && null != mAgentWeb.getWebLifeCycle()) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (null != mAgentWeb && null != mAgentWeb.getWebLifeCycle()) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }
}
