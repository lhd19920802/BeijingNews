package com.atguigu.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by acer on 2017/3/1 9:23.
 * 作用：数据缓存工具类
 */
public class CacheUtils
{

    /*
    保存数据
     */
    public static void putBoolean(Context context, String key, boolean value)
    {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取数据
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    /**
     * 保存string数据
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value)
    {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        return sp.getString(key,"");

    }
}
