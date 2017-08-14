package com.google.splashactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xiaomi.ad.AdListener;
import com.xiaomi.ad.AdSdk;
import com.xiaomi.ad.SplashAdListener;
import com.xiaomi.ad.adView.BannerAd;
import com.xiaomi.ad.adView.InterstitialAd;
import com.xiaomi.ad.adView.SplashAd;
import com.xiaomi.ad.common.pojo.AdError;
import com.xiaomi.ad.common.pojo.AdEvent;

import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 2017/8/10.
 */



public class AdUtils {


    public static final String APPID = "2882303761517521554";
    private static final String POSITION_ID_SPLASH = "bfe5161c5e56c5258c24b3a2f4c8b051";
    private static final String POSITION_ID = "ed65ad4028db0d794303aa35a08c9554";
    private static String BANNER_ID = "9fa525d19113801c60e86d0660f0033a";
    private static final String TAG = "AD_LOG";
    private static final String APP_VERSION = "BLOCKMC";
    private static final String SAVE_PATH = "/sdcard/games";
    static AdListener adListener;

    private static boolean isBannerShowed = false;
    private static BannerAd h5BannerAd;
    private static boolean canShowBanner = false;
    private static boolean isInRunnabale = false;


    static Handler mHandler = new Handler();

    public static void copy_apk(Context context){
        File file = new File(SAVE_PATH);
        if (!file.exists()){
            file.mkdir();
        }
    }
    public static void init(Context context){
        if (APP_VERSION.equals("BLOCKMC")){

        }
        AdSdk.initialize(context, APPID);
        adListener = new AdListener() {
            @Override
            public void onAdError(AdError adError) {

            }

            @Override
            public void onAdEvent(AdEvent adEvent) {
                if (adEvent.mType == AdEvent.TYPE_FINISH){
                    showBanner();
                }else if (adEvent.mType == AdEvent.TYPE_SKIP){
                    showBanner();
                }else if (adEvent.mType == AdEvent.TYPE_LOAD_FAIL){
                    showBanner();
                }else if (adEvent.mType == AdEvent.TYPE_VIEW){
                    canShowBanner = false;
                    hideBanner();

                }
                Log.d(TAG,adEvent.mType+"");

            }

            @Override
            public void onAdLoaded() {
                interstitialAd.show();

            }

            @Override
            public void onViewCreated(View view) {

            }
        };

    }

    static FrameLayout flayout;
    private static ImageView button;
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Activity mActivity, float dpValue) {
        final float scale = mActivity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static void showBanner(){
        canShowBanner = true;
        isBannerShowed = false;

        Log.d(TAG,"banner 展示了");
        controlCloseButton(isBannerShowed);
        h5BannerAd.show(BANNER_ID);

    }


    public static void hideBanner(){
        if (flayout==null){
            return;
        }
        flayout.setVisibility(View.INVISIBLE);
        if (!isInRunnabale){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (canShowBanner){
                        isInRunnabale = false;
                        showBanner();
                    }
                }
            },360000);
        }
        isInRunnabale = true;

    }

    public static void controlCloseButton(boolean close){
        if (button != null){
            if (close){
                button.setVisibility(ImageView.VISIBLE);
            }else {
                button.setVisibility(ImageView.INVISIBLE);
            }

        }else {
            Log.d(TAG,"button is null");
        }
    }


    private static void bannerLayout(final Activity activity){

        flayout = new FrameLayout(activity);

        flayout.removeAllViews();

        WindowManager windowManager = (WindowManager) activity
                .getSystemService(activity.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        double scal_x_y = 0;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;


        params.height =  ViewGroup.LayoutParams.WRAP_CONTENT; //SUtils.dip2px(activity,50);

        button = new ImageView(activity);

        if (true){// XmParms.isBannerCanClose
            try {
                // 设置图片
                button.setImageBitmap(BitmapFactory.decodeStream(activity.getAssets().open("my_cancel.png")));

            } catch (IOException e) {
                e.printStackTrace();
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideBanner();
                }
            });
        }


        // 设置 layout 的 大小各种样式 对子view进行处理（其实就是banner广告）
        FrameLayout.LayoutParams ban_par = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ban_par.weight = 15;
//        ban_par.height = (int) (188 * scal_x_y);
        ban_par.gravity = Gravity.CENTER_VERTICAL;
        FrameLayout.LayoutParams btn_par = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        btn_par.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        btn_par.gravity = Gravity.RIGHT;;
        btn_par.width = dip2px(activity,25);
        btn_par.height = dip2px(activity,25);
        Log.e("LittleDog : ","width : "+btn_par.width+" height : "+ btn_par.height);
        FrameLayout ban_frameLayout = new FrameLayout(activity);
        ban_frameLayout.setLayoutParams(ban_par);
//        btn_frameLayout = new FrameLayout(activity);
        button.setLayoutParams(btn_par);
        // 刚开始关闭按钮 banner广告加载成功后才 显示
        controlCloseButton(false);

        flayout.addView(ban_frameLayout);
        flayout.addView(button);

        windowManager.addView(flayout, params);


        h5BannerAd = new BannerAd(activity, ban_frameLayout, new BannerAd.BannerListener() {


            @Override
            public void onAdEvent(AdEvent adEvent) {
                Log.d(TAG, "onAdEvent : "+ adEvent);
                if (adEvent.mType == AdEvent.TYPE_CLICK) {
                    Log.d(TAG, "ad has been clicked!");
                    hideBanner();
                } else if (adEvent.mType == AdEvent.TYPE_SKIP) {
                    Log.d(TAG, "x button has been clicked!");
                } else if (adEvent.mType == AdEvent.TYPE_LOAD){

                }else if (adEvent.mType == AdEvent.TYPE_VIEW) {
                    Log.d(TAG, "ad has been showed!,这个是轮播事件");
                    isBannerShowed = true;
                    // banner广告加载成功后才 显示关闭按钮
                    controlCloseButton(true);
                    flayout.setVisibility(View.VISIBLE);


                }else if (adEvent.mType == AdEvent.TYPE_INTERRUPT){
                    Log.d(TAG, "AdEvent.TYPE_INTERRUPT : "+AdEvent.TYPE_INTERRUPT);
                }else if (adEvent.mType == AdEvent.TYPE_LOAD_FAIL){
                    Log.d(TAG, "AdEvent.TYPE_LOAD_FAIL : "+AdEvent.TYPE_LOAD_FAIL);
                }else if (adEvent.mType == AdEvent.TYPE_APP_LAUNCH_FAIL){
                    Log.d(TAG, "AdEvent.TYPE_APP_LAUNCH_FAIL : "+AdEvent.TYPE_APP_LAUNCH_FAIL);
                }else {
                    Log.d(TAG, "unknow : "+adEvent.mType);
                }
            }


        });
    }



    static InterstitialAd interstitialAd;

    public static void preInterAd(Context context){
        bannerLayout((Activity) context);
        interstitialAd = new InterstitialAd(context.getApplicationContext(), (Activity) context);
    }

    public static void showInterstitialAd(Context context){
        interstitialAd.requestAd(POSITION_ID, adListener);

    }

    public static void showSplash(final Context context){
        FrameLayout layout = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((Activity)context).addContentView(layout,layoutParams);
        String imgname = "default_splash_";
        int imgid = context.getResources().getIdentifier(imgname, "drawable", context.getPackageName());
        SplashAd splashAd = new SplashAd(context, layout, imgid, new SplashAdListener() {
            @Override
            public void onAdPresent() {

            }

            @Override
            public void onAdClick() {
                //gotoNextActivity(context);
            }

            @Override
            public void onAdDismissed() {
                //gotoNextActivity(context);
            }

            @Override
            public void onAdFailed(String s) {
                //gotoNextActivity(context);
            }
        });
        splashAd.requestAd(POSITION_ID_SPLASH);
    }

    public static void gotoNextActivity(Context context) {

        Intent intent = new Intent(context, NextActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();


    }
}
