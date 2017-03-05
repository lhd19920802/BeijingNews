package com.atguigu.beijingnews;

import android.app.Application;

import org.xutils.x;

/**
 * Created by acer on 2017/3/1 19:48.
 * 作用：
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
