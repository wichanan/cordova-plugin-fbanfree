class FBANBanner: FBANBase, FBAdViewDelegate {
    var adView: FBAdView!
    var adSize: FBAdSize!
    var position: String!

    init(id: Int, placementID: String, adSize: FBAdSize, position: String) {
        super.init(id: id, placementID: placementID)
        
        self.adSize = adSize
        self.position = position
        self.prepareBanner()
    }

    deinit {
        adView = nil
    }

    func prepareBanner() {
        self.adView = FBAdView(placementID: self.placementID, adSize: self.adSize, rootViewController: plugin.viewController)
        let size: CGSize = plugin.viewController.view.bounds.size
        let yOffset: CGFloat = size.height - 50
        self.adView?.frame = CGRect(x: 0, y: yOffset, width: size.width, height: 50)
        self.adView?.delegate = self
    }

    func showBanner() {
        self.adView?.loadAd()
    }
    
    func adViewDidLoad(_ adView: FBAdView) {
        if (self.adView != nil && self.adView!.isAdValid) {
            plugin.viewController.view.addSubview(self.adView!)
        }
    }

    func adViewDidClick(_ adView: FBAdView) {
        plugin.emit(eventType: FBANEvents.bannerClick)
    }

    func adViewWillLogImpression(_ adView: FBAdView) {
        plugin.emit(eventType: FBANEvents.bannerImpression)
    }
    
    func adView(_ adView: FBAdView, didFailWithError error: Error) {
        print("Banner failed with error" + error.localizedDescription)
        plugin.emit(eventType: FBANEvents.bannerLoadFail, data: error)
    }
}
