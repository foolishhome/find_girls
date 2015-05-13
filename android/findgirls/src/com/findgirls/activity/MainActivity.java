package com.findgirls.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.actionbarsherlock.view.MenuItem;
import com.duowan.mobile.utils.YLog;
import com.findgirls.BuildConfig;
import com.findgirls.R;
import com.findgirls.activity.live.LivePageFragment;
import com.findgirls.app.YYApp;
import com.findgirls.app.YYAppModel;

import java.util.List;
import java.util.Timer;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;

public class MainActivity extends BaseFragmentActivity
        implements ISimpleDialogListener
{

    private FragmentTabHost tabHost;
    public static final String START_TAB = "start_tab";
    public static final int START_TAB_RESULT = 9;
    private static final String LIVE_TAB = "live";
    private static final String ASK_TAB = "ask";
    private static final String CUSTOM_TAB = "custom";
    public static boolean bQuit = false;
    Timer timer = new Timer();
    private int tabIndex = -1;
    public static final String LIVE_SID = "live_id";
    public static final String LIVE_SUB_ID = "live_sub_id";
    private UpdateProgressNotify mProgressNotify;
    private int nLoginMedicalServerCount = 0;
    private int MAX_LOGIN_MEDICAL_TRY_COUNT = 3;

    private int __imageViewArray[]={
            R.drawable.tab_live,
            R.drawable.tab_ask,
            R.drawable.tab_profile
    };

    private Class __fragmentArray[]={
            LivePageFragment.class,
            LivePageFragment.class,
            LivePageFragment.class
    };

    private int __tabTextArray[]={
            R.drawable.tab_live,
            R.drawable.tab_ask,
            R.string.my
    };

    private String __tagArray[]={
            LIVE_TAB,
            ASK_TAB,
            CUSTOM_TAB
    };

    private View makeTab(final int index, int text, int icon) {
        View tab = getLayoutInflater().inflate(R.layout.layout_tab_bottom, null, false);
        ImageView iconView = (ImageView) tab.findViewById(R.id.iv_image);
        iconView.setImageResource(icon);
        return tab;
    }

    @TargetApi(11)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMenuKey();



        tabHost = (FragmentTabHost)findViewById(R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(),R.id.realtabcontent);

        if (Build.VERSION.SDK_INT >= 11) {
            tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        } else {
            tabHost.getTabWidget().setDividerDrawable(null);
        }

        tabHost.clearAllTabs();

        int count = __fragmentArray.length;
        for (int index = 0; index < count; ++index){
            tabHost.addTab(tabHost.newTabSpec(__tagArray[index]).setIndicator(makeTab(index, __tabTextArray[index], __imageViewArray[index])), __fragmentArray[index], null);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.compareTo(__tagArray[1]) == 0) {

                    //慢的问题修复
                    //if(DialogUtil.isMedicalLoginCheckFail_Jump(getActivity()))
                    //   return;

                }
            }
        });

//        MainPagerAdapter functionPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
//
//        functionPager = (SwipeControllableViewPager) findViewById(R.id.vp_function);
//        functionPager.setOffscreenPageLimit(2);
//        functionPager.setAdapter(functionPagerAdapter);
//
//        pageTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_page);
//        pageTabs.setViewPager(functionPager);
//        pageTabs.setTextColorResource(R.drawable.tab_text_selector);
//        pageTabs.setTextSize(DimensionUtil.dipToPx(getActivity(), 16));


        if (!BuildConfig.DEBUG) {
            // 检查更新
//            if (NetworkUtils.getMyNetworkType(getApplicationContext()) == NetworkUtils.NET_WIFI) {
//                YYAppModel.INSTANCE.updateModel().queryLastVersionInfo();
//            }
        }


        if(YYApp.isNetworkConnected(getActivity()))
        {
//            linkState = TypeInfo.LinkState.LinkStateConnected;
//            YYAppModel.INSTANCE.setNetWorkState(true);
//            __setActivityTitle();
        }




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        for (android.support.v4.app.Fragment fragment : fragments)
        {
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_TAB_RESULT && resultCode >= 0) {
            tabIndex = resultCode;
        }
    }


    private void addMenuKey() {
        try {
            int menuFlag = WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null);
            YLog.info(this, "menuFlag %d", menuFlag);
            // 解决4.0后手机没有硬MENU键的问题
            getWindow().setFlags(menuFlag, menuFlag);
        } catch (Exception e) {
            YLog.error(this, "below 3.0 has no menuFlag." + e.getMessage());
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavigationUtil.toCurrentChannel(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean canGoBack() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
//            final Dialogs.TipBottomDialogFragment exitDialog = new Dialogs.TipBottomDialogFragment();
//            exitDialog.setPositiveButton(R.string.str_exit_app, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    _exitApp();
//                }
//            });
//            YYAppModel.INSTANCE.dialogModel().show(exitDialog);
        }

        return super.onKeyDown(keyCode, event);
    }

    private void _exitApp() {
        YYAppModel.INSTANCE.exit(getActivity());
        finish();
    }

    private void backToDeskTop() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
