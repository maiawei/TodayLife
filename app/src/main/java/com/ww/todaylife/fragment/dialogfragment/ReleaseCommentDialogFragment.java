package com.ww.todaylife.fragment.dialogfragment;


import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ww.commonlibrary.util.ScreenUtils;
import com.ww.todaylife.R;

import java.lang.reflect.Field;


public class ReleaseCommentDialogFragment extends DialogFragment {
    private EditText mEdit;
    private TextView mReleaseTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.release_comment_bottom, container, false);
        mEdit = view.findViewById(R.id.commentEdit);
        mReleaseTv = view.findViewById(R.id.releaseTv);
        return view;
    }

    @Override
    public void onStart() {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ScreenUtils.getScreenWidth();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(params);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        super.onStart();
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
