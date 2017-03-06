package com.atguigu.beijingnews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsDetailsActivity extends Activity implements View.OnClickListener
{
    private TextView tvBasePager;
    private ImageView ivTitleButton;
    private ImageButton ivTitleBack;
    private ImageButton ivTitleTextSize;
    private ImageButton ivTitleShare;
    private ProgressBar pb_news_details;
    private WebView wv_news_details;
    private int position=2;
    private WebSettings settings;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-03-05 11:25:31 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews()
    {
        tvBasePager = (TextView) findViewById(R.id.tv_base_pager);
        ivTitleButton = (ImageView) findViewById(R.id.iv_title_button);
        ivTitleBack = (ImageButton) findViewById(R.id.iv_title_back);
        ivTitleTextSize = (ImageButton) findViewById(R.id.iv_title_textSize);
        ivTitleShare = (ImageButton) findViewById(R.id.iv_title_share);
        pb_news_details = (ProgressBar) findViewById(R.id.pb_news_details);
        wv_news_details = (WebView) findViewById(R.id.wv_news_details);


        tvBasePager.setVisibility(View.GONE);

        ivTitleBack.setVisibility(View.VISIBLE);
        ivTitleTextSize.setVisibility(View.VISIBLE);
        ivTitleShare.setVisibility(View.VISIBLE);

        ivTitleBack.setOnClickListener(this);
        ivTitleTextSize.setOnClickListener(this);
        ivTitleShare.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-03-05 11:25:31 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v)
    {
        if (v == ivTitleBack)
        {
            // Handle clicks for ivTitleBack
        }
        else if (v == ivTitleTextSize)
        {
            String[] items={"超大字体","大字体","正常字体","小字体","超小字体"};
            new AlertDialog.Builder(this)
                        .setTitle("改变字体")
                        .setSingleChoiceItems(items, position, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                position=which;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                changeTextSize(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            // Handle clicks for ivTitleTextSize
        }
        else if (v == ivTitleShare)
        {
            // Handle clicks for ivTitleShare
        }
    }

    /**
     * 改变字体大小
     * @param position
     */
    private void changeTextSize(int position)
    {
        switch (position) {
            case 0 :
                settings.setTextZoom(200);
                break;
            case 1 :
                settings.setTextZoom(150);

                break;
            case 2 :
                settings.setTextZoom(100);

                break;
            case 3 :
                settings.setTextZoom(75);

                break;
            case 4 :

                settings.setTextZoom(50);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        findViews();
        String url = getIntent().getStringExtra("url");
//        url="http://www.atguigu.com/";
        if (!TextUtils.isEmpty(url))
        {
            wv_news_details.loadUrl(url);

        }

        wv_news_details.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                pb_news_details.setVisibility(View.GONE);
            }
        });
        settings = wv_news_details.getSettings();
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);



    }


}
