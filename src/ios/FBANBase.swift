class FBANFreeBase: NSObject {
    static var ads = Dictionary<Int, Any>()
    static weak var plugin: FBANPlugin!

    var plugin: AMSPlugin {
        // tba
    }

    init(placementID: String) {
        super.init()

        FBANFreeBase.ads[id] = self
    }

    deinit {
        FBANFreeBase.ads.removeValue(forKey: self.id)
        self.adUnitID = nil
    }

    func fitAds() {
        for (_, ad) in FBANFreeBase.ads {
            // tba
        }
    }
}
