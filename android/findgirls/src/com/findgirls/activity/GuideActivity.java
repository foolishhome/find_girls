package com.findgirls.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.findgirls.widget.viewpagerindicator.CirclePageIndicator;
import com.findgirls.R;

public class GuideActivity extends SherlockActivity {

    public static final String SHOW_GUIDE = "show_guide_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_view_pager_with_indicator);
        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setBackgroundColor(getResources().getColor(R.color.white));
        pager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(final ViewGroup container, int position) {
                final ImageView imageView = new ImageView(container.getContext());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(params);
//                imageView.setBackgroundColor(getResources().getColor(R.color.red));
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                container.addView(imageView);
                int image;
                if (position == 0) {
                    image = R.drawable.guide_1;
                } else if (position == 1) {
                    image = R.drawable.guide_2;
                } else if (position == 2) {
                    image = R.drawable.guide_3;
                } else {
                    image = R.drawable.guide_4;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(GuideActivity.this).edit();
                            editor.putBoolean(SHOW_GUIDE, false);
                            editor.commit();
                            finish();
                            NavigationUtil.toMain(GuideActivity.this, 0);
                        }
                    });
                }
                imageView.setImageResource(image);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view != null && view.equals(object);
            }
        });

        CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);
        pageIndicator.setViewPager(pager);
    }

    private void toMain() {
        finish();
        NavigationUtil.toMain(this, 0);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
