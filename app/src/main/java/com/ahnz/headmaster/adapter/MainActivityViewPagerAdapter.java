package com.ahnz.headmaster.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ahnz.headmaster.view.activity_main.FindFragment;
import com.ahnz.headmaster.view.activity_main.HomeFragment;
import com.ahnz.headmaster.view.activity_main.MakeFragment;
import com.ahnz.headmaster.view.activity_main.MyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xzb
 * @description: 主页 ViewPager 滑动适配器
 *
 * 适合于 Fragment数量不多的情况。当某个页面不可见时，该页面对应的View可能会被销毁，但是所有的Fragment都会一直存在于内存中。
 * FragmentStatePagerAdapter(碎片状态适配器)
 * 适合于Fragment数量较多的情况。当页面不可见时， 对应的Fragment实例可能会被销毁，但是Fragment的状态会被保存。
 * @date :2020/7/7 17:56
 */
public class MainActivityViewPagerAdapter extends FragmentPagerAdapter {
    private int size;

    private List<Fragment> fragments;

    public MainActivityViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.size = behavior;
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new FindFragment());
        fragments.add(new MakeFragment());
        fragments.add(new MyFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return size;
    }

    //https://blog.csdn.net/mr_liabill/article/details/48749807
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
    }
}
