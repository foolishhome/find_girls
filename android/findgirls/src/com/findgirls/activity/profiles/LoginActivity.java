package com.findgirls.activity.profiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appmodel.SDKModel;
import com.appmodel.notification.SDKCallback;
import com.appmodel.util.ImeUtil;
import com.appmodel.util.StringUtils;
import com.duowan.mobile.utils.YLog;
import com.findgirls.R;
import com.findgirls.activity.NavigationUtil;
import com.findgirls.activity.ProfileFragmentActivity;
import com.findgirls.app.AppModel;
import com.findgirls.widget.Dialogs;
import com.findgirls.widget.LoginPortraitView;
import com.findgirls.widget.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;

public class LoginActivity extends ProfileFragmentActivity implements ISimpleDialogListener
        ,TextView.OnEditorActionListener
        ,SDKCallback.Login {
    public static final String FORM_REGIST = "formRegist";
    public static final String KEY_WHO_OPEN = "keyWhoOpen";
    public static final String KEY_ACCOUT = "keyAccout";
    private static final String FAKE_PASSWORD = "##########";
    private TextView accountEditText;
    private TextView passwordEditText;
    private List<SDKModel.AccountInfo> accounts;

    private LoginPortraitView portraitView = null;
    private ImageView portaintImageView = null;

    private View accountLayout;
    private ImageView accountDrop;
    private PopupWindow popupWindow;

    private BaseAdapter accountAdapter;
    private String realPassword = "";
    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            updateInputView();
        }
    };
    private boolean quit = true;
    private Animation.AnimationListener afterPortraitMove = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animation.setFillAfter(true);
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateInputView();
            updateLoginState();
            String userName = accountEditText.getText().toString().trim();
            if (portraitView != null) {
                portraitView.setCurrentUser(userName, true);

                boolean needClearPassword = true;
                for (SDKModel.AccountInfo account : accounts) {
                    if (account.name.equalsIgnoreCase(userName)) {
                        needClearPassword = false;
                        realPassword = account.pwd;
                        passwordEditText.setText(FAKE_PASSWORD);
                        break;
                    }
                }

                if (needClearPassword) {
                    realPassword = "";
                    passwordEditText.setText("");
                }
            } else if (portaintImageView != null) {
                displayAccountPortrait(userName);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImeUtil.hideIME(getActivity());
            }
        });
        accountLayout = findViewById(R.id.ll_account);

        accountEditText = (TextView) findViewById(R.id.et_account);
        accountEditText.setOnFocusChangeListener(focusChangeListener);
        accountEditText.addTextChangedListener(textWatcher);
        passwordEditText = (TextView) findViewById(R.id.et_password);
        passwordEditText.setOnFocusChangeListener(focusChangeListener);
        //passwordEditText.addTextChangedListener(textWatcher);
        passwordEditText.setOnEditorActionListener(this);
        accountDrop = (ImageView) findViewById(R.id.iv_account_drop);
        accountDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountDrop.setImageResource(R.drawable.arrow_up);
                popAccounts();
            }
        });
        findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Dialogs.showNoLoginMessage(getActivity())) {
                    return;
                }
                login();
            }
        });

        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quit = false;
                NavigationUtil.toRegister(getActivity());
            }
        });

        findViewById(R.id.tv_forgot_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quit = false;
                NavigationUtil.toForgotPwd(LoginActivity.this);
            }
        });

        RelativeLayout portaitLayout = (RelativeLayout) findViewById(R.id.rl_portrait);
        getLayoutInflater().inflate(R.layout.activity_login_portraitview, portaitLayout);
        portraitView = (LoginPortraitView) findViewById(R.id.portrait);
        portraitView.initAction();
        portraitView.initUserInfo();

        accounts = AppModel.INSTANCE.sdkModel().getAccounts();
        updateAccountView();

        Intent intent = getIntent();
        if (intent != null) {
            String from = intent.getStringExtra(KEY_WHO_OPEN);
            if (from != null && from.equals(FORM_REGIST)) {
                String accout = intent.getStringExtra(KEY_ACCOUT);
                YLog.error("LoginActivity", "RegisterActivity :: new into LoginActivity.onCreate function --> accout = " + accout);
                if (accout == null) {
                    accountEditText.setText("");
                } else {
                    accountEditText.setText(accout);
                }
                passwordEditText.setText("");
            }

        }
    }


    private void login() {
        YLog.info("Login", "Medical_login_start");
        String username = accountEditText.getText().toString();
        String input = passwordEditText.getText().toString();
        String pass;
        if (FAKE_PASSWORD.equals(input)) {
            pass = realPassword;
        } else {
            pass = input;
        }
        AppModel.INSTANCE.dialogModel().showUnCancelableProgress(R.string.logining);

        String accountName = accountEditText.getText().toString().trim();
        AppModel.INSTANCE.sdkModel().login(username, pass);
    }

    private void updateInputView() {
        /*if (accountEditText.hasFocus() && accountEditText.getText().length() > 0) {
            removeAccount.setVisibility(View.VISIBLE);
        } else {
            removeAccount.setVisibility(View.GONE);
        }

        if (passwordEditText.hasFocus() && passwordEditText.getText().length() > 0) {
            removePassword.setVisibility(View.VISIBLE);
        } else {
            removePassword.setVisibility(View.GONE);
        }*/
    }

    private void updateLoginState() {
        String userName = accountEditText.getText().toString().trim();
        onlineStateShow();
    }

    private void updateAccountView() {
        if (accounts == null || accounts.size() == 0) {
            accountDrop.setVisibility(View.GONE);
            displayAccount(new SDKModel.AccountInfo());
            return;
        }
        for (SDKModel.AccountInfo account : accounts) {
            if (account.uid == AppModel.INSTANCE.sdkModel().myAccount().uid) {
                displayAccount(account);
                return;
            }
        }
        displayAccount(accounts.get(0));
    }

    private long getUidByUserName(String userName) {
        if (StringUtils.isNullOrEmpty(userName)) {
            return 0;
        }

        for (SDKModel.AccountInfo account : accounts) {
            if (account.name != null && userName.equals(account.name)) {
                return account.uid;
            }
        }
        return 0;
    }

    private void onlineStateShow() {
//        loginStateView.setImageResource(R.drawable.status_online);
    }

    public void displayAccount(SDKModel.AccountInfo account) {
        //Image.loadPortrait(portrait, account.portraitFile);
        String accountName = getDisplayUserName(account.name);
        accountEditText.setText(accountName);
        if (!StringUtils.isNullOrEmpty(account.pwd)) {

            passwordEditText.setText(FAKE_PASSWORD);
            realPassword = account.pwd;
        } else {
            realPassword = "";
            passwordEditText.setText("");
        }
        if (!StringUtils.isNullOrEmpty(accountName)) {
            //accountEditText.setSelection(accountName.length());
        }
    }

    private void displayAccountPortrait(String accountName) {
        for (SDKModel.AccountInfo account : accounts) {
            if (account.name != null && account.name.equals(accountName)) {
                ImageLoader.getInstance().displayImage(account.portraitUrl, portaintImageView);
                displayAccount(account);
                return;
            }
        }
        passwordEditText.setText("");
        portaintImageView.setImageResource(R.drawable.def_portrait_online);
    }

    private void popAccounts() {
        if (popupWindow == null) {
            accountAdapter = new BaseAdapter() {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    ViewHolder holder;
                    if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.layout_account_item, parent, false);
                        holder = new ViewHolder();
                        holder.portrait = (ImageView) convertView.findViewById(R.id.account_item_portrait);
                        holder.username = (TextView) convertView.findViewById(R.id.account_item_username);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    SDKModel.AccountInfo info = accounts.get(position);
                    String portraitUrl = info.portraitUrl;
                    ImageLoader.getInstance().displayImage(portraitUrl, holder.portrait);
                    holder.username.setText(getDisplayUserName(info.name));

                    ImageView removeIcon = (ImageView) convertView.findViewById(R.id.account_item_remove);

                    removeIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popRemoveAccount(position);
                        }
                    });

                    return convertView;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public int getCount() {
                    return accounts.size();
                }
            };
            ListView listView = new ListView(getActivity());
            listView.setAdapter(accountAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SDKModel.AccountInfo info = accounts.get(position);
                    if (info != null) {
                        displayAccount(info);
                        popupWindow.dismiss();
                    }

                }
            });
            popupWindow = new PopupWindow(getActivity());
            popupWindow.setContentView(listView);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_account_list));
            popupWindow.setWidth(accountLayout.getWidth());
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    accountDrop.setImageResource(R.drawable.arrow_down);
                }
            });
        }
        popupWindow.showAsDropDown(accountEditText);
        popupWindow.getContentView().requestFocus();
    }

    private void popRemoveAccount(final int position) {
        popupWindow.dismiss();
        Dialogs.TipDialogFragment deleteAccountDialog = new Dialogs.TipDialogFragment();
        deleteAccountDialog.setTitle(getString(R.string.remove_account));
        deleteAccountDialog.setMessage(getString(R.string.remove_account_message, accounts.get(position).name));
        deleteAccountDialog.setPositiveButton(R.string.remove, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < accounts.size()) {
                    AppModel.INSTANCE.sdkModel().deleteAccount(accounts.get(position).name);
                    accounts.remove(position);
                    updateAccountView();
                    accountAdapter.notifyDataSetChanged();

                    if (Build.VERSION.SDK_INT > 11) {
                        if (portraitView != null) {
                            portraitView.initUserInfo();
                            portraitView.invalidate();
                        }
                    } else {
                    }

                    AppModel.INSTANCE.dialogModel().dismiss();
                }
            }
        });
        deleteAccountDialog.setNegativeButton(getString(R.string.btn_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppModel.INSTANCE.dialogModel().dismiss();
            }
        });
        deleteAccountDialog.setPositiveTextColor(Color.RED);
        AppModel.INSTANCE.dialogModel().show(deleteAccountDialog);
    }

    private String getDisplayUserName(String username) {
        if (StringUtils.isNameMatchMobilePattern(username)) {
            username = StringUtils.getMobileFromName(username);
        }
        return username;
    }

    @Override
    public void onSuccess() {
        YLog.info("Login", "Medical_login_onSuccess");

        dismissLogin();
        finish();
    }

    @Override
    public void onError(int result, String message) {
        YLog.error("Login", "Medical_login_onError%s", message);
//        if (result == TypeInfo.LoginResult.LoginResultUserNonexist || result == TypeInfo.LoginResult.LoginResultPasswdError) {
//            showError(getString(R.string.login_error));
//        } else {
//            if (TypeInfo.LoginResult.LoginResultPicCodeFailed == result) {
//                layout_identify_code.setVisibility(View.GONE);
//            }
//            showError(message);
//        }
        passwordEditText.requestFocus();
    }

    private void showError(String message) {
        dismissLogin();
        ToastUtil.show(this, message);
    }

    private void dismissLogin() {
        AppModel.INSTANCE.dialogModel().dismiss();
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        super.onPositiveButtonClicked(requestCode);
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        //nothing
    }


    @Override
    protected void onResume() {
        super.onResume();
        quit = true;

        updateLoginState();

        if (accounts != null && !accounts.isEmpty()) {
            accountEditText.clearFocus();
            if (portraitView != null) {
                portraitView.requestFocus();
            } else if (portaintImageView != null) {
                portaintImageView.requestFocus();
            }
        } else {
            accountEditText.requestFocus();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            login();
        }
        return false;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (tryQuitApp()) {
            finish();
        }
    }

    private boolean tryQuitApp() {
        if (!canGoBack() && quit == true) {
            AppModel.INSTANCE.exit(this);
            return true;
        } else {
            return false;
        }
    }

    static class ViewHolder {
        ImageView portrait;
        TextView username;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLogin();
        YLog.info("Login","LoginActivity OnDestroy");
    }
}
