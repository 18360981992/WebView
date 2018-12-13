package com.dabao.demo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public ImageView iv_dv_view;

    private LinearLayout item_LinearLayout;
    public int item_LinearLayout_width;
    public int iv_dv_view_width;
    public int screenWidth;
    private RelativeLayout xuanfu_RelativeLayout;
    public int xuanfu_relativeLayout_width;

    public int down_x;  // 只用于按下到时候
    public int down_y;
    public int sx; // 用于记录每次移动后的位置
    public int sy;

    int i=1; // 用于记录
    public LinearLayout item_text;
    public int cha_ju;
    public int screenHeight;
    public int xuanfu_relativeLayout_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }



    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        switch (v.getId()) {
            // 如果手指放在imageView上拖动
            case R.id.iv_dv_view:
                // event.getRawX(); //获取手指第一次接触屏幕在x方向的坐标
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                        sx = (int) event.getRawX();
                        sy = (int) event.getRawY();
                        down_x = sx;
                        down_y = sy;

                        item_text.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();
                        // 获取手指移动的距离
                        int dx = x - sx;
                        int dy = y - sy;
                        // 得到imageView最开始的各顶点的坐标
                        int l = iv_dv_view.getLeft();
                        int r = iv_dv_view.getRight();
                        int t = iv_dv_view.getTop();
                        int b = iv_dv_view.getBottom();
                        // 更改imageView在窗体的位置
                        iv_dv_view.layout(l + dx, t + dy, r + dx, b + dy);

                        int l1 = xuanfu_RelativeLayout.getLeft();
                        int r1 = xuanfu_RelativeLayout.getRight();
                        int t1 = xuanfu_RelativeLayout.getTop();
                        int b1 = xuanfu_RelativeLayout.getBottom();
                        xuanfu_RelativeLayout.layout(l1 + dx, t1 + dy, r1 + dx, b1 + dy);
                        // 获取移动后的位置
                        sx = (int) event.getRawX();
                        sy = (int) event.getRawY();

                        Log.i("jba","xuanfu_RelativeLayout.getLeft()=="+xuanfu_RelativeLayout.getLeft()+",xuanfu_RelativeLayout.getRight()=="+xuanfu_RelativeLayout.getRight());

                        // 限制悬浮球的活动范围 防止超出当前屏幕
                        if(xuanfu_RelativeLayout.getLeft()<0-cha_ju){
                            xuanfu_RelativeLayout.layout(0-cha_ju, t1 + dy, iv_dv_view_width+cha_ju, b1 + dy);
                        }else{
                            if(xuanfu_RelativeLayout.getRight()-screenWidth > cha_ju){
                                xuanfu_RelativeLayout.layout(screenWidth-cha_ju-iv_dv_view_width, t1 + dy, screenWidth+cha_ju, b1 + dy);
                            }
                        }
                        Log.i("jba","xuanfu_RelativeLayout.getTop()=="+xuanfu_RelativeLayout.getTop()+",xuanfu_RelativeLayout.getBottom()=="+xuanfu_RelativeLayout.getBottom());

                        if(xuanfu_RelativeLayout.getTop()<0){
                            xuanfu_RelativeLayout.layout(l1 + dx, 0, r1 + dx, xuanfu_relativeLayout_height);
                        }else{
                            if(xuanfu_RelativeLayout.getBottom()>screenHeight){
                                xuanfu_RelativeLayout.layout(l1 + dx, screenHeight-xuanfu_relativeLayout_height, r1 + dx, screenHeight);
                            }
                        }


                        break;
                    case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
                        // 记录最后图片在窗体的位置
                        iv_dv_view.setBackgroundResource(R.drawable.details_djzk_1);

                        // 获取移动后的位置
                        int up_x = (int) event.getRawX();
                        int up_y = (int) event.getRawY();
                        int dx1 = up_x - down_x;
                        int dy1 = up_y - down_y;

                        // 条目的隐藏与显示判断  根据是否有移动  移动==不显示
                        if(Math.abs(dx1)<=3&&Math.abs(dy1)<=3){ // 未移动  这里的 3 只是个范围 随便设 只是为了让他有一点点的活动范围
                            if(i%2==1){
                                int xin = xuanfu_RelativeLayout.getLeft() + xuanfu_relativeLayout_width / 2;
                                if (xin <= screenWidth / 2) { // 判断移动后的位置是小于等于屏幕宽度的中心  true == 动画在右边
                                    item_LinearLayout.setVisibility(View.VISIBLE);
                                    ObjectAnimator animatorimg2 = ObjectAnimator.ofFloat(item_LinearLayout, "translationX", iv_dv_view.getRight() - item_LinearLayout_width, iv_dv_view.getLeft()); // 移动
                                    ObjectAnimator alphaimg2 = ObjectAnimator.ofFloat(item_LinearLayout, "alpha", 0f, 1.0f); // 透明度
                                    AnimatorSet animatorSetimg2 = new AnimatorSet();
                                    animatorSetimg2.play(alphaimg2).with(animatorimg2);
                                    animatorimg2.setDuration(2000);
                                    animatorimg2.setInterpolator(new OvershootInterpolator(1));
                                    animatorimg2.start();

                                    animatorimg2.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            item_text.layout(iv_dv_view_width+15 ,item_text.getTop(),item_LinearLayout_width,item_text.getBottom());
                                            item_text.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                } else {  // false == 动画在左边
                                    item_LinearLayout.setVisibility(View.VISIBLE);
                                    ObjectAnimator animatorimg2 = ObjectAnimator.ofFloat(item_LinearLayout, "translationX", iv_dv_view.getLeft(), iv_dv_view.getRight() - item_LinearLayout_width); // 移动
                                    ObjectAnimator alphaimg2 = ObjectAnimator.ofFloat(item_LinearLayout, "alpha", 0f, 1.0f); // 透明度
                                    AnimatorSet animatorSetimg2 = new AnimatorSet();
                                    animatorSetimg2.play(alphaimg2).with(animatorimg2);
                                    animatorimg2.setDuration(2000);
                                    animatorimg2.setInterpolator(new OvershootInterpolator(1));
                                    animatorimg2.start();

                                    animatorimg2.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {

                                            item_text.layout(0+15,item_text.getTop(),item_LinearLayout_width-iv_dv_view_width,item_text.getBottom());
                                            item_text.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                }
                            }else{
                                item_LinearLayout.setVisibility(View.INVISIBLE);
                            }
                           i++;

                        }else{  // 移动  将条目不显示
                            i=1;
                            item_LinearLayout.setVisibility(View.INVISIBLE);
                        }

                        break;
                }
                break;
        }
        return true;// 不会中断触摸事件的返回
    }

    private void initView() {
        xuanfu_RelativeLayout = (RelativeLayout) findViewById(R.id.xuanfu_RelativeLayout);
        iv_dv_view = (ImageView) this.findViewById(R.id.iv_dv_view);
        iv_dv_view.setOnTouchListener(this);
        item_LinearLayout = (LinearLayout) findViewById(R.id.item_LinearLayout);
        item_text = (LinearLayout) findViewById(R.id.item_text);

        final TextView text1 = (TextView) findViewById(R.id.text1);
        final TextView text2 = (TextView) findViewById(R.id.text2);
        final TextView text3 = (TextView) findViewById(R.id.text3);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,""+text1.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });


        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,""+text2.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,""+text3.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        //通过设置监听来获取 微弹窗 控件的宽度
        item_LinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                item_LinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //获取item_LinearLayout控件的初始宽度  用来图片回弹时
                xuanfu_relativeLayout_width = xuanfu_RelativeLayout.getMeasuredWidth();
                xuanfu_relativeLayout_height = xuanfu_RelativeLayout.getMeasuredHeight();
                item_LinearLayout_width = item_LinearLayout.getMeasuredWidth();
                iv_dv_view_width = iv_dv_view.getMeasuredWidth();

                cha_ju = xuanfu_relativeLayout_width / 2 - iv_dv_view_width / 2;

            }
        });

        // 获取当前屏幕的宽度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度(像素)
        screenWidth = metric.widthPixels;
        // 屏幕高度(像素)
        screenHeight = metric.heightPixels;   // 这里的屏幕高度  不包含系统自带的title  具体的情况需要看有没有沉浸式   这里就按有沉浸式来处理了
    }
}
