package com.atguigu.beijingnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atguigu.beijingnews.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by acer on 2017/3/3 15:26.
 * 作用：
 */
public class RefreshListView extends ListView
{

    private LinearLayout ll_refresh_header;
    /**
     * 下拉刷新空间和viewpager的整体
     */
    private LinearLayout topnews;
    /**
     * 下拉刷新控件的高度
     */
    private int viewHeight;
    private int measuredHeight;
    /**
     * 顶部轮播图部分
     */
    private View topViewPager;

    /**
     * 下拉刷新状态
     */
    public static final int PULL_DOWN_REFRESH = 0;


    /**
     * 手松刷新状态
     */
    public static final int RELEASE_REFRESH = 1;


    /**
     * 正在刷新状态
     */
    public static final int REFRESHING = 2;

    /**
     * 默认为下拉刷新状态
     */
    private int currentStatus = PULL_DOWN_REFRESH;
    private float distance;

    private ImageView iv_refresh_header;
    private ProgressBar pb_refresh_header;
    private TextView tv_refresh_header;

    private TextView tv_refresh_date;
    private RotateAnimation downAnimation;
    private RotateAnimation upAnimation;

    private LinearLayout ll_refresh_foot;
    private View footView;
    private boolean isLoadMore=false;
    private int footViewHeight;

    public RefreshListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
        initAnimation();

    }

    private void initAnimation()
    {
        downAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(200);
        downAnimation.setFillAfter(true);

        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                .RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(200);
        upAnimation.setFillAfter(true);
    }


    private void updateRefreshStatus()
    {
        switch (currentStatus)
        {
            case PULL_DOWN_REFRESH:
                iv_refresh_header.startAnimation(downAnimation);
                pb_refresh_header.setVisibility(View.GONE);
                tv_refresh_header.setText("下拉刷新");
                break;
            case RELEASE_REFRESH:
                iv_refresh_header.startAnimation(upAnimation);
                tv_refresh_header.setText("手松刷新");
                break;
            case REFRESHING:
                iv_refresh_header.clearAnimation();
                iv_refresh_header.setVisibility(View.GONE);
                pb_refresh_header.setVisibility(View.VISIBLE);
                tv_refresh_header.setText("正在刷新");
                ll_refresh_header.setPadding(0, 0, 0, 0);
                break;
        }
    }

    private void initView(Context context)
    {

        topnews = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        ll_refresh_header = (LinearLayout) topnews.findViewById(R.id.ll_refresh_header);
        iv_refresh_header = (ImageView) topnews.findViewById(R.id.iv_refresh_header);
        pb_refresh_header = (ProgressBar) topnews.findViewById(R.id.pb_refresh_header);
        tv_refresh_header = (TextView) topnews.findViewById(R.id.tv_refresh_header);
        tv_refresh_date = (TextView) topnews.findViewById(R.id.tv_refresh_date);
        //测量以后才能得到宽高
        ll_refresh_header.measure(0, 0);
        measuredHeight = ll_refresh_header.getMeasuredHeight();
        //隐藏
        ll_refresh_header.setPadding(0, -measuredHeight, 0, 0);
        //加载更多

        footView = View.inflate(context, R.layout.refresh_foot, null);
        //设置默认为隐藏
        ll_refresh_foot = (LinearLayout) footView.findViewById(R.id.ll_refresh_foot);
        this.addHeaderView(topnews);
        this.addFooterView(footView);

        ll_refresh_foot.measure(0, 0);
        footViewHeight = ll_refresh_foot.getMeasuredHeight();
        //设置默认为隐藏
        footView.setPadding(0, -footViewHeight, 0, 0);

        setOnScrollListener(new MyOnScrollListener());
    }

    class MyOnScrollListener implements OnScrollListener
    {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState)
        {
            //当惯性滚动或者静止并且滑动到最后一个时候
            if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING)
            {
                if (getAdapter().getCount() - 1 == getLastVisiblePosition())
                {
                    if(onRefreshListener!=null) {

                        onRefreshListener.onLoadMore();
                    }

                    //显示加载更多的控件
                    footView.setPadding(0,0,0,0);

                    //设置状态
                    isLoadMore = true;
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
                totalItemCount)
        {

        }
    }

    //下拉刷新动态隐藏显示

    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        float eventY = ev.getY();
        switch (ev.getAction())
        {

            case MotionEvent.ACTION_DOWN:
                lastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentStatus == REFRESHING)
                {
                    break;
                }
                //解决下拉刷新的bug
                if (!isTotalShowing())
                {
                    break;
                }
                float dy = eventY - lastY;
                if (dy > 0)
                {
                    distance = dy - measuredHeight;

                    /**
                     * 初始化下拉刷新的状态
                     */
                    if (distance >= 0 && currentStatus != RELEASE_REFRESH)
                    {
                        //松开刷新
                        currentStatus = RELEASE_REFRESH;
                        updateRefreshStatus();
                        Log.e("TAG", "手松刷新");
                    }
                    else if (distance < 0 && currentStatus != PULL_DOWN_REFRESH)
                    {
                        //下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        updateRefreshStatus();
                        Log.e("TAG", "下拉刷新");
                    }

                    ll_refresh_header.setPadding(0, (int) distance, 0, 0);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (currentStatus == PULL_DOWN_REFRESH)
                {

                    ll_refresh_header.setPadding(0, -measuredHeight, 0, 0);
                }
                else if (currentStatus == RELEASE_REFRESH)
                {
                    currentStatus = REFRESHING;
                    updateRefreshStatus();

                    //回调接口请求网络
                    if (onRefreshListener != null)
                    {
                        onRefreshListener.onRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    public void finishRefresh(boolean isSuccess)
    {
        if(isLoadMore) {
            isLoadMore=false;
            footView.setPadding(0,-viewHeight,0,0);
        }
        else
        {
            iv_refresh_header.clearAnimation();
            iv_refresh_header.setVisibility(View.VISIBLE);
            pb_refresh_header.setVisibility(View.GONE);

            currentStatus = PULL_DOWN_REFRESH;
            tv_refresh_header.setText("下拉刷新");
            ll_refresh_header.setPadding(0, -measuredHeight, 0, 0);
            if (isSuccess)
            {
                tv_refresh_date.setText(getSystemTime());
            }
        }

    }

    private String getSystemTime()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public interface OnRefreshListener
    {
        public void onRefresh();

        //加载更多
        public void onLoadMore();
    }

    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener)
    {
        this.onRefreshListener = onRefreshListener;
    }

    //顶部轮播图部分 即viewpager
    public void addToRefreshListView(View headView)
    {
        this.topViewPager = headView;

        topnews.addView(headView);
    }

    //判断顶部轮播图是否完全显示
    public boolean isTotalShowing()
    {
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        int listViewY = location[1];
        topViewPager.getLocationOnScreen(location);
        int viewPagerY = location[1];
        return listViewY <= viewPagerY;
    }
}
