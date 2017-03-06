package com.atguigu.beijingnews.pager.menupager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.domain.PhotosBean;
import com.atguigu.beijingnews.pager.MenuDetailsPager;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.UrlUtils;
import com.google.gson.Gson;

import org.xutils.x;

import java.io.UnsupportedEncodingException;

/**
 * Created by acer on 2017/3/2 9:20.
 * 作用：图组页面
 */
public class PicMenuDetail extends MenuDetailsPager
{
    private PhotosBean.DataBean data;
    private ListView lv_photos_details;
    private GridView gv_photos_details;

    public PicMenuDetail(Context context)
    {
        super(context);

    }

    @Override
    public View initView()
    {
        View view = View.inflate(context, R.layout.photos_details, null);
        lv_photos_details = (ListView) view.findViewById(R.id.lv_photos_details);
        gv_photos_details = (GridView) view.findViewById(R.id.gv_photos_details);



        return view;

    }
    class MyListAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return data.getNews().size();
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
                convertView = View.inflate(context, R.layout.item_photos, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_item_photo = (ImageView) convertView.findViewById(R.id.iv_item_photo);
                viewHolder.tv_item_photo = (TextView) convertView.findViewById(R.id.tv_item_photo);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            PhotosBean.DataBean.NewsBean newsBean = data.getNews().get(position);
            x.image().bind(viewHolder.iv_item_photo,newsBean.getListimage());
            viewHolder.tv_item_photo.setText(newsBean.getTitle());
            return convertView;
        }

        class ViewHolder
        {
            ImageView iv_item_photo;
            TextView tv_item_photo;
        }
    }

    @Override
    public void initData()
    {
        super.initData();

        //得到缓存
        String saveJson = CacheUtils.getString(context, UrlUtils.PHOTOS_URL);
        if (!TextUtils.isEmpty(saveJson))
        {
            //有缓存直接解析 不再从网络获取
            processData(saveJson);
        }
        getDataFromNet();
    }

    private void getDataFromNet()
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        final StringRequest request = new StringRequest(UrlUtils.PHOTOS_URL, new Response
                .Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e("TAG", "数据获取成功=========" + response);
                CacheUtils.putString(context, UrlUtils.PHOTOS_URL, response);
                processData(response);


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("TAG", "数据获取失败=============" + error);
            }
        }){
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

    private void processData(String json)
    {
        PhotosBean photosBean=parseData(json);
        String title = photosBean.getData().getNews().get(0).getTitle();
        data = photosBean.getData();

        lv_photos_details.setAdapter(new MyListAdapter());
        Log.e("TAG", "title========="+title);
    }

    /**
     * 解析json
     * @param json
     * @return
     */
    private PhotosBean parseData(String json)
    {

        return new Gson().fromJson(json,PhotosBean.class);
    }
}
