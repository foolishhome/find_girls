package com.findgirls.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.appmodel.util.notification.NotificationCenter;
import com.findgirls.R;
import com.findgirls.app.AppModel;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.PauseAble;

public abstract class BaseFragmentActivity extends SherlockFragmentActivity implements ISimpleDialogListener, PauseAble {

    private static final int CHANNEL_KICKED_BY_SELF_DIALOG = -1;
    private static final int RELOGIN_DIALOG = -2;

    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationCenter.INSTANCE.addObserver(this);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (canGoBack()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setBackgroundDrawable(actionBarBackGroundColor());
                if (isDark()) {
                    actionBar.setLogo(R.drawable.actionbar_white_logo_with_back);
                } else {
                    actionBar.setLogo(R.drawable.actionbar_logo_with_back);
                }

            } else {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setLogo(R.drawable.actionbar_logo);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.INSTANCE.removeObserver(this);
    }

    public boolean isDark() {
        return false;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        paused = false;
        AppModel.INSTANCE.dialogModel().onResumeFragments(this);
    }

    @Override
    protected void onPause() {
        AppModel.INSTANCE.dialogModel().onPause(this);
        super.onPause();
        hiidoOnPause();
        paused = true;
    }

    protected void hiidoOnPause() {
        //数据上报接口
        String pageName = getActivity().getClass().getSimpleName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected Activity getActivity() {
        return this;
    }

    public boolean canGoBack() {
        return true;
    }

    //修改action背景重载此函数即可
    public Drawable actionBarBackGroundColor() {
        return getResources().getDrawable(R.color.deftitlebg_color);
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        // do nothing
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode == CHANNEL_KICKED_BY_SELF_DIALOG) {
            onChannelKickedBySelfNoticed();
        }
    }


    public boolean isPaused() {
        return paused;
    }

    protected void onChannelKickedBySelfNoticed() {
        // overwrite if need to deal with the kicked channel by self event
    }

    @Override
    public void registerForContextMenu(View view) {
        final long longClickInterval = 300;
        view.setOnTouchListener(new View.OnTouchListener() {
            long start = 0;
            long end = 0;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        start = System.currentTimeMillis();
                        return false;
                    case MotionEvent.ACTION_UP:
                        end = System.currentTimeMillis();
                        if (end - start > longClickInterval) {
                            return true;
                        }
                        return false;
                }
                return false;
            }
        });
        super.registerForContextMenu(view);
    }
}
