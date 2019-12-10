package com.ww.commonlibrary.view.botomSheet;




import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.id;
import com.google.android.material.R.layout;
import com.google.android.material.R.style;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


/**
 * created by wang.wei on 2019-12-07
 */


public class BottomSheetDialog extends AppCompatDialog {
    private BottomSheetBehavior<FrameLayout> behavior;
    boolean cancelable;
    private boolean canceledOnTouchOutside;
    private boolean canceledOnTouchOutsideSet;
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback;
    class NamelessClass_1 extends BottomSheetBehavior.BottomSheetCallback {
        NamelessClass_1() {
        }

        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == 5) {
                BottomSheetDialog.this.cancel();
            }

        }

        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    }
    public BottomSheetDialog(@NonNull Context context) {
        this(context, 0);
    }

    public BottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, getThemeResId(context, theme));
        this.cancelable = true;
        this.canceledOnTouchOutside = true;
        this.bottomSheetCallback = new NamelessClass_1();
        this.supportRequestWindowFeature(1);
    }
    
    protected BottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.cancelable = true;
        this.canceledOnTouchOutside = true;

        

        this.bottomSheetCallback = new NamelessClass_1();
        this.supportRequestWindowFeature(1);
        this.cancelable = cancelable;
    }

    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(this.wrapInBottomSheet(layoutResId, (View)null, (LayoutParams)null));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        if (window != null) {
            if (VERSION.SDK_INT >= 21) {
                window.clearFlags(67108864);
                window.addFlags(-2147483648);
            }

            window.setLayout(-1, -1);
        }

    }

    public void setContentView(View view) {
        super.setContentView(this.wrapInBottomSheet(0, view, (LayoutParams)null));
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(this.wrapInBottomSheet(0, view, params));
    }

    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        if (this.cancelable != cancelable) {
            this.cancelable = cancelable;
            if (this.behavior != null) {
                this.behavior.setHideable(cancelable);
            }
        }

    }

    protected void onStart() {
        super.onStart();
        if (this.behavior != null && this.behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            this.behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel && !this.cancelable) {
            this.cancelable = true;
        }

        this.canceledOnTouchOutside = cancel;
        this.canceledOnTouchOutsideSet = true;
    }

    private View wrapInBottomSheet(int layoutResId, View view, LayoutParams params) {
        FrameLayout container = (FrameLayout)View.inflate(this.getContext(), layout.design_bottom_sheet_dialog, (ViewGroup)null);
        CoordinatorLayout coordinator = (CoordinatorLayout)container.findViewById(id.coordinator);
        if (layoutResId != 0 && view == null) {
            view = this.getLayoutInflater().inflate(layoutResId, coordinator, false);
        }

        FrameLayout bottomSheet = (FrameLayout)coordinator.findViewById(id.design_bottom_sheet);
        this.behavior = BottomSheetBehavior.from(bottomSheet);
        this.behavior.setBottomSheetCallback(this.bottomSheetCallback);
        this.behavior.setHideable(this.cancelable);
        if (params == null) {
            bottomSheet.addView(view);
        } else {
            bottomSheet.addView(view, params);
        }

        coordinator.findViewById(id.touch_outside).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (BottomSheetDialog.this.cancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside()) {
                    BottomSheetDialog.this.cancel();
                }

            }
        });
        ViewCompat.setAccessibilityDelegate(bottomSheet, new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                if (BottomSheetDialog.this.cancelable) {
                    info.addAction(1048576);
                    info.setDismissable(true);
                } else {
                    info.setDismissable(false);
                }

            }

            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                if (action == 1048576 && BottomSheetDialog.this.cancelable) {
                    BottomSheetDialog.this.cancel();
                    return true;
                } else {
                    return super.performAccessibilityAction(host, action, args);
                }
            }
        });
        bottomSheet.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return true;
            }
        });
        return container;
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!this.canceledOnTouchOutsideSet) {
            TypedArray a = this.getContext().obtainStyledAttributes(new int[]{16843611});
            this.canceledOnTouchOutside = a.getBoolean(0, true);
            a.recycle();
            this.canceledOnTouchOutsideSet = true;
        }

        return this.canceledOnTouchOutside;
    }

    private static int getThemeResId(Context context, int themeId) {
        if (themeId == 0) {
            TypedValue outValue = new TypedValue();
            if (context.getTheme().resolveAttribute(attr.bottomSheetDialogTheme, outValue, true)) {
                themeId = outValue.resourceId;
            } else {
                themeId = style.Theme_Design_Light_BottomSheetDialog;
            }
        }

        return themeId;
    }
}

