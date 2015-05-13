package com.findgirls.activity;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockActivity;
import com.duowan.mobile.utils.YLog;
import com.findgirls.R;
import com.findgirls.app.YYAppModel;

public class SplashActivity extends SherlockActivity {
    public static final String INSTALL_SHORTCUT = "install_shortcut";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YLog.verbose("yy3.0", "startApp SplashActivity onCreate");
        setContentView(R.layout.activity_splash);

        if (YYAppModel.INSTANCE.showSplash()) {
            toMainOrGuide();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toMainOrGuide();
                }
            }, 1500L);
        }
    }

    private void toMainOrGuide() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showGuide = prefs.getBoolean(GuideActivity.SHOW_GUIDE, true);
        finish();
        if (showGuide) {
            NavigationUtil.toGuide(this);
        } else {
            NavigationUtil.toMain(this, 0);
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (!isInstallShortcut()) {
                createShortcut();
            }
        } catch (Exception e) {
            YLog.error(this, "install shortcut error!");
        }
    }

    private void createShortcut() {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.medical_logo);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        shortcut.putExtra("duplicate", false);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory("android.intent.category.LAUNCHER");
        ComponentName componentName = new ComponentName(getPackageName(), SplashActivity.class.getName());
        intent.setComponent(componentName);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        this.sendBroadcast(shortcut);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean(INSTALL_SHORTCUT, true);
        editor.commit();
    }

    private boolean isInstallShortcut() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isInstallShortcut = prefs.getBoolean(INSTALL_SHORTCUT, false);
        // If user delete the shortcut, do nothing.
        if (!isInstallShortcut) {
            // Duplicate shortcut setting will invalid in some android devices.
            final ContentResolver cr = getContentResolver();
            final String authority = "com.android.launcher.settings";
            final Uri contentUri = Uri.parse("content://" + authority + "/favorites?notify=true");
            Cursor c = cr.query(contentUri, new String[]{"title", "iconResource"}, "title=?", new String[]{SplashActivity.this.getString(R.string.app_name).trim()}, null);
            if (c != null && c.getCount() > 0) {
                isInstallShortcut = true;
            }
        }
        return isInstallShortcut;
    }
}
