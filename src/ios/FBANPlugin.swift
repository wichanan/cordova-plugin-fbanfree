@objc(FBANPlugin)
class FBANPlugin: CDVPlugin {
    static let testAdID = ""
    
    var readyCallbackId: String!

    override func pluginInitialize() {
        super.pluginInitialize()
        
        FBANBase.plugin = self
    }
    
    @objc(ready:)
    func ready(command: CDVInvokedUrlCommand) {
        readyCallbackId = command.callbackId
        
        self.emit(eventType: FBANEvents.ready, data: [
            "platform": "ios",
            "sdkVersion": FB_AD_SDK_VERSION,
            "isRunningInTestLab": false])
    }

    @objc(banner_show:)
    func banner_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let placementID = opts.value(forKey: "placementID") as? String,
            let position = opts.value(forKey: "position") as? String,
            var banner = FBANBase.ads[id] as? FBANBanner?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if banner == nil {
            let adSize = getAdSize(opts)
            banner = FBANBanner(id: id, placementID: placementID, adSize: adSize, position: position)
        }
        
        banner!.showBanner()
        print("banner callback id " + command.callbackId)
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(native_show:)
    func native_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let placementID = opts.value(forKey: "placementID") as? String,
            var native = FBANBase.ads[id] as? FBANNative?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        
        if native == nil {
            native = FBANNative(id: id, placementID: placementID, adViewType: FBNativeAdViewType.dynamic)
        }
        
        native!.show()
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }

    @objc(interstitial_show:)
    func interstitial_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let placementID = opts.value(forKey: "placementID") as? String,
            var interstitial = FBANBase.ads[id] as? FBANInterstitial?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if interstitial == nil {
            interstitial = FBANInterstitial(id: id, placementID: placementID)
        }
        interstitial!.show()

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }

    @objc(reward_video_show:)
    func reward_video_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let placementID = opts.value(forKey: "placementID") as? String,
            var reward_video = FBANBase.ads[id] as? FBANRewardVideo?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if reward_video == nil {
            reward_video = FBANRewardVideo(id: id, placementID: placementID)
        }
        reward_video!.show()

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }

    func emit(eventType: String, data: Any = false) {
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: ["type": eventType, "data": data])
        result?.setKeepCallbackAs(true)
        self.commandDelegate!.send(result, callbackId: readyCallbackId)
    }

    func getAdSize(_ opts: NSDictionary) -> FBAdSize {
        if let adSizeType = opts.value(forKey: "adSize") as? Int {
            switch adSizeType {
            case 0:
                return kFBAdSizeHeight90Banner
            case 1:
                return kFBAdSizeHeight50Banner
            case 2:
                return kFBAdSizeHeight250Rectangle
            case 3:
                return kFBAdSize320x50
            default:
                break;
            }
        }
        
        return kFBAdSizeHeight50Banner
    }
}
