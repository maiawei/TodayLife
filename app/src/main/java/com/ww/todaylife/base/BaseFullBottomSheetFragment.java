package com.ww.todaylife.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ww.commonlibrary.util.LogUtils;
import com.ww.commonlibrary.view.botomSheet.BottomSheetDialogFragment;
import com.ww.todaylife.R;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static java.security.AccessController.getContext;


public abstract class BaseFullBottomSheetFragment extends BottomSheetDialogFragment {

    private int height = 0;
    private Unbinder unbinder;
    protected Context mContext;
    private Dialog dialog;
    public void setDialogDisMissListener(DialogDisMissListener listener) {
        this.listener = listener;
    }

    private DialogDisMissListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return new BottomSheetDialog(getContext(), R.style.TransBottomSheetDialogStyle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(providerView(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onStart();
        dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = height == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : height; //可以写入自己想要的高度
        }
        final View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (listener != null) {
            listener.onDialogDisMiss();
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        if(dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
        super.onDestroy();
    }

    public abstract void initData();

    public abstract int providerView();

    public void setHeight(int height) {
        this.height = height;
    }

    public interface DialogDisMissListener {
        void onDialogDisMiss();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            Field shownByMe = DialogFragment.class.getDeclaredField("mShownByMe");
            dismissed.setAccessible(true);
            shownByMe.setAccessible(true);
            dismissed.set(this, false);
            shownByMe.set(this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        // replace commit()
        ft.commitAllowingStateLoss();
    }
}
