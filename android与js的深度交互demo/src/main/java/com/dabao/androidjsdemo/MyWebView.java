package com.dabao.androidjsdemo;

import android.content.Context;
import android.util.AttributeSet;

import com.hjhrq1991.library.tbs.TbsBridgeWebView;
import com.tencent.smtt.sdk.WebSettings;

/**
 * Created by zzt on 2018/8/21.
 * 自定义webview 获取滑动监听
 */

public class MyWebView extends TbsBridgeWebView {

    private OnScrollChangeListener mOnScrollChangeListener;

    boolean isflag;

    // 设置webview是否需要滑动回调
    public void setIsflag(boolean isflag) {
        this.isflag = isflag;
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public MyWebView(Context context) {
        super(context);
        initView(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(isflag){
            mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.mOnScrollChangeListener = listener;
    }

    public interface OnScrollChangeListener {
         void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    private void initView(Context context) {

//        // 清缓存和记录，缓存引起的白屏
//        clearCache(true);
//        clearHistory();
//        requestFocus();
//        getSettings().setDatabaseEnabled(true);
//        // 缓存白屏
//        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath() + "/webcache";
//        // 设置 Application Caches 缓存目录
//        getSettings().setAppCachePath(appCachePath);
//        getSettings().setDatabasePath(appCachePath);
//        getSettings().setAppCacheEnabled(false);// 应用可以有缓存 true false 没有缓存 去除白屏


//        setLayerType(LAYER_TYPE_SOFTWARE,null); // 关闭硬件加速
//        setHorizontalFadingEdgeEnabled(false);  // 去除边缘阴影
//        setOverScrollMode(OVER_SCROLL_ALWAYS);  // 设置滚动视图的样式
//        setWebChromeClient(new WebChromeClient());//使安卓支持网页的弹出框

        WebSettings settings = getSettings();
        settings.setUseWideViewPort(true);//设定支持h5viewport
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置javascript弹窗
        settings.setDomStorageEnabled(true); // 打开DOM存储API  这个很重要 是装载js方法执行的关键
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // LOAD_CACHE_ELSE_NETWORK 默认不使用缓存！  LOAD_NO_CACHE
//        setDefaultHandler(new DefaultHandler());
//        setWebViewClient(new WebViewClientImpl(true) );// 在不同android版本上出现白屏的情况

    }


}


