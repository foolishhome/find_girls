package com.findgirls.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.appmodel.util.notification.NotificationCenter;

public abstract class BaseFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotificationCenter.INSTANCE.addObserver(this);
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotificationCenter.INSTANCE.removeObserver(this);
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
