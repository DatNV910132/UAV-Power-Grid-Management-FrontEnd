package com.dji.sdk.sample.internal.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.dji.sdk.sample.R;
import dji.common.error.DJIError;

/**
 * Created by dji on 2/3/16.
 */
public class DialogUtils {
    /**
     * Show dialog.
     *
     * @param ctx the ctx
     * @param str the str
     */
    public static void showDialog(Context ctx, String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.set_dialog);
        builder.setMessage(str);
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Show dialog.
     *
     * @param ctx   the ctx
     * @param strId the str id
     */
    public static void showDialog(Context ctx, int strId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.set_dialog);
        builder.setMessage(strId);
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Show confirmation dialog.
     *
     * @param ctx             the ctx
     * @param strId           the str id
     * @param onClickListener the on click listener
     */
    public static void showConfirmationDialog(Context ctx, int strId,
                                              DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.set_dialog);
        builder.setMessage(strId);
        builder.setPositiveButton(android.R.string.ok, onClickListener);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Show dialog based on error.
     *
     * @param ctx      the ctx
     * @param djiError the dji error
     */
    public static void showDialogBasedOnError(Context ctx, DJIError djiError) {
        if (null == djiError) {
            showDialog(ctx, R.string.success);
        } else {
            showDialog(ctx, djiError.getDescription());
        }
    }
}
