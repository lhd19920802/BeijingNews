package com.atguigu.beijingnews.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.domain.NewsBean;
import com.atguigu.beijingnews.pager.NewsPager;
import com.atguigu.beijingnews.utils.DensityUtil;

import java.util.List;

/**
 * Created by acer on 2017/3/1 10:40.
 * 作用：
 */
public class LeftMenuFragment extends BaseFragment
{

    private ListView listView;
    private List<NewsBean.DataBean> data;

    private int selectPosition;
    private MyAdapter myAdapter;


    @Override
    public View initView()
    {
        listView = new ListView(context);
        listView.setBackgroundColor(Color.BLACK);
        listView.setPadding(0, DensityUtil.dip2px(context, 40), 0, 0);
        //选中效果不显示
        listView.setSelector(android.R.color.transparent);
        //        listView.setDividerHeight(1);
        //        listView.setDivider(new ColorDrawable(Color.RED));


        return listView;

    }

    @Override
    public void initData()
    {
        super.initData();

        Log.e("TAG", "左侧菜单的数据被初始化了");

    }

    public void setData(List<NewsBean.DataBean> data)
    {
        //设置适配器
        this.data = data;
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        //设置监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.e("TAG", "position==" + position);
                selectPosition = position;
                myAdapter.notifyDataSetChanged();

                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//开关变换


                switchPager(selectPosition);


            }
        });
    }

    private void switchPager(int position)
    {
        //显示对应页面
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment=mainActivity.getContentFragment();
        NewsPager newsPager=contentFragment.getNewsPager();
        newsPager.switchPager(selectPosition);
    }

    class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return data.size();
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
            TextView textView = (TextView) View.inflate(context, R.layout.textview_menu, null);
            textView.setText(data.get(position).getTitle());
            textView.setEnabled(selectPosition == position);



            return textView;
        }
    }
}
