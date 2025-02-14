package com.bblabs.module_ads;

import com.binarybrigde.dev.ads.ads.BBLAd;
import com.binarybrigde.dev.ads.config.AdjustConfig;
import com.binarybrigde.dev.ads.config.AppsflyerConfig;
import com.binarybrigde.dev.ads.config.BBDAdConfig;
import com.binarybrigde.dev.ads.application.AdsMultiDexApplication;
import com.binarybrigde.dev.ads.applovin.AppLovin;
import com.binarybrigde.dev.ads.applovin.AppOpenMax;
import com.binarybrigde.dev.ads.billing.AppPurchase;
import com.binarybrigde.dev.ads.admob.Admob;
import com.binarybrigde.dev.ads.admob.AppOpenManager;
import com.bblabs.module_ads.activity.MainActivity;
import com.bblabs.module_ads.activity.SplashActivity;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends AdsMultiDexApplication {
    private final String APPSFLYER_TOKEN = "";
    private final String ADJUST_TOKEN = "";
    private final String EVENT_PURCHASE_ADJUST = "";
    private final String EVENT_AD_IMPRESSION_ADJUST = "";
    protected StorageCommon storageCommon;
    private static MyApplication context;
    public static MyApplication getApplication() {
        return context;
    }
    public StorageCommon getStorageCommon() {
        return storageCommon;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Admob.getInstance().setNumToShowAds(0);

        storageCommon = new StorageCommon();
        initBilling();
        initAds();

    }

    private void initAds() {
        String environment = BuildConfig.env_dev ? BBDAdConfig.ENVIRONMENT_DEVELOP : BBDAdConfig.ENVIRONMENT_PRODUCTION;
        BBDAdConfig = new BBDAdConfig(this, BBDAdConfig.PROVIDER_ADMOB, environment);

        AdjustConfig adjustConfig = new AdjustConfig(true,ADJUST_TOKEN);
        adjustConfig.setEventAdImpression(EVENT_AD_IMPRESSION_ADJUST);

        adjustConfig.setEventNamePurchase(EVENT_PURCHASE_ADJUST);
        BBDAdConfig.setAdjustConfig(adjustConfig);

        AppsflyerConfig appsflyerConfig = new AppsflyerConfig(true,APPSFLYER_TOKEN);


        listTestDevice.add("EC25F576DA9B6CE74778B268CB87E431");
        BBDAdConfig.setListDeviceTest(listTestDevice);
        BBDAdConfig.setIntervalInterstitialAd(15);

        BBLAd.getInstance().init(this, BBDAdConfig, false);

        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        AppLovin.getInstance().setDisableAdResumeWhenClickAds(true);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);

        if (BBLAd.getInstance().getMediationProvider() == BBDAdConfig.PROVIDER_ADMOB) {
            AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        } else {
            AppOpenMax.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        }
    }

    private void initBilling() {
        List<String> listINAPId = new ArrayList<>();
        listINAPId.add(MainActivity.PRODUCT_ID);
        List<String> listSubsId = new ArrayList<>();

        AppPurchase.getInstance().initBilling(getApplication(), listINAPId, listSubsId);
    }

}
