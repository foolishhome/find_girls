package com.findgirls.activity.MoodDiary;

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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class MoodListAdapter extends BaseAdapter {
    static class Mood {
        Date date;
        String text;
        int upUserCounts;
    }
    static class MoodHolder {
        public TextView date;
        public TextView text;
        public ImageView thumbs;

        public View root;
    }
    private View createItem(ViewGroup parent, MoodHolder holder) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mood_page_item, null);
        holder.root = root;
        holder.date = (TextView) root.findViewById(R.id.tv_date);
        holder.text = (TextView) root.findViewById(R.id.tv_content);

        root.setTag(holder);
        return root;
    }

    private ArrayList<Mood> moodData = new ArrayList<Mood>();


    public void setMoodData(JSONObject data) {
        moodData.clear();
        appendMoodData(data);
    }

    public void appendMoodData(JSONObject data) {
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int result = data.getInt("result");
            switch (result) {
                case 0: // fail
                    String err = data.getString("err");
                    break;
                case 1: // succeed
                    JSONArray ary = data.getJSONArray("data");
                    for (int i = 0; i < ary.length(); i++) {
                        JSONObject obj = (JSONObject) ary.get(i);
                        Mood mood = new Mood();
                        mood.date = simpledateformat.parse(obj.getString("date"), new ParsePosition(0));
                        mood.text = obj.getString("text");
                        mood.upUserCounts = obj.getInt("users");
                        moodData.add(mood);
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
        return moodData.size();
    }

    @Override
    public Object getItem(int i) {
        if (i >= 0 && i < moodData.size())
            return moodData.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view || view.getTag() instanceof MoodHolder) {
            view = createItem(viewGroup, new MoodHolder());
        }

        MoodHolder holder = (MoodHolder)view.getTag();
        setMoodItemData(i, viewGroup, holder);

        return view;
    }

    private void setMoodItemData(int index, final ViewGroup parent, MoodHolder holder) {
        if (index < 0 || index >= moodData.size())
            return;
        final Mood mood = moodData.get(index);
        if (mood != null) {
            if (mood.date == null)
                holder.date.setText("");
            else {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd \n hh:mm:ss");
                holder.date.setText(df.format(mood.date));
            }
            holder.text.setText(String.valueOf((mood.text != null)? mood.text: ""));

            holder.root.setVisibility(View.VISIBLE);
        } else {
            holder.root.setVisibility(View.INVISIBLE);
        }
    }
}
