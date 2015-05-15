package com.findgirls.activity.profiles;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.appmodel.notification.SDKCallback;
import com.duowan.mobile.utils.YLog;
import com.findgirls.R;
import com.findgirls.activity.NavigationUtil;
import com.findgirls.activity.ProfileFragmentActivity;
import com.findgirls.activity.UpdateProgressNotify;
import com.findgirls.app.App;
import com.findgirls.app.AppModel;
import com.findgirls.widget.Dialogs;

public class SettingActivity extends ProfileFragmentActivity implements SDKCallback.UpdateProfiles {

    private static final int UPDATE_NOTIFY_ID = 1;
    private View versionView;
    private TextView updateTextBtn;
    private TextView versionTextView;
    private UpdateProgressNotify mProgressNotify;
    private View.OnClickListener onVerUpdateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            YLog.debug(this, "--  versionView  --");
            AppModel.INSTANCE.sdkModel().queryLastVersionInfo(App.getAppVersion(getApplicationContext()));
        }
    };
    private View.OnClickListener onLastVersionUpdateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(), getString(R.string.update_no_need), Toast.LENGTH_SHORT).show();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.base_setting));

        final ActionBar actionBar = getSupportActionBar();
        String strTitle = "<font color='#ffffff'>";
        strTitle += getString(R.string.base_setting);
        strTitle += "</font>";
        actionBar.setTitle(Html.fromHtml(strTitle));

        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root);

        int itemHeight = getResources().getDimensionPixelOffset(R.dimen.setting_item_height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        int marginTop = getResources().getDimensionPixelOffset(R.dimen.setting_margin_top);

        View notificationView = createItemView(getString(R.string.str_setting_notification), true, false);
        LinearLayout.LayoutParams notificationParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        notificationParams.setMargins(0, marginTop, 0, 0);
        linearLayout.addView(notificationView, notificationParams);

        View buddyValidationView = createItemView(getString(R.string.str_setting_buddy_validation), true, true);
        linearLayout.addView(buddyValidationView, params);

        View suggestionView = createItemView(getString(R.string.str_setting_suggestion), true, true);
        LinearLayout.LayoutParams suggestionParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        suggestionParams.setMargins(0, marginTop, 0, 0);
        linearLayout.addView(suggestionView, suggestionParams);

        versionView = createVersionItemView(getString(R.string.str_version), false, true);
        linearLayout.addView(versionView, params);
        updateTextBtn = (TextView) versionView.findViewById(R.id.tv_right);

        View clearCacheView = createItemView(getString(R.string.clear_cache), true, true);
        LinearLayout.LayoutParams clearCacheParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        clearCacheParams.setMargins(0, marginTop, 0, 0);
        linearLayout.addView(clearCacheView, clearCacheParams);

        suggestionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtil.toSuggestion(getActivity());
            }
        });

        clearCacheView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppModel.INSTANCE.statisticModel().clickReport(StatisticModel.PROFILE_CACHE_CLICK);
                Dialogs.TipBottomDialogFragment tipBottomDialogFragment = new Dialogs.TipBottomDialogFragment();
                tipBottomDialogFragment.setPositiveButton(R.string.confirm_clear_cache, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        List<Info> infoList = AppModel.INSTANCE.channelModel().getRecent();
//                        for (Info info : infoList) {
//                            AppModel.INSTANCE.channelModel().removeRecent(info.sid());
//                        }
                        AppModel.INSTANCE.dialogModel().dismiss();
                    }
                });
                tipBottomDialogFragment.setPositiveTextColor(Color.RED);
                AppModel.INSTANCE.dialogModel().show(tipBottomDialogFragment);
            }
        });

        versionView.setOnClickListener(onVerUpdateClickListener);
        versionTextView = (TextView) versionView.findViewById(R.id.tv_version);
        setVersion();
        AppModel.INSTANCE.sdkModel().queryLastVersionInfo(App.getAppVersion(getApplicationContext()));
    }

    private void setVersion() {
        String version = App.getAppVersion(getApplicationContext());
        if (versionTextView != null) {
            versionTextView.setText(version);
        }
    }

    private View createItemView(String itemName, boolean topLineShow, boolean bottomLineShow) {
        return createView(R.layout.setting_view_item, itemName, topLineShow, bottomLineShow);
    }

    private View createVersionItemView(String itemName, boolean topLineShow, boolean bottomLineShow) {
        return createView(R.layout.setting_version_item, itemName, topLineShow, bottomLineShow);
    }

    private View createView(int resource, String itemName, boolean topLineShow, boolean bottomLineShow) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(resource, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(itemName);
        View topLineView = view.findViewById(R.id.top_line);
        View bottomLineView = view.findViewById(R.id.bottom_line);
        if (topLineShow) {
            topLineView.setVisibility(View.VISIBLE);
        } else {
            topLineView.setVisibility(View.INVISIBLE);
        }

        if (bottomLineShow) {
            bottomLineView.setVisibility(View.VISIBLE);
        } else {
            bottomLineView.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public Drawable actionBarBackGroundColor() {
        return getResources().getDrawable(R.color.titlebg_color);
    }
}
