package com.yy.appmodel;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.List;

/**
 * Created by computer-boy on 15/1/5.
 */
public class FkXiaomi {

    public static boolean isMIUI(Context context) {
        if (!"Xiaomi".equalsIgnoreCase(Build.MANUFACTURER))
            return false;
        return true; // hasSecurityCenter(context);
    }

    // 这个函数有点问题，isIntentAvailable检测不到红米的安全设置
    // 也就是，发现是MIUI的，直接弹出来，不检测了
    private static boolean hasSecurityCenter(Context context) {
        boolean result = false;
        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        i.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        i.putExtra("extra_pkgname", context.getPackageName());
        if (isIntentAvailable(context, i)) {
            result = true;
        }
        return result;
    }

    private static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    public static void gotoSecurityCenter(Context context) {
        //跳转到设置界面
        try {
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter",
                            "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }
    }
}
