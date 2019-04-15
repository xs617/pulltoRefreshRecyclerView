package com.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wenjiarong on 2019/4/13 0013.
 */
public class MyNestedScrollView extends NestedScrollView {
    String TAG = "PullToRefresh";
    final String TAGRecycler = "PullToRefresh Recycler";

    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "MyNestedScrollView : dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "MyNestedScrollView : onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "MyNestedScrollView : onTouchEvent");
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View var1, @NonNull View var2, int var3, int var4) {
//        Log.e(TAGRecycler, "MyNestedScrollView : onStartNestedScroll");
        return super.onStartNestedScroll(var1, var2, var3, var4);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View var1, @NonNull View var2, int var3, int var4) {
//        Log.e(TAGRecycler, "MyNestedScrollView : onNestedScrollAccepted");
        super.onNestedScrollAccepted(var1, var2, var3, var4);
    }

    @Override
    public void onStopNestedScroll(@NonNull View var1, int var2) {
//        Log.e(TAGRecycler, "MyNestedScrollView : onStopNestedScroll");
        super.onStopNestedScroll(var1, var2);
    }

    @Override
    public void onNestedScroll(@NonNull View var1, int var2, int var3, int var4, int var5, int var6) {
//        Log.e(TAGRecycler, "MyNestedScrollView : onNestedScroll");
        super.onNestedScroll(var1, var2, var3, var4, var5, var6);
    }

    @Override
    public void onNestedPreScroll(@NonNull View var1, int var2, int var3, @NonNull int[] var4, int var5) {
//        Log.e(TAGRecycler, "MyNestedScrollView : onNestedPreScroll");
        super.onNestedPreScroll(var1, var2, var3, var4, var5);
    }

    @Override
    public boolean startNestedScroll(int var1, int var2) {
//        Log.e(TAGRecycler, "MyNestedScrollView : startNestedScroll");
        return super.startNestedScroll(var1, var2);
    }

    @Override
    public void stopNestedScroll(int var1) {
//        Log.e(TAGRecycler, "MyNestedScrollView : stopNestedScroll");
        super.stopNestedScroll(var1);
    }

    @Override
    public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, @Nullable int[] var5, int var6) {
//        Log.e(TAGRecycler, "MyNestedScrollView : dispatchNestedScroll");
        return super.dispatchNestedScroll(var1, var2, var3, var4, var5, var6);
    }

    @Override
    public boolean dispatchNestedPreScroll(int var1, int var2, @Nullable int[] var3, @Nullable int[] var4, int var5) {
//        Log.e(TAGRecycler, "MyNestedScrollView : dispatchNestedPreScroll");
        return super.dispatchNestedPreScroll(var1, var2, var3, var4, var5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
