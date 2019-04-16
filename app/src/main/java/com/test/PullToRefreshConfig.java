package com.test;

import android.view.View;

/**
 * Created by wenjiarong on 2019/4/15 0015.
 */
public class PullToRefreshConfig {
    private View headerView;
    private View footerView;

    IPullToRefreshOperator iPullToRefreshOperator;
    IPullToRefreshIndicator iPullToRefreshIndicator;

    private PullToRefreshConfig() {
    }

    public static class Builder {
        PullToRefreshConfig pullToRefreshConfig = new PullToRefreshConfig();

        public PullToRefreshConfig build() {
            return pullToRefreshConfig;
        }

        public Builder setHeader(View header) {
            pullToRefreshConfig.headerView = header;
            return this;
        }

        public Builder setFooter(View footer) {
            pullToRefreshConfig.footerView = footer;
            return this;
        }

        public Builder setPullToRefreshOperator(IPullToRefreshOperator operator) {
            pullToRefreshConfig.iPullToRefreshOperator = operator;
            return this;
        }

        public Builder setPullToRefreshIndicator(IPullToRefreshIndicator indicator) {
            pullToRefreshConfig.iPullToRefreshIndicator = indicator;
            return this;
        }
    }
}
