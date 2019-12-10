package com.ww.todaylife.bean.httpResponse;

import androidx.room.Embedded;

import java.io.Serializable;

public class NewsDisplayInfo implements Serializable {
    @Embedded(prefix = "bb_")

    public NewsSelfInfo self_info;
}
