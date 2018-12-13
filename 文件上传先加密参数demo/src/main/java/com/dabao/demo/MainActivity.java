package com.dabao.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabao.demo.apis.APIs;
import com.dabao.demo.services.ApiService;
import com.dabao.demo.utils.BaseServer;
import com.dabao.demo.utils.Md5RequestUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.ele.download.FinalHttp;
import me.ele.download.http.AjaxCallBack;
import me.ele.download.http.AjaxParams;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.dabao.demo.MyAppliction.loggingInterceptor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private TextView textview;
    public Retrofit retrofit;
    public File coverFile;
    private ImageView img;
    public Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        File f = Environment.getExternalStorageDirectory();
        coverFile = new File(f, "img1.png");
        Bitmap bitmap=BitmapFactory.decodeFile(coverFile.toString());
        img.setImageBitmap(bitmap);


        // log 属于拦截器 所以需要将他注入okhttp中
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new ApplictionInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        // 初始化retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(APIs.debug)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }


    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
        textview = (TextView) findViewById(R.id.textview);
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:

                Map<String,Object> map =new HashMap<>();
                map.put("dealerid", "288175116921024660");
                map.put("serial", "YX24ED5832D7128A00000S");
                map.put("syscode", "ab4cedcb9e35aa7d67517e83147b8fe5");
                map.put("scode", "f50e9d10b930e7e18857be269f4d7957");
                map.put("type", "1");
                map.put("tm", String.valueOf(System.currentTimeMillis()/1000));
                Map<String, Object> upmap = Md5RequestUtils.compartorList(map, "upfile");
                // 通过反射获得 apiservice接口
                ApiService apiService = retrofit.create(ApiService.class);
                //addFormDataPart()第一个参数为表单名字，这是和后台约定好的
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)

                        .addFormDataPart("dealerid", (String)upmap.get("dealerid"))
                        .addFormDataPart("serial", (String)upmap.get("serial"))
                        .addFormDataPart("syscode", (String)upmap.get("syscode"))
                        .addFormDataPart("scode", (String)upmap.get("scode"))
                        .addFormDataPart("type", (String)upmap.get("type"))
                        .addFormDataPart("tm", (String)upmap.get("tm"))
                        .addFormDataPart("sig", (String)upmap.get("sig"));

                //注意，file是后台约定的参数，如果是多图，file[]，如果是单张图片，file就行
                builder.addFormDataPart("file", coverFile.getName(), RequestBody.create(MediaType.parse("image/png"), coverFile));

                RequestBody requestBody = builder.build();

                Observable<String> upload = apiService.upload(requestBody);
                upload.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseServer() {
                            @Override
                            public void onSuccess(String json) {
                                Log.i("retr", "头像上传成功=====" + json);
                                textview.setText("" + json);
                            }

                            @Override
                            public void onErroy(String ss) {
                                Log.i("retr", "头像上传失败=====" + ss);
                                textview.setText("" + ss);
                            }
                        });
                break;
            case R.id.btn1:

                getUplode(coverFile);
                break;

        }
    }


    /**
     * 上传文件
     * @param fileUri  文件类型
     * @param i  // 状态值  1==正面 2==背面
     */
    FinalHttp fh = new FinalHttp();  // 框架的实例化
    private void getUplode(File fileUri) {
        Map<String,Object> map =new HashMap<>();
        map.put("dealerid", "288175116921024660");
        map.put("serial", "YX24ED5832D7128A00000S");
        map.put("syscode", "ab4cedcb9e35aa7d67517e83147b8fe5");
        map.put("scode", "f50e9d10b930e7e18857be269f4d7957");
        map.put("type", "1");
        map.put("tm", String.valueOf(System.currentTimeMillis()/1000));

        Map<String, Object> upmap = Md5RequestUtils.compartorList(map, "upfile");
        AjaxParams params = new AjaxParams();
        try {
            params.put("dealerid", (String)upmap.get("dealerid"));
            params.put("serial", (String)upmap.get("serial"));
            params.put("syscode", (String)upmap.get("syscode"));
            params.put("scode", (String)upmap.get("scode"));
            params.put("type", (String)upmap.get("type"));
            params.put("tm", (String)upmap.get("tm"));
            params.put("file", fileUri);
            params.put("sig", (String)upmap.get("sig"));

            fh.post("http://dls-pad-d.yunxitech.cn/upfile", params, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String json) {
                    super.onSuccess(json);
                    textview.setText("" + json);
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    textview.setText("" + strMsg);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //参数拦截器
    public static class ApplictionInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取到请求
            Request original = chain.request();
            //获取请求的方式
            String method = original.method();
            //获取请求的路径
            String oldUrl = original.url().toString();
            Log.i("retr","拦截器==="+oldUrl);

            if ("POST".equals(method)) {
                Request build = original.newBuilder()
                        .method(method, original.body())
                        .url(oldUrl)
                        .build();

                Response response = chain.proceed(build);

                return response;
            }
            return null;
        }
    }

}
