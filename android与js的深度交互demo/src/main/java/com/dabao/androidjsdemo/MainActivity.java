package com.dabao.androidjsdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private WebView webView;

    String path = "http://192.168.1.134:4200/#/h5/take/delivery?userName=13137426563&token=16EB4BCB-5B9D-4D00-B389-27BEC662E972";
//    String path="file:///android_asset/test.html";
    private ProgressBar progressBar;

//    @Override
//    public void getLodeurl() {
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
// http://baoku.wjybk.com/#/h5/take/delivery?userName=13137426563&token=A192218162e1342a1
//        webView.loadUrl("http://baoku.wjybk.com/#/h5/take/delivery?userName=13137426563&token=A192218162e1342a1");
//        webView.loadUrl("http://www.baokuyt.com:8020/#/h5/take/delivery?userName=13137426563&token=A192218162e1342a1");
        webView.loadUrl("http://www.baidu.com");
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);

                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }


    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView.getSettings().setUseWideViewPort(true);//设定支持h5viewport
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置javascript弹窗
        webView.getSettings().setDomStorageEnabled(true); // 打开DOM存储API  这个很重要 是装载js方法执行的关键
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // LOAD_CACHE_ELSE_NETWORK 默认不使用缓存！  LOAD_NO_CACHE
        webView.setWebViewClient(new WebViewClientImpl(true) );// 在不同android版本上出现白屏的情况

//        webView.setIsflag(false);
//        webView.addJavascriptInterface(new DeliveryInterface(), "android_Delivery");
    }


    public class WebViewClientImpl extends WebViewClient {

        boolean mClearHistry;

        public WebViewClientImpl(boolean mClearHistry) {
            this.mClearHistry = mClearHistry;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不另跳浏览器
            // 在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
            // 如下方案可在非微信内部WebView的H5页面中调出微信支付
             if (url.startsWith("weixin://wap/pay?")) {
                 Log.i("jba","Refererurl==="+url);
                 Intent intent = new Intent();
                 intent.setAction(Intent.ACTION_VIEW);
                 intent.setData(Uri.parse(url));
                 startActivity(intent);
                 return true;
             }
            return super.shouldOverrideUrlLoading(view, url);

//            if (url.startsWith("http://") || url.startsWith("https://")) {
//                view.loadUrl(url);
//                return true;
//            }
//            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            view.getSettings().setBlockNetworkImage(true);// 将图片下载阻塞
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.getSettings().setBlockNetworkImage(false); // 通过图片的延迟载入
            view.setLayerType(View.LAYER_TYPE_HARDWARE,null); // 通过加载完成后启动硬件渲染加速
//            view.getSettings().setDefaultFontSize(75);// 设置默认字体大小
            view.getSettings().setTextZoom(100);   // 设置页面字体的百分比  用于屏幕适配
            view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 自适应屏幕.
            view.getSettings().setSupportZoom(true);//支持缩放网页
            view.getSettings().setBuiltInZoomControls(true);//支持缩放网页

            //这里必须要,不然回退有问题.
            if(mClearHistry){
                //mClearHistry在用到Webview的Activity的onCreate中设为true
                mClearHistry = false;
                view.clearHistory();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { }

    }

    /**
     * Js调用的JavascriptInterface
     */
    public class DeliveryInterface {

        /**
         *
         *  window.android_Delivery.android_Delivery_login()  // 跳到登录页面
         *
         *  window.android_Delivery.android_Delivery_finish()  // 返回
         *
         */


        @JavascriptInterface
        public void android_Delivery_login() {
            Toast.makeText(MainActivity.this, "跳到登录页面。。。" , Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void android_Delivery_finish() {
            Toast.makeText(MainActivity.this, "点击返回键。。。" , Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
