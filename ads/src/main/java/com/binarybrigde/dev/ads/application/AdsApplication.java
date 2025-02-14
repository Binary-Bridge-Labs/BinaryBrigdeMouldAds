package com.binarybrigde.dev.ads.application;

import android.app.Application;

import com.binarybrigde.dev.ads.config.BBDAdConfig;
import com.binarybrigde.dev.ads.util.AppUtil;
import com.binarybrigde.dev.ads.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public abstract class AdsApplication extends Application {

    protected BBDAdConfig BBDAdConfig;
    protected List<String> listTestDevice;

    @Override
    public void onCreate() {
        super.onCreate();
        listTestDevice = new ArrayList<String>();
        BBDAdConfig = new BBDAdConfig(this);
        if (SharePreferenceUtils.getInstallTime(this) == 0) {
            SharePreferenceUtils.setInstallTime(this);
        }
        AppUtil.currentTotalRevenue001Ad = SharePreferenceUtils.getCurrentTotalRevenue001Ad(this);
    }

}
