package fban.plugin.ads;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;


import fban.plugin.Action;
import fban.plugin.Events;

public class NativeAds extends AdBase {
    private static final String TAG = "FBAN-Free()::NativeADs";
    private NativeAd nativeAd;
    private View nativeAdView;
    private ViewGroup parentView;

    NativeAds(int id, String placementID) {
        super(id, placementID);
    }

    public static boolean executeNativeShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                NativeAds nativeAds = (NativeAds) action.getAd();
                if (nativeAds == null) {
                    nativeAds = new NativeAds(
                            action.optId(),
                            action.getPlacementID()
                    );
                }
                nativeAds.show();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public static boolean executeNativeHideAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BannerAd bannerAd = (BannerAd) action.getAd();
                if (bannerAd != null) {
                    bannerAd.hide();
                }

                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public void hide() {
        if (nativeAdView != null) {
            Log.d(TAG, "tryin to hide the nativead");
            nativeAdView.setVisibility(View.GONE);
        }
    }

    public void show() {
        if (nativeAdView == null) {
            nativeAd = new NativeAd(plugin.webView.getContext(), placementID);
            addNativeView(nativeAd);
            nativeAd.loadAd();
        } else if (nativeAdView.getVisibility() == View.GONE) {
            Log.d(TAG, "already add to view");
            nativeAd.loadAd();
            nativeAdView.setVisibility(View.VISIBLE);
        } else {
            View view = plugin.webView.getView();
            ViewGroup wvParentView = (ViewGroup) view.getParent();
            if (parentView != wvParentView) {
                parentView.removeAllViews();
                if (parentView.getParent() != null) {
                    ((ViewGroup)parentView.getParent()).removeView(parentView);
                }
                addNativeView(nativeAd);
                nativeAd.loadAd();
            }
        }

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                Log.d(TAG, "Media loaded");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "err loaded" + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "ad loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "ad clicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "ad impression");
            }
        });
    }

    private void addNativeView(NativeAd nativeAd) {
        View view = plugin.webView.getView();
        Log.d(TAG, "Add native ad to the parent view000");
        nativeAdView = NativeAdView.render(plugin.webView.getContext(), nativeAd);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 420);
        params.setMargins(0, view.getHeight() - 420, 0, 0);
        nativeAdView.setLayoutParams(params);

//        ViewGroup wvParentView = (ViewGroup) view.getParent();
//        if (parentView == null) {
//            parentView = new LinearLayout(plugin.webView.getContext());
//        }
//        Log.d(TAG, "Add native ad to the parent view111");
//        if (wvParentView != null && wvParentView != parentView) {
//            wvParentView.removeView(view);
//            LinearLayout content = (LinearLayout) parentView;
//            content.setOrientation(LinearLayout.VERTICAL);
//            content.setGravity(Gravity.CENTER_VERTICAL);
//            parentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));
//            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 420, 0.9F));
//            parentView.addView(view);
//            wvParentView.addView(parentView);
//        }


        Log.d(TAG, "Add native ad to the parent view");
//        parentView.addView(
//                nativeAdView,
//                new LinearLayout.LayoutParams(nativeAdView.getWidth(), nativeAdView.getHeight())
//        );
//        parentView.bringToFront();
//        parentView.requestLayout();
//        parentView.requestFocus();
    }
}
