package com.ww.commonlibrary.view.autoLoadMoreRecyclerView;

import com.ww.commonlibrary.R;

/**
 * created by wang.wei on 2019-11-29
 */
public enum RecyclerItemType {
    TYPE_NORMAL(0, 0),
    TYPE_LOADING(1, R.string.list_footer_loading),
    TYPE_LOAD_COMPLETE(2, R.string.list_footer_complete),
    TYPE_EMPTY(3, R.string.list_footer_empty),
    TYPE_NETWORK(4, R.string.list_footer_network),
    TYPE_HEADER(5, -1);
    public int type;

    public int msgId;

    RecyclerItemType(int type, int msgId) {
        this.type = type;
        this.msgId = msgId;
    }
}
