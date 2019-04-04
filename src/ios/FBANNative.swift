class FBANNative: FBNativeAdDelegate{
    var nativeAd: FBNativeAd!
    var adViewType: FBNativeAdViewType!
    var position: String!

    var view: UIView {
        return self.plugin.viewController.view
    }

    init(placementID: String, adViewType: FBNativeAdViewType, position: String) {
        super.init(placementID: placementID)

        self.adViewType = adViewType
        self.position = position
    }

    deinit {
        nativeAd = nil
    }

    func show(request: GADRequest) {
        let nativeAd = FBNativeAd(placementID: placementID)
        nativeAd.delegate = self
        nativeAd.loadAd()
    }

    func nativeAdDidLoad(_ nativeAd: FBNativeAd) {
        self.nativeAd = nativeAd
        showNativeAd()
    }

    func showNativeAd() {
        if (self.nativeAd != nil && self.nativeAd!.isAdValid) {
            let adView = FBNativeAdView(nativeAd: self.nativeAd!, with: self.adViewType)
            
            self.view.addSubview(adView)
            
            let size: CGSize = self.view.bounds.size
            let xOffset: CGFloat = size.width / 2 - 160
            let yOffset: CGFloat = (size.height > size.width) ? 100 : 20
            adView.frame = CGRect(x: xOffset, y: yOffset, width: 320, height: 300)
        }
    }

    func nativeAdDidClick(_ nativeAd: FBNativeAd) {
        print("Native ad clicked")
    }
    
    func nativeAdDidFinishHandlingClick(_ nativeAd: FBNativeAd) {
        print("Native ad finished click handling")
    }
    
    func nativeAdWillLogImpression(_ nativeAd: FBNativeAd) {
        print("Native ad impression is being captured.")
    }
    
    func nativeAd(_ nativeAd: FBNativeAd, didFailWithError error: Error) {
        print("Error showing Native ad \(error)")
    }
}
