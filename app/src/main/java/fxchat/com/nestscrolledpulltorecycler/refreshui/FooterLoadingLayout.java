package fxchat.com.nestscrolledpulltorecycler.refreshui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import fxchat.com.nestscrolledpulltorecycler.R;


/**
 * 这个类封装了下拉刷新的布局
 *
 * @author Li Hong
 * @since 2013-7-30
 */
public class FooterLoadingLayout extends LoadingLayout {
    /**
     * 进度条
     */
    private GifView mProgressBar;
    /**
     * 显示的文本
     */
    private TextView mHintView;

    /**
     * 构造方法
     *
     * @param context context
     */
    public FooterLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    private String finishText;

    public void setFinishText(String finishText) {
        this.finishText = finishText;
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        mProgressBar = (GifView) findViewById(R.id.MyGifView);
        mHintView = (TextView) findViewById(R.id.pull_to_load_footer_hint_textview);
        //   mProgressBar.setMovieResource(R.drawable.asd);
        setState(State.RESET);
    }

    private View loadView;

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_load_footer, null);
        loadView = container.findViewById(R.id.loadView);
        return container;
    }

    @Override
    public void setBackgroundColor(int color) {
        loadView.setBackgroundColor(color);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
    }

    @Override
    public int getContentSize() {
        View view = findViewById(R.id.pull_to_load_footer_content);
        if (null != view) {
            return view.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 40);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        mProgressBar.setVisibility(View.GONE);
        mHintView.setVisibility(View.INVISIBLE);

        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mHintView.setText(R.string.loading);
    }

    @Override
    protected void onPullToRefresh() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_normal2);
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    @Override
    protected void onRefreshing() {
        mProgressBar.setVisibility(View.VISIBLE);
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.loading);
//        if(finishText==null){
//            mHintView.setText("");
//        }else {
//            mHintView.setText(R.string.loading);
//        }

    }

    @Override
    protected void onNoMoreData() {
        mHintView.setVisibility(View.VISIBLE);
        if (finishText == null) {
            mHintView.setText(R.string.pushmsg_center_no_more_msg);
        } else {
            mHintView.setText(finishText);
        }
    }
}
