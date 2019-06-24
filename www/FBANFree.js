// import exec from 'cordova/exec'
// import { exec, getAdUnitId } from './driver'
var exec = require('cordova/exec')

/**
 * @ignore
 */
function buildEvents(adType, eventKeys) {
  return eventKeys.reduce((acc, eventKey) => {
    acc[eventKey] = `admob.${adType}.events.${eventKey}`
    return acc
  }, {})
}

const bannerEvents = buildEvents('banner', [
    'click',
    'impression',
    'load_fail',
]);

const interEvents = buildEvents('interstitial', [
    'click',
    'close',
    'impression',
    'load',
    'load_fail',
    'will_close'
]);

const rewardVideoEvents = buildEvents('interstitial', [
    'click',
    'close',
    'complete',
    'impression',
    'load',
    'load_fail',
    'server_success',
    'server_fail',
    'will_close'
]);

const nativeEvents = buildEvents('interstitial', [
    'click',
    'click_finish',
    'impression',
    'load_fail',
]);

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

exports.showBanner = function() {
    return execute('banner_show', bannerConfig('1345786662228899_1352655241542041'));
}

exports.showInterstitial = function() {
    return execute('interstitial_show', adConfig('1345786662228899_1352651671542398'));
}

exports.showRewardedVideo = function() {
    return execute('reward_video_show', adConfig(''));
}

exports.showNative = function() {
    return execute('native_show', adConfig('1345786662228899_1352651668209065'));
}

exports.showNativeBanner = function() {
    return execute('native_banner_show', bannerConfig('1345786662228899_1352651668209065'));
}
