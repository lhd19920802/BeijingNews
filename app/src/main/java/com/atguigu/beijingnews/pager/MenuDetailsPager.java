package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.view.View;

/**
 * Created by acer on 2017/3/2 9:12.
 * 作用：
 */
public abstract class MenuDetailsPager
{
    public Context context;
    public View rootView;

    public MenuDetailsPager(Context context)
    {
        this.context=context;
        rootView=initView();

    }
    //初始化data
    public abstract View initView();
    //初始化数据
    public void initData()
    {

    }
}
