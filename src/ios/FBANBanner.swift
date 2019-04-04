class FBANBanner: FBAdViewDelegate {
	var adView: FBAdView!
	var adSize: FBAdSize!
	var position: String!

	init(placementID: String, adSize: FBAdSize, position: String) {
        super.init(placementID: placementID)

        self.adSize = adSize
        self.position = position
    }

    deinit {
    	adView = nil
    }

    func show() {
        self.adView = FBAdView(placementID: placementID, adSize: self.adSize, rootViewController: self)
        let size: CGSize = self.view.bounds.size
        let yOffset: CGFloat = size.height - 50
        self.adView?.frame = CGRect(x: 0, y: yOffset, width: size.width, height: 50)
        self.adView?.delegate = self
        self.adView?.loadAd()
    }

    func showBanner() {
        if (self.adView != nil && self.adView!.isAdValid) {
            self.view.addSubview(self.adView!)
        }
    }

    func adViewDidClick(_ adView: FBAdView) {
        print("Banner ad: click the adview")
    }
    
    func adViewDidFinishHandlingClick(_ adView: FBAdView) {
        print("Banner ad did finish click handling")
    }
    
    func adViewWillLogImpression(_ adView: FBAdView) {
        print("Banner ad impression is being captured")
    }
    
    func adView(_ adView: FBAdView, didFailWithError error: Error) {
        print("Banner ad failed with error \(error)")
    }
}
