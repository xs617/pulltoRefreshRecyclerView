package fxchat.com.nestscrolledpulltorecycler.refreshui;

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

import static android.support.v4.view.ViewCompat.TYPE_NON_TOUCH;
import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Created by wenjiarong on 2019/4/11 0011.
 */
public class PullToRefreshRecyclerView2 extends PullToRefreshRecyclerView implements NestedScrollingChild2, NestedScrollingParent2 {
    NestedScrollingChildHelper mChildHelper = new NestedScrollingChildHelper(this);
    NestedScrollingParentHelper mParentHelper = new NestedScrollingParentHelper(this);

    final String TAG = "PullToRefresh";

    public PullToRefreshRecyclerView2(Context context) {
        this(context, null);
    }

    public PullToRefreshRecyclerView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshRecyclerView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mChildHelper.setNestedScrollingEnabled(true);
    }

    float mLastY;
    boolean isSkipFling = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e(TAG, "onInterceptTouchEvent:");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
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
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onTouchEvent:");
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        Log.e(TAG, "startNestedScroll:");
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll(int type) {
        Log.e(TAG, "stopNestedScroll:");
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        Log.e(TAG, "hasNestedScrollingParent:");
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        Log.e(TAG, "dispatchNestedScroll:");
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        Log.e(TAG, "dispatchNestedPreScroll:");
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.e(TAG, "onStartNestedScroll:");
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.e(TAG, "onNestedScrollAccepted :");
        isSkipFling = false;
        mParentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        Log.e(TAG, "onStopNestedScroll :");
        stopNestedScroll(type);
        mParentHelper.onStopNestedScroll(target);
        switch (type) {
            case TYPE_TOUCH:
                Log.e(TAG, "onStopNestedScroll Touch :" + offset);
                if (offset < 0) {
                    if (offset <= getFooterLoadingLayout().getMeasuredHeight() * -1) {
                        //上拉到最大，开始loading
                        startLoading();
                    } else {
                        resetFooterLayout();
                    }
                }
                if (offset > 0) {
                    if (offset >= getHeaderLoadingLayout().getMeasuredHeight()) {
                        //下拉到最大，开始refreshing
                        startRefreshing();
                    } else {
                        resetHeaderLayout();
                    }
                }
                break;
            case TYPE_NON_TOUCH:
                Log.e(TAG, "onStopNestedScroll  Fling");
                break;
            default:
                break;
        }
        offset = 0;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type) {
        Log.e(TAG, "onNestedScroll :");

    }

    int offset;

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  int type) {
        consumed[0] = 0;
        consumed[1] = 0;
        int[] headLocation = new int[2];
        int[] footerLocation = new int[2];
        int[] pullToRefreshLocation = new int[2];
        int[] mParentConsume = new int[2];
        int[] mOffsetInWindow = new int[2];

        if (this.dispatchNestedPreScroll(dx, dy, mParentConsume, mOffsetInWindow, type)) {
            consumed[0] += mParentConsume[0];
            consumed[1] += mParentConsume[1];
            dx -= mParentConsume[0];
            dy -= mParentConsume[1];
        }

        getHeaderLoadingLayout().getLocationOnScreen(headLocation);
        getFooterLoadingLayout().getLocationOnScreen(footerLocation);
        this.getLocationOnScreen(pullToRefreshLocation);
        offset = headLocation[1] - pullToRefreshLocation[1] + getHeaderLoadingLayout().getMeasuredHeight();

        Log.e(TAG, "dy :" + dy + " offset :" + offset + " ,parentUnconsumedDy :" + dy + " type :" + type + " parentConsume :" + mParentConsume[1]);
        int pullDy = (int) (dy / OFFSET_RADIO);
        if (dy > 0) {
            //上拉
            //如果header已经拉出来了一部分,把拉出来的收回去
            if (offset > 0) {
                int headerConsume = offset - dy > 0 ? dy : offset;
                pullHeaderLayout(headerConsume * -1);
                consumed[1] += headerConsume;
                dy -= headerConsume;
            } else {
                if (isPullLoadEnabled() && isReadyForPullUp()) {
                    switch (type) {
                        case TYPE_TOUCH:
                            int pullY = getFooterLoadingLayout().getMeasuredHeight() + offset - pullDy > 0 ? pullDy * -1 : (getFooterLoadingLayout().getMeasuredHeight() + offset) * -1;
                            pullFooterLayout(pullY);
                            consumed[1] += dy;
                            dy = 0;
                            Log.e(TAG, "consumed up:" + consumed[1]);
                            break;
                        case TYPE_NON_TOUCH:
                            isSkipFling = true;
                            break;
                        default:
                            break;
                    }
                }
                if (isSkipFling) {
                    consumed[1] = +dy;
                    dy = 0;
                    Log.e(TAG, "consumed fling:" + consumed[1]);
                }
            }
        } else if (dy < 0) {
            //下拉
            //如果footer已经拉出来了一部分，把拉出来的收回去
            if (offset < 0) {
                int footerConsume = offset - dy < 0 ? dy : offset;
                pullFooterLayout(footerConsume * -1);
                dy -= footerConsume;
                consumed[1] += footerConsume;
            } else {
                if (isPullRefreshEnabled() && isReadyForPullDown()) {
                    switch (type) {
                        case TYPE_TOUCH:
                            int pullY = getHeaderLoadingLayout().getMeasuredHeight() - (offset - pullDy) > 0 ? pullDy * -1 : (getHeaderLoadingLayout().getMeasuredHeight() - offset);
                            pullHeaderLayout(pullY);
                            consumed[1] = +dy;
                            dy = 0;
                            Log.e(TAG, "consumed down:" + consumed[1]);
                            break;
                        case TYPE_NON_TOUCH:
                            isSkipFling = true;
                            break;
                        default:
                            break;
                    }
                }
                if (isSkipFling) {
                    consumed[1] = +dy;
                    dy = 0;
                    Log.e(TAG, "consumed Fling:" + consumed[1]);
                }
            }
        }

        dispatchNestedScroll(consumed[0], consumed[1], dx, dy, mOffsetInWindow, type);
    }
}
