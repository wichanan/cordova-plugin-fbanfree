package fban.plugin;

import fban.plugin.ads.AdBase;

import org.json.JSONArray;
import org.json.JSONObject;

public class Action {
    private JSONObject opts;

    Action(JSONArray args) {
        this.opts = args.optJSONObject(0);
    }

    public int optId() {
        return opts.optInt("id");
    }

    public String optPosition() {
        return opts.optString("position");
    }

    public AdBase getAd() {
        return AdBase.getAd(optId());
    }

    public String getPlacementID() {
        return this.opts.optString("placementID");
    }
}
