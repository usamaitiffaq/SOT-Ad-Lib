package com.manual.mediation.library.sotadlib.adMobAdClasses

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.manual.mediation.library.sotadlib.BuildConfig
import com.manual.mediation.library.sotadlib.utils.NetworkCheck

class AdMobBannerAdSplash(
    activity: Activity? = null,
    private val placementID: String,
    private val bannerContainer: FrameLayout,
    private val shimmerContainer: View,
    private val onAdFailed: (() -> Unit)? = null,
    private val onAdLoaded: (() -> Unit)? = null,
    private val onAdClicked: (() -> Unit)? = null) {
    private var adView: AdView? = null
    private var currentActivity: Activity? = activity
    var isBannerLoaded = false

    init {
        currentActivity?.let {
            if (NetworkCheck.isNetworkAvailable(it)) {
                loadBannerAd(onAdFailed, onAdLoaded, onAdClicked)
            } else {
                Log.i("SOT_ADS_TAG", "AdMob: BannerAd : No Network Available")
            }
        }
    }

    private fun loadBannerAd(onAdFailed: (() -> Unit)? = null, onAdLoaded: (() -> Unit)? = null, onAdClicked: (() -> Unit)? = null) {
        if (isBannerLoaded) {
            return
        }

        adView = AdView(currentActivity!!.baseContext).apply {
            adUnitId = placementID
            setAdSize(AdSize.BANNER)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.i("SOT_ADS_TAG", "AdMob: BannerAd : onAdLoaded()")
                    shimmerContainer.visibility = View.INVISIBLE
                    bannerContainer.addView(this@apply)
                    isBannerLoaded = true
                    onAdLoaded?.invoke()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.i("SOT_ADS_TAG", "AdMob: BannerAd : onAdFailedToLoad: $error")
                    onAdFailed?.invoke()
                    currentActivity?.let {
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(it, "AdMob Banner Ad Failed to Load", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onAdClicked() {
                    Log.i("SOT_ADS_TAG", "AdMob: BannerAd : onAdClicked()")
                    onAdClicked?.invoke()
                }
            }
        }

        adView?.loadAd(AdRequest.Builder().build())
    }

    fun removeAd() {
        bannerContainer.removeAllViews()
        adView = null
        Log.i("SOT_ADS_TAG", "AdMob: BannerAd : removeAd()")
    }
}