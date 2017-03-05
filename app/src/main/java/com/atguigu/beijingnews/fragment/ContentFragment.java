package com.atguigu.beijingnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.pager.AffairPager;
import com.atguigu.beijingnews.pager.HomePager;
import com.atguigu.beijingnews.pager.NewsPager;
import com.atguigu.beijingnews.pager.ServicePager;
import com.atguigu.beijingnews.pager.SettingPager;
import com.atguigu.beijingnews.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/3/1 10:40.
 * 作用：
 */
public class ContentFragment extends BaseFragment
{
    @ViewInject(R.id.vp_content_fragment)
    public NoScrollViewPager vp_content_fragment;
    @ViewInject(R.id.rg_content_fragment)
    public RadioGroup rg_content_fragment;
    private List<BasePager> pagers;
    private ContentAdapter contentAdapter;

    @Override
    public View initView()
    {

        View view = View.inflate(context, R.layout.content_fragment, null);
        x.view().inject(this, view);
        return view;

    }

    @Override
    public void initData()
    {
        super.initData();
        Log.e("TAG", "主页面的数据被初始化了");
        //默认选中第一个
        rg_content_fragment.check(R.id.rb_home);
        //使主页不能往左滑动
        isEnableSlideMenu(false);


        //获取数据
        pagers = new ArrayList<>();
        pagers.add(new HomePager(context));
        pagers.add(new NewsPager(context));
        pagers.add(new ServicePager(context));
        pagers.add(new AffairPager(context));
        pagers.add(new SettingPager(context));
        contentAdapter = new ContentAdapter();
        vp_content_fragment.setAdapter(contentAdapter);
        //默认显示主页内容
        pagers.get(0).initData();
        //设置切换页面的监听
        rg_content_fragment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.rb_home:
                        vp_content_fragment.setCurrentItem(0, false);
                        isEnableSlideMenu(false);

                        break;
                    case R.id.rb_news:
                        isEnableSlideMenu(true);
                        vp_content_fragment.setCurrentItem(1, false);
                        break;
                    case R.id.rb_service:
                        vp_content_fragment.setCurrentItem(2, false);
                        isEnableSlideMenu(false);

                        break;
                    case R.id.rb_govaffair:
                        isEnableSlideMenu(false);

                        vp_content_fragment.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:

                        isEnableSlideMenu(false);
                        vp_content_fragment.setCurrentItem(4, false);
                        break;
                }
            }
        });
        //设置viewpager状态改变的监听
        vp_content_fragment.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                switch (position)

                {
                    case 0:
                        rg_content_fragment.check(R.id.rb_home);
                        break;
                    case 1:

                        rg_content_fragment.check(R.id.rb_news);
                        break;
                    case 2:

                        rg_content_fragment.check(R.id.rb_service);
                        break;
                    case 3:
                        rg_content_fragment.check(R.id.rb_govaffair);

                        break;
                    case 4:
                        rg_content_fragment.check(R.id.rb_setting);

                        break;
                }
                BasePager basePager = pagers.get(position);
                basePager.initData();

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

    }

    private void isEnableSlideMenu(boolean slide)

    {
        MainActivity mainActivity= (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if(slide) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }
        else
        {
            //设置为不可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }



    }

    public NewsPager getNewsPager()
    {

        return (NewsPager) pagers.get(1);

    }

    class ContentAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootView;

            //屏蔽数据预加载 不要在此处初始化数据
            //            basePager.initData();
            container.addView(rootView);

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            //            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

    }
}
