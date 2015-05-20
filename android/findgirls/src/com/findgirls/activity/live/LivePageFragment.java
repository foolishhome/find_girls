package com.findgirls.activity.live;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findgirls.R;
import com.findgirls.activity.BaseFragment;
import com.findgirls.widget.ServerLoadingViewAnimator;
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

    private int start;
    private static final int PAGE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.layout_pull_to_refreshlist, container, false);

        adapter = new LivePageAdapter();
        listView = (PullToRefreshListView) root.findViewById(R.id.list_view);
        serverLoadingViewAnimator = (ServerLoadingViewAnimator) root.findViewById(R.id.loading_animator);
        serverLoadingViewAnimator.attachContentView(adapter, getString(R.string.nocontent));
        listView.setEmptyView(serverLoadingViewAnimator);

        listView.setOnRefreshListener(this);
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
        listView.setAdapter(adapter);

        query(0);

        return root;
     }

    private void query(int st) {
        start = st;
        listView.setRefreshing();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://172.19.207.12:9292/mobile/girlslist?start=" + start + "&size=10", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                if (start == 0)
                    adapter.setLivesData(response);
                else
                    adapter.appendLivesData(response);
                listView.onRefreshComplete();
            }
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                System.out.println(errorResponse);
                listView.onRefreshComplete();
                serverLoadingViewAnimator.showFailView(LivePageFragment.this);
            }
        });

    }

    public void onRetryClick() {
        query(start);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        query(0);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        query(start + 10);
    }
}
