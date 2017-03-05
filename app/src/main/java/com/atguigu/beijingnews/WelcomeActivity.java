package com.atguigu.beijingnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.atguigu.beijingnews.utils.CacheUtils;

/**
 * 欢迎界面
 */
public class WelcomeActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //启动页面的动画效果
        startAnimation();

    }

    private void startGuideActivity()
    {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);


    }

    private void startAnimation()
    {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(false);//不设置加速器
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);

        findViewById(R.id.rl_welcome).startAnimation(animationSet);

        //设置动画的监听
        animationSet.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                boolean isEnterMain= CacheUtils.getBoolean(WelcomeActivity.this, GuideActivity
                        .START_MAIN);
                if(isEnterMain) {
                    //进入过主页面
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    //没有进入过主页面 进入向导页面
                    //启动向导页面
                    startGuideActivity();

                }

                //关闭当前页面
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }
}
