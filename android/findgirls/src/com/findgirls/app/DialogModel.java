package com.findgirls.app;

import android.app.Application;
import android.content.DialogInterface;
import android.os.Handler;

import com.appmodel.Model;
import com.appmodel.util.notification.NotificationCenter;
import com.duowan.mobile.utils.YLog;
import com.findgirls.activity.BaseFragmentActivity;
import com.findgirls.widget.Dialogs;

public class DialogModel extends Model {
    private BaseFragmentActivity activeActivity;
    private Dialogs.YYDialogFragment dialog;

    @Override
    public void unInit() {

    }

    @Override
    public void clear() {

    }

    @Override
    public void init(Application application, Handler ioHandler) {
        super.init(application, ioHandler);

        NotificationCenter.INSTANCE.addObserver(this);
    }

    public void onResumeFragments(BaseFragmentActivity activity) {
        YLog.debug(this, "onResumeFragments: %s", activity);
        activeActivity = activity;
        doShowDialog(activity, dialog);
    }
    public void onPause(BaseFragmentActivity activity) {
        YLog.debug(this, "onPause: %s", activity);
        if (dialog != null) {
            if (!dialog.isShown()) {
                YLog.debug(this, "already dismissed or canceled, dialog: %s", dialog);
                dialog = null;
            } else {
                dialog.dismiss();
            }
        }
        activeActivity = null;
    }

    public void show(Dialogs.YYDialogFragment dialog) {
        if (dialog == null) {
            return;
        }
        if (this.dialog == dialog && dialog.isShown()) {
            return;
        }
        dismiss();
        this.dialog = dialog;
        doShowDialog(activeActivity, dialog);
    }

    public void dismiss() {
        doDismissDialog(activeActivity);
    }

    public void showUnCancelableProgress(int message) {
        showUnCancelableProgress(getString(message));
    }
    public void showUnCancelableProgress(String message) {
        showProgress(message, false, null);
    }
    public void showProgress(String message, boolean isCancelable, DialogInterface.OnCancelListener onCancelListener) {
        showProgress(message, isCancelable, false, onCancelListener);
    }
    public void showProgress(String message, boolean isCancelable, boolean canceledOnTouchOutside, DialogInterface.OnCancelListener onCancelListener) {
        Dialogs.LoadingDialog loadingDialog = new Dialogs.LoadingDialog();
        loadingDialog.setMessage(message);
        loadingDialog.setCancelable(isCancelable);
        loadingDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        if (isCancelable && onCancelListener != null) {
            loadingDialog.setOnCancelListener(onCancelListener);
        }
        show(loadingDialog);
    }

    private void doShowDialog(BaseFragmentActivity activity, Dialogs.YYDialogFragment dialog) {
        if (dialog != null) {
            if (!dialog.isShown() && activity != null && !activity.isPaused()) {
                YLog.debug(this, "show, dialog: %s, activity: %s", dialog, activity);
                dialog.show(activity, dialog.toString());
            } else {
                YLog.warn(this, "show dialog failed, dialog: %s, activity: %s, isShown: %b, isPaused: %b", dialog, activity, dialog.isShown(), activity == null || activity.isPaused());
            }
        }
    }

    private void doDismissDialog(BaseFragmentActivity activity) {
        if (dialog != null) {
            if (!dialog.isShown()) {
                YLog.debug(this, "already dismissed or never shown, dialog: %s, activity: %s", dialog, activity);
                dialog = null;
                return;
            }
            if (activity != null && !activity.isPaused()) {
                YLog.debug(this, "dimiss, dialog: %s, activity: %s", dialog, activity);
                dialog.dismiss();
                dialog = null;
            } else {
                YLog.warn(this, "dismiss dialog failed, dialog: %s, activity: %s, isShown: %b, isPaused: %b", dialog, activity, dialog.isShown(), activity == null || activity.isPaused());
                dialog = null;
            }
        }
    }
}
