// import exec from 'cordova/exec'
import { exec } from './driver'

export function showBanner() {
    return exec('banner_show', [])
}

export function showInterstitial() {
    return exec('interstitial_show')
}

export function showRewardedVideo() {
    return exec('reward_video_show')
}
