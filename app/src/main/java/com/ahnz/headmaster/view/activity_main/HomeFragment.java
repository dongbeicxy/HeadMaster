package com.ahnz.headmaster.view.activity_main;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.viewpager.widget.ViewPager;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.adapter.HomeFragmentViewPagerAdapter;
import com.ahnz.headmaster.view.base.LazyFragment;
import com.ahnz.headmaster.widget.ScaleTransitionPagerTitleView;
import com.gyf.immersionbar.ImmersionBar;
import com.renj.pagestatuscontroller.annotation.RPageStatus;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * @author xzb
 * @description:首页
 * @date :2020/7/8 9:13
 */
public class HomeFragment extends LazyFragment {
    private static final String[] CHANNELS = new String[]{"最新精品", "火爆人气",};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected int getContentViewId() {
        Log.e("Lazy", "首页加载布局");
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        Log.e("Lazy", "首页加载数据");
    }

    @Override
    protected void initEvent() {
        pageStatusController.changePageStatus(RPageStatus.CONTENT);
        //页卡对应内容
        viewPager.setAdapter(new HomeFragmentViewPagerAdapter(getActivity().getSupportFragmentManager(), mDataList.size()));
        //指示器 白色背景
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setScrollPivotX(0.8f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setTextSize(20);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setLineHeight(6.0f);
                indicator.setRoundRadius(0f);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.RED);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).fitsSystemWindows(true).init();
    }

}
