package fban.plugin.ads;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;

import fban.plugin.Action;
import fban.plugin.Events;

public class FBNativeAd extends AdBase {
    private static final String TAG = "FBAN-Free()::NativeADs";
    private NativeAd nativeAd;
    private View nativeAdView;
    private ViewGroup parentView;
    private JSONObject position;

    FBNativeAd(int id, String placementID, JSONObject position) {
        super(id, placementID);
        this.position = position;
    }

    public static boolean executeNativeShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
                if (fbNativeAd == null) {
                    fbNativeAd = new FBNativeAd(
                            action.optId(),
                            action.getPlacementID(),
                            action.optPosition()
                    );
                }
                fbNativeAd.show();
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
                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
                if (fbNativeAd != null) {
                    fbNativeAd.hide();
                }

                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public static boolean executeNativeHideAllAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
                if (fbNativeAd != null) {
                    fbNativeAd.hideAll();
                }

                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public void hide() {
        if (nativeAdView != null) {
            View view = plugin.webView.getView();
            FrameLayout webView = (FrameLayout) view.getParent();
            nativeAd.destroy();
            webView.removeView(nativeAdView);
        }
    }

    public void hideAll() {
        View view = plugin.webView.getView();
        FrameLayout webView = (FrameLayout) view.getParent();

        int count = webView.getChildCount();
        for (int i = 0; i<count; i++) {
            View v = webView.getChildAt(i);
            if (v instanceof NativeAdLayout) {
                webView.removeView(v);
            }
        }
    }

    public void show() {
        if (nativeAdView == null) {
            nativeAd = new NativeAd(plugin.webView.getContext(), placementID);
            addNativeView(nativeAd);
        } else {
            nativeAd.destroy();
            nativeAd = new NativeAd(plugin.webView.getContext(), placementID);
            addNativeView(nativeAd);
        }
        nativeAd.loadAd();

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                Log.d(TAG, "Media loaded");
                plugin.emit(Events.NATIVE_MEDIA_LOAD);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "Error loading ad with" + adError.getErrorMessage());
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

        params.setMargins(0, (int)position.optDouble("top"), 0, 0);
        nativeAdView.setLayoutParams(params);
        nativeAdView.bringToFront();
    }
}
