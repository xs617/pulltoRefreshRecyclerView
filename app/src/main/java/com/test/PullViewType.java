package com.test;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.test.PullViewType.FOOTER;
import static com.test.PullViewType.HEADER;

/**
 * Created by wenjiarong on 2019/4/15 0015.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({HEADER, FOOTER})
public @interface PullViewType {
    int HEADER = 1;
    int FOOTER = 2;
}
