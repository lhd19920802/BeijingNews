package com.atguigu.beijingnews;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.atguigu.beijingnews.fragment.ContentFragment;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //设置左侧菜单页面
        setBehindContentView(R.layout.left_page);

        //设置滑动右侧页面
        SlidingMenu slidingMenu = getSlidingMenu();
//        slidingMenu.setSecondaryMenu(R.layout.right_page);


        //设置滑动模式
        slidingMenu.setMode(SlidingMenu.LEFT);

        //设置滑动区域
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //设置主页占200dp
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this, 200));

        //初始化fragment
        initFragment();
    }

    private void initFragment()
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_main, new ContentFragment(), "content_fragment");
        transaction.replace(R.id.fl_left, new LeftMenuFragment(), "left_fragment");

        transaction.commit();
    }


    public LeftMenuFragment getLeftMenu()
    {
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag("left_fragment");

    }

    public ContentFragment getContentFragment()
    {

        return (ContentFragment) getSupportFragmentManager().findFragmentByTag("content_fragment");

    }
}
