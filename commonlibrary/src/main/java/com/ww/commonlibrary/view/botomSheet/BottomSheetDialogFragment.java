package com.ww.commonlibrary.view.botomSheet;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


/**
 * created by wang.wei on 2019-12-07
 */
public class BottomSheetDialogFragment extends AppCompatDialogFragment {
    public BottomSheetDialogFragment() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(this.getContext(), this.getTheme());
    }
}
