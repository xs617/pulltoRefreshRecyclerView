package fxchat.com.nestscrolledpulltorecycler.refreshui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ListView;

import fxchat.com.nestscrolledpulltorecycler.R;
import fxchat.com.nestscrolledpulltorecycler.refreshui.ILoadingLayout.State;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 *
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshListView extends PullToRefreshBase<ListView> implements OnScrollListener {

    /**
     * ListView
     */
    private ListView mListView;
    /**
     * 用于滑到底部自动加载的Footer
     */
    public LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private OnScrollListener mScrollListener;
    private boolean isgone = true;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setPullLoadEnabled(false);
    }

    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshListView);
        boolean isScroll = typedArray.getBoolean(R.styleable.RefreshListView_isScroll, false);
        ListView listView;
        if (isScroll){
            listView = new ListView(context, attrs);
        }else{
            listView = new ListView(context);
        }
        typedArray.recycle();
        mListView = listView;
        mListView.setDivider(null);
        mListView.setSelection(0);
        mListView.setVerticalScrollBarEnabled(false);//隐藏滚动条
//        去掉到头和底的阴影
        mListView.setOverScrollMode(OVER_SCROLL_NEVER);
        listView.setOnScrollListener(this);
        return listView;
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled){
        mListView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);//隐藏滚动条
    }

    public void setDivider(Drawable d, int height) {
        mListView.setDivider(d);
        mListView.setDividerHeight(height);
    }


    public void setDivider(int color, int height) {
        setDivider(new ColorDrawable(color), height);
    }

    private String finishText;

    public void setFinishText(String finishText) {
        this.finishText = finishText;
    }

    public void setLoadColor(int color) {
        ((FooterLoadingLayout) mLoadMoreFooterLayout).setBackgroundColor(color);
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
//            if (null != mLoadMoreFooterLayout) {
//                if (mLoadMoreFooterLayout instanceof FooterLoadingLayout) {
//                    ((FooterLoadingLayout) mLoadMoreFooterLayout).setFinishText(finishText);
//                    //   ((FooterLoadingLayout) mLoadMoreFooterLayout).setFinishText(finishText);
//                }
//                mLoadMoreFooterLayout.setState(State.NO_MORE_DATA);
//            }

            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                if (footerLoadingLayout instanceof FooterLoadingLayout) {
                    ((FooterLoadingLayout) footerLoadingLayout).setFinishText(finishText);
                    //  ((FooterLoadingLayout) footerLoadingLayout).setFinishText(finishText);
                }
                footerLoadingLayout.setState(State.NO_MORE_DATA);
            }
        }
    }

    /**
     * 设置滑动的监听器
     *
     * @param l 监听器
     */
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.REFRESHING);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }

            if (null == mLoadMoreFooterLayout.getParent()) {
                mListView.addFooterView(mLoadMoreFooterLayout, null, false);
            }
            mLoadMoreFooterLayout.show(true);
        } else {//不显示底部上拉布局
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    public void setScrollLoadDownEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }

            if (null == mLoadMoreFooterLayout.getParent()) {
                mListView.addFooterView(mLoadMoreFooterLayout, null, false);
            }
            mLoadMoreFooterLayout.show(true);
        } else {//不现实底部上拉布局
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }
        return super.getFooterLoadingLayout();
    }
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition;// 标记上次滑动位置
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        //防止在滚动的时候重复加载图片,glide
//        if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
//            //滑动列表停止，恢复请求额
//            Glide.with(getContext()).resumeRequests();
//        }else{
//            //滑动列表在滑动时，停止请求
//            Glide.with(getContext()).pauseRequests();
//        }
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }

        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }

        if(scrollState== OnScrollListener.SCROLL_STATE_IDLE){
            lastVisibleItemPosition=mListView.getFirstVisiblePosition();
            scrollFlag = false;
        }else if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            scrollFlag = true;
        } else {
            scrollFlag = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mScrollListener) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (scrollFlag&&mListView.getFirstVisiblePosition()!=0) {
            if (firstVisibleItem > lastVisibleItemPosition) {
                Log.e("dc", "上滑-隐藏");
                if (onShowTopTabLisenter != null) {
                    onShowTopTabLisenter.showOnTopTab(false);
                }
            }

            if (firstVisibleItem < lastVisibleItemPosition) {
                Log.e("dc", "下滑--显示");
                if (onShowTopTabLisenter != null) {
                    onShowTopTabLisenter.showOnTopTab(true);
                }
            }

            if (firstVisibleItem == lastVisibleItemPosition) {
                return;
            }
            lastVisibleItemPosition = firstVisibleItem;
        }else {
            if (mListView.getFirstVisiblePosition()==0&&onShowTopTabLisenter != null) {
                onShowTopTabLisenter.showOnTopTab(false);
            }
        }
    }



    public OnShowTopTabLisenter onShowTopTabLisenter;

    public void setOnShowTopTabLisenter(OnShowTopTabLisenter onShowTopTabLisenter) {
        this.onShowTopTabLisenter = onShowTopTabLisenter;
    }

    public interface OnShowTopTabLisenter {
        void showOnTopTab(boolean isShowTopTab);
    }

    private LoadingLayout mRotateLoadingLayout;

    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        mRotateLoadingLayout = new RotateLoadingLayout(context);
        return mRotateLoadingLayout;
    }

    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA)) {
            return false;
        }

        return true;
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
        if (mostTop >= 0 &&  mListView.getFirstVisiblePosition()==0) {
            return true;
        }

        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
            final int childCount = mListView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mListView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mListView.getBottom();
            }
        }

        return false;
    }

    public ListView getmListView() {
        return mListView;
    }

    /**
     * 显示和隐藏数据加载的提示
     */
    public void showFootLayout(int size) {
        if (size > 0) {
            mLoadMoreFooterLayout.show(true);
        } else {
            mLoadMoreFooterLayout.show(false);
        }
    }
}
