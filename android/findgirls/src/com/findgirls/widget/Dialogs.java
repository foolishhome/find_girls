package com.findgirls.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appmodel.util.StringUtils;
import com.duowan.mobile.utils.YLog;
import com.findgirls.R;
import com.findgirls.activity.BaseFragmentActivity;
import com.findgirls.app.App;
import com.findgirls.app.AppModel;
import java.io.Serializable;
import java.util.Random;
import eu.inmite.android.lib.dialogs.BaseDialogBuilder;

public class Dialogs {
    public static final String Tag ="findgirl";

    public static boolean showNoLoginMessage(Context activity){
        if (!App.isNetworkConnected(activity)){
            ToastUtil.show(activity, activity.getResources().getString(R.string.str_not_permission));
            YLog.info(Tag, "网络break");
            return true;
        }
        return false;
    }

    public static abstract class BaseDialogFragment extends DialogFragment {
        private boolean isShown = false;

        @Override
        public void show(FragmentManager manager, String tag) {
            super.show(manager, tag);
            isShown = true;
        }

        @Override
        public int show(FragmentTransaction transaction, String tag) {
            int id = super.show(transaction, tag);
            isShown = true;
            return id;
        }

        @Override
        public void dismiss() {
            super.dismiss();
            isShown = false;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            isShown = false;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            isShown = false;
        }

        public boolean isShown() {
            return isShown;
        }
    }
/*
    public static AlertDialog messageDlg(Context c, int titleId, int msgId, DialogInterface.OnClickListener okListener) {
        String title = c.getString(titleId);
        String message = c.getString(msgId);
        return messageDlg(c, title, message, okListener);
    }

    public static AlertDialog messageDlg(Context c, String title, String msg, DialogInterface.OnClickListener btnListener) {
        String btnText = c.getString(R.string.btn_confirm);
        return messageDlg(c, title, msg, btnText, btnListener);
    }

    public static AlertDialog messageDlg(Context c, int titleId, int msgId, int buttonTextId, DialogInterface.OnClickListener btnListener) {
        String title = c.getString(titleId);
        String msg = c.getString(msgId);
        String btnText = c.getString(buttonTextId);
        return messageDlg(c, title, msg, btnText, btnListener);
    }

    public static AlertDialog messageDlg(Context c, String title, String msg, String buttonText, DialogInterface.OnClickListener btnListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(title).setMessage(msg).setPositiveButton(buttonText, btnListener);
        return builder.create();
    }

    public static AlertDialog selectDlg(Context c, String title, CharSequence msg, int positiveId, int negativeId, DialogInterface.OnClickListener click) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveId, click).setNegativeButton(negativeId, click);
        return builder.create();
    }

    public static ProgressDialog progressDlg(Context c, int msgId) {
        ProgressDialog dlg = new ProgressDialog(c);
        dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlg.setMessage(c.getString(msgId));
        // dlg.setOnDismissListener(dismissListener);
        return dlg;
    }
*/
    public static class InputDialogFragment extends YYDialogFragment {

        private InputResultListener inputResultListener;
        private EditText inputEdit;

        public interface InputResultListener {
            void onConfirmed(String text);

            void onCanceled();
        }

        public void setInputResultListener(InputResultListener listener) {
            this.inputResultListener = listener;
        }

        @Override
        public void build() {
            inputEdit = new EditText(getActivity());
            inputEdit.setPadding(20, 20, 0, 20);
            inputEdit.setInputType(InputType.TYPE_CLASS_TEXT);
            setCustomView(inputEdit);
            setPositiveButton(R.string.btn_confirm, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onConfirm();
                }
            });
            setNegativeButton(getString(R.string.btn_cancel), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCancel();
                }
            });
        }

        public void onConfirm() {
            String text = inputEdit.getText().toString();
            AppModel.INSTANCE.dialogModel().dismiss();
            if (inputResultListener != null) {
                inputResultListener.onConfirmed(text);
            }
        }

        public void onCancel() {
            AppModel.INSTANCE.dialogModel().dismiss();
            if (inputResultListener != null) {
                inputResultListener.onCanceled();
            }
        }
    }

    public static class TipDialogFragment extends YYDialogFragment {
        private String titleStr = null;
        private String messageStr = null;

        public void init(String title, String msg, OnClickListener positiveClickListener, OnClickListener negativeClickListener) {
            titleStr = title;
            messageStr = msg;
            onPositiveClickListener = positiveClickListener;
            onNegativeClickListener = negativeClickListener;
        }

        public void setPositiveText(int text) {
            positiveText = text;
        }

        public void setMessage(String message) {
            messageStr = message;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void build() {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_message_dlg, null);
            TextView message = (TextView) view.findViewById(R.id.tv_message);
            if (titleStr != null) {
                setTitle(titleStr);
            }
            if (!StringUtils.isNullOrEmpty(messageStr)) {
                message.setText(messageStr);
                setCustomView(view);
            }

            setPositiveButton(positiveText, onPositiveClickListener);
            setNegativeButton((negativeText == null || negativeText.length() == 0) ? getString(R.string.btn_cancel) : negativeText, onNegativeClickListener);
        }
    }

    public static class MenuDialogFragment extends YYDialogFragment implements AdapterView.OnItemClickListener {
        protected static final String ARG_MENU_ITEMS = "items";
        protected static final String ARG_DATA = "data";
        public static String ARG_TITLE = "title";

        public interface OnMenuItemClickListener {
            void onMenuItemClicked(int requestCode, int position, Object userInfo);
        }

        public static MenuDialogBuilder createMenuBuilder(Context context, FragmentManager fragmentManager) {
            return new MenuDialogBuilder(context, fragmentManager, MenuDialogFragment.class);
        }

        @Override
        public void build() {
            final String title = getTitle();
            if (!TextUtils.isEmpty(title)) {
                setTitle(title);
            }

            final String[] items = getMenuItems();

            if (items != null && items.length > 0) {
                View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_menu, null);
                ListView viewMenu = (ListView) layout.findViewById(R.id.lv_menu);
                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_menu, items);
                viewMenu.setAdapter(adapter);
                viewMenu.setOnItemClickListener(this);
                setCustomView(layout);
            }
        }

        protected String getTitle() {
            return getArguments().getString(ARG_TITLE);
        }

        protected Serializable getData() {
            return getArguments().getSerializable(ARG_DATA);
        }

        protected String[] getMenuItems() {
            return getArguments().getStringArray(ARG_MENU_ITEMS);
        }

        protected int getRequestCode() {
            return getArguments().getInt(BaseDialogBuilder.ARG_REQUEST_CODE, 0);
        }

        private OnMenuItemClickListener getOnMenuItemClickedListener() {

            Activity activity = getActivity();

            if (activity instanceof OnMenuItemClickListener) {
                return (OnMenuItemClickListener) activity;
            }
            return null;
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OnMenuItemClickListener listener = getOnMenuItemClickedListener();
            if (listener != null) {
                listener.onMenuItemClicked(getRequestCode(), position, getData());
            }
            if(dialogMenu != null)
            {
                dialogMenu.handle(getRequestCode(), position, getData(), getActivity());
            }
            AppModel.INSTANCE.dialogModel().dismiss();
        }


        public static class MenuDialogBuilder {
            public static String ARG_REQUEST_CODE = "request_code";
            public static String ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_oto";
            public static String DEFAULT_TAG = "simple_dialog";
            public static int DEFAULT_REQUEST_CODE = -42;

            protected final Context mContext;
            protected final FragmentManager mFragmentManager;
            protected final Class<? extends YYDialogFragment> mClass;

            private Fragment mTargetFragment;
            private boolean mCancelable = true;
            private boolean mCancelableOnTouchOutside = true;

            private String mTag = DEFAULT_TAG;
            private int mRequestCode = DEFAULT_REQUEST_CODE;

            private String title;
            private String[] items;
            private Serializable data;

            protected MenuDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends YYDialogFragment> clazz) {
                mContext = context.getApplicationContext();
                mFragmentManager = fragmentManager;
                mClass = clazz;
            }

            public MenuDialogBuilder setCancelable(boolean cancelable) {
                mCancelable = cancelable;
                return self();
            }

            public MenuDialogBuilder setCancelableOnTouchOutside(boolean cancelable) {
                mCancelableOnTouchOutside = cancelable;
                if (cancelable) {
                    mCancelable = cancelable;
                }
                return self();
            }

            public MenuDialogBuilder setTargetFragment(Fragment fragment, int requestCode) {
                mTargetFragment = fragment;
                mRequestCode = requestCode;
                return self();
            }

            public MenuDialogBuilder setRequestCode(int requestCode) {
                mRequestCode = requestCode;
                return self();
            }

            public MenuDialogBuilder setTag(String tag) {
                mTag = tag;
                return self();
            }


            public YYDialogFragment show() {
                final Bundle args = prepareArguments();

                final YYDialogFragment fragment = (YYDialogFragment) Fragment.instantiate(mContext, mClass.getName(), args);

                args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside);

                if (mTargetFragment != null) {
                    fragment.setTargetFragment(mTargetFragment, mRequestCode);
                } else {
                    args.putInt(ARG_REQUEST_CODE, mRequestCode);
                }
                fragment.setCancelable(mCancelable);
                AppModel.INSTANCE.dialogModel().show(fragment);

                return fragment;
            }

            protected MenuDialogBuilder self() {
                return this;
            }

            public MenuDialogBuilder setTitle(int titleResourceId) {
                title = mContext.getString(titleResourceId);
                return this;
            }


            public MenuDialogBuilder setTitle(String title) {
                this.title = title;
                return this;
            }

            public MenuDialogBuilder setData(Serializable data) {
                this.data = data;
                return this;
            }

            public MenuDialogBuilder setMenuItems(String[] items) {
                this.items = items;
                return this;
            }

            protected Bundle prepareArguments() {
                Bundle args = new Bundle();
                args.putString(ARG_TITLE, title);
                args.putStringArray(ARG_MENU_ITEMS, items);
                args.putSerializable(ARG_DATA, data);
                return args;
            }
        }

        public static class MenuItem {
            public String text;
            public Object data;
        }
    }


    public static class TipBottomDialogFragment extends YYDialogFragment {
        private String messageStr = null;

        public void init(String msg, OnClickListener positiveClickListener) {
            messageStr = msg;
            onPositiveClickListener = positiveClickListener;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Dialog dialog = getDialog();
            if (dialog != null && dialog.getWindow() != null) {
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.y = 10;
                dialog.getWindow().setAttributes(lp);
            }
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void build() {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_message_bottom_dlg, null);
            TextView message = (TextView) view.findViewById(R.id.tv_message);
            if (!StringUtils.isNullOrEmpty(messageStr)) {
                message.setText(messageStr);
                setCustomView(view);
            }
            message.setOnClickListener(onPositiveClickListener);
        }
    }

    public static class MedicalTipBottomDialogFragment extends YYDialogFragment {
        private String messageStr = null;
        private boolean bSignalLine = false;

        public void init(String msg,int msgOK, String msgCancel, OnClickListener positiveClickListener,OnClickListener negativeClickListener) {
            positiveText = msgOK;
            negativeText = msgCancel;
            messageStr  = msg;
            onPositiveClickListener = positiveClickListener;
            onNegativeClickListener = negativeClickListener;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Dialog dialog = getDialog();
            if (dialog != null && dialog.getWindow() != null) {
                dialog.setCanceledOnTouchOutside(true);
            }
            View view = inflater.inflate(R.layout.layout_medical_dialog_fragment, container, false);
            customViewParent = (RelativeLayout) view.findViewById(R.id.rl_custom);
            title = (TextView) view.findViewById(R.id.tv_title);
            btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
            btnCancel = (Button) view.findViewById(R.id.btn_cancel);
            line_vertical = (View) view.findViewById(R.id.line_vertical);
            line_horizontal = (View) view.findViewById(R.id.line_horizontal);
            TextView message = (TextView) view.findViewById(R.id.tv_message);

            setPositiveTextColor(getResources().getColor(R.color.tab_select_text));

            build();
            if (!StringUtils.isNullOrEmpty(titleStr)) {
                title.setText(titleStr);
                title.setVisibility(View.VISIBLE);
            }
            if (customView != null) {
                customViewParent.addView(customView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                customViewParent.setVisibility(View.VISIBLE);
            }


            String positiveStr = getString(positiveText > 0 ? positiveText : R.string.btn_confirm);
            btnConfirm.setText(positiveStr);
            btnConfirm.setVisibility(View.VISIBLE);
            line_vertical.setVisibility(View.VISIBLE);

            if (onPositiveClickListener != null) {
                btnConfirm.setOnClickListener(onPositiveClickListener);
            }else{
                btnConfirm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
            }

            if (onNegativeClickListener != null) {
                btnCancel.setOnClickListener(onNegativeClickListener);
            }else{
                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
            }

            if (negativeText != "") {
                btnCancel.setText(negativeText);
                btnCancel.setVisibility(View.VISIBLE);
                line_horizontal.setVisibility(View.VISIBLE);
            }
            return view;
        }

        @Override
        public void build() {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_medical_message_bottom_dlg, null);
            TextView message = (TextView) view.findViewById(R.id.tv_message);
            if (!StringUtils.isNullOrEmpty(messageStr)) {
                message.setText(messageStr);
                setCustomView(view);
            }
            message.setOnClickListener(onPositiveClickListener);
        }
    }


    public static abstract class YYDialogFragment extends BaseDialogFragment {
        protected RelativeLayout customViewParent;
        protected TextView title;
        protected Button btnConfirm;
        protected Button btnCancel;
        protected View line_horizontal;
        protected View line_vertical;
        protected String titleStr;
        protected int positiveText;
        protected String negativeText;
        protected View customView;
        protected OnClickListener onPositiveClickListener;
        protected OnClickListener onNegativeClickListener;
        protected int positiveTextColor;
        protected boolean canceledOnTouchOutside = true;
        protected DialogMenu dialogMenu;

        @Override
        @Deprecated
        public void show(FragmentManager manager, String tag) {
            if (getTag() == null || getTag().length() == 0) {
                super.show(manager, tag + new Random().nextInt());
            } else {
                super.show(manager, getTag());
            }
        }

        public void show(FragmentActivity activity, String tag) {
            if (activity instanceof BaseFragmentActivity && ((BaseFragmentActivity) activity).isPaused()) {
                return;
            }
            YLog.verbose(this, "dialog tag %s", tag);
            super.show(activity.getSupportFragmentManager(), tag + new Random().nextInt());
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (Build.VERSION.SDK_INT >= 11) {
                setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragmentStyleOver11);
            } else {
                setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Translucent_NoTitle);
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            // safety check
            if (getDialog() == null) {
                return;
            }


            getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            Dialog dialog = getDialog();
            if (dialog != null && dialog.getWindow() != null) {
                dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            }
            View view = inflater.inflate(R.layout.layout_base_dialog_fragment, container, false);
            customViewParent = (RelativeLayout) view.findViewById(R.id.rl_custom);
            title = (TextView) view.findViewById(R.id.tv_title);
            btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
            btnCancel = (Button) view.findViewById(R.id.btn_cancel);
            line_vertical = (View) view.findViewById(R.id.line_vertical);

            build();
            if (!StringUtils.isNullOrEmpty(titleStr)) {
                title.setText(titleStr);
                title.setVisibility(View.VISIBLE);
            }
            if (customView != null) {
                customViewParent.addView(customView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                customViewParent.setVisibility(View.VISIBLE);
            }
            if (onPositiveClickListener != null) {
                String positiveStr = getString(positiveText > 0 ? positiveText : R.string.btn_confirm);
                btnConfirm.setText(positiveStr);
                btnConfirm.setOnClickListener(onPositiveClickListener);
                btnConfirm.setVisibility(View.VISIBLE);
                line_vertical.setVisibility(View.VISIBLE);
            }
            if (positiveTextColor != 0) {
                btnConfirm.setTextColor(positiveTextColor);
            }
            if (onNegativeClickListener != null) {
                btnCancel.setText(negativeText);
                btnCancel.setOnClickListener(onNegativeClickListener);
                btnCancel.setVisibility(View.VISIBLE);
            }
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            if (customView != null) {
                customViewParent.removeView(customView);
            }
        }

        public void setTitle(String text) {
            titleStr = text;
        }

        public void setPositiveButton(int btnText, OnClickListener positiveClickListener) {
            positiveText = btnText;
            onPositiveClickListener = positiveClickListener;
        }

        public void setPositiveTextColor(int color) {
            positiveTextColor = color;
        }

        public void setNegativeButton(String btnText, OnClickListener negativeClickListener) {
            negativeText = btnText;
            onNegativeClickListener = negativeClickListener;
        }

        public void setCustomView(View view) {
            customView = view;
        }

        public void setCanceledOnTouchOutside(boolean cancel) {
            canceledOnTouchOutside = cancel;
        }

        public abstract void build();

        public void setDialogMenu(DialogMenu dialogMenu)
        {
            this.dialogMenu = dialogMenu;
        }
    }

    public static class LoadingDialog extends YYDialogFragment {

        private String message;
        private OnCancelListener onCancelListener;

        public void setMessage(String message) {
            this.message = message;
        }

        public void setOnCancelListener(OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
        }

        @Override
        public void build() {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_progress_dialog, null);
            TextView messageTextView = (TextView) view.findViewById(R.id.tv_message);
            messageTextView.setText(message);
            setCustomView(view);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            if (onCancelListener != null) {
                onCancelListener.onCancel(dialog);
            }
            super.onCancel(dialog);
        }
    }


}
