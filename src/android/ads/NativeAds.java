package fban.plugin.ads;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
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
            nativeAdView.setVisibility(View.GONE);
        }
    }

    public void show() {
        if (nativeAdView == null) {
            nativeAd = new NativeAd(plugin.webView.getContext(), placementID);
            addNativeView(nativeAd);
            nativeAd.loadAd();
        } else if (nativeAdView.getVisibility() == View.GONE) {
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
                plugin.emit(Events.NATIVE_MEDIA_LOAD);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "Error showing ad with" + adError.getErrorMessage());
                plugin.emit(Events.NATIVE_LOAD_FAIL);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                plugin.emit(Events.NATIVE_LOAD);
            }

            @Override
            public void onAdClicked(Ad ad) {
                plugin.emit(Events.NATIVE_CLICK);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                plugin.emit(Events.NATIVE_IMPRESSION);
            }
        });
    }

    private void addNativeView(NativeAd nativeAd) {
        nativeAdView = NativeAdView.render(plugin.webView.getContext(), nativeAd);
        int adHeight = (int)AdBase.pxFromDp(plugin.webView.getContext(), 240f);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, adHeight
        );
        View view = plugin.webView.getView();
        FrameLayout webview = (FrameLayout) view.getParent();

        webview.addView(nativeAdView);

        int diffHeight = webview.getHeight() - view.getHeight();
        int marginForAd = ((webview.getHeight() / 2) - adHeight) + diffHeight;
        params.setMargins(0, marginForAd, 0, 0);
        nativeAdView.setLayoutParams(params);
    }
}
