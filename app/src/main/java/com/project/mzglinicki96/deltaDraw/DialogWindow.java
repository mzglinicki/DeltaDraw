package com.project.mzglinicki96.deltaDraw;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by mazg on 06.03.2017.
 */

public class DialogWindow {

    public DialogWindow(final Context context, final int messageResId, final int positiveBtnResId, final int negativeBtnResId,
                        final View view, final boolean isCancelable,
                        final DialogActionListener dialogActionListener) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(messageResId);

        if (view != null) {
            builder.setView(view);
        }

        builder.setPositiveButton(positiveBtnResId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialogActionListener.onPositiveBtnClick();
            }
        })
                .setNegativeButton(negativeBtnResId, null)
                .setCancelable(isCancelable)
                .create()
                .show();
    }
}
