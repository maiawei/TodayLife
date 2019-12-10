package com.ww.commonlibrary.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.ww.commonlibrary.R;
import com.ww.commonlibrary.view.ToastView;

import java.lang.ref.WeakReference;


/**
 * Created by wang.wei on 2018/2/7.
 */

public class UiUtils {
    //弱引用 有助于重复利用以及内存回收
    private static WeakReference<ToastView> sToastRef = null;


    public static void showAProgressDialog(final Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if (context != null) {
                        ((Activity) context).finish();
                    }
                }
                return false;
            }
        });
        // sProgressDialogRef = new WeakReference<ProgressDialog>(progressDialog);
        progressDialog.show();
    }



    public static AlertDialog showAlertDialog(Context context, String title, String message, final DialogClickListener listener) {
        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog).setTitle(title).setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null)
                            listener.okClick();
                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.cancelClick();
                        }
                        dialog.dismiss();
                    }
                }
        );

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    public static Dialog showAlertDialog(Context context, String message, DialogClickListener listener) {
        return showAlertDialog(context, "提示", message, listener);
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
        public void okClick();

        public void cancelClick();
    }

}
