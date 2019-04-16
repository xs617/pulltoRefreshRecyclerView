package com.test;

import android.view.View;

/**
 * Created by wenjiarong on 2019/4/15 0015.
 */
public interface IPullToRefreshOperator {
    void reset(@PullViewType int type);

    void pull(@PullViewType int type, int delta);

    View getView(@PullViewType int type);

    void startDoingAnimation(@PullViewType int type);

    void stopDoingAnimation(@PullViewType int type);

}
