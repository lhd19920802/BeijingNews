package com.atguigu.beijingnews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atguigu.beijingnews.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity
{
    private static final String TAG = GuideActivity.class.getSimpleName();
    private ViewPager vp_guide;
    private List<ImageView> imageViews = new ArrayList<>();
    private LinearLayout ll_guide;
    private ImageView iv_guide_red;

    //两点间距
    private int pointDistance=0;
    //红点移动的距离
    private int moveDistance;

    //屏幕适配 把dp转换成像素
    private int screenPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        screenPoint = DensityUtil.dip2px(this, 10);
        initView();
        showGuide();


    }

    private void showGuide()
    {
        //提供数据
        int[] datas = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        for (int i = 0; i < datas.length; i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(datas[i]);
            imageViews.add(imageView);
        }

        for (int i = 0; i < imageViews.size(); i++)
        {
            ImageView nomal_gray = new ImageView(this);
            nomal_gray.setBackgroundResource(R.drawable.nomal_shape);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenPoint, screenPoint);

            nomal_gray.setLayoutParams(params);

            if (i != 0)
            {
                params.leftMargin = screenPoint;
            }
            ll_guide.addView(nomal_gray);

        }


        //计算间距
        //View 从创建到显示过程中主要的方法，构造方法，onMeasure()-->onLayout()->onDraw()
        //onLayout()在这个阶段肯定还有距离

        iv_guide_red.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()

        {
            @Override
            public void onGlobalLayout()
            {
                //反注册观察者 防止多次打印pointDistance
//                iv_guide_red.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //得到间距
                pointDistance = ll_guide.getChildAt(1).getLeft() - ll_guide.getChildAt(0).getLeft();
                Log.e(TAG, "pointDistance:"+pointDistance);
            }
        });

        //创建适配器
        vp_guide.setAdapter(new GuideAdapter());

        //设置viewpager的监听

        vp_guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            /**
             *
             * @param position viewPager的位置
             * @param positionOffset 偏移的百分比
             * @param positionOffsetPixels 偏移的像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                //得到移动的距离  带移动过程
//                moveDistance = (int) (pointDistance * positionOffset+position*pointDistance);
                moveDistance = (int) (position*pointDistance);//不带移动过程

                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(screenPoint,screenPoint);
                //移动原理
                params.leftMargin = moveDistance;
                iv_guide_red.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }

    private void initView()
    {
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        ll_guide = (LinearLayout) findViewById(R.id.ll_guide);

        iv_guide_red = (ImageView)findViewById(R.id.iv_guide_red);
    }

    class GuideAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            //            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
