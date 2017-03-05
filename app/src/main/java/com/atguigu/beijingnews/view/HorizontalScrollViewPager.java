package com.atguigu.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by acer on 2017/3/3 9:46.
 * 作用：
 */
public class HorizontalScrollViewPager extends ViewPager
{
    public HorizontalScrollViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private float lastX, lastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {

        float eventX = ev.getX();
        float eventY = ev.getY();
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = eventX;
                lastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = eventX - lastX;
                float dy = eventY - lastY;
                if (Math.abs(dx) > Math.abs(dy))
                {
                    //X轴方向滑动
                    if(getCurrentItem()==0&&dx>0) {
                        //不请求
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    else if (getCurrentItem()==getAdapter().getCount()-1&&dx<0)

                    {
                        //最后一个从右向左滑动 不请求
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    else
                    {
                        //其他 请求父亲不要拦截事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                else
                {
                    //Y轴方向滑动 不请求
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
