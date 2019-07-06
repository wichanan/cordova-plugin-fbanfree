## Cordova Facebook Audience Network Free

This Plugin uses the lastest version of `Facebook Audience Network` SDKs for Android and iOS
to monetize the Audience Network ads on your Hybrid application


```
Free
Definition of free in English by oxforddictionaries:
1. Able to act or be done as one wishes; not under the control of another.
2. Not or no longer confined or imprisoned.
...
5. Given or available without charge.
and so on
```
This plugin is inspired by [AdMob Plus](https://github.com/admob-plus/admob-plus)

## Installation
```
cordova plugin add cordova-plugin-fbanfree
```

## Important(iOS)
This Plugin uses `cordova-plugin-cocoapod-support` to generate the Podfile to match the min ios version in pod file
you must check at `yourprojectDir/config.xml` before using this plugin

add this
```
<platform name="ios">
    ...
    <preference name="pods_ios_min_version" value="11.0"/>
    ...
</platform>
```

## Prerequisite(Android)

The Audience network Android SDK requires the `android.support:recyclerview` suppoort library. Before the installation 
please check the version of your project `recyclerview` and add another `--variable` to the `cordova plugin add`

The default uses `27+`


```
cordova plugin add cordova-plugin-fbanfree --variable ANDROID_SUPPORT_RECYCLERVIEW_VERSION=20+
```




## Usage

To show the banner use:

```
fbanfree.showBanner('YOUR_PLACEMENT_ID)
.then(res => {
    console.log('banner show success', res);
}).catch(err => {
    console.log('err showing banner', err);
});
```

To show the Native ad(right now can only show at the middle of the screen, I'll add more option later):

```
fbanfree.showNative('YOUR_PLACEMENT_ID')
.then(res => {
    console.log('native show success:', res);
})
.catch(err => {
    console.log('error showing native:', err);
});
```


To show the Interstitial ad:
```
fbanfree.showInterstitial('YOUR_PLACEMENT_ID')
.then(res => {
   console.log('interstitial show success:', res);
})
.catch(err => {
   console.log('error showing interstitial:', err);
});
```

To show the Rewared Video:

```
fbanfree.showRewardedVideo('YOUR_PLACEMENT_ID')
.then(res => {
   console.log('rewarded video show success:', res);
})
.catch(err => {
   console.log('error showing rewarded video:', err);
});
```

[License](LICENSE)

