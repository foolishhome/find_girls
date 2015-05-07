package com.yy.medical.activity.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yy.medical.R;
import com.yy.medical.activity.BaseFragment;

public class LivePageFragment extends BaseFragment {
    private PullToRefreshListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.activity_my_yb, container, false);

        adapter = new LiveMainPageAdapter(getActivity());
        serverLoadingViewAnimator = (ServerLoadingViewAnimator) findViewById(R.id.loading_animator);
        listView = (PullToRefreshListView) serverLoadingViewAnimator.addContentView(R.layout.layout_pull_to_refresh_expanded_list, adapter, getString(R.string.nocontent));
        listView.setOnRefreshListener(this);
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.INSTANCE, false, true));
        listView.setAdapter(adapter);

        query();

        return root;
    }


}
