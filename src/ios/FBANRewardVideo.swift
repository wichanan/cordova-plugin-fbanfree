class FBANInterstitial: FBRewardedVideoAdDelegate {
	var rewardedVideoAd: FBRewardedVideoAd!

    deinit {
    	rewardedVideoAd = nil
    }

    func show() {
        self.rewardedVideoAd = FBRewardedVideoAd(placementID: placementID)
        self.rewardedVideoAd!.delegate = self
        self.rewardedVideoAd!.load()
    }

    func rewardedVideoAdDidLoad(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("reward videio ad did load")
        if (rewardedVideoAd.isAdValid) {
            rewardedVideoAd.show(fromRootViewController: self)
        }
    }

    func rewardedVideoAdDidClick(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("Video ad clicked")
    }
    
    func rewardedVideoAdDidClose(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("Rewarded Video ad closed - this can be triggered by closing the application, or closing the video end card")
    }
    
    func rewardedVideoAdVideoComplete(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("Rewarded Video ad video complete - this is called after a full video view, before the ad end card is shown. You can use this event to initialize your reward")
    }
    
    func rewardedVideoAdWillClose(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("The user clicked on the close button, the ad is just about to close")
    }
    
    func rewardedVideoAdWillLogImpression(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("Rewared video logged impression")
    }
    
    func rewardedVideoAd(_ rewardedVideoAd: FBRewardedVideoAd, didFailWithError error: Error) {
        print("Rewarded video ad failed to load with error: \(error)")
    }
}
