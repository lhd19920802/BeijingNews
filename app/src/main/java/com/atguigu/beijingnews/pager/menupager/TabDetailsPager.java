package com.atguigu.beijingnews.pager.menupager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.beijingnews.NewsDetailsActivity;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.domain.NewsBean;
import com.atguigu.beijingnews.domain.TabDetailsBean;
import com.atguigu.beijingnews.pager.MenuDetailsPager;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.DensityUtil;
import com.atguigu.beijingnews.utils.UrlUtils;
import com.atguigu.beijingnews.view.RefreshListView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by acer on 2017/3/2 12:02.
 * 作用：
 */
public class TabDetailsPager extends MenuDetailsPager
{
    public static final String GRAY_ARRAY = "gray_array";
    //页签页面的数据
    private final NewsBean.DataBean.ChildrenBean childrenBean;
    private ViewPager vp_tab_details;
    private TabDetailsBean tabDetailsBean;
    private List<TabDetailsBean.DataBean.TopnewsBean> topnews;
    private LinearLayout ll_tab_details;
    private TextView tv_tab_details;

    private int prePosition;
    private ImageView point;
    private RefreshListView lv_tab_details;

    //加载更多的链接
    private String moreUrl;

    //是否加载更多
    private boolean loadMore = false;
    /**
     * 列表新闻的数据
     */
    private List<TabDetailsBean.DataBean.NewsBean> news;
    private String url;
    private MyAdapter adapter;

    public TabDetailsPager(Context context, NewsBean.DataBean.ChildrenBean childrenBean)
    {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView()
    {
        View view = View.inflate(context, R.layout.tab_details_pager, null);
        View headView = View.inflate(context, R.layout.tab_details_head, null);


        vp_tab_details = (ViewPager) headView.findViewById(R.id.vp_tab_details);
        ll_tab_details = (LinearLayout) headView.findViewById(R.id.ll_tab_details);
        tv_tab_details = (TextView) headView.findViewById(R.id.tv_tab_details);
        lv_tab_details = (RefreshListView) view.findViewById(R.id.lv_tab_details);

        //        lv_tab_details.addHeaderView(headView);

        //把headView传到自定义listView里面 使headView和上面的刷新部分称为一个整体
        lv_tab_details.addToRefreshListView(headView);
        /**
         * 设置页面刷新的监听
         *
         */
        lv_tab_details.setOnRefreshListener(new RefreshListView.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getDataFromNet();
            }

            @Override
            public void onLoadMore()
            {
                if (TextUtils.isEmpty(moreUrl))
                {
                    loadMore = false;
                    lv_tab_details.finishRefresh(false);
                }
                else
                {
                    getMoreDateFromNet();

                }

            }
        });

        //设置点击某一条listView后变灰
        lv_tab_details.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String grayArray = CacheUtils.getString(context, GRAY_ARRAY);
                int newsId = news.get(position - 1).getId();
                if (TextUtils.isEmpty(grayArray) || !grayArray.contains(newsId + ""))
                {
                    //没有保存过
                    //保存id
                    CacheUtils.putString(context, GRAY_ARRAY, grayArray + newsId + ",");

                    adapter.notifyDataSetChanged();
                }

                //开启新闻详情页面
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("url", news.get(position - 1).getUrl());
                context.startActivity(intent);

            }


        });


        return view;

    }

    private void getMoreDateFromNet()
    {
        //请求数据
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(3000);
        x.http().get(params, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e("TAG", "数据请求成功==" + result);
                //缓存数据
                CacheUtils.putString(context, url, result);
                //加载更多请求成功
                loadMore = true;
                //解析并显示数据
                processData(result);
                lv_tab_details.finishRefresh(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                Log.e("TAG", "数据请求失败==" + ex.getMessage());
                lv_tab_details.finishRefresh(true);
            }

            @Override
            public void onCancelled(CancelledException cex)
            {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished()
            {
                Log.e("TAG", "onFinished==");
            }
        });
    }


    @Override
    public void initData()
    {
        super.initData();
        url = UrlUtils.BASE_URL + childrenBean.getUrl();
        Log.e("TAG", url);

        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson))
        {
            processData(saveJson);
        }
        getDataFromNet();


    }

    private void processData(String json)
    {
        tabDetailsBean = parseData(json);
        topnews = tabDetailsBean.getData().getTopnews();
        news = tabDetailsBean.getData().getNews();


        //加载更多的数据
        String moreData = tabDetailsBean.getData().getMore();
        if (TextUtils.isEmpty(moreData))
        {
            moreUrl = "";
        }
        else
        {
            moreUrl = UrlUtils.BASE_URL + moreData;
        }

        if (!loadMore)
        {
            Log.e("TAG", "题目====" + topnews.get(0).getTitle());
            //设置适配器
            vp_tab_details.setAdapter(new MyPagerAdapter());


            //processData被调用两次 点数被创建了两次 所以要移除
            ll_tab_details.removeAllViews();
            //设置红点
            setPoint();
            tv_tab_details.setText(topnews.get(0).getTitle());
            //监听viewpager的页面变化
            vp_tab_details.setOnPageChangeListener(new onPageChangeListener());

            //设置列表新闻的适配器
            adapter = new MyAdapter();
            lv_tab_details.setAdapter(adapter);
        }
        else
        {
            loadMore = false;
            //加载更多
            List<TabDetailsBean.DataBean.NewsBean> moreNews = tabDetailsBean.getData().getNews();
            news.addAll(moreNews);
            adapter.notifyDataSetChanged();
        }


    }

    class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return news.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder;
            if (convertView == null)
            {
                convertView = View.inflate(context, R.layout.tab_item_news, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_item_icon = (ImageView) convertView.findViewById(R.id.iv_item_icon);
                viewHolder.tv_item_title = (TextView) convertView.findViewById(R.id.tv_item_title);
                viewHolder.tv_item_date = (TextView) convertView.findViewById(R.id.tv_item_date);

                convertView.setTag(viewHolder);

            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TabDetailsBean.DataBean.NewsBean newsBean = news.get(position);
            //请求图片
            Glide.with(context).load(newsBean.getListimage()).into(viewHolder.iv_item_icon);
            //            x.image().bind(viewHolder.iv_item_icon,newsBean.getListimage());
            viewHolder.tv_item_title.setText(newsBean.getTitle());
            viewHolder.tv_item_date.setText(newsBean.getPubdate());


            //设置点击后的某一条变灰
            String grayArray = CacheUtils.getString(context, GRAY_ARRAY);
            if (grayArray.contains(newsBean.getId() + ""))
            {
                //变成灰色
                viewHolder.tv_item_title.setTextColor(Color.GRAY);
            }
            else
            {
                //黑色
                viewHolder.tv_item_title.setTextColor(Color.BLACK);
            }

            return convertView;
        }

        class ViewHolder
        {
            ImageView iv_item_icon;
            TextView tv_item_title;
            TextView tv_item_date;
        }
    }

    class onPageChangeListener implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            tv_tab_details.setText(topnews.get(position).getTitle());
            ll_tab_details.getChildAt(prePosition).setEnabled(false);
            ll_tab_details.getChildAt(position).setEnabled(true);
            prePosition = position;

        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

    private void setPoint()
    {
        for (int i = 0; i < topnews.size(); i++)
        {
            point = new ImageView(context);
            point.setBackgroundResource(R.drawable.point_selector);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px
                    (context, 8), DensityUtil.dip2px(context, 8));
            point.setLayoutParams(params);

            if (i == 0)
            {
                point.setEnabled(true);
            }
            else
            {
                point.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(context, 8);
            }

            ll_tab_details.addView(point);

        }
    }

    class MyPagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView imageView = new ImageView(context);
            //                imageView.setBackgroundResource(R.drawable.home_scroll_default);
            container.addView(imageView);
            //请求图片
            x.image().bind(imageView, topnews.get(position).getTopimage());
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }

    private TabDetailsBean parseData(String json)
    {
        return new Gson().fromJson(json, TabDetailsBean.class);

    }

    private void getDataFromNet()
    {
        //请求数据
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(3000);
        x.http().get(params, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e("TAG", "数据请求成功==" + result);
                //缓存数据
                CacheUtils.putString(context, url, result);
                //解析并显示数据
                processData(result);
                lv_tab_details.finishRefresh(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                Log.e("TAG", "数据请求失败==" + ex.getMessage());
                lv_tab_details.finishRefresh(true);
            }

            @Override
            public void onCancelled(CancelledException cex)
            {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished()
            {
                Log.e("TAG", "onFinished==");
            }
        });
    }
}
