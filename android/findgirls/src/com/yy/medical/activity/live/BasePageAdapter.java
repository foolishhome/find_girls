package com.yy.medical.activity.live;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yy.medical.R;

/**
 * Created by Administrator on 2015/1/26.
 */
public abstract class BasePageAdapter extends BaseAdapter {
    static class ChildHolder {
        public TextView channelNames;
        public TextView userCounts;
        public ImageView thumbs;
        public ImageView doctors;
        public ImageView imgTimes;
        public TextView times;

        public View root;
        public Button btn_Subscribes;
        public TextView bookCounts;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View createItemview(ViewGroup parent, ChildHolder holder) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_page_item, null);
        holder.root = root;
        holder.channelNames = (TextView) root.findViewById(R.id.tv_channelName);
        holder.userCounts = (TextView) root.findViewById(R.id.tv_user_count);
        holder.thumbs = (ImageView) root.findViewById(R.id.iv_thumb);
        holder.doctors = (ImageView) root.findViewById(R.id.img_doctor);
        holder.imgTimes = (ImageView) root.findViewById(R.id.img_time);
        holder.times = (TextView) root.findViewById(R.id.tv_time);
        holder.btn_Subscribes = (Button) root.findViewById(R.id.btn_book);
        holder.bookCounts = (TextView) root.findViewById(R.id.bookCount);

        root.setTag(holder);
        return root;
    }






}
