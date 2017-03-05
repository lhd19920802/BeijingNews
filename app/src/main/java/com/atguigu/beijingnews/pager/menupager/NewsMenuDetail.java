package com.atguigu.beijingnews.pager.menupager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.domain.NewsBean;
import com.atguigu.beijingnews.pager.MenuDetailsPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/3/2 9:20.
 * 作用：
 */
public class NewsMenuDetail extends MenuDetailsPager
{
    //得到页签页面的数据
    private final List<NewsBean.DataBean.ChildrenBean> children;
    //得到页签页面
    private List<MenuDetailsPager> tabDetailsPagers;
    private ViewPager vp_news_details;
    private MyAdapter adapter;
    private TabPageIndicator tpi_news_details;
    private ImageButton ib_news_details;

    public NewsMenuDetail(Context context, List<NewsBean.DataBean.ChildrenBean> children)
    {
        super(context);
        this.children=children;
    }

    @Override
    public View initView()
    {
        View view = View.inflate(context, R.layout.news_menu_details, null);
        vp_news_details = (ViewPager) view.findViewById(R.id.vp_news_details);
        tpi_news_details = (TabPageIndicator) view.findViewById(R.id.tpi_news_details);
        ib_news_details = (ImageButton) view.findViewById(R.id.ib_news_details);
        ib_news_details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                vp_news_details.setCurrentItem(vp_news_details.getCurrentItem()+1);
            }
        });
        return view;

    }

    @Override
    public void initData()
    {
        super.initData();
        tabDetailsPagers = new ArrayList<>();
        //根据数据个数创建页面
        for (int i = 0; i < children.size(); i++)
        {
            tabDetailsPagers.add(new TabDetailsPager(context,children.get(i)));
        }
        //设置适配器
        adapter = new MyAdapter();
        vp_news_details.setAdapter(adapter);
        //和viewpager进行关联
        tpi_news_details.setViewPager(vp_news_details);

        tpi_news_details.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if (position == 0)
                {
                    //slidingmenu可以滑动
                    isEnableSlideMenu(true);

                }
                else
                {
                    //不能滑动
                    isEnableSlideMenu(false);
                }
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
    class MyAdapter extends PagerAdapter
    {
        @Override
        public CharSequence getPageTitle(int position)
        {
            return children.get(position).getTitle();

        }

        @Override
        public int getCount()
        {
            return tabDetailsPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            MenuDetailsPager menuDetailsPager = tabDetailsPagers.get(position);
            View rootView = menuDetailsPager.rootView;
            menuDetailsPager.initData();
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
