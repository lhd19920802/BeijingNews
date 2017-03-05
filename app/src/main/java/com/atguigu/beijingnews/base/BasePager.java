package com.atguigu.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.R;

/**
 * Created by acer on 2017/3/1 16:54.
 * 作用：
 */
public class BasePager
{
    public Context context;
    public View rootView;
    public TextView tv_base_pager;
    public ImageView iv_title_button;
    public FrameLayout fl_base_pager;


    public BasePager(Context context)
    {
        this.context=context;
        rootView=initView();

    }
    //初始化data
    public View initView()
    {
        View view = View.inflate(context, R.layout.base_pager, null);
        tv_base_pager = (TextView) view.findViewById(R.id.tv_base_pager);
        iv_title_button = (ImageView) view.findViewById(R.id.iv_title_button);
        fl_base_pager = (FrameLayout) view.findViewById(R.id.fl_base_pager);


        return view;
    }
    //初始化数据
    public void initData()
    {
        iv_title_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//开关变换
            }
        });




    }


}
