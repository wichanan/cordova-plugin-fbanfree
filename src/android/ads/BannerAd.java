package fban.plugin.ads;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import fban.plugin.Action;

public class BannerAd extends AdBase {
    private static final String TAG = "FBAN-Free()::BannerAD";
    private AdView adView;
    private ViewGroup parentView;
    private int gravity;

    BannerAd(int id, String placementID, int gravity) {
        super(id, placementID);
        this.gravity = gravity;

        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "Error showing ad with" + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Ad loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Ad clicked" );
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "ad impression");
            }
        });
    }

    public static boolean executeShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                BannerAd bannerAd = (BannerAd) action.getAd();
                if (bannerAd == null) {
                    bannerAd = new BannerAd(
                            action.optId(),
                            action.getPlacementID(),
                            Gravity.BOTTOM
                    );
                }
                bannerAd.show();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
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
    }

    private void addBannerView(AdView adView) {
        View view = plugin.webView.getView();
        ViewGroup wvParentView = (ViewGroup) view.getParent();
        if (parentView == null) {
            parentView = new LinearLayout(plugin.webView.getContext());
        }

        if (wvParentView != null && wvParentView != parentView) {
            wvParentView.removeView(view);
            LinearLayout content = (LinearLayout) parentView;
            content.setOrientation(LinearLayout.VERTICAL);
            parentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0F));
            parentView.addView(view);
            wvParentView.addView(parentView);
        }

        parentView.addView(adView);
        parentView.bringToFront();
        parentView.requestLayout();
        parentView.requestFocus();
    }
}
