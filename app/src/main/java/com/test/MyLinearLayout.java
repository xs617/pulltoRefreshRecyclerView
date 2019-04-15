package com.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by wenjiarong on 2019/4/15 0015.
 */
public class MyLinearLayout extends LinearLayout {
    final String TAGRecycler = "PullToRefresh Recycler";

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.e(TAGRecycler, "MyLinearLayout : onTouchEvent");
//        return super.onTouchEvent(event);
//    }
}
