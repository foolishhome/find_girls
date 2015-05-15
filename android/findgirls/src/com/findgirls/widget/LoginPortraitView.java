package com.findgirls.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.util.LongSparseArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.PropertyValuesHolder;
import com.actionbarsherlock.internal.nineoldandroids.animation.ValueAnimator;
import com.appmodel.SDKModel;
import com.duowan.mobile.utils.YLog;
import com.findgirls.R;
import com.findgirls.activity.profiles.LoginActivity;
import com.findgirls.app.AppModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class LoginPortraitView extends View
{

    /**
     * 头像的最小大小。
     */
    // final static ImageSize miniSize = new ImageSize(300, 300);

    private boolean isInited = false;
    /**
     * 登录过的用户信息。
     */
    private List<SDKModel.AccountInfo> accounts;

    private List<String> imagesQueue = new ArrayList<String>();

    /**
     * 用户头像。
     */
    private LongSparseArray<Bitmap> userPortraits = new LongSparseArray<Bitmap>();
    private long beforeBeforeUid = 0;
    private long beforeUid = 0;
    private long currentUid = 0;
    private long afterUid = 0;
    private long afterAfterUid = 0;
    private long leftReserved = 0;
    private long rightReserved = 0;
    private ImageLoadingListener afterImageLoaded = new ImageLoadingListener()
    {
        private void loadNext(String imageUri) {
            if (imagesQueue.contains(imageUri)) {
                imagesQueue.remove(imageUri);
            }
            if (!imagesQueue.isEmpty()) {
                ImageLoader.getInstance().loadImage(imagesQueue.get(0), afterImageLoaded);
            }
        }

        @Override
        public void onLoadingStarted(String imageUri, View view)
        {
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason)
        {
            YLog.error(this, "LoginPortraitView Load bitmap failed %s, %s", failReason.getType().toString(), imageUri);
            imageLoaded(imageUri,null);
            loadNext(imageUri);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
        {
            YLog.debug(this, "LoginPortraitView Load bitmap successfully, %s", imageUri);
            imageLoaded(imageUri, loadedImage);
            loadNext(imageUri);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view)
        {
            YLog.error(this, "LoginPortraitView Load bitmap canceled, %s", imageUri);
            loadNext(imageUri);
            imagesQueue.add(imageUri);
            loadNext("");
        }
    };
    private ValueAnimator portraitAnimator = new ValueAnimator();
    private boolean moveLeft = false;
    private float pointDown = 0.0f;
    private GestureDetector moveAction;
    private GestureDetector.OnGestureListener onMoveAction = new GestureDetector.SimpleOnGestureListener()
    {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            float w = LoginPortraitView.this.getWidth() / 3;
            float rate = e.getX() / w;
            if (rate < 1.0f) {
                move(false, 1000);
                return true;
            }
            else if (rate > 2.0f) {
                move(true, 1000);
                return true;
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            fling(e1, e2, velocityX, velocityY);
            return true;
        }
    };

    public LoginPortraitView(Context context)
    {
        super(context);
    }

    public LoginPortraitView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LoginPortraitView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return bitmap;
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawOval(rectF, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    public void initAction() {
        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!isInited) {
                    int h = getMeasuredHeight();
                    int w = getMeasuredWidth();
                    int t = Math.min(w / 3, h);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(t * 3, t);
                    setLayoutParams(params);

                    moveAction = new GestureDetector(getContext(), onMoveAction);

                    isInited = true;
                }
                return true;
            }
        });


//        int h = (int) Math.min(getContext().getResources().getDisplayMetrics().widthPixels / 3, 150 * density);
//        int w = h * 3;
        // setLayoutParams(new ViewGroup.LayoutParams(w, h));

    }
    public void initUserInfo()
    {
        beforeBeforeUid = 0;
        beforeUid = 0;
        currentUid = 0;
        afterUid = 0;
        afterAfterUid = 0;
        leftReserved = 0;
        rightReserved = 0;

        clearUserPortraits();

        accounts = AppModel.INSTANCE.sdkModel().getAccounts();
        loadDefaultPortrait();
        Bitmap defaultPortrait = userPortraits.get(0);

        currentUid = AppModel.INSTANCE.sdkModel().myAccount().uid;
        if(currentUid == 0 && !accounts.isEmpty())
        {
            currentUid = accounts.get(0).uid;
        }

        for(SDKModel.AccountInfo account : accounts)
        {
            if(currentUid == account.uid)
            {
                setCurrentUser(account.name, false);
            }
            userPortraits.put(account.uid, defaultPortrait);
            if (account.portraitUrl != null && account.portraitUrl.length() > 0) {
                imagesQueue.add(account.portraitUrl);
            }
        }
        if (!imagesQueue.isEmpty())
            ImageLoader.getInstance().loadImage(imagesQueue.get(0), afterImageLoaded);
    }

    private void loadDefaultPortrait()
    {
        Bitmap defaultPortrait = BitmapFactory.decodeResource(getResources(), R.drawable.def_portrait_online);
        userPortraits.put(0, defaultPortrait);
        Bitmap emptyProtait = BitmapFactory.decodeResource(getResources(), R.drawable.def_portrait_offline);
        userPortraits.put(1, emptyProtait);
        Bitmap cleanProtait = BitmapFactory.decodeResource(getResources(), R.drawable.def_portrait_offline_empty);
        userPortraits.put(2, cleanProtait);
    }

    private void clearUserPortraits() {
        if (userPortraits.get(0) != null && !userPortraits.get(0).isRecycled())
            userPortraits.get(0).recycle();
        if (userPortraits.get(1) != null && !userPortraits.get(1).isRecycled())
            userPortraits.get(1).recycle();
        if (userPortraits.get(2) != null && !userPortraits.get(2).isRecycled())
            userPortraits.get(2).recycle();
        userPortraits.clear();
    }

    @Override
    protected void onDetachedFromWindow() {
        imagesQueue.clear();
        clearUserPortraits();
        super.onDetachedFromWindow();
    }

    public void setCurrentUser(String userName, boolean needUpdate)
    {
        setCurrentUserByUid(0, userName, false, needUpdate);
    }

    /**
     * @param uid
     * @param userName
     * @param byUidFlags
     * @param needUpdate
     */
    private void setCurrentUserByUid(long uid, String userName, boolean byUidFlags, boolean needUpdate)
    {
        beforeBeforeUid = 0;
        beforeUid = 0;
        currentUid = 0;
        afterUid = 0;
        afterAfterUid = 0;
        leftReserved = 0;
        rightReserved = 0;
        // 如果是uid设置，表示是内部逻辑。需要更新到界面。
        SDKModel.AccountInfo currentAccount = null;
        for(SDKModel.AccountInfo account : accounts)
        {
            if ((!byUidFlags && userName.compareTo(account.name) == 0) || (byUidFlags && uid == account.uid))
            {
                currentUid = account.uid;
                if(beforeUid == 0)
                {
                    beforeUid = beforeBeforeUid;
                    beforeBeforeUid = 0;
                }

                if(byUidFlags)
                {
                    currentAccount = account;
                }
            }
            else
            /*处理前2个号码*/
                if (currentUid == 0)
                {
                    ++leftReserved;
                    if (accounts.size() > 1 && beforeBeforeUid == 0)
                    {
                        beforeBeforeUid = account.uid;
                    }
                    else
                    {
                        if(beforeUid != 0)
                        {
                            beforeBeforeUid = beforeUid;
                        }
                        beforeUid = account.uid;
                    }
                }

            if(currentUid != 0)
            {
                if(afterUid == 0)
                {
                    if(currentUid != account.uid)
                    {
                        afterUid = account.uid;
                    }
                }
                else
                {
                    if(currentUid != account.uid)
                    {
                        afterAfterUid = account.uid;
                        break;
                    }
                }

                if(rightReserved == 0)
                {
                    rightReserved = accounts.size() - leftReserved - 1;
                }
            }
        }
        if(needUpdate)
        {
            invalidate();
        }

        if(currentAccount != null)
        {
            LoginActivity activity = (LoginActivity)getContext();
            activity.displayAccount(currentAccount);
        }
    }

    private Bitmap getPortrait(long uid, boolean emptyWithDefaultHeader)
    {
        if (uid == 0) {
            if (emptyWithDefaultHeader)
                return userPortraits.get(1);
            else
                return userPortraits.get(2);
        }
        Bitmap bmp = userPortraits.get(uid);
        if (bmp != null)
            return bmp;
        return userPortraits.get(0);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        long uid = currentUid;
        if (userPortraits.indexOfKey(uid) >= 0)
        {
            Bitmap portrait = getPortrait(uid, true);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            Matrix mx = new Matrix();
            float viewWidth = getWidth();
            float portraitWidth = viewWidth / 3;

            // 以向左移动为准。
            float appear = portraitAnimator.isRunning() ? Float.parseFloat(portraitAnimator.getAnimatedValue("appear").toString()) : 0.6f;
            float disappear = portraitAnimator.isRunning() ? Float.parseFloat(portraitAnimator.getAnimatedValue("disappear").toString()) : 0.6f;

            // 左头像。
            Bitmap leftPortrait = getPortrait((!moveLeft && portraitAnimator.isRunning()) ? beforeBeforeUid : beforeUid, false);
            mx.postScale(portraitWidth / 2 / leftPortrait.getWidth() * (85.0f / 75.0f), portraitWidth / 2 / leftPortrait.getWidth() * (85.0f / 75.0f));
            mx.postTranslate(portraitWidth / 4 - 10, portraitWidth / 4 - 5.0f);
            paint.setAlpha((int)((moveLeft ? disappear : appear)* 255));
            canvas.drawBitmap(leftPortrait, mx, paint);
            // 右头像。
            Bitmap rightPortrait = getPortrait((moveLeft && portraitAnimator.isRunning()) ? afterAfterUid : afterUid, false);
            mx.setScale(portraitWidth / 2 / rightPortrait.getWidth() * (85.0f / 75.0f), portraitWidth / 2 / rightPortrait.getWidth() * (85.0f / 75.0f));
            mx.postTranslate(viewWidth - portraitWidth * 3 / 4, portraitWidth / 4 - 5.0f);
            paint.setAlpha((int)((moveLeft ? appear : disappear) * 255));
            canvas.drawBitmap(rightPortrait, mx, paint);

            if(portraitAnimator != null && portraitAnimator.isRunning())
            {
                leftPortrait = getPortrait(beforeUid, false);
                rightPortrait = getPortrait(afterUid, false);
                drawPortrait(
                        canvas,
                        paint,
                        portraitAnimator.getAnimatedValue("scaleLeftToCenter"),
                        portraitAnimator.getAnimatedValue("alphaLeftToCenter"),
                        portraitAnimator.getAnimatedValue("xLeftToCenter"),
                        portraitAnimator.getAnimatedValue("yLeftToCenter"),
                        moveLeft ? portrait : leftPortrait
                );
                drawPortrait(
                        canvas,
                        paint,
                        portraitAnimator.getAnimatedValue("scaleCenterToRight"),
                        portraitAnimator.getAnimatedValue("alphaCenterToRight"),
                        portraitAnimator.getAnimatedValue("xCenterToRight"),
                        portraitAnimator.getAnimatedValue("yCenterToRight"),
                        moveLeft ? rightPortrait : portrait
                );
            }
            else
            {
                paint.setAlpha(255);
                mx = new Matrix();
                mx.postScale(portraitWidth / portrait.getWidth(), portraitWidth / portrait.getWidth());
                mx.postTranslate(portraitWidth, 0.0f);
                canvas.drawBitmap(portrait, mx, paint);
            }
        }
    }

    private void drawPortrait(Canvas canvas, Paint paint, Object valueScale, Object valueAlpha, Object valueX, Object valueY, Bitmap portrait)
    {
        Matrix mx = new Matrix();
        if(valueScale != null)
        {
            float scale = Float.parseFloat(valueScale.toString());
            mx.postScale(scale, scale);
        }
        if(valueAlpha != null)
        {
            paint.setAlpha((int)(Float.parseFloat(valueAlpha.toString()) * 255.0f));
        }
        float x = 0.0f, y = 0.0f;
        if(valueX != null)
            x = Float.parseFloat(valueX.toString());
        if(valueY != null)
            y = Float.parseFloat(valueY.toString());
        mx.postTranslate(x, y);
        canvas.drawBitmap(portrait, mx, paint);
    }

    private long portraitFileToUid(String portraitFile) {
        if (portraitFile == null || portraitFile.length() == 0)
            return 0;

        for (SDKModel.AccountInfo account : accounts) {
            if (account.portraitUrl != null && portraitFile.compareToIgnoreCase(account.portraitUrl) == 0) {
                if (account.uid > 0)
                    return account.uid;
            }
        }

        return 0;
    }

    private void imageLoaded(String portraitFile, Bitmap portrait)
    {
        long uid = portraitFileToUid(portraitFile);
        if (uid > 0 && userPortraits.indexOfKey(uid) >= 0) {
            userPortraits.remove(uid);
            userPortraits.put(uid, portrait); // GetRoundedCornerBitmap(portrait));
            YLog.debug(this, "LoginPortraitView imageLoaded uid=%s, url=%s", uid, portraitFile);
            // 要用延迟更新
            postInvalidate();
        }
    }

    private void move(boolean left, float velocityX)
    {
        float viewWidth = getWidth();
        float portraitWidth = viewWidth / 3;

        if(portraitAnimator.isRunning())
        {/*
            if((!moveLeft && left) || (moveLeft && !left))
            {
                portraitAnimator.reverse();
                portraitAnimator.setRepeatCount(0);
            }*/
        }
        else
        {
            portraitAnimator = new ValueAnimator();
            portraitAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    invalidate();
                }
            });

            portraitAnimator.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animator)
                {
                }

                @Override
                public void onAnimationEnd(Animator animator)
                {
                    if(moveLeft)
                    {
                        setCurrentUserByUid(afterUid, "", true, false);
                    }
                    else
                    {
                        setCurrentUserByUid(beforeUid, "", true, false);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator)
                {
                }

                @Override
                public void onAnimationRepeat(Animator animator)
                {
                    if(moveLeft)
                    {
                        setCurrentUserByUid(afterUid, "", true, false);
                    }
                    else
                    {
                        setCurrentUserByUid(beforeUid, "", true, false);
                    }
                }
            });

            // 左头像。
            Bitmap leftPortrait = getPortrait(beforeUid, false);
            // 右头像。
            Bitmap rightPortrait = getPortrait(afterUid, false);
            // 中头像。
            Bitmap portrait = getPortrait(currentUid, true);
            float leftToCenterWidth = left ? portrait.getWidth() : leftPortrait.getWidth();
            float rightToCenterWidth = left ? rightPortrait.getWidth() : portrait.getWidth();
            portraitAnimator.setValues(
                    PropertyValuesHolder.ofFloat("scaleLeftToCenter", portraitWidth / 2 / leftToCenterWidth * (85.0f / 75.0f), portraitWidth / leftToCenterWidth),
                    PropertyValuesHolder.ofFloat("alphaLeftToCenter", 0.5f, 1.0f),
                    PropertyValuesHolder.ofFloat("xLeftToCenter", portraitWidth / 4 - 10, portraitWidth),
                    PropertyValuesHolder.ofFloat("yLeftToCenter", portraitWidth / 4 - 5, 0),
                    PropertyValuesHolder.ofFloat("scaleCenterToRight", portraitWidth / rightToCenterWidth, portraitWidth / 2 / rightToCenterWidth * ( 85.0f / 75.0f)),
                    PropertyValuesHolder.ofFloat("alphaCenterToRight", 1.0f, 0.5f),
                    PropertyValuesHolder.ofFloat("xCenterToRight", portraitWidth, viewWidth - portraitWidth * 3 / 4),
                    PropertyValuesHolder.ofFloat("yCenterToRight", 0, portraitWidth / 4 - 5),
                    PropertyValuesHolder.ofFloat("appear", left ? 0.6f : 0.0f, left ? 0.0f : 0.6f),
                    PropertyValuesHolder.ofFloat("disappear", left ? 0.0f : 0.6f, left ? 0.6f : 0.0f)
            );

            long repeat = 1;
            if(velocityX > 10000)
            {
                repeat = 2;
            }

            if(!left)
            {
                if(leftReserved == 0)
                {
                    return;
                }
                else
                {
                    repeat = Math.min(repeat, leftReserved);
                    portraitAnimator.setDuration(500 / repeat);
                    portraitAnimator.setRepeatCount((int)(repeat -1));
                }
                moveLeft = false;
                portraitAnimator.start();
            }
            else
            {
                if(rightReserved == 0)
                {
                    return;
                }
                else
                {
                    repeat = Math.min(repeat, rightReserved);
                    portraitAnimator.setDuration(500 / repeat);
                    portraitAnimator.setRepeatCount((int)(repeat -1));
                }
                moveLeft = true;
                portraitAnimator.reverse();
            }
        }
    }

    public boolean fling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        if(Math.abs(velocityX) > 300)
        {
            move(e2.getX() < e1.getX(), Math.abs(velocityX));
            System.out.print("fucking onFling velocityX=");
        }
        else
        {
            System.out.print("fucking onScroll");
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        if (moveAction != null)
            moveAction.onTouchEvent(event);
        switch(action)
        {
            case (MotionEvent.ACTION_DOWN):
            {
                pointDown = event.getX();
                return true;
            }
            case (MotionEvent.ACTION_CANCEL):
            {
                YLog.debug(this, "LoginPortraitView fucking cancel %s", event.getX() - pointDown);
                if (Math.abs(event.getX() - pointDown) > 50) {
                    move(event.getX() < pointDown, 500);
                }
                pointDown = event.getX();
            }
            break;
        }
        return super.onTouchEvent(event);
    }
}
