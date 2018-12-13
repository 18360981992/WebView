package com.dabao.demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsing_toolbar_layout;
    private TextView mTitle;
    private TextView test;
    private AppBarLayout appbar;
    private float mSelfHeight = 0;  //用以判断是否得到正确的宽高值
    private float mTitleScale;      //标题缩放值
    private float mTestScaleY;      //测试按钮y轴缩放值
    private float mTestScaleX;      //测试按钮x轴缩放值
    public float max_yidong_height;
    public float textSize;
    public float testSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

//        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
//        collapsing_toolbar_layout.setTitle("外卖接单中");
////        collapsing_toolbar_layout.setCollapsedTitleGravity(Gravity.CENTER);
//        //通过CollapsingToolbarLayout修改字体颜色  自动形成颜色渐变
//        collapsing_toolbar_layout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
//        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色



//        collapsing_toolbar_layout.setContentScrim(getResources().getDrawable(R.drawable.details_ds_2)); // 设置缩小后的背景颜色  设置图片等都可以
        collapsing_toolbar_layout.setContentScrimColor(getResources().getColor(R.color.lv)); // 设置缩小后的背景颜色

        final float screenW = getResources().getDisplayMetrics().widthPixels;
        final float toolbarHeight = getResources().getDimension(R.dimen.tool_bar_height2);//90
        final float initHeight = getResources().getDimension(R.dimen.app_bar_height);//500

        textSize = getResources().getDimension(R.dimen.text_size_toolbar);//32
        final float text_size_toolbar = getResources().getDimension(R.dimen.text_size_toolbar2);// 设置标题文字的最小值

        testSize = getResources().getDimension(R.dimen.test_size);
        final float test_size2 = getResources().getDimension(R.dimen.test_size2);// 设置标题文字的最小值

        /**
         *   移动效果值／最终效果值 =  移动距离／ 能移动总距离（确定）
         */

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.i("jba","verticalOffset=="+verticalOffset);

                if (mSelfHeight == 0) {
                    //获取标题高度
                    mSelfHeight = mTitle.getHeight();
                    Log.i("jba","mSelfHeight=="+mSelfHeight);

                    Log.i("jba","mTitle.getTop()=="+mTitle.getTop());
                    //得到标题的高度差
                    float distanceTitle = mTitle.getTop() - (toolbarHeight - mTitle.getHeight()) / 2.0f;
                    //得到测试按钮的高度差
                    float distanceTest = test.getY() - (toolbarHeight - test.getHeight()) / 2.0f;

                    //得到标题的水平差值  屏幕宽度一半 - 按钮宽度一半
                    float distanceSubscribeX = screenW / 2.0f ; // - (mTitle.getWidth() / 2.0f)

                    //得到高度差缩放比值  高度差／能滑动总长度 以此类推
                    mTitleScale = distanceTitle / (initHeight - toolbarHeight);
                    mTestScaleY = distanceTest / (initHeight - toolbarHeight);

                    mTestScaleX = distanceSubscribeX / (initHeight - toolbarHeight);
                }

                //设置标题y轴平移
                mTitle.setTranslationY(mTitleScale * verticalOffset);
                //设置测试按钮x跟y平移
                test.setTranslationY(mTestScaleY * verticalOffset);
                //设置标题x轴平移 居中
                mTitle.setTranslationX(-mTestScaleX * verticalOffset);

                if(verticalOffset==0){
                    mTitle.setTextSize(textSize);
                    test.setTextSize(testSize);
                }else{
                    int abs = Math.abs(verticalOffset);
                    if((textSize * ((max_yidong_height - abs) / max_yidong_height))>=text_size_toolbar){
                        mTitle.setTextSize(textSize * ((max_yidong_height - abs) / max_yidong_height));
                    }else{
                        mTitle.setTextSize(text_size_toolbar);
                    }

                    if((testSize * ((max_yidong_height - abs) / max_yidong_height))>=test_size2){
                        test.setTextSize(testSize * ((max_yidong_height - abs) / max_yidong_height));
                    }else{
                        test.setTextSize(test_size2);
                    }


                }

            }
        });


    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsing_toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mTitle = (TextView) findViewById(R.id.subscription_title);
        test = (TextView) findViewById(R.id.test);
        appbar = (AppBarLayout) findViewById(R.id.appbar);

        //通过设置监听来获取 微弹窗 控件的宽度
        appbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                appbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //获取item_LinearLayout控件的初始宽度  用来图片回弹时
                int appbar_height = appbar.getMeasuredHeight();
                int toolbar_height = toolbar.getMeasuredHeight();
                max_yidong_height = appbar_height - toolbar_height;
            }
        });
    }
}
