package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.domain.NewsBean;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.pager.menupager.ContactMenuDetail;
import com.atguigu.beijingnews.pager.menupager.NewsMenuDetail;
import com.atguigu.beijingnews.pager.menupager.PicMenuDetail;
import com.atguigu.beijingnews.pager.menupager.TopicMenuDetail;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.UrlUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/3/1 18:01.
 * 作用：首页
 */
public class NewsPager extends BasePager
{

    public static final String TAG = NewsPager.class.getSimpleName();

    public List<MenuDetailsPager> menuDetailsPagers;
    private List<NewsBean.DataBean> data;

    public NewsPager(Context context)
    {
        super(context);

    }


    @Override
    public void initData()
    {
        super.initData();
        tv_base_pager.setText("新闻");
        iv_title_button.setVisibility(View.VISIBLE);
        TextView textView = new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        fl_base_pager.addView(textView);


        //联网之前判断是否有缓存
        String saveJson = CacheUtils.getString(context, UrlUtils.CATE_URL);
        if (!TextUtils.isEmpty(saveJson))
        {
            processData(saveJson);
        }
        //        getDataFromNet();

        getDataFromNetByVollay();
    }

    private void getDataFromNetByVollay()
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, UrlUtils.CATE_URL, new
                Response.Listener<String>()
        {
            @Override
            public void onResponse(String result)
            {
                Log.e(TAG, "result:" + result);
                //缓存数据
                CacheUtils.putString(context, UrlUtils.CATE_URL, result);
                Log.e("TAG", "缓存数据成功==");
                processData(result);


                switchPager(0);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "error:" + error);
            }
        })
        {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response)
            {
                try
                {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));

                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };

        queue.add(request);
    }

    private void getDataFromNet()
    {
        RequestParams params = new RequestParams(UrlUtils.CATE_URL);
        x.http().get(params, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.e(TAG, "result:" + result);
                //缓存数据
                CacheUtils.putString(context, UrlUtils.CATE_URL, result);
                Log.e("TAG", "缓存数据成功==");
                processData(result);


                switchPager(0);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {

                Log.e(TAG, "error:" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex)
            {

                Log.e(TAG, "result:" + cex.getMessage());
            }

            @Override
            public void onFinished()
            {
                Log.e(TAG, "onFinished:");

            }
        });

    }

    /**
     * 解析数据
     *
     * @param result
     */
    private void processData(String result)
    {
        //解析数据
        NewsBean newsBean = new Gson().fromJson(result, NewsBean.class);
        Log.e("TAG", "gson解析数据成功==" + newsBean.getData().get(0).getTitle());
        //把数据传递到menufragment中
        data = newsBean.getData();
        MainActivity mainActivity = (MainActivity) context;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenu();
        leftMenuFragment.setData(data);

        menuDetailsPagers = new ArrayList<>();
        menuDetailsPagers.add(new NewsMenuDetail(context, data.get(0).getChildren()));
        menuDetailsPagers.add(new TopicMenuDetail(context));
        menuDetailsPagers.add(new PicMenuDetail(context));
        menuDetailsPagers.add(new ContactMenuDetail(context));
    }


    //根据指定的位置切换到不同的页面
    public void switchPager(final int position)
    {
        //设置标题
        tv_base_pager.setText(data.get(position).getTitle());
        MenuDetailsPager menuDetailsPager = menuDetailsPagers.get(position);
        View rootView = menuDetailsPager.rootView;
        menuDetailsPager.initData();

        fl_base_pager.removeAllViews();

        fl_base_pager.addView(rootView);
        if (position == 2)
        {
            iv_title_switch.setVisibility(View.VISIBLE);
        }
        else
        {
            iv_title_switch.setVisibility(View.GONE);
        }
    }
}
