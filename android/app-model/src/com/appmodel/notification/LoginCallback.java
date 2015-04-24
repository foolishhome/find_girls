package com.appmodel.notification;

public interface LoginCallback {

    /**
     * 登录结果回调
     */
    interface Result {

        /**
         * 登录成功,可以通过调用{@code com.yy.a.appmodel.LoginModel.getUid()}获取登录用户uid
         */
        void onSuccess();

        /**
         * 登录失败
         *
         * @param result  错误类型
         * @param message 错误信息,中文
         */
        void onError(int result, String message);
    }


    interface Logout {
        void onLogout();
    }

    interface NetworkStatus {
        void onNetworkStatusChange(boolean online);
    }
}