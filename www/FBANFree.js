// import exec from 'cordova/exec'
import { exec } from './driver'

export function showBanner() {
    return exec('showBanner', [])
}

export function prepareBanner() {
    return exec('prepareBanner', [])
}

