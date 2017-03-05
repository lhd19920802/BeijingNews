package com.atguigu.beijingnews.pager.menupager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.pager.MenuDetailsPager;

/**
 * Created by acer on 2017/3/2 9:20.
 * 作用：
 */
public class PicMenuDetail extends MenuDetailsPager
{
    private TextView textView;
    public PicMenuDetail(Context context)
    {
        super(context);

    }

    @Override
    public View initView()
    {

        textView = new TextView(context);

        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;

    }

    @Override
    public void initData()
    {
        super.initData();
        textView.setText("图组详情的内容...");

    }
}
