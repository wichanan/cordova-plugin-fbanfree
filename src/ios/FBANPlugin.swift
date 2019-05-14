@objc(FBANPlugin)
class FBANPlugin: CDVPlugin {
    @static let testAdID = ""

    override func pluginInitialize() {
        super.pluginInitialize()
    }

    @objc(banner_show:)
    func banner_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let placementID = opts.value(forKey: "placementID") as? String,
            let position = opts.value(forKey: "position") as? String,
            var banner = FBANBase.ads[id] as? FBANBBanner?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if banner == nil {
            let adSize = getAdSize(opts)
            banner = AMSBanner(id: id, adUnitID: adUnitID, adSize: adSize, position: position)
        }
        banner!.show(request: createGADRequest(opts))

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }

    func getAdSize(_ opts: NSDictionary) -> GADAdSize {
        if let adSizeType = opts.value(forKey: "adSize") as? Int {
            switch adSizeType {
            case 0:
                return kGADAdSizeBanner
            case 1:
                return kGADAdSizeLargeBanner
            case 2:
                return kGADAdSizeMediumRectangle
            case 3:
                return kGADAdSizeFullBanner
            case 4:
                return kGADAdSizeLeaderboard
            default: break
            }
        }
        guard let adSizeDict = opts.value(forKey: "size") as? NSDictionary,
            let width = adSizeDict.value(forKey: "width") as? Int,
            let height = adSizeDict.value(forKey: "height") as? Int
            else {
                if UIDevice.current.orientation.isPortrait {
                    return kGADAdSizeSmartBannerPortrait
                } else {
                    return kGADAdSizeSmartBannerLandscape
                }
        }
        return GADAdSizeFromCGSize(CGSize(width: width, height: height))
    }
}
