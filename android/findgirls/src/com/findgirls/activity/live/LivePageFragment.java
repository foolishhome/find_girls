package com.findgirls.activity.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.findgirls.widget.ServerLoadingViewAnimator;
import com.findgirls.R;
import com.findgirls.activity.BaseFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.apache.http.Header;
import org.json.JSONObject;

public class LivePageFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, ServerLoadingViewAnimator.RetryClickListener {
    private PullToRefreshListView listView;
    private LivePageAdapter adapter;
    private ServerLoadingViewAnimator serverLoadingViewAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.layout_pull_to_refreshlist, container, false);

        adapter = new LivePageAdapter();
        listView = (PullToRefreshListView) root.findViewById(R.id.list_view);
        serverLoadingViewAnimator = (ServerLoadingViewAnimator) root.findViewById(R.id.loading_animator);
        serverLoadingViewAnimator.attachContentView(listView, adapter, getString(R.string.nocontent));

        listView.setOnRefreshListener(this);
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
        listView.setAdapter(adapter);

        query();

        return root;
    }


    private void query() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://172.19.207.37:9292/mobile/girlslist", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                adapter.setLivesData(response);
                listView.onRefreshComplete();
            }
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                System.out.println(errorResponse);
                serverLoadingViewAnimator.showFailView(LivePageFragment.this);
                listView.onRefreshComplete();
            }
        });

    }

    public void onRetryClick() {
        query();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        query();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
}
