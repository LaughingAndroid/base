package com.kibey.android.data.model;

import java.io.Serializable;

public class Model implements Serializable, IKeepProguard {
    /**
     *
     */
    private static final long serialVersionUID = 8213253712901639198L;

    public String getId() {
        return null;
    }

    public String getEchoViewType() {
        return getClass().getName();
    }
}
