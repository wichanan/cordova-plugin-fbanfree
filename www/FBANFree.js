// import exec from 'cordova/exec'
// import { exec, getAdUnitId } from './driver'
var exec = require('cordova/exec')


/**
 * @ignore
 */
function execute(method, args) {
    console.log('i am in execution', method, args)
    return new Promise((resolve, reject) => {
        exec(resolve, reject, 'FBANFree', method, [args]);
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

exports.showBanner = function () {
    return execute(
        'banner_show', {
        placementID: 'IMG_16_9_APP_INSTALL#1345786662228899_1352655241542041',
        adSize: 1,
        position: 'bottom',
        id: getAdUnitId('IMG_16_9_APP_INSTALL#1345786662228899_1352655241542041')
    });
}

exports.showInterstitial = function () {
    return exec('interstitial_show')
}

exports.showRewardedVideo = function() {
    return exec('reward_video_show')
}

exports.showNative = function() {
    return exec('native_show')
}
