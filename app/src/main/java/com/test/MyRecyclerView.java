package com.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wenjiarong on 2019/4/13 0013.
 */
public class MyRecyclerView extends RecyclerView {
    final String TAG = "PullToRefresh";
    final String TAGRecycler = "PullToRefresh Recycler";

    public MyRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setNestedScrollingEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        Log.e(TAG, "MyRecyclerView :onTouchEvent:");
        return super.onTouchEvent(e);
    }

    @Override
    public boolean startNestedScroll(int var1, int var2) {
//        Log.e(TAGRecycler, "MyRecyclerView :startNestedScroll:");
        return super.startNestedScroll(var1, var2);
    }

    @Override
    public void stopNestedScroll(int var1) {
//        Log.e(TAGRecycler, "MyRecyclerView :stopNestedScroll:");
        super.stopNestedScroll(var1);
    }

    @Override
    public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, @Nullable int[] var5, int var6) {
//        Log.e(TAGRecycler, "MyRecyclerView :dispatchNestedScroll:");
        return super.dispatchNestedScroll(var1, var2, var3, var4, var5, var6);
    }

    @Override
    public boolean dispatchNestedPreScroll(int var1, int var2, @Nullable int[] var3, @Nullable int[] var4, int var5) {
//        Log.e(TAGRecycler, "MyRecyclerView :dispatchNestedPreScroll:");
        return super.dispatchNestedPreScroll(var1, var2, var3, var4, var5);
    }

}
