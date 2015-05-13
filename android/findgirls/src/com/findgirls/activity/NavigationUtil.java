package com.findgirls.activity;

import android.app.Activity;
import android.content.Intent;

public class NavigationUtil {
    public static void toGuide(Activity activity) {
        activity.startActivity(new Intent(activity, GuideActivity.class));
    }

    public static void toMain(Activity activity, int tab) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(MainActivity.START_TAB, tab);
        activity.startActivity(intent);
    }

/*
    public static void toLogin(Context activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    public static void toLoginFromRegist(Context activity, String accout) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(LoginActivity.KEY_WHO_OPEN, LoginActivity.FORM_REGIST);
        intent.putExtra(LoginActivity.KEY_ACCOUT, accout);
        //YLog.error("NavigationUtil", "RegisterActivity :: into  NavigationUtil.toLoginFromRegist function and req open LoginView --> accout = " + accout);
        activity.startActivity(intent);
    }


    public static void toLoginAndReturnToMainTab(Activity activity, int tab) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(MainActivity.START_TAB, tab);
        activity.startActivityForResult(intent, MainActivity.START_TAB_RESULT);
    }

    public static void toChannel(Context activity, long channelId, long subSid, String formAct) {
        YLog.verbose(NavigationUtil.class, "toChannel %s,%s", channelId, subSid);
        if(DialogUtil.showNoLoginMessage(activity))
            return;

       if (YYAppModel.INSTANCE.channelModel().isRecording() && (YYAppModel.INSTANCE.channelModel().getSid() != channelId || YYAppModel.INSTANCE.channelModel().getSubSid() != subSid)) {
            Dialogs.StopRecordDialogFragment.showStopRecordDialogFragment(activity, channelId, subSid, formAct);
        } else
        {
            Intent intent = new Intent(activity, ChannelActivity.class);
            intent.putExtra(ChannelActivity.JOIN_SID, channelId);
            intent.putExtra(ChannelActivity.JOIN_SSID, subSid);
            activity.startActivity(intent);
        }
    }

    public static void toDoctorList(Activity activity) {
        activity.startActivity(new Intent(activity, DoctorListFragment.class));
    }

    public static void toSetting(Activity act) {
        if (act == null) {
            return;
        }
        Intent intent = new Intent(act, SettingActivity.class);
        act.startActivity(intent);
    }

    public static void toBaseSetting(Activity act) {
        if (null == act) {
            return;
        }

        Intent intent = new Intent(act, BaseSettingActivity.class);
        act.startActivity(intent);

    }

    public static void toNotificationSetting(Activity act) {
        if (act == null) {
            return;
        }
        Intent intent = new Intent(act, NotificationSettingActivity.class);
        act.startActivity(intent);
    }

    public static void toBuddySetting(Activity act) {
        if (act == null) {
            return;
        }

        Intent intent = new Intent(act, BuddySettingActivity.class);
        act.startActivity(intent);
    }

    public static void toSuggestion(Activity act) {
        if (act == null) {
            return;
        }

        if (DialogUtil.showNoLoginMessage(act)){
            return;
        }

        Intent intent = new Intent(act, SuggestActivity.class);
        act.startActivity(intent);
    }


    public static void toRegister(Activity activity) {
        activity.startActivity(new Intent(activity, RegisterActivity.class));
    }

    public static void toForgotPwd(Activity act) {
        //YYAppModel.INSTANCE.statisticModel().clickReport(StatisticModel.FORGOT_PWD_CLICK);
        toWeb(act, "https://udb.yy.com/account/forgetPassword2.do");
    }

    public static void toContacts(Activity activity) {
        //tirle del
//        activity.startActivity(new Intent(activity, ContactsActivity.class));
    }

    public static void toConsultImage(Activity activity, String imageUrl)
    {
        Intent intent = new Intent(activity, ConsultImageActivity.class);
        intent.putExtra(ConsultImageActivity.CONSULT_IMAGE, imageUrl);
        activity.startActivity(intent);
    }

    public static void toConsult(Activity activity, ConsultActivity.ConsultIntent consultIntent)
    {
        Intent intent = new Intent(activity, ConsultActivity.class);
        intent.putExtra(ConsultActivity.SCENE, consultIntent.scene.toString());
        intent.putExtra(ConsultActivity.CONSULT_ID, consultIntent.consultId);
        intent.putExtra(ConsultActivity.DOCTOR_UID, consultIntent.doctorUid);
        intent.putExtra(ConsultActivity.PATIENT_UID, consultIntent.patientUid);
        intent.putExtra(ConsultActivity.CONSULT_CONTENT, consultIntent.consultContent);
        intent.putExtra(ConsultActivity.CONSULT_REWARD, consultIntent.reward);
        intent.putExtra(ConsultActivity.CONSULT_PHOTO_URL, consultIntent.photoUrl);
        intent.putExtra(ConsultActivity.CONSULT_STATUS, consultIntent.consultStatus.ordinal());
        intent.putExtra(ConsultActivity.CONSULT_UNREAD_MSG_COUNT, consultIntent.unReadMsgCount);
        activity.startActivity(intent);
    }

    public static void toConsultSessions(Activity activity,long consultId,String consultStr,long reward,long acceptDoctorUid, String photoUrl ,ConsultStatus consultStatus)
    {
        Intent intent = new Intent(activity, ConsultingSessionsActivity.class);
        intent.putExtra(ConsultingSessionsActivity.EXTRA_CONSULT_ID, consultId);
        intent.putExtra(ConsultingSessionsActivity.EXTRA_CONSULT_STR, consultStr);
        intent.putExtra(ConsultingSessionsActivity.EXTRA_CONSULT_REWARD, reward);
        intent.putExtra(ConsultingSessionsActivity.EXTRA_PHOTO_URL, photoUrl);
        intent.putExtra(ConsultingSessionsActivity.EXTRA_CONSULT_ACCEPT_DOCTOR, acceptDoctorUid);
        intent.putExtra(ConsultingSessionsActivity.EXTRA_CONSULT_STATUS, consultStatus.ordinal());
        activity.startActivity(intent);
    }


    public static void toContactsClearTop(Context context) {
        //tirle del
//        Intent intent = new Intent(context, ContactsActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        context.startActivity(intent);
    }

    public static void toProfileActivity(Activity activity) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        activity.startActivity(intent);
    }

    public static void toDocProfileActivity(Activity activity) {
        if(!DocProfileActivity.showing)
        {
            DocProfileActivity.showing = true;
            Intent intent = new Intent(activity, DocProfileActivity.class);
            activity.startActivity(intent);
        }
    }

    public static void toDoctorProfileActivity(Activity activity, long docUid){
        if (activity == null || docUid <= 0) {
            return;
        }

        Intent intent = new Intent(activity, DoctorProfileActivity.class);
        intent.putExtra(DoctorProfileActivity.EXTRA_DOCTOR_ID, docUid);
        activity.startActivity(intent);

    }

    public static void toMoneyActivity(Activity activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, MoneyActivity.class);
        activity.startActivity(intent);
    }

    public static void toMoneyPayActivity(Activity activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, MoneyPayActivity.class);
        activity.startActivity(intent);
    }

    public static void toWebMoneyPay(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    public static void toAlipayMoneyPay(Activity activity, String uri) {
        Intent intent = new Intent(activity, PayAlipayViewActivity.class);
        intent.putExtra(PayAlipayViewActivity.ORDERINFO, uri);
        activity.startActivity(intent);
    }

    public static void toImChat(Context context, long uid) {
        Intent intent = new Intent(context, ImChatActivity.class);
        intent.putExtra(ImChatActivity.EXTRA_USER_ID, uid);
        context.startActivity(intent);
    }

    public static void toEditText(Activity activity, int requestCode, String title, String content, int minLength, int maxLength) {
        Intent intent = new Intent(activity, EditTextActivity.class);
        intent.putExtra(EditTextActivity.EXTRA_MAX_LENGTH, maxLength);
        intent.putExtra(EditTextActivity.EXTRA_TITLE, title);
        intent.putExtra(EditTextActivity.EXTRA_MIN_LENGTH, minLength);
        intent.putExtra(EditTextActivity.EXTRA_CONTENT, content);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void toEditNickName(Activity activity, int requestCode, String title, String content, int minLength, int maxLength) {
        Intent intent = new Intent(activity, EditNickNameActivity.class);
        intent.putExtra(EditTextActivity.EXTRA_MAX_LENGTH, maxLength);
        intent.putExtra(EditTextActivity.EXTRA_TITLE, title);
        intent.putExtra(EditTextActivity.EXTRA_MIN_LENGTH, minLength);
        intent.putExtra(EditTextActivity.EXTRA_CONTENT, content);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void toChooseSite(Context activity, int regionType, int regionId) {
        Intent intent = new Intent(activity, SetSiteActivity.class);
        intent.putExtra(SetSiteActivity.REGION_TYPE, regionType);
        intent.putExtra(SetSiteActivity.REGION_ID, regionId);
        activity.startActivity(intent);
    }

    public static void toSplash(Context activity) {
        activity.startActivity(new Intent(activity, SplashActivity.class));
    }

    public static void toCurrentChannel(Context activity) {
        if (YYAppModel.INSTANCE.channelModel().isValid()) {
            toChannel(activity, YYAppModel.INSTANCE.channelModel().getSid(), YYAppModel.INSTANCE.channelModel().getSubSid(), null);
        }
    }

    public static void serviceToCurrentChannel(Context context) {
        if (YYAppModel.INSTANCE.channelModel().isValid()) {
            Intent intent = new Intent(context, ChannelActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ChannelActivity.JOIN_SID, YYAppModel.INSTANCE.channelModel().getSid());
            intent.putExtra(ChannelActivity.JOIN_SSID, 0);
            context.startActivity(intent);
            //YYAppModel.INSTANCE.statisticModel().clickReport(StatisticModel.ENTER_CHANNEL_FROM_FLOATING_WINDOW);
        }
    }

    public static void toTabs(Context context) {
//        context.startActivity(new Intent(context, TabsActivity.class));
    }

    public static void toLiveList(Context context, Tab tab) {
        Intent intent = new Intent(context, LiveListActivity.class);
        intent.putExtra(LiveListActivity.EXTRA_TAB, tab);
        context.startActivity(intent);
    }

    public static void toSearchVideo(Context context, String key, int extraId) {
        Intent intent = new Intent(context, SearchVideoActivity.class);
        intent.putExtra(SearchVideoActivity.EXTRA_SEARCH, key);
        intent.putExtra(SearchVideoActivity.EXTRA_ID, extraId);
        context.startActivity(intent);
    }

    public static void toSearchVideoItem(Context context, String key)
    {
        Intent intent = new Intent(context, DepartmentsInSearchFragment.class);
        intent.putExtra(DepartmentsInSearchFragment.SEARCH_KEY, key);
        context.startActivity(intent);
    }

    public static void toAccount(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    public static void toMyFollow(Context context) {
        context.startActivity(new Intent(context, MyFollowActivity.class));
    }

    public static void toDetail(Context context, long uid) {
        if (uid == YYAppModel.INSTANCE.loginModel().getUid()) {
            return;
        }
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.DETAIL_UID, uid);
        context.startActivity(intent);
    }

    public static void toWeb(Context context, String linkUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.URL, linkUrl);
        context.startActivity(intent);
    }

    public static void jump2Market(Activity act) {
        if (act == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        YLog.info("NavigationUtil", "-- PackageName = %s --", act.getPackageName());
        intent.setData(Uri.parse("market://details?id=" + act.getPackageName())); //正式
        act.startActivity(intent);
    }

    public static void toPhotoViewer(Context activity, ArrayList<CharSequence> images) {
        if (images == null || images.size() == 0) {
            YLog.warn(NavigationUtil.class, "toSearchLive images empty %s", images);
            return;
        }
        String imageUrl = images.get(0).toString();
        Intent intent;
        if (imageUrl.endsWith(".gif")) {
            intent = new Intent(activity, GifPhotoViewerActivity.class);
            intent.putExtra(PhotoViewerActivity.KEY_IMAGES, imageUrl);
        } else {
            intent = new Intent(activity, PhotoViewerActivity.class);
            intent.putExtra(GifPhotoViewerActivity.KEY_IMAGES, imageUrl);
        }
        activity.startActivity(intent);
    }

    public static void toMainNewActivityAndFinished(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(MainActivity.ACTION_FINISHED);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void toMaiAndLogin(Context context) {
        Intent contentIntent = new Intent(context, MainActivity.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        contentIntent.putExtra(MainActivity.START_TAB, 0);
        contentIntent.putExtra(MainActivity.NOTIFY_INTENT, new Intent(context, LoginActivity.class));
        context.startActivity(contentIntent);
    }

    public static void openUrl(Activity activity, String url) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            YLog.error(NavigationUtil.class, "open Url error!");
            ToastUtil.show(activity, R.string.cant_open_url);
        }
    }


    public static void toConsultingHistoryActivity(Activity activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, ConsultingHistoryActivity.class);
        activity.startActivity(intent);
    }

    //快速提问
    public static void toConsultingActivity(Activity activity) {
        if (activity == null) {
            return;
        }

        if(DialogUtil.showNoLoginMessage(activity))
            return;

        Intent intent = new Intent(activity, ConsultingActivity.class);
        activity.startActivity(intent);
    }

    public static void toVideoActivity(Activity activity, String url , String title) {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            intent = new Intent(activity, VideoActivityUnder4.class);
        else
            intent = new Intent(activity, VideoActivity.class);

        VideoUtil.clearVideoRecord();
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(VideoActivity.VIDEO_URL, url);
        intent.putExtra(VideoActivity.VIDEO_TITLE, title);
        activity.startActivity(intent);
    }
*/
}
