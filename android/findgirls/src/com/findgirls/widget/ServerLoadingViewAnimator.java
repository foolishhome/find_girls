package com.findgirls.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.findgirls.R;

public class ServerLoadingViewAnimator extends ViewAnimator {

    private View contentView;
    private View emptyView;
    private String emptyText;
    private int emptyPosition;
    private View failView;
    private int failPosition;
    private DataSetObserver observer;

    public ServerLoadingViewAnimator(Context context) {
        super(context);
    }

    public ServerLoadingViewAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void attachContentView(final Adapter adapter, String emptyText) {
        observer = new ListObserver(adapter);
        adapter.registerDataSetObserver(observer);

        this.emptyText = emptyText;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View loading = inflater.inflate(R.layout.layout_loading, null);

        addView(loading, 0);
        showLoadingView();
    }

    public View addContentView(int id, final Adapter adapter, String emptyText) {
        observer = new ListObserver(adapter);
        adapter.registerDataSetObserver(observer);
        return initContentView(id, emptyText);
    }

    public View addContentView(int id, ExpandableListAdapter adapter, String emptyText) {
        observer = new ExpandedListObserver(adapter);
        adapter.registerDataSetObserver(observer);
        return initContentView(id, emptyText);
    }

    private View initContentView(int id, String emptyText) {
        this.emptyText = emptyText;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View loading = inflater.inflate(R.layout.layout_loading, null);
        addView(loading);
        showLoadingView();
        contentView = inflate(getContext(), id, null);
        addView(contentView);
        return contentView;
    }

    private class ListObserver extends DataSetObserver {

        private Adapter adapter;

        public ListObserver(Adapter adapter) {
            this.adapter = adapter;
        }

        public void onChanged() {
            if (adapter.getCount() == 0) {
                showEmptyView();
            } else {
                showContentView();
            }
        }
    }

    private class ExpandedListObserver extends DataSetObserver {

        private ExpandableListAdapter adapter;

        public ExpandedListObserver(ExpandableListAdapter adapter) {
            this.adapter = adapter;
        }

        public void onChanged() {
            int count = 0;
            for (int i = 0; i < adapter.getGroupCount(); i++) {
                for (int j = 0; j < adapter.getChildrenCount(i); j++) {
                    count++;
                }
            }
            if (count == 0) {
                showEmptyView();
            } else {
                showContentView();
            }
        }
    }

    public void showLoadingView() {
        setDisplayedChild(0);
    }

    private void showContentView() {
        setDisplayedChild(1);
    }

    public void showEmptyView() {
        if (emptyView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            emptyView = inflater.inflate(R.layout.layout_no_content, null);
            ((TextView) emptyView.findViewById(R.id.tv_text)).setText(emptyText);
            addView(emptyView);
            emptyPosition = getChildCount() - 1;
        }
        setDisplayedChild(emptyPosition);
    }

    public void showFailView(final RetryClickListener listener) {
        if (failView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            failView = inflater.inflate(R.layout.layout_fail, null);
            failView.findViewById(R.id.tv_retry).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRetryClick();
                    showLoadingView();
                }
            });
            addView(failView);
            failPosition = getChildCount() - 1;
        }
        setDisplayedChild(failPosition);
    }

    public interface RetryClickListener {
        void onRetryClick();
    }

}
