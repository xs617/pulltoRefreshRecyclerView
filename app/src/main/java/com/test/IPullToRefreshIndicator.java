package com.test;

/**
 * Created by wenjiarong on 2019/4/15 0015.
 */
public interface IPullToRefreshIndicator {
    boolean isEnable(@PullOperatorType int type);

    boolean isReady(@PullOperatorType int type);

    void setEnable(@PullOperatorType int type, boolean isEnable);

    void setReady(@PullOperatorType int type, boolean isReady);
}
