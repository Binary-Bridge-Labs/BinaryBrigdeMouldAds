package com.binarybrigde.dev.ads.event;

import android.content.Context;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAdRevenue;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.applovin.mediation.MaxAd;
import com.binarybrigde.dev.ads.ads.BBDAd;
import com.google.android.gms.ads.AdValue;

public class BBDAdjust {
    public static boolean enableAdjust = false;
    private static String eventNamePurchase = "";

    public static void setEventNamePurchase(String eventNamePurchase) {
        BBDAdjust.eventNamePurchase = eventNamePurchase;
    }

    public static void trackAdRevenue(String id) {
        AdjustAdRevenue adjustAdRevenue = new AdjustAdRevenue(id);
        Adjust.trackAdRevenue(adjustAdRevenue);
    }

    public static void onTrackEvent(String eventName) {
        AdjustEvent event = new AdjustEvent(eventName);
        Adjust.trackEvent(event);
    }

    public static void onTrackEvent(String eventName, String id) {
        AdjustEvent event = new AdjustEvent(eventName);
        // Assign custom identifier to event which will be reported in success/failure callbacks.
        event.setCallbackId(id);
        Adjust.trackEvent(event);
    }

    public static void onTrackRevenue(String eventName, float revenue, String currency) {
        AdjustEvent event = new AdjustEvent(eventName);
        // Add revenue 1 cent of an euro.
        event.setRevenue(revenue / 1000000.0, currency);
        Adjust.trackEvent(event);
    }

    public static void onTrackRevenuePurchase(float revenue, String currency) {
        if (BBDAdjust.enableAdjust) {
            onTrackRevenue(eventNamePurchase, revenue, currency);
        }

    }

    public static void pushTrackEventAdmob(AdValue adValue) {
        if (BBDAdjust.enableAdjust) {
            AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
            adRevenue.setRevenue(adValue.getValueMicros() / 1000000.0, adValue.getCurrencyCode());

            Adjust.trackAdRevenue(adRevenue);
        }
    }

    public static void pushTrackEventApplovin(MaxAd ad, Context context) {
        if (BBDAdjust.enableAdjust) {
            AdjustAdRevenue adjustAdRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_APPLOVIN_MAX);
            adjustAdRevenue.setRevenue(ad.getRevenue(), "USD");
            adjustAdRevenue.setAdRevenueNetwork(ad.getNetworkName());
            adjustAdRevenue.setAdRevenueUnit(ad.getAdUnitId());
            adjustAdRevenue.setAdRevenuePlacement(ad.getPlacement());

            Adjust.trackAdRevenue(adjustAdRevenue);

        }
    }

    static void logPaidAdImpressionValue(double revenue, String currency) {
        if (BBDAd.getInstance().getAdConfig().getAdjustConfig() != null && BBDAd.getInstance().getAdConfig().getAdjustConfig().isEnableAdjust()) {
            AdjustEvent event = new AdjustEvent(BBDAd.getInstance().getAdConfig().getAdjustConfig().getEventAdImpression());
            event.setRevenue(revenue, currency);
            Adjust.trackEvent(event);
        }
    }

}
