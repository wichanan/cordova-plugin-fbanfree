class FBANInterstitial: FBANBase, FBInterstitialAdDelegate {
    var interstitialAd: FBInterstitialAd!

    deinit {
        interstitialAd = nil
    }

    func show() {
        self.interstitialAd = FBInterstitialAd(placementID: self.placementID)
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
        plugin.emit(eventType: FBANEvents.interstitialClick)
    }
    
    func interstitialAdWillLogImpression(_ interstitialAd: FBInterstitialAd) {
        plugin.emit(eventType: FBANEvents.interstitialImpression)
    }
    
    func interstitialAdDidClose(_ interstitialAd: FBInterstitialAd) {
        plugin.emit(eventType: FBANEvents.interstitialClose)
    }
    
    func interstitialAdWillClose(_ interstitialAd: FBInterstitialAd) {
        plugin.emit(eventType: FBANEvents.interstitialWillClose)
    }
    
    func interstitialAd(_ interstitialAd: FBInterstitialAd, didFailWithError error: Error) {
        print("Error showing interstitial ad with: " + error.localizedDescription)
        plugin.emit(eventType: FBANEvents.interstitialLoadFail, data: error)
    }
}
