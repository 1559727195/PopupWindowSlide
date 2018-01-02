package com.massky.popupwindowslide;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_pop;
    private PopupWindow popupWindow;
    private SharedPreferences sp;
    private ImageView iv_01, iv_02, iv_03;
    private TextView tv_01, tv_02, tv_03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("state", MODE_PRIVATE);
        // 初始化时清除保存的状态数据
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();

        initView();
    }

    private void initView() {
        tv_pop = (TextView) findViewById(R.id.tv_pop);
        tv_pop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pop:
                showPopWindow(getView());
                break;

            default:
                break;
        }
    }

    private void showPopWindow(View view) {
        try {

            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            ColorDrawable cd = new ColorDrawable(0x00ffffff);// 背景颜色全透明
            popupWindow.setBackgroundDrawable(cd);
            int[] location = new int[2];
            tv_pop.getLocationOnScreen(location);
            popupWindow.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效
            backgroundAlpha(0.5f);// 设置背景半透明
            popupWindow.showAsDropDown(tv_pop);
            //popupWindow.showAtLocation(tv_pop, Gravity.NO_GRAVITY, location[0]+tv_pop.getWidth(),location[1]);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    popupWindow = null;// 当点击屏幕时，使popupWindow消失
                    backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消
                }
            });

            if (sp.getInt("flag", 1) == 1) {
                iv_01.setVisibility(View.VISIBLE);
                tv_01.setTextColor(Color.RED);
            } else if (sp.getInt("flag", 2) == 2) {
                iv_02.setVisibility(View.VISIBLE);
                tv_02.setTextColor(Color.RED);
            } else if (sp.getInt("flag", 3) == 3) {
                iv_03.setVisibility(View.VISIBLE);
                tv_03.setTextColor(Color.RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 设置popupWindow背景半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        getWindow().setAttributes(lp);
    }

    /*
     * 得到popupwindow的View
     */
    private View getView() {
        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.popupwindow, null);
        iv_01 = (ImageView) view.findViewById(R.id.iv_01);
        iv_02 = (ImageView) view.findViewById(R.id.iv_02);
        iv_03 = (ImageView) view.findViewById(R.id.iv_03);
        tv_01 = (TextView) view.findViewById(R.id.tv_01);
        tv_02 = (TextView) view.findViewById(R.id.tv_02);
        tv_03 = (TextView) view.findViewById(R.id.tv_03);
        RelativeLayout rl_01 = (RelativeLayout) view.findViewById(R.id.rl_01);
        RelativeLayout rl_02 = (RelativeLayout) view.findViewById(R.id.rl_02);
        RelativeLayout rl_03 = (RelativeLayout) view.findViewById(R.id.rl_03);

        rl_01.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                tv_pop.setText("名称排序");
                saveCurrentState(1);
            }
        });
        rl_02.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                tv_pop.setText("从高到低排序");
                saveCurrentState(2);

            }
        });
        rl_03.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_03.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
                tv_pop.setText("从低到高排序");
                saveCurrentState(3);
            }
        });
        return view;
    }

    /*
     * 保存当前状态 1-->名称排序 2-->从高到低 3-->从低到高
     */
    private void saveCurrentState(int i) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("flag", i);
        editor.commit();
    }
}

