// import exec from 'cordova/exec'
// import { exec, getAdUnitId } from './driver'
var exec = require('cordova/exec')

/**
 * @ignore
 */
function execute(method, args) {
    return new Promise((resolve, reject) => {
        exec(resolve, reject, 'FBANFree', method, [args])
    });
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

function bannerConfig(placementID) {
    return {
        placementID: placementID,
        adSize: 1,
        position: 'bottom',
        id: getAdUnitId(placementID)
    }
}

function adConfig(placementID) {
    return {
        placementID: placementID,
        id: getAdUnitId(placementID)
    }
}

function nativeConfig(data) {
    return {
        position: data.position,
        placementID: data.placementID,
        id: getAdUnitId(data.placementID)
    }
}

exports.ready = function() {
    return execute('ready', {});
}

exports.showBanner = function(placementID) {
    return execute('banner_show', bannerConfig(placementID));
}

exports.hideBanner = function(placementID) {
    return execute('banner_hide', {id: getAdUnitId(placementID)});
}

exports.showInterstitial = function(placementID) {
    return execute('interstitial_show', adConfig(placementID));
}

exports.showRewardedVideo = function(placementID) {
    return execute('reward_video_show', adConfig(placementID));
}

exports.showNative = function(data) {

    return execute('native_show', nativeConfig(data));
}

exports.hideNative = function(placementID) {
    return execute('native_hide', {id: getAdUnitId(placementID)})
}

exports.hideAllNative = function() {
    return execute('native_hide_all', {})
}

exports.showNativeBanner = function(placementID) {
    return execute('native_banner_show', adConfig(placementID));
}
