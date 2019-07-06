package fban.plugin.ads;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.NativeAdLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import java.util.ArrayList;

import fban.plugin.Action;
import fban.plugin.Events;

public class BannerAd extends AdBase {
    private static final String TAG = "FBAN-Free()::BannerAD";
    private AdView adView;
    private ViewGroup parentView;

    BannerAd(int id, String placementID) {
        super(id, placementID);
    }

    public static boolean executeShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                BannerAd bannerAd = (BannerAd) action.getAd();
                if (bannerAd == null) {
                    bannerAd = new BannerAd(
                            action.optId(),
                            action.getPlacementID()
                    );
                }
                bannerAd.show();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public static boolean executeHideAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BannerAd bannerAd = (BannerAd) action.getAd();
                Log.d(TAG, "Trying to hide the banner 9999");
                if (bannerAd != null) {
                    Log.d(TAG, "Trying to hide the banner 988");
                    bannerAd.hide();
                }

                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public void hide() {
        if (adView != null) {
            Log.d(TAG, "Trying to hide the banner");
            adView.setVisibility(View.GONE);

            FrameLayout webView = (FrameLayout) plugin.webView.getView().getParent();
            View view = plugin.webView.getView();

            int count = webView.getChildCount();
            ArrayList<View> views = new ArrayList<View>();
            for (int i = 0; i<count; i++) {
                View v = webView.getChildAt(i);
                if (v instanceof NativeAdLayout) {
                    views.add(v);
                }
            }

            webView.removeView(view);
            FrameLayout.LayoutParams webViewParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    webView.getHeight()
            );
            view.setLayoutParams(webViewParams);
            webView.addView(view);

            for(int i = 0; i < views.size(); i++) {
                views.get(i).bringToFront();
            }
        }
    }

    public void show() {
        if (adView == null) {
            adView = new AdView(plugin.cordova.getActivity(), placementID, AdSize.BANNER_HEIGHT_50);
            addBannerView(adView);
            adView.loadAd();
        } else if (adView.getVisibility() == View.GONE) {
            adView.loadAd();
            adView.setVisibility(View.VISIBLE);
        } else {
            View view = plugin.webView.getView();
            ViewGroup wvParentView = (ViewGroup) view.getParent();
            if (parentView != wvParentView) {
                parentView.removeAllViews();
                if (parentView.getParent() != null) {
                    ((ViewGroup)parentView.getParent()).removeView(parentView);
                }
                addBannerView(adView);
                adView.loadAd();
            }
        }

        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "Error loading ad with" + adError.getErrorMessage());
                plugin.emit(Events.BANNER_LOAD_FAIL);
            }

            @Override
            public void onAdLoaded(Ad ad) {
//                Log.d(TAG, "Ad loaded");
                plugin.emit(Events.BANNER_LOAD);
            }

            @Override
            public void onAdClicked(Ad ad) {
                plugin.emit(Events.BANNER_CLICK);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                plugin.emit(Events.BANNER_IMPRESSION);
            }
        });
    }

    private void addBannerView(AdView adView) {
        float dip = 50f;
        float px = AdBase.pxFromDp(plugin.webView.getContext(), dip);

        FrameLayout webView = (FrameLayout) plugin.webView.getView().getParent();
        int adPosition = webView.getHeight() - (int)Math.floor(px);

        View view = plugin.webView.getView();

        webView.removeView(view);
        FrameLayout.LayoutParams webViewParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                adPosition
        );
        view.setLayoutParams(webViewParams);

        webView.addView(view);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, adPosition, 0, 0);
        adView.setLayoutParams(params);

        webView.addView(adView);
    }
}
