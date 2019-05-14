class FBANBase: NSObject {
    static var ads = Dictionary<Int, Any>()
    static weak var plugin: FBANPlugin!

    var placementID: String!

    var plugin: FBANPlugin {
        return FBANBase.plugin
    }

    init(placementID: String) {
        super.init()
        
        self.placementID = placementID
        FBANBase.ads[placementID] = self
    }

    deinit {
        FBANBase.ads.removeValue(forKey: self.id)
        self.adUnitID = nil
    }

    //func fitAds() {
        //for (_, ad) in FBANBase.ads {
            // tba
        //}
    //}
}
