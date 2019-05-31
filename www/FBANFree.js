// import exec from 'cordova/exec'
import { exec, getAdUnitId } from './driver'

export function showBanner() {
    return exec('banner_show', {
        placementID: 'IMG_16_9_APP_INSTALL#1345786662228899_1352655241542041',
        adSize: 1,
        position: 'bottom',
        id: getAdUnitId('IMG_16_9_APP_INSTALL#1345786662228899_1352655241542041')
    })
}

export function showInterstitial() {
    return exec('interstitial_show')
}

export function showRewardedVideo() {
    return exec('reward_video_show')
}

export function showNative() {
    return exec('native_show')
}
