package fban.plugin.ads;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

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
        if (adView != null) {
            Log.d(TAG, "tryin to hide the banner");
            adView.setVisibility(View.GONE);
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
                Log.d(TAG, "Error showing ad with" + adError.getErrorMessage());
                AdBase.plugin.emit(Events.BANNER_LOAD_FAIL);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                AdBase.plugin.emit(Events.BANNER_LOAD);
            }

            @Override
            public void onAdClicked(Ad ad) {
                AdBase.plugin.emit(Events.BANNER_CLICK);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                AdBase.plugin.emit(Events.BANNER_IMPRESSION);
            }
        });
    }

    private void addBannerView(AdView adView) {
        float dip = 50f;
        Resources r = plugin.webView.getView().getResources();
        float px = pxFromDp(plugin.webView.getContext(), dip);

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

    private static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
