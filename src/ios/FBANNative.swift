class FBANNative: FBANBase, FBNativeAdDelegate{
    var nativeAd: FBNativeAd!
    var adViewType: FBNativeAdViewType!
    var position: String!

    var view: UIView {
        return self.plugin.viewController.view
    }

    init(id: Int, placementID: String, adViewType: FBNativeAdViewType, position: String) {
        super.init(id: id, placementID: placementID)

        self.adViewType = adViewType
        self.position = position
    }

    deinit {
        nativeAd = nil
    }

    func show() {
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
            
            plugin.viewController.view.addSubview(adView)
            let adSize = adView.bounds.size
            
            let size: CGSize = plugin.viewController.view.bounds.size
            let xOffset: CGFloat = (size.width / 2) - (adSize.width / 2)
            adView.frame = CGRect(x: xOffset, y: 0, width: size.width, height: adSize.height)
        }
    }

    func nativeAdDidClick(_ nativeAd: FBNativeAd) {
        plugin.emit(eventType: FBANEvents.nativeClick)
    }
    
    func nativeAdDidFinishHandlingClick(_ nativeAd: FBNativeAd) {
        plugin.emit(eventType: FBANEvents.nativeClickFinish)
    }
    
    func nativeAdWillLogImpression(_ nativeAd: FBNativeAd) {
        plugin.emit(eventType: FBANEvents.nativeImpression)
    }
    
    func nativeAd(_ nativeAd: FBNativeAd, didFailWithError error: Error) {
        plugin.emit(eventType: FBANEvents.nativeLoadFail, data: error)
    }
}
