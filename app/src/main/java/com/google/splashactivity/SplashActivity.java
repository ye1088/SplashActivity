package com.google.splashactivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by admin on 2017/8/10.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(this, "Splash activity showed ~~", Toast.LENGTH_SHORT).show();
        AdUtils.init(this);
        AdUtils.showSplash(this);
    }
}
