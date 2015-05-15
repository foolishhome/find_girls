package com.findgirls.activity;

import com.actionbarsherlock.view.MenuItem;
import com.findgirls.R;

public class ProfileFragmentActivity extends BaseFragmentActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            overridePendingTransition(R.anim.slide_left50_in, R.anim.slide_right_out);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left50_in, R.anim.slide_right_out);
    }
}
