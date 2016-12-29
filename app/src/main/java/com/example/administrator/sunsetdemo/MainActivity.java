package com.example.administrator.sunsetdemo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    PullToZoomListView mList;
    MyAdapter adapter;
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (PullToZoomListView) findViewById(R.id.mListView);
        mToolBar = (Toolbar) this.findViewById(R.id.mToolBar);
        setSupportActionBar(mToolBar);
        adapter = new MyAdapter(this);
        mList.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.
                    LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }

    public static int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
