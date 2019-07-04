package fban.plugin;

import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import fban.plugin.FBANFree;

public abstract class AdBase {
    protected static FBANFree plugin;

    final int id;
    String placementID;

    private static SparseArray<AdBase> ads = new SparseArray<AdBase>();


    AdBase(int id, String placementID) {
        this.id = id;
        this.placementID = placementID;

        ads.put(id, this);
    }

    public static void initialize(FBANFree plugin) {
        AdBase.plugin = plugin;
    }

    public static AdBase getAd(Integer id) {
        return ads.get(id);
    }

    JSONObject buildErrorPayload(int errorCode) {
        JSONObject data = new JSONObject();
        try {
            data.put("errorCode", errorCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void destroy() {
        ads.remove(id);
    }

    abstract String getLoadedEvent();
    abstract String getFailedToLoadEvent();
    abstract String getOpenedEvent();
    abstract String getClosedEvent();
    abstract String getLeftApplicationEvent();
    String getImpressionEvent() { return null; }
    String getClickedEvent() { return null; }
}