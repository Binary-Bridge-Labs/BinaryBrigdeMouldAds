package com.binarybrigde.dev.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.binarybrigde.dev.ads.admob.AppOpenManager;
import com.binarybrigde.dev.ads.ads.BBDAd;
import com.binarybrigde.dev.ads.ads.BBDAdCallback;
import com.binarybrigde.dev.ads.config.BBDAdConfig;
import com.binarybrigde.dev.ads.funtion.AdCallback;
import com.mia.module.BuildConfig;
import com.mia.module.R;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private List<String> list = new ArrayList<>();
    private String idAdSplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (BBDAd.getInstance().getMediationProvider() == BBDAdConfig.PROVIDER_ADMOB)
            idAdSplash = BuildConfig.ad_interstitial_splash;
        else
            idAdSplash = getString(R.string.applovin_test_inter);

//        AppOpenManager.getInstance().loadAdOpenSplash2id(SplashActivity.class, this,
//                "ca-app-pub-3940256099942544/3419835294",
//                "ca-app-pub-3940256099942544/3419835294", 25000, new AdCallback() {
//                    @Override
//                    public void onNextAction() {
//                        super.onNextAction();
//                        startMain();
//                    }
//                });
        BBDAd.getInstance().loadSplashInterstitialAdsHighFloor(this,
                "ca-app-pub-3940256099942544/3419835294",
                "ca-app-pub-3940256099942544/3419835294",
                25000,
                5000,
                new BBDAdCallback());
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppOpenManager.getInstance().onCheckShowAppOpenSplashWhenFail(this, new AdCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                startMain();
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Splash onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "Splash onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}