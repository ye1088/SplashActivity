package com.google.splashactivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.Utils2;

/**
 * Created by admin on 2017/8/10.
 */

public class NextActivity extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdUtils.init(this);
        AdUtils.preInterAd(this);
        Toast.makeText(this, "我是 nextActivity", Toast.LENGTH_SHORT).show();
        Utils2.addImageButton(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Utils2.selectVersion(this);
        AdUtils.showInterstitialAd(this);
    }
}
