// import exec from 'cordova/exec'
// import { exec, getAdUnitId } from './driver'
var exec = require('cordova/exec')


/**
 * @ignore
 */
function execute(method, args) {
    return new Promise((resolve, reject) => {
        exec(resolve, reject, 'FBANFree', method, [args])
    })
}

var nextId = 100
var adUnits = {}

function getAdUnitId(adUnitId) {
    if (adUnits[adUnitId]) {
      return adUnits[adUnitId]
    }
    adUnits[adUnitId] = nextId
    nextId += 1
    return adUnits[adUnitId]
}

exports.showBanner = function() {
    // exec(success, fail, 'FBANFree', 'banner_show', [{
    //     placementID: 'IMG_16_9_APP_INSTALL#1345786662228899_1352655241542041',
    //     adSize: 1,
    //     position: 'bottom',
    //     id: getAdUnitId('IMG_16_9_APP_INSTALL#1345786662228899_1352655241542041')
    // }]);
    return execute(
        'banner_show', {
        placementID: 'IMG_16_9_APP_INSTALL#1345786662228899_1352655241542041',
        adSize: 1,
        position: 'bottom',
        id: getAdUnitId('IMG_16_9_APP_INSTALL#1345786662228899_1352655241542041')
    });
}

exports.showInterstitial = function() {
    return execute('interstitial_show', {
        placementID: 'VID_HD_16_9_46S_APP_INSTALL#1345786662228899_1352651671542398',
        id: getAdUnitId('VID_HD_16_9_46S_APP_INSTALL#1345786662228899_1352651671542398')
    });
}

exports.showRewardedVideo = function() {
    return execute('reward_video_show', {
        placementID: 'VID_HD_16_9_46S_APP_INSTALL#1345786662228899_1352651671542398',
        id: getAdUnitId('VID_HD_16_9_46S_APP_INSTALL#1345786662228899_1352651671542398')
    });
}

exports.showNative = function() {
    return execute('native_show', {
        placementID: 'VID_HD_16_9_46S_APP_INSTALL#1345786662228899_1352651668209065',
        id: getAdUnitId('VID_HD_16_9_46S_APP_INSTALL#1345786662228899_1352651668209065')
    });
}

exports.showNativeBanner = function() {
    return execute('native_banner_show', {
        placementID: 'IMG_16_9_APP_INSTALL#1345786662228899_1352651668209065',
        id: getAdUnitId: 'IMG_16_9_APP_INSTALL#1345786662228899_1352651668209065'
    });
}
