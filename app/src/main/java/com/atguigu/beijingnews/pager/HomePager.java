package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnews.base.BasePager;

/**
 * Created by acer on 2017/3/1 18:01.
 * 作用：首页
 */
public class HomePager extends BasePager
{
    public HomePager(Context context)
    {
        super(context);
    }

    @Override
    public void initData()
    {
        super.initData();
        tv_base_pager.setText("首页");
        TextView textView = new TextView(context);
        textView.setText("首页的内容...");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        fl_base_pager.addView(textView);

    }
}
