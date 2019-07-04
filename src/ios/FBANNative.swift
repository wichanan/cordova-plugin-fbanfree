class FBANNative: FBANBase, FBNativeAdDelegate{
    var nativeAd: FBNativeAd!
    var nativeAdView: FBNativeAdView!
    var adViewType: FBNativeAdViewType!
    var position: String!

    var view: UIView {
        return self.plugin.viewController.view
    }

    init(id: Int, placementID: String, adViewType: FBNativeAdViewType) {
        super.init(id: id, placementID: placementID)

        self.adViewType = adViewType
    }

    deinit {
        self.nativeAd = nil
    }

    func show() {
        self.nativeAd = FBNativeAd(placementID: placementID)
        self.nativeAd?.delegate = self
        self.nativeAd?.loadAd(withMediaCachePolicy: FBNativeAdsCachePolicy.all)
    }
    
    func hide() {
        if (self.nativeAdView?.superview != nil) {
            self.nativeAd.delegate = nil
            self.nativeAdView.rootViewController = nil
            self.nativeAdView.removeFromSuperview()
        }
    }

    func nativeAdDidLoad(_ nativeAd: FBNativeAd) {
        showNativeAd()
    }

    func showNativeAd() {
        if (self.nativeAd != nil && self.nativeAd!.isAdValid) {
            self.nativeAdView = FBNativeAdView(nativeAd: self.nativeAd!, with: self.adViewType)
            
            plugin.viewController.view.addSubview(self.nativeAdView)
            
            let size: CGSize = plugin.viewController.view.bounds.size
            let yOffset: CGFloat = (size.height / 2) - 210
            self.nativeAdView.frame = CGRect(x: 0, y: yOffset, width: size.width, height: 420)
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
        print("Error loadin native ad with: " + error.localizedDescription)
        plugin.emit(eventType: FBANEvents.nativeLoadFail, data: error)
    }
}
