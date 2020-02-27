package com.dzm.ffmpeg.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.ViewTreeObserver;

/**
 * @author Denggggggggggggg
 * @Description:
 * @date :19-5-21 下午4:12
 */
public final class DetachableDialogDismissListener implements DialogInterface.OnDismissListener {

    public static DetachableDialogDismissListener wrap(DialogInterface.OnDismissListener delegate) {
        return new DetachableDialogDismissListener(delegate);
    }

    private DialogInterface.OnDismissListener delegateOrNull;

    private DetachableDialogDismissListener(DialogInterface.OnDismissListener delegate) {
        this.delegateOrNull = delegate;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (delegateOrNull != null) {
            delegateOrNull.onDismiss(dialog);
            delegateOrNull = null;
        }
    }

    public void clearOnDetach(Dialog dialog) {
        dialog.getWindow()
                .getDecorView()
                .getViewTreeObserver()
                .addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
                    @Override
                    public void onWindowAttached() {
                        // do nothing
                    }

                    @Override
                    public void onWindowDetached() {
                        if (delegateOrNull != null) {
                            delegateOrNull.onDismiss(dialog);
                            delegateOrNull = null;
                        }
                    }
                });
    }
}
