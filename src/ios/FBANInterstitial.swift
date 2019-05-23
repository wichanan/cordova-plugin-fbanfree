class FBANInterstitial: FBANBase, FBInterstitialAdDelegate {
	var interstitialAd: FBInterstitialAd!

    deinit {
    	interstitialAd = nil
    }

    func show() {
        self.interstitialAd = FBInterstitialAd(placementID: placementID)
        self.interstitialAd!.delegate = self
        self.interstitialAd!.load()
    }

    func interstitialAdDidLoad(_ interstitialAd: FBInterstitialAd) {
        print("Inter ad did load")
        if (interstitialAd.isAdValid) {
            interstitialAd.show(fromRootViewController: plugin.viewController)
        }
    }

    func interstitialAdDidClick(_ interstitialAd: FBInterstitialAd) {
        print("Interstitial ad was clicked")
    }
    
    func interstitialAdWillLogImpression(_ interstitialAd: FBInterstitialAd) {
        print("Interstitial ad logged impression")
    }
    
    func interstitialAdDidClose(_ interstitialAd: FBInterstitialAd) {
        print("Interstitial ad did close")
    }
    
    func interstitialAdWillClose(_ interstitialAd: FBInterstitialAd) {
        print("The user clicked on the close button, the ad is just about to close")
    }
    
    func interstitialAd(_ interstitialAd: FBInterstitialAd, didFailWithError error: Error) {
        print("Interstitial ad error with: \(error)")
    }
}
