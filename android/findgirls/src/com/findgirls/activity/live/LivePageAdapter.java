package com.findgirls.activity.live;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.findgirls.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LivePageAdapter extends BaseAdapter {
    static class Live {
        String channelNames;
        int userCounts;
        String thumbs;
    }
    static class LiveHolder {
        public TextView channelNames;
        public TextView userCounts;
        public ImageView thumbs;

        public View root;
    }
    private View createItem(ViewGroup parent, LiveHolder holder) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_page_item, null);
        holder.root = root;
        holder.channelNames = (TextView) root.findViewById(R.id.tv_channelName);
        holder.userCounts = (TextView) root.findViewById(R.id.tv_user_count);
        holder.thumbs = (ImageView) root.findViewById(R.id.iv_thumb);

        root.setTag(holder);
        return root;
    }

    private ArrayList<Live> liveData = new ArrayList<Live>();

    public void setLivesData(JSONObject data) {
        liveData.clear();
        appendLivesData(data);
    }

    public void appendLivesData(JSONObject data) {
        try {
            int result = data.getInt("result");
            switch (result) {
                case 0: // fail
                    String err = data.getString("err");
                    break;
                case 1: // succeed
                    JSONArray ary = data.getJSONArray("data");
                    for (int i = 0; i < ary.length(); i++) {
                        JSONObject obj = (JSONObject) ary.get(i);
                        Live live = new Live();
                        live.channelNames = obj.getString("liveName");
                        live.userCounts = obj.getInt("users");
                        live.thumbs = obj.getString("thumb");
                        liveData.add(live);
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return liveData.size();
    }

    @Override
    public Object getItem(int i) {
        if (i >= 0 && i < liveData.size())
            return liveData.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view || view.getTag() instanceof LiveHolder) {
            view = createItem(viewGroup, new LiveHolder());
        }

        LiveHolder holder = (LiveHolder)view.getTag();
        setLiveItemData(i, viewGroup, holder);

        return view;
    }

    private void setLiveItemData(int index, final ViewGroup parent, LiveHolder holder) {
        if (index < 0 || index >= liveData.size())
            return;
        final Live live = liveData.get(index);
        if (live != null) {
            holder.channelNames.setText(live.channelNames);
            holder.userCounts.setText(String.valueOf(live.userCounts));
            holder.userCounts.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(live.thumbs, holder.thumbs);

            holder.root.setVisibility(View.VISIBLE);
        } else {
            holder.root.setVisibility(View.INVISIBLE);
        }
    }
}
