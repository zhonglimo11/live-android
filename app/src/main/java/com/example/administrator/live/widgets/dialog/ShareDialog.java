package com.example.administrator.live.widgets.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.administrator.live.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ShareDialog extends BottomSheetDialog {

    public ShareDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_share);

    }
}
