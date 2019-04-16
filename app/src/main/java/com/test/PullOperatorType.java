package com.test;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.test.PullOperatorType.BOTTOM_TRIGGER;
import static com.test.PullOperatorType.PULL_DOWN;
import static com.test.PullOperatorType.PULL_UP;

/**
 * Created by wenjiarong on 2019/4/15 0015.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({PULL_DOWN, PULL_UP, BOTTOM_TRIGGER})
public @interface PullOperatorType {
    int PULL_DOWN = 1;
    int PULL_UP = 2;
    int BOTTOM_TRIGGER = 3;
}
