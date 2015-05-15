package com.findgirls.activity.profiles;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.appmodel.notification.SDKCallback;
import com.duowan.mobile.utils.YLog;
import com.findgirls.R;
import com.findgirls.activity.ProfileFragmentActivity;
import com.findgirls.app.AppModel;
import com.findgirls.widget.Dialogs;

public class SuggestActivity extends ProfileFragmentActivity implements SDKCallback.Suggestion {

    private static final int SUBMIT_ID = 0;
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 500;
    private EditText editSuggestion;
    private ProgressDialog loading;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.str_setting_suggestion));

        final ActionBar actionBar = getSupportActionBar();
        String strTitle = "<font color='#ffffff'>";
        strTitle += getString(R.string.str_setting_suggestion);
        strTitle += "</font>";
        actionBar.setTitle(Html.fromHtml(strTitle));

        setContentView(R.layout.activity_suggestion);
        initView();
    }

    private void initView() {
        editSuggestion = (EditText) findViewById(R.id.et_suggestion);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String strTitle = "<font color='#ffffff'>";
        strTitle += getString(R.string.str_submit);
        strTitle += "</font>";

        menu.add(Menu.NONE, SUBMIT_ID, 0, R.string.str_submit).setTitle(Html.fromHtml(strTitle)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Dialogs.showNoLoginMessage(getActivity())){
            return false;
        }

        if (item.getItemId() == SUBMIT_ID) {
            YLog.debug(this, "-- submit --");
            String suggestionStr = editSuggestion.getText().toString();
            int length = suggestionStr.length();
            if (length < MIN_LENGTH) {
                Toast.makeText(getApplicationContext(), getString(R.string.str_input_exceed_min, MIN_LENGTH), Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            } else if (length > MAX_LENGTH) {
                Toast.makeText(getApplicationContext(), getString(R.string.str_input_exceed_max, MAX_LENGTH), Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            AppModel.INSTANCE.sdkModel().submitFeedback(suggestionStr);
            showLoading();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading() {
        if (loading == null) {
            loading = new ProgressDialog(getActivity());
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.setMessage(getString(R.string.buddy_adding));
            loading.setCanceledOnTouchOutside(false);
            loading.setCancelable(true);
            loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
        }
        loading.show();
    }

    @Override
    public void onSuggestion(boolean success) {
        if (success) {
            Toast.makeText(getApplicationContext(), getString(R.string.thx_for_suggestion), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.submit_suggestion_error), Toast.LENGTH_SHORT).show();
        }
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public boolean isDark() {
        return true;
    }

    @Override
    public Drawable actionBarBackGroundColor() {
        return getResources().getDrawable(R.color.titlebg_color);
    }
}
