package com.appmodel;

import android.app.Application;
import android.os.Handler;

import com.appmodel.util.notification.NotificationCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamelin on 5/14/15.
 */
public class SDKModel extends Model {
    public static class AccountInfo {
        public long uid;
        public String name;
        public String email;
        public String pwd;
        public String portraitUrl;
    }
    private AccountInfo accountInfo = new AccountInfo();

    @Override
    public void unInit() {
        NotificationCenter.INSTANCE.removeObserver(this);
    }

    @Override
    public void clear() {

    }

    @Override
    public void init(Application application, Handler ioHandler) {
        super.init(application, ioHandler);

        NotificationCenter.INSTANCE.addObserver(this);
    }

    public void login(String username, String pwd) {

    }

    public void logout(boolean b) {

    }

    public AccountInfo myAccount() {
        return accountInfo;
    }
    public List<AccountInfo> getAccounts() {
        return new ArrayList<AccountInfo>();
    }
    public void deleteAccount(String account) {

    }
    public void submitFeedback(String suggestion) {

    }

    public void queryLastVersionInfo(String version) {

    }
}
