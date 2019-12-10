package com.ww.todaylife.util;

import android.view.ViewGroup;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * created by wang.wei on 2019-11-26
 */
public class JzInstanceUtil {
    public static ViewGroup jzParentView;
    public static Jzvd Jzvd;
    public static void SetJzParentView(Jzvd jzPlayer) {
        Jzvd=jzPlayer;
        jzParentView=(ViewGroup)Jzvd.getParent();
    }

    public static void restoreJz(Jzvd jz) {
        ViewGroup viewGroup= (ViewGroup) jz.getParent();
        viewGroup.removeView(jz);
        jzParentView.addView(jz, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
