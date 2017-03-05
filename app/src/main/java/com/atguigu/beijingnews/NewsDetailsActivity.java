package com.atguigu.beijingnews;

import android.app.Activity;
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
            // Handle clicks for ivTitleTextSize
        }
        else if (v == ivTitleShare)
        {
            // Handle clicks for ivTitleShare
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        findViews();
        String url = getIntent().getStringExtra("url");
        url="http://www.atguigu.com/";
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
                pb_news_details.setVisibility(View.VISIBLE);
            }
        });
        WebSettings settings = wv_news_details.getSettings();
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);



    }


}
