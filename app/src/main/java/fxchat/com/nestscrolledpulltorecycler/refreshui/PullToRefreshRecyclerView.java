package fxchat.com.nestscrolledpulltorecycler.refreshui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.View;

import com.test.MyRecyclerView;


/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 *
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<MyRecyclerView> {

    private MyRecyclerView mRecyclerView;
    /**
     * 用于滑到底部自动加载的Footer
     */
    public LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private OnScrollListener mScrollListener;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshRecyclerView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setPullLoadEnabled(false);
    }

    @Override
    protected MyRecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        mRecyclerView = new MyRecyclerView(context);
//        去掉到头和底的阴影
        mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        return mRecyclerView;
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        mRecyclerView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);//隐藏滚动条
    }


    private String finishText;

    public void setFinishText(String finishText) {
        this.finishText = finishText;
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {

            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                if (footerLoadingLayout instanceof FooterLoadingLayout) {
                    ((FooterLoadingLayout) footerLoadingLayout).setFinishText(finishText);
                }
                footerLoadingLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
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
    public boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    public boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(ILoadingLayout.State.RESET);
        }
    }

    private LoadingLayout mRotateLoadingLayout;

    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        mRotateLoadingLayout = new RotateLoadingLayout(context);
        return mRotateLoadingLayout;
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final RecyclerView.Adapter adapter = mRecyclerView.getAdapter();

        if (null == adapter || adapter.getItemCount() == 0) {
            return true;
        }

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            View firstChild = layoutManager.findViewByPosition(0);
            if (firstChild != null && firstChild.getTop() >= 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final RecyclerView.Adapter adapter = mRecyclerView.getAdapter();

        if (null == adapter || adapter.getItemCount() == 0) {
            return false;
        }

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager ll = (LinearLayoutManager) layoutManager;
            int firstVisibleItem = ll.findFirstVisibleItemPosition();
            int lastVisibleItem = ll.findLastVisibleItemPosition();
            int height = 0;
            if (firstVisibleItem > 0) height += ll.findViewByPosition(firstVisibleItem).getHeight();
            for (int i = firstVisibleItem; i < lastVisibleItem + 1; i++) {
                View view = ll.findViewByPosition(firstVisibleItem);
                height += view.getHeight();
            }
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() == mRecyclerView.getAdapter().getItemCount() - 1;
        }

        return false;
    }

    @Override
    public void resetHeaderLayout() {
        super.resetShowGif();
        super.resetHeaderLayout();
    }

    @Override
    public void resetFooterLayout() {
        super.resetFooterLayout();
    }

    public ILoadingLayout.State getPullDownState() {
        return mPullDownState;
    }

    public void setPullDownState(ILoadingLayout.State state) {
        this.mPullDownState = state;
    }

    public ILoadingLayout.State getPullUpState() {
        return mPullUpState;
    }

    public void setPullUpState(ILoadingLayout.State state) {
        this.mPullUpState = state;
    }

    @Override
    public void startRefreshing() {
        super.startRefreshing();
    }

    @Override
    public void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(ILoadingLayout.State.REFRESHING);
        }
    }

    @Override
    public void pullHeaderLayout(float delta) {
        super.pullHeaderLayout(delta);
    }

    @Override
    public void pullFooterLayout(float delta) {
        super.pullFooterLayout(delta);
    }

    public float getOffsetRadio() {
        return OFFSET_RADIO;
    }

    public boolean isPullDownBusy() {
        if (mPullDownState == ILoadingLayout.State.PULL_TO_REFRESH || mPullDownState == ILoadingLayout.State.REFRESHING || mPullDownState == ILoadingLayout.State.RELEASE_TO_REFRESH) {
            return true;
        }
        return false;
    }

    public boolean isPullUpBusy() {
        if (mPullUpState == ILoadingLayout.State.PULL_TO_REFRESH || mPullUpState == ILoadingLayout.State.REFRESHING || mPullUpState == ILoadingLayout.State.RELEASE_TO_REFRESH) {
            return true;
        }
        return false;
    }
}
