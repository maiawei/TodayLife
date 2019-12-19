package com.ww.todaylife.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ww.commonlibrary.MyApplication;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.commonlibrary.view.botomSheet.BottomSheetDialogFragment;
import com.ww.todaylife.R;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFullBottomSheetFragment extends BottomSheetDialogFragment {

    private int height = 0;
    private Unbinder unBinder;
    protected Context mContext;
    private BottomSheetDialog dialog;
    private boolean hasLoad;

    public void setDismissListenerListener(DismissListener listener) {
        this.listener = listener;
    }

    private DismissListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }
    //懒加载 优化显示速度
    @Override
    public void onResume() {
        super.onResume();
        if (!hasLoad) {
            initData();
        }
        hasLoad = true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return new BottomSheetDialog(getContext(), R.style.bottomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(providerView(), container, false);
        unBinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        // 设置软键盘不自动弹出
        dialog = (BottomSheetDialog) getDialog();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = height == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : height;
            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            // 初始为展开状态
            behavior.setPeekHeight(height == 0 ? ScreenUtils.getScreenHeight() - ScreenUtils.getStatusBarHeight(MyApplication.getApp()) : height);
        }
    }

    public abstract void initData();

    public abstract int providerView();

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void onDestroy() {
        LogUtils.e("onDestroy");
        if (unBinder != null) {
            unBinder.unbind();
        }
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        hasLoad=false;
        if (listener != null) {
            listener.onDismiss();
        }
       super.onDismiss(dialog);
    }

    public interface DismissListener {
        void onDismiss();
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if(this.isAdded()){
            return;
        }
        super.show(manager, tag);
    }
}
