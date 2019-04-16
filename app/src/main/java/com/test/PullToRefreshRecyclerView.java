package com.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import fxchat.com.nestscrolledpulltorecycler.refreshui.INestedPullTrigger;
import fxchat.com.nestscrolledpulltorecycler.refreshui.IPullToRefresh;
import fxchat.com.nestscrolledpulltorecycler.refreshui.LoadingLayout;
import fxchat.com.nestscrolledpulltorecycler.refreshui.PullToRefreshBase;

import static android.support.v4.view.ViewCompat.TYPE_NON_TOUCH;
import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Created by wenjiarong on 2019/4/15 0015.
 */
public class PullToRefreshRecyclerView<T extends View> extends LinearLayout implements IPullToRefresh<T>, NestedScrollingParent2, NestedScrollingChild2 {
    final String TAG = "PullToRefresh";
    private static final float OFFSET_RADIO = 2.5f;
    boolean isSkipFling = false;
    int offset;

    NestedScrollingChildHelper mChildHelper = new NestedScrollingChildHelper(this);
    NestedScrollingParentHelper mParentHelper = new NestedScrollingParentHelper(this);
    INestedPullTrigger iNestedPullTrigger;

    PullToRefreshConfig pullToRefreshConfig;

    public PullToRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setNestedScrollingEnabled(true);
    }

    public void config(PullToRefreshConfig config) {
        this.pullToRefreshConfig = config;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, 0);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll(int type) {
        mChildHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        isSkipFling = false;
        mParentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mParentHelper.onStopNestedScroll(target);
        stopNestedScroll(type);
        switch (type) {
            case TYPE_TOUCH:
                Log.e(TAG, "PullToRefreshRecyclerView2 onStopNestedScroll Touch :" + offset);
                if (offset < 0) {
                    if (offset <= pullToRefreshConfig.iPullToRefreshOperator.getView(PullViewType.FOOTER).getMeasuredHeight() * -1) {
                        //上拉到最大，开始loading
                        pullToRefreshConfig.iPullToRefreshOperator.startDoingAnimation(PullViewType.FOOTER);
                    } else {
                        pullToRefreshConfig.iPullToRefreshOperator.reset(PullViewType.FOOTER);
                    }
                }
                if (offset > 0) {
                    if (offset >= pullToRefreshConfig.iPullToRefreshOperator.getView(PullViewType.HEADER).getMeasuredHeight()) {
                        //下拉到最大，开始refreshing
                        pullToRefreshConfig.iPullToRefreshOperator.startDoingAnimation(PullViewType.HEADER);
                    } else {
                        pullToRefreshConfig.iPullToRefreshOperator.reset(PullViewType.HEADER);
                    }
                }
                break;
            case TYPE_NON_TOUCH:
                Log.e(TAG, "PullToRefreshRecyclerView2 onStopNestedScroll  Fling");
                break;
            default:
                break;
        }
        offset = 0;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type) {
        int dx = dxUnconsumed;
        int dy = dyUnconsumed;
        int myConsumeX = 0;
        int myConsumeY = 0;
        int[] headLocation = new int[2];
        int[] footerLocation = new int[2];
        int[] pullToRefreshLocation = new int[2];
        int[] mOffsetInWindow = new int[2];

        View header = pullToRefreshConfig.iPullToRefreshOperator.getView(PullViewType.HEADER);
        View footer = pullToRefreshConfig.iPullToRefreshOperator.getView(PullViewType.FOOTER);
        header.getLocationOnScreen(headLocation);
        footer.getLocationOnScreen(footerLocation);
        this.getLocationOnScreen(pullToRefreshLocation);
        offset = headLocation[1] - pullToRefreshLocation[1] + header.getMeasuredHeight();

        Log.e(TAG, "dy :" + dy + " offset :" + offset + " ,parentConsumedDy :" + dyConsumed + " type :" + type);
        int pullDy = (int) (dy / OFFSET_RADIO);
        if (dy > 0) {
            //上拉
            //如果header已经拉出来了一部分,把拉出来的收回去
            if (offset > 0) {
                int headerConsume = offset - dy > 0 ? dy : offset;
                pullToRefreshConfig.iPullToRefreshOperator.pull(PullViewType.HEADER, headerConsume * -1);
                myConsumeY += headerConsume;
                dy -= headerConsume;
            } else {
                if (isPullLoadEnabled() && pullToRefreshConfig.iPullToRefreshIndicator.isReady(PullOperatorType.PULL_UP)) {
                    switch (type) {
                        case TYPE_TOUCH:
                            int pullY = footer.getMeasuredHeight() + offset - pullDy > 0 ? pullDy * -1 : (footer.getMeasuredHeight() + offset) * -1;
                            pullToRefreshConfig.iPullToRefreshOperator.pull(PullViewType.FOOTER, pullY);
                            myConsumeY += dy;
                            dy = 0;
//                            Log.e(TAG, "consumed up:" + myConsumeY);
                            break;
                        case TYPE_NON_TOUCH:
                            isSkipFling = true;
                            break;
                        default:
                            break;
                    }
                }
                if (isSkipFling) {
                    myConsumeY = +dy;
                    dy = 0;
//                    Log.e(TAG, "consumed fling:" + myConsumeY);
                }
            }
        } else if (dy < 0) {
            //下拉
            //如果footer已经拉出来了一部分，把拉出来的收回去
            if (offset < 0) {
                int footerConsume = offset - dy < 0 ? dy : offset;
                pullToRefreshConfig.iPullToRefreshOperator.pull(PullViewType.FOOTER, footerConsume * -1);
                dy -= footerConsume;
                myConsumeY += footerConsume;
            } else {
                if (isPullRefreshEnabled() && pullToRefreshConfig.iPullToRefreshIndicator.isReady(PullOperatorType.PULL_DOWN)) {
                    switch (type) {
                        case TYPE_TOUCH:
                            int pullY = header.getMeasuredHeight() - (offset - pullDy) > 0 ? pullDy * -1 : (header.getMeasuredHeight() - offset);
                            pullToRefreshConfig.iPullToRefreshOperator.pull(PullViewType.HEADER, pullY);
                            myConsumeY = +dy;
                            dy = 0;
//                            Log.e(TAG, "consumed down:" + myConsumeY);
                            break;
                        case TYPE_NON_TOUCH:
                            isSkipFling = true;
                            break;
                        default:
                            break;
                    }
                }
                if (isSkipFling) {
                    myConsumeY = +dy;
                    dy = 0;
//                    Log.e(TAG, "consumed Fling:" + myConsumeY);
                }
            }
        }
        Log.e(TAG, "myConsumeY :" + myConsumeY + " dy :" + dy + " type :" + type);
        dispatchNestedScroll(myConsumeX, myConsumeY, dx, dy, mOffsetInWindow, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  int type) {
        dispatchNestedPreScroll(dx, dy, consumed, null, type);
    }

    public void setNestedPullTrigger(INestedPullTrigger iNestedPullTrigger) {
        this.iNestedPullTrigger = iNestedPullTrigger;
    }


    @Override
    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        pullToRefreshConfig.iPullToRefreshIndicator.setEnable(PullOperatorType.PULL_DOWN, pullRefreshEnabled);
    }

    @Override
    public void setPullLoadEnabled(boolean pullLoadEnabled) {
        pullToRefreshConfig.iPullToRefreshIndicator.setEnable(PullOperatorType.PULL_UP, pullLoadEnabled);
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        pullToRefreshConfig.iPullToRefreshIndicator.setEnable(PullOperatorType.BOTTOM_TRIGGER, scrollLoadEnabled);
    }

    @Override
    public boolean isPullRefreshEnabled() {
        return pullToRefreshConfig.iPullToRefreshIndicator.isEnable(PullOperatorType.PULL_DOWN);
    }

    @Override
    public boolean isPullLoadEnabled() {
        return pullToRefreshConfig.iPullToRefreshIndicator.isEnable(PullOperatorType.PULL_UP);
    }

    @Override
    public boolean isScrollLoadEnabled() {
        return pullToRefreshConfig.iPullToRefreshIndicator.isEnable(PullOperatorType.BOTTOM_TRIGGER);
    }

    @Override
    public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> refreshListener) {

    }

    @Override
    public void onPullDownRefreshComplete() {

    }

    @Override
    public void onPullUpRefreshComplete() {

    }

    @Override
    public T getRefreshableView() {
        return null;
    }

    @Override
    public LoadingLayout getHeaderLoadingLayout() {
        return null;
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        return null;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {

    }
}
