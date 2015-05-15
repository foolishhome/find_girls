package com.findgirls.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.findgirls.R;

import java.lang.ref.WeakReference;

public class ToastUtil {

    private static WeakReference<Toast> toast = new WeakReference<Toast>(null);

    public static void show(Context context, String message) {
        makeToast(context, message);
    }

    private static Toast makeToast(Context context, String message) {
        Toast weakToast = toast.get();
        if (weakToast == null) {
            weakToast = makeToast(context.getApplicationContext(), message, Toast.LENGTH_LONG);
            toast = new WeakReference<Toast>(weakToast);
            weakToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            TextView textView = (TextView) weakToast.getView().findViewById(R.id.tv_toast);
            textView.setText(message);
        }
        weakToast.show();
        return weakToast;
    }

    private static Toast makeToast(Context context, String message, int duration) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_view,null);
        TextView textView = (TextView) toastView.findViewById(R.id.tv_toast);
        textView.setText(message);

        Toast toast = new Toast(context);
        toast.setView(toastView);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER,0,0);
        return toast;
    }

    public static void show(Context context, int messageId) {
        makeToast(context, context.getString(messageId));
    }

}
