package com.atguigu.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by acer on 2017/3/1 18:34.
 * 作用：自定义viewPager 屏蔽viwpager的滑动效果
 */
public class NoScrollViewPager extends ViewPager
{
    public NoScrollViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
//        return super.onTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return false;

    }
}
