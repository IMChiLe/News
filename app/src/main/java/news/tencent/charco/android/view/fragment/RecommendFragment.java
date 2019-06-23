package news.tencent.charco.android.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import news.tencent.charco.android.R;
import news.tencent.charco.android.base.BaseFragment;
import news.tencent.charco.android.utils.UIUtils;
import news.tencent.charco.android.view.adapter.BaseFragmentAdapter;
import news.tencent.charco.android.widget.magicindicator.MagicIndicator;
import news.tencent.charco.android.widget.magicindicator.buildins.UIUtil;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.indicators.TestPagerIndicator;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import news.tencent.charco.android.widget.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

/**
 * Created 2019/6/14
 * Author: Lunatic Princess
 * Version:V1.0
 * ----------------------------------------------------
 * 文件描述：一、这个fragment碎片是News底层的ToolBar里的推荐（热推字样）模块，点击后滑动进入这个碎片内。
 *           此方法描述了推荐碎片内的顶层的MagicIndicator指示器，即关注，推荐，热榜这三个碎片的推荐碎片内的滑动。
 * ----------------------------------------------------
 */

public class RecommendFragment extends BaseFragment {

    private ViewPager mViewPager;
    private MagicIndicator mIndicator;
    private String[] recommends ;
    private List<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initView(View rootView) {
        mViewPager = findViewById(R.id.viewpager);
        mIndicator= findViewById(R.id.magic_indicator);
    }

//    /**
//     * Fragment中初始化Toolbar
//     * @param toolbar
//     * @param title 标题
//     * @param isDisplayHomeAsUp 是否显示返回箭头
//     */
//    public void initToolbar(Toolbar toolbar, String title, boolean isDisplayHomeAsUp) {
//        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
//        appCompatActivity.setSupportActionBar(toolbar);
//        ActionBar actionBar = appCompatActivity.getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(title);
//            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUp);
//        }
//    }

    /**
     * 加载链条数据指示器
     * */
    @Override
    protected void loadData() {
        recommends = getResources().getStringArray(R.array.recommend);
        initIndicator();
        for (int i=0;i<recommends.length;i++){
            Bundle args = new Bundle();
            args.putString("key",i+"");
            RecommendListFragment f = RecommendListFragment.newInstance(args);
            RecommendListFragment_2 fragment_2 = RecommendListFragment_2.newInstance(args);
            RecommendListFragment_3 fragment_3 = RecommendListFragment_3.newInstance(args);
            f.setKey(i);
            fragments.add(f);
            fragments.add(fragment_2);
            fragments.add(fragment_3);
        }
        mViewPager.setAdapter(new BaseFragmentAdapter(fragments,getChildFragmentManager()));
    }

    @Override
    public void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    private void initIndicator(){
        CommonNavigator mCommonNavigator = new CommonNavigator(UIUtils.getContext());
        mCommonNavigator.setSkimOver(true);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return recommends.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(recommends[index]);
                clipPagerTitleView.setTextSize(UIUtils.sp2px(16));      //UI工具绘制计算出每次移动的距离
                clipPagerTitleView.setClipColor(getResources().getColor(R.color.color_BDBDBD));
                clipPagerTitleView.setTextColor(getResources().getColor(R.color.colorPrimary));
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                indicator.setColors(R.color.colorPrimary);
                indicator.setMaxCircleRadius(11);
                indicator.setMinCircleRadius(7);
                indicator.setYOffset(122);
                return indicator;
            }
        });
        mIndicator.setNavigator(mCommonNavigator);
    }
}
