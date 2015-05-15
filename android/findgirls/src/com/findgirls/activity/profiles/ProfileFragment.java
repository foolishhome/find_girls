package com.findgirls.activity.profiles;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmodel.notification.SDKCallback;
import com.findgirls.R;
import com.findgirls.activity.BaseFragment;
import com.findgirls.activity.NavigationUtil;
import com.findgirls.app.AppModel;

public class ProfileFragment extends BaseFragment
        implements SDKCallback.Login
{

    private ImageView portrait;
    private TextView nickname;
    private TextView signature;
    private ImageView loginStateView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        portrait = (ImageView) root.findViewById(R.id.iv_portrait);
        nickname = (TextView) root.findViewById(R.id.tv_title);
        signature = (TextView) root.findViewById(R.id.tv_signature);
        long uid = AppModel.INSTANCE.sdkModel().myAccount().uid;

        root.findViewById(R.id.rl_header_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtil.toLogin(getActivity());
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left50_out);
            }
        });

        root.findViewById(R.id.rl_suggestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtil.toSuggestion(getActivity());
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left50_out);
            }
        });

        root.findViewById(R.id.rl_setting_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtil.toSetting(getActivity());
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left50_out);
            }
        });

        root.findViewById(R.id.tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppModel.INSTANCE.sdkModel().logout(true);
                NavigationUtil.toLogin(getActivity());
            }
        });


//        int msgFlag = AppModel.INSTANCE.liveModel().getLiveMsg();
//        james
//        onLiveNotifyStart(msgFlag);

        return root;
    }

    private void update() {

    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onSuccess() {
        update();
    }

    @Override
    public void onError(int result, String message) {

    }
}
