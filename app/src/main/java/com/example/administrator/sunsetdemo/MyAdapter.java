package com.example.administrator.sunsetdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MyAdapter extends BaseAdapter {
    Context context;
    ImageView imageView;
    Button btn_down;
    Button btn_getGift;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
            imageView = (ImageView) view.findViewById(R.id.list_item_head_img);
            btn_down = (Button) view.findViewById(R.id.btn_get);
            btn_getGift = (Button) view.findViewById(R.id.btn_down);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int ev = motionEvent.getAction();
                    switch (ev){
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                    return false;
                }
            });
            btn_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"开始下载",Toast.LENGTH_SHORT).show();
                }
            });
            btn_getGift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"领取礼包",Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }


}
