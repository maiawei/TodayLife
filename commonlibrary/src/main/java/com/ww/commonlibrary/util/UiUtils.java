package com.ww.commonlibrary.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ww.commonlibrary.R;
import com.ww.commonlibrary.view.ToastView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;


/**
 * Created by wang.wei on 2018/2/7.
 */

public class UiUtils {
    //弱引用 有助于重复利用以及内存回收
    private static WeakReference<ToastView> sToastRef = null;
    private static WeakReference<MaterialDialog> sProgressDialogRef = null;


    public static void hideProgressDialog() {
        if (sProgressDialogRef != null) {
            if (sProgressDialogRef.get() != null) {
                sProgressDialogRef.get().dismiss();
            }
        }
    }

    public static void showConfirmDialog(Context context, int contentId, final DialogClickListener listener) {
        new MaterialDialog.Builder(context)
                .title(R.string.dialog_title)
                .content(contentId)
                .positiveText(R.string.dialog_ok)
                .negativeText(R.string.dialog_cancel)
                .titleColorRes(R.color.black)
                .contentColorRes(R.color.black_333)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NotNull MaterialDialog dialog, @NotNull DialogAction which) {
                        listener.onClick(which);
                    }
                }).show();
    }

    public static void showProgressDialog(Context context, int contentId) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .content(contentId)
                .contentColorRes(R.color.black_333)
                .progress(true, 0).show();
        hideProgressDialog();
        sProgressDialogRef = new WeakReference<>(dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public static void showSingleChoiceDialog(Context context, int titleId, int itemsId, int selected, final DialogItemSelectedListener listener) {
        new MaterialDialog.Builder(context)
                .title(titleId)
                .items(itemsId)
                .titleColorRes(R.color.black)
                .contentColorRes(R.color.black_333)
                .itemsCallbackSingleChoice(selected, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        listener.onItemSelected(which);
                        return true;
                    }
                })
                .show();
    }


    public static void showLongToast(Context context, String msg, int status) {
        showToast(Toast.LENGTH_LONG, msg, context, status);
    }

    public static void showToast(int duration, String msg, Context context, int status) {
        ToastView toast;
        if (sToastRef == null || sToastRef.get() == null) {
            toast = new ToastView(context);
            sToastRef = new WeakReference<ToastView>(toast);
        } else {
            toast = sToastRef.get();
        }
        toast.setDuration(duration);
        toast.show(msg, status);
    }

    public static void showShortToast(Context context, String msg, int status) {
        showToast(Toast.LENGTH_SHORT, msg, context, status);
    }

    public static void showShortToast(Context context, String msg) {
        showToast(Toast.LENGTH_SHORT, msg, context, ToastView.TYPE_ERROR);
    }

    /**
     * 显示输入法
     * showSoftInput
     *
     * @param activity
     * @since 1.0
     */
    public static void showSoftInput(Activity activity) {
        // activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }


    /**
     * 隐藏输入法
     * hideSoftInput
     *
     * @param activity
     * @since 1.0
     */
    public static void hideSoftInput(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
    }

    public interface DialogClickListener {
        public void onClick(DialogAction type);
    }

    public interface DialogItemSelectedListener {
        public void onItemSelected(int position);
    }
}
