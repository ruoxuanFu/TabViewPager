package com.quaie.wms.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.quaie.wms.myapplication.Fragment.VpSimpleFragment;
import com.quaie.wms.myapplication.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//自定义viewpager联动效果2，不在xml文本中设置text，直接用代码创建tab
public class Main2Activity extends FragmentActivity {

    private ViewPager mViewPager;
    private ViewPagerIndicator mVpIndicator;

    //内容区域标题
    private List<String> mTitles = Arrays.asList("短信", "收藏", "推荐", "短信1", "收藏1", "推荐1", "短信2", "收藏2", "推荐2");

    //内容显示区域的fragment
    private List<VpSimpleFragment> mContents = new ArrayList<VpSimpleFragment>();

    //ViewPager适配器
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
        initData();

        //给ViewPager设置adapter
        mViewPager.setAdapter(mAdapter);
        //设置viewpager设置三角形滑动
        mVpIndicator.setViewPager(mViewPager, 0);
    }

    private void initData() {

        //给mContents赋值
        for (String title : mTitles) {
            VpSimpleFragment vpSimpleFragment = VpSimpleFragment.newInstance(title);
            mContents.add(vpSimpleFragment);
        }

        //初始化ViewPager的Adapter
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }

            @Override
            public int getCount() {
                return mContents.size();
            }
        };

        //给tab的title设置文本
        mVpIndicator.setTabItemTitles(mTitles);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewPager_main);
        mVpIndicator = (ViewPagerIndicator) findViewById(R.id.id_vpIndicator_main);

        //指定可显示的tab数量
        mVpIndicator.setmTabVisibleCount(5);
    }
}
