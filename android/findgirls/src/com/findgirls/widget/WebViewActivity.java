package com.findgirls.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.duowan.mobile.utils.YLog;
import com.findgirls.R;
import com.findgirls.activity.BaseFragmentActivity;
import com.findgirls.activity.NavigationUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity extends BaseFragmentActivity {

    public static final String URL = "url";
    public static final String JSOPERATION_YYMEDICAL = "yymedical";
    public static final String WEB_INTENT = "web_view_activity";
    private static final int STATUS_ONLINE = 1;
    private static final int STATUS_OFFLINE = 0;
    private static final String KEY_NAME = "page";
    private static final String KEY_ID = "sid";
    private static final String KEY_SUBID = "subSid";
    private static final String NAME_JUMP_TO_LOGIN = "login";
    private static final String NAME_JUMP_TO_MESSAGE = "message";
    private static final String NAME_JUMP_TO_LIVE = "live";
    private static final String NAME_JUMP_TO_PROFILE = "profile";
    private static final int TAB_LIVE = 0;
    private static final int TAB_MESSAGE = 1;
    private static final int TAB_PROFILE = 2;
    private static final String FLAG_TICKET = "${ticket}";
    private static final String FLAG_TICKET_REPLACED = "ticket=";
    CustomWebChromeClient chromeClient = new CustomWebChromeClient();
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private CusWebViewClient mCusWebViewClient = new CusWebViewClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);
        String url = "";
        if (getIntent().getData() != null) {
            url = getIntent().getData().toString();
        }
        if (TextUtils.isEmpty(url)) {
            url = getIntent().getStringExtra(URL);
        }
        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }
        mWebView = (WebView) findViewById(R.id.content_webView);

        mWebView.setWebViewClient(mCusWebViewClient);
        mWebView.setWebChromeClient(chromeClient);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true); //支持缩放
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(new JsOperation(this), JSOPERATION_YYMEDICAL);
        mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        mWebView.clearFormData();
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mWebView.clearHistory();

        mProgressBar = (ProgressBar) findViewById(R.id.load_progress);

        mWebView.loadUrl(url);
    }

    @Override
    protected void onPause() {
        CookieSyncManager.createInstance(WebViewActivity.this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeAllCookie();
        super.onPause();
    }

    class CusWebViewClient extends WebViewClient {
        private boolean mStartDownloadRes = false;

        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);
            mStartDownloadRes = true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            YLog.verbose(this, url);
            if (url.contains(FLAG_TICKET)) {
                url = url.replace(FLAG_TICKET, FLAG_TICKET_REPLACED);
                view.loadUrl(url);
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            YLog.verbose(this, url);

            if (mStartDownloadRes) {
                mStartDownloadRes = false;
                getSupportActionBar().setTitle(view.getTitle());
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    class CustomWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

    class JsOperation {

        Activity mActivity;

        public JsOperation(Activity activity) {
            mActivity = activity;
        }

        @JavascriptInterface
        public void checkLoginState(String fun) {
            final String callback = fun;
            if (Looper.myLooper() != Looper.getMainLooper()) {
                YLog.verbose(this, "this is not in main looper");
            }
            loadUrl("javascript:" + callback + "('" + String.valueOf(STATUS_ONLINE) + "')");
        }

        @JavascriptInterface
        public void jumpPage(String json) {
            if (json == null) {
                return;
            }
            YLog.verbose(this, json, json);
            try {
                JSONObject msg = new JSONObject(json);
                String name = msg.optString(KEY_NAME);
                if (NAME_JUMP_TO_LOGIN.equals(name)) {
                    NavigationUtil.toLogin(mActivity);
                } else if (NAME_JUMP_TO_LIVE.equals(name)) {
                    NavigationUtil.toMain(mActivity, TAB_LIVE);
                } else if (NAME_JUMP_TO_MESSAGE.equals(name)) {
                    NavigationUtil.toMain(mActivity, TAB_MESSAGE);
                } else if (NAME_JUMP_TO_PROFILE.equals(name)) {
                    NavigationUtil.toMain(mActivity, TAB_PROFILE);
                }
            } catch (JSONException e) {
                YLog.verbose(this, "json for jumpToPage is illegal", json);
            }
        }

        @JavascriptInterface
        public void pushLoginPage() {
            YLog.verbose(this, "login page");
            NavigationUtil.toLogin(mActivity);
        }

        private void loadUrl(final String url) {
            WebViewActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        mWebView.loadUrl(url);
                    } catch (Exception e) {
                        YLog.error(this, "mWebView.loadUrl error", e);
                    }
                }
            });
        }
    }
}
