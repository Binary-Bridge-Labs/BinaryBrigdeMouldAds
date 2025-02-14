package com.binarybrigde.dev.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.binarybrigde.dev.ads.admob.Admob;
import com.binarybrigde.dev.ads.admob.AppOpenManager;
import com.binarybrigde.dev.ads.ads.BBDAd;
import com.binarybrigde.dev.ads.ads.BBDAdCallback;
import com.binarybrigde.dev.ads.ads.bannerAds.BBDBannerAdView;
import com.binarybrigde.dev.ads.ads.nativeAds.BBDNativeAdView;
import com.binarybrigde.dev.ads.ads.wrapper.ApAdError;
import com.binarybrigde.dev.ads.ads.wrapper.ApInterstitialAd;
import com.binarybrigde.dev.ads.ads.wrapper.ApRewardAd;
import com.binarybrigde.dev.ads.billing.AppPurchase;
import com.binarybrigde.dev.ads.config.BBDAdConfig;
import com.binarybrigde.dev.ads.dialog.DialogExitApp1;
import com.binarybrigde.dev.ads.dialog.InAppDialog;
import com.binarybrigde.dev.ads.event.BBDAdjust;
import com.binarybrigde.dev.ads.funtion.AdCallback;
import com.binarybrigde.dev.ads.funtion.DialogExitListener;
import com.binarybrigde.dev.ads.funtion.PurchaseListener;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.nativead.NativeAd;
import com.mia.module.BuildConfig;
import com.mia.module.R;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String PRODUCT_ID = "android.test.purchased";
    private static final String TAG = "MAIN_TEST";

    private static final String EVENT_TOKEN_SIMPLE = "";
    private static final String EVENT_TOKEN_REVENUE = "";


    private FrameLayout frAds;
    private NativeAd unifiedNativeAd;
    private ApInterstitialAd mInterstitialAd;
    private ApRewardAd rewardAd;


    private String idBanner = "";
    private String idNative = "";
    private String idInter = "";

    private int layoutNativeCustom;
    private BBDNativeAdView BBDNativeAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BBDNativeAdView = findViewById(R.id.bbd_native_ads);


        new Thread(
                () ->
                        // Initialize the Google Mobile Ads SDK on a background thread.
                        MobileAds.initialize(
                                this,
                                initializationStatus -> {
                                    Map<String, AdapterStatus> statusMap =
                                            initializationStatus.getAdapterStatusMap();
                                    for (String adapterClass : statusMap.keySet()) {
                                        AdapterStatus status = statusMap.get(adapterClass);
                                        Log.d(
                                                "MyApp",
                                                String.format(
                                                        "Adapter name: %s, Description: %s, Latency: %d",
                                                        adapterClass, status.getDescription(), status.getLatency()));
                                    }
                                    // Start loading ads here...
                                }))
                .start();

        configMediationProvider();
        BBDAd.getInstance().setCountClickToShowAds(3);

        AppOpenManager.getInstance().setEnableScreenContentCallback(true);
        AppOpenManager.getInstance().setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                Log.e("AppOpenManager", "onAdShowedFullScreenContent: ");

            }
        });

        BBDNativeAdView.loadNativeAd(this, idNative, new BBDAdCallback() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });


        AppPurchase.getInstance().setPurchaseListener(new PurchaseListener() {
            @Override
            public void onProductPurchased(String productId, String transactionDetails) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void displayErrorMessage(String errorMsg) {
            }

            @Override
            public void onUserCancelBilling() {

            }
        });

        BBDBannerAdView bannerAdView = findViewById(R.id.bannerView);
        bannerAdView.loadBanner(this, idBanner, new BBDAdCallback() {
            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
        loadAdInterstitial();

        findViewById(R.id.btShowAds).setOnClickListener(v -> {
            if (mInterstitialAd.isReady()) {

                ApInterstitialAd inter = BBDAd.getInstance().getInterstitialAds(this, idInter);

                BBDAd.getInstance().showInterstitialAdByTimes(this, mInterstitialAd, new BBDAdCallback() {
                    @Override
                    public void onNextAction() {
                        startActivity(new Intent(MainActivity.this, ContentActivity.class));
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                    }

                    @Override
                    public void onInterstitialShow() {
                        super.onInterstitialShow();
                    }
                }, true);
            } else {
                loadAdInterstitial();
            }
        });

        findViewById(R.id.btForceShowAds).setOnClickListener(v -> {
            if (mInterstitialAd.isReady()) {
                BBDAd.getInstance().forceShowInterstitial(this, mInterstitialAd, new BBDAdCallback() {
                    @Override
                    public void onNextAction() {
                        startActivity(new Intent(MainActivity.this, SimpleListActivity.class));
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                    }

                    @Override
                    public void onInterstitialShow() {
                        super.onInterstitialShow();
                    }
                }, true);
            } else {
                loadAdInterstitial();
            }

        });

        findViewById(R.id.btnShowReward).setOnClickListener(v -> {
            if (rewardAd != null && rewardAd.isReady()) {
                BBDAd.getInstance().forceShowRewardAd(this, rewardAd, new BBDAdCallback());
                return;
            }
            rewardAd = BBDAd.getInstance().getRewardAd(this, BuildConfig.ad_reward);
        });

        Button btnIAP = findViewById(R.id.btIap);
        if (AppPurchase.getInstance().isPurchased()) {
            btnIAP.setText("Consume Purchase");
        } else {
            btnIAP.setText("Purchase");
        }
        btnIAP.setOnClickListener(v -> {
            if (AppPurchase.getInstance().isPurchased()) {
                AppPurchase.getInstance().consumePurchase(AppPurchase.PRODUCT_ID_TEST);
            } else {
                InAppDialog dialog = new InAppDialog(this);
                dialog.setCallback(() -> {
                    AppPurchase.getInstance().purchase(this, PRODUCT_ID);
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
        Button btnNativeFull = findViewById(R.id.btnNativeFull);
        btnNativeFull.setOnClickListener(v -> {
            if (mInterstitialAd.isReady()) {

                ApInterstitialAd inter = BBDAd.getInstance().getInterstitialAds(this, idInter);

                BBDAd.getInstance().showInterstitialAdByTimes(this, mInterstitialAd, new BBDAdCallback() {
                    @Override
                    public void onNextAction() {
                        //ShowNativeFull

                        startActivity(new Intent(MainActivity.this, ContentActivity.class));
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                    }

                    @Override
                    public void onInterstitialShow() {
                        super.onInterstitialShow();
                    }
                }, true);
            } else {
                loadAdInterstitial();
            }
        });

    }

    private void configMediationProvider() {
        if (BBDAd.getInstance().getMediationProvider() == BBDAdConfig.PROVIDER_ADMOB) {
            idBanner = BuildConfig.ad_banner;
            idNative = BuildConfig.ad_native;
            idInter = BuildConfig.ad_interstitial_splash;
            layoutNativeCustom = com.binarybrigde.dev.ads.R.layout.custom_native_admod_medium_rate;
        } else {
            idBanner = getString(R.string.applovin_test_banner);
            idNative = getString(R.string.applovin_test_native);
            idInter = getString(R.string.applovin_test_inter);
            layoutNativeCustom = com.binarybrigde.dev.ads.R.layout.custom_native_max_medium;
        }
    }

    private void loadAdInterstitial() {

        mInterstitialAd = BBDAd.getInstance().getInterstitialAds(this, idInter);
    }


    public void onTrackSimpleEventClick(View v) {
        BBDAdjust.onTrackEvent(EVENT_TOKEN_SIMPLE);
    }

    public void onTrackRevenueEventClick(View v) {
        BBDAdjust.onTrackRevenue(EVENT_TOKEN_REVENUE, 1f, "EUR");
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNativeExit();
    }

    private void loadNativeExit() {

        if (unifiedNativeAd != null)
            return;
        Admob.getInstance().loadNativeAd(this, BuildConfig.ad_native, new AdCallback() {
            @Override
            public void onUnifiedNativeAdLoaded(NativeAd unifiedNativeAd) {
                MainActivity.this.unifiedNativeAd = unifiedNativeAd;
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (unifiedNativeAd == null)
            return;

        DialogExitApp1 dialogExitApp1 = new DialogExitApp1(this, unifiedNativeAd, 3);
        dialogExitApp1.setDialogExitListener(new DialogExitListener() {
            @Override
            public void onExit(boolean exit) {
                MainActivity.super.onBackPressed();
            }
        });
        dialogExitApp1.setCancelable(false);
        dialogExitApp1.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppPurchase.getInstance().isPurchased(this)) {
            findViewById(R.id.btIap).setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}