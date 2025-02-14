package com.binarybrigde.dev.ads.ads;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.binarybrigde.dev.ads.ads.wrapper.ApAdError;
import com.binarybrigde.dev.ads.ads.wrapper.ApInterstitialAd;
import com.binarybrigde.dev.ads.ads.wrapper.ApNativeAd;
import com.binarybrigde.dev.ads.ads.wrapper.ApRewardItem;

public class BBDAdCallback {
    public void onNextAction() {
    }

    public void onAdClosed() {
    }

    public void onAdFailedToLoad(@Nullable ApAdError adError) {
    }

    public void onAdFailedToShow(@Nullable ApAdError adError) {
    }

    public void onAdLeftApplication() {
    }

    public void onAdLoaded() {

    }

    // ad splash loaded when showSplashIfReady = false
    public void onAdSplashReady() {

    }

    public void onInterstitialLoad(@Nullable ApInterstitialAd interstitialAd) {

    }

    public void onAdClicked() {
    }

    public void onAdImpression() {
    }


    public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {

    }

    public void onUserEarnedReward(@NonNull ApRewardItem rewardItem) {

    }

    public void onInterstitialShow() {

    }
}
