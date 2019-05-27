class FBANBanner: FBANBase, FBAdViewDelegate {
	var adView: FBAdView!
	var adSize: FBAdSize!
	var position: String!

	init(placementID: String, adSize: FBAdSize, position: String) {
        super.init(placementID: placementID)

        self.adSize = adSize
        self.position = position
        self.prepareBanner()
    }

    deinit {
    	adView = nil
    }

    func prepareBanner() {
        self.adView = FBAdView(placementID: self.placementID, adSize: self.adSize, rootViewController: plugin.viewController)
        let adHeight = self.adView.bounds.size.height
        let size: CGSize = plugin.viewController.bounds.size
        let yOffset: CGFloat = size.height - adHeight
        self.adView?.frame = CGRect(x: 0, y: yOffset, width: size.width, height: adHeight)
        self.adView?.delegate = self
        self.adView?.loadAd()
    }

    func showBanner() {
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
        plugin.emit(eventType: FBANEvents.bannerLoadFail, data: error)
    }
}
