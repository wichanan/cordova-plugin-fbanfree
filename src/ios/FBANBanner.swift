class FBANBanner: FBANBase, FBAdViewDelegate {
    var adView: FBAdView!
    var adSize: FBAdSize!
    var position: String!
    
    var view: UIView {
        return self.plugin.viewController.view
    }

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
        let size: CGSize = view.bounds.size
        let yOffset: CGFloat = size.height - 50
        self.adView?.frame = CGRect(x: 0, y: yOffset, width: size.width, height: 50)
        self.adView?.delegate = self
    }

    func showBanner() {
        self.adView?.loadAd()
    }

    func adViewDidLoad(_ adView: FBAdView) {
        if (self.adView != nil && self.adView!.isAdValid) {
            view.addSubview(self.adView!)
            if #available(iOS 11.0, *) {
                positionBannerInSafeArea(adView)

                let background = UIView()
                background.translatesAutoresizingMaskIntoConstraints = false
                background.backgroundColor = .black
                view.addSubview(background)
                view.sendSubview(toBack: background)
                NSLayoutConstraint.activate([
                    background.leadingAnchor.constraint(equalTo: view.leadingAnchor),
                    background.trailingAnchor.constraint(equalTo: view.trailingAnchor),
                    background.bottomAnchor.constraint(equalTo: view.bottomAnchor),
                    background.topAnchor.constraint(equalTo: adView.topAnchor)
                ])
            } else {
                positionBanner(adView)
            }
        }
    }

    @available (iOS 11, *)
    func positionBannerInSafeArea(_ adView: UIView) {
        let guide: UILayoutGuide = view.safeAreaLayoutGuide
        var constraints = [
            adView.centerXAnchor.constraint(equalTo: guide.centerXAnchor),
            self.plugin.webView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            self.plugin.webView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
        ]
        //if position == "top" {
        //    constraints += [
        //        bannerView.topAnchor.constraint(equalTo: guide.topAnchor),
        //        self.plugin.webView.topAnchor.constraint(equalTo: bannerView.bottomAnchor),
        //       self.plugin.webView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        //    ]
        //} else {
        constraints += [
            adView.bottomAnchor.constraint(equalTo: guide.bottomAnchor),
            self.plugin.webView.topAnchor.constraint(equalTo: view.topAnchor),
            self.plugin.webView.bottomAnchor.constraint(equalTo: adView.topAnchor),
        ]
        //}
        NSLayoutConstraint.activate(constraints)
    }

    func positionBanner(_ adView: UIView) {
        view.addConstraint(NSLayoutConstraint(item: adView,
                                              attribute: .centerX,
                                              relatedBy: .equal,
                                              toItem: view,
                                              attribute: .centerX,
                                              multiplier: 1,
                                              constant: 0))
        //if position == "top" {
        //    view.addConstraint(NSLayoutConstraint(item: bannerView,
        //                                          attribute: .top,
        //                                          relatedBy: .equal,
        //                                          toItem: plugin.viewController.topLayoutGuide,
        //                                          attribute: .top,
        //                                          multiplier: 1,
        //                                          constant: 0))
        //} else {
        view.addConstraint(NSLayoutConstraint(item: adView,
                                              attribute: .bottom,
                                              relatedBy: .equal,
                                              toItem: plugin.viewController.bottomLayoutGuide,
                                              attribute: .top,
                                              multiplier: 1,
                                              constant: 0))
        //}
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
