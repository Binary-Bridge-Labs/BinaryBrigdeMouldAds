package com.binarybrigde.dev.ads.ads.nativeAds;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.binarybrigde.dev.ads.R;
import com.binarybrigde.dev.ads.admob.Admob;
import com.binarybrigde.dev.ads.ads.wrapper.ApAdValue;
import com.binarybrigde.dev.ads.ads.wrapper.ApNativeAd;
import com.binarybrigde.dev.ads.ads.wrapper.StatusAd;
import com.binarybrigde.dev.ads.funtion.AdCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BBDAdPlacer {
    String TAG = "PAIAdPlacer";
    private HashMap<Integer, ApNativeAd> listAd = new HashMap<>();
    private List<Integer> listPositionAd = new ArrayList<>();
    private BBDAdPlacerSettings settings;
    private RecyclerView.Adapter adapterOriginal;
    private Activity activity;
    private int countLoadAd = 0;

    public BBDAdPlacer(BBDAdPlacerSettings settings, RecyclerView.Adapter adapterOriginal, Activity activity) {
        this.settings = settings;
        this.adapterOriginal = adapterOriginal;
        this.activity = activity;
        configData();

    }

    public void configData() {
        if (settings.isRepeatingAd()) {
            int posAddAd = 0;
            int countNewAdapter = adapterOriginal.getItemCount();
            while (posAddAd <= countNewAdapter - settings.getPositionFixAd()) {
                posAddAd += settings.getPositionFixAd();
                if (listAd.get(posAddAd) == null) {
                    listAd.put(posAddAd, new ApNativeAd(StatusAd.AD_INIT));
                    listPositionAd.add(posAddAd);
                }
                posAddAd++;
                countNewAdapter++;
            }
        } else {
            listPositionAd.add(settings.getPositionFixAd());
            listAd.put(settings.getPositionFixAd(), new ApNativeAd(StatusAd.AD_INIT));
        }
    }

    public void renderAd(int pos, RecyclerView.ViewHolder holder) {
        ApNativeAd adNative = listAd.get(pos);
        if (adNative.getAdmobNativeAd() == null) {
            if (listAd.get(pos).getStatus() != StatusAd.AD_LOADING) {
                holder.itemView.post(() -> {
                    ApNativeAd nativeAd = new ApNativeAd(StatusAd.AD_LOADING);
                    listAd.put(pos, nativeAd);
                    Admob.getInstance().loadNativeAd(activity, settings.getAdUnitId(), new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            super.onUnifiedNativeAdLoaded(unifiedNativeAd);
                            unifiedNativeAd.setOnPaidEventListener(new OnPaidEventListener() {
                                @Override
                                public void onPaidEvent(@NonNull AdValue adValue) {
                                    BBDAdPlacer.this.onAdRevenuePaid(new ApAdValue(adValue));
                                }
                            });

                            BBDAdPlacer.this.onAdLoaded(pos);
                            nativeAd.setAdmobNativeAd(unifiedNativeAd);
                            nativeAd.setStatus(StatusAd.AD_LOADED);
                            listAd.put(pos, nativeAd);
                            populateAdToViewHolder(holder, unifiedNativeAd, pos);
                        }

                        @Override
                        public void onAdFailedToLoad(@Nullable LoadAdError i) {
                            super.onAdFailedToLoad(i);
                            ShimmerFrameLayout containerShimmer = holder.itemView.findViewById(R.id.shimmer_container_native);
                            containerShimmer.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            BBDAdPlacer.this.onAdClicked();
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                            BBDAdPlacer.this.onAdImpression();
                        }
                    });
                });
            }
        } else {
            if (listAd.get(pos).getStatus() == StatusAd.AD_LOADED) {
                populateAdToViewHolder(holder, listAd.get(pos).getAdmobNativeAd(), pos);
            }
        }
    }

    private void populateAdToViewHolder(RecyclerView.ViewHolder holder, NativeAd unifiedNativeAd, int pos) {
        NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(activity)
                .inflate(settings.getLayoutCustomAd(), null);
        FrameLayout adPlaceHolder = holder.itemView.findViewById(R.id.fl_adplaceholder);
        ShimmerFrameLayout containerShimmer = holder.itemView.findViewById(R.id.shimmer_container_native);


        containerShimmer.stopShimmer();
        containerShimmer.setVisibility(View.GONE);
        adPlaceHolder.setVisibility(View.VISIBLE);
        Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, nativeAdView);
        Log.i(TAG, "native ad in recycle loaded position: " + pos + "  title: " + unifiedNativeAd.getHeadline() + "   count child ads:" + adPlaceHolder.getChildCount());
        adPlaceHolder.removeAllViews();

        adPlaceHolder.addView(nativeAdView);
    }

    public void loadAds() {
        countLoadAd = 0;
        Admob.getInstance().loadNativeAds(activity, settings.getAdUnitId(), new AdCallback() {
            @Override
            public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                super.onUnifiedNativeAdLoaded(unifiedNativeAd);
                ApNativeAd nativeAd = new ApNativeAd(settings.getLayoutCustomAd(), unifiedNativeAd);
                nativeAd.setStatus(StatusAd.AD_LOADED);
                listAd.put(listPositionAd.get(countLoadAd), nativeAd);
                Log.i(TAG, "native ad in recycle loaded: " + countLoadAd);
                countLoadAd++;
            }
        }, Math.min(listAd.size(), settings.getPositionFixAd()));
    }

    public boolean isAdPosition(int pos) {
        ApNativeAd nativeAd = listAd.get(pos);
        return nativeAd != null;
    }

    public int getOriginalPosition(int posAdAdapter) {
        int countAd = 0;
        for (int i = 0; i < posAdAdapter; i++) {
            if (listAd.get(i) != null)
                countAd++;
        }
        return posAdAdapter - countAd;
    }

    public int getAdjustedCount() {
        int countMinAd;
        if (settings.isRepeatingAd()) {
            countMinAd = adapterOriginal.getItemCount() / settings.getPositionFixAd();
        } else if (adapterOriginal.getItemCount() >= settings.getPositionFixAd()) {
            countMinAd = 1;
        } else {
            countMinAd = 0;
        }

        return adapterOriginal.getItemCount() + Math.min(countMinAd, listAd.size());
    }


    public void onAdLoaded(int position) {
        Log.i(TAG, "Ad native loaded in pos: " + position);
        if (settings.getListener() != null)
            settings.getListener().onAdLoaded(position);
    }

    public void onAdRemoved(int position) {
        Log.i(TAG, "Ad native removed in pos: " + position);
        if (settings.getListener() != null)
            settings.getListener().onAdRemoved(position);
    }

    public void onAdClicked() {
        Log.i(TAG, "Ad native clicked ");
        if (settings.getListener() != null)
            settings.getListener().onAdClicked();
    }

    public void onAdRevenuePaid(ApAdValue adValue) {
        Log.i(TAG, "Ad native revenue paid ");
        if (settings.getListener() != null)
            settings.getListener().onAdRevenuePaid(adValue);
    }

    public void onAdImpression() {
        Log.i(TAG, "Ad native impression ");
        if (settings.getListener() != null)
            settings.getListener().onAdImpression();
    }


    public interface Listener {
        void onAdLoaded(int position);

        void onAdRemoved(int position);

        void onAdClicked();

        void onAdRevenuePaid(ApAdValue adValue);

        void onAdImpression();
    }
}
