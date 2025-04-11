package com.manual.mediation.library.sotadlib.mintegralAdClasses

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.manual.mediation.library.sotadlib.BuildConfig
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.mbridge.msdk.out.BannerAdListener
import com.mbridge.msdk.out.BannerSize
import com.mbridge.msdk.out.MBBannerView
import com.mbridge.msdk.out.MBridgeIds

object MintegralBannerAdManager {
    private val bannerCache = HashMap<String, MBBannerView?>()
    private val adLoadingState = HashMap<String, Boolean>()

    fun requestBannerAd(
        activity: Activity?,
        placementId: String,
        unitId: String,
        adName: String,
        remoteConfig: Boolean = true,
        populateView: Boolean = false,
        bannerContainer: FrameLayout? = null,
        shimmerContainer: View? = null,
        onAdFailed: (() -> Unit)? = null,
        onAdLoaded: (() -> Unit)? = null,
        onAdClicked: (() -> Unit)? = null
    ) {
        if (activity == null) {
            Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : Activity is null; cannot load ad.")
            onAdFailed?.invoke()
            return
        }

        if (populateView) {
            if (!NetworkCheck.isNetworkAvailable(activity) || !remoteConfig) {
                shimmerContainer?.visibility = View.GONE
                Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : No network available")
                onAdFailed?.invoke()
                return
            } /*else {
                shimmerContainer?.visibility = View.VISIBLE
            }*/

            if (adLoadingState[adName] == true && bannerCache[adName] != null) {
                Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : showCachedAd()")
                showCachedAd(adName, bannerContainer, onAdLoaded)
                return
            }
        } else {
            Log.i("SOT_ADS_TAG","Mintegral: BannerAd : canPopulateView")
        }

        adLoadingState[adName] = true

        val bannerView = MBBannerView(activity).apply {
            init(BannerSize(BannerSize.LARGE_TYPE, 320, 50), placementId, unitId)
            setBannerAdListener(object : BannerAdListener {
//                override fun onLoadSuccessed(ids: MBridgeIds?) {
//                    Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : onLoadSuccess()")
//                    bannerCache[adName] = this@apply
//
//                    if (populateView) {
//                        bannerContainer?.removeAllViews()
//                        bannerContainer?.addView(this@apply)
//                    } else {
//                        activity.let {
//                            if (BuildConfig.DEBUG) {
//                                Toast.makeText(activity,"Mintegral: BannerAd : Loaded()\n$adName", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                    onAdLoaded?.invoke()
//                }

                override fun onLoadSuccessed(ids: MBridgeIds?) {
                    Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : onLoadSuccess()")
                    bannerCache[adName] = this@apply

                    if (populateView) {
                        // Remove the view from its current parent if it has one
                        (this@apply.parent as? ViewGroup)?.removeView(this@apply)
                        bannerContainer?.removeAllViews()
                        bannerContainer?.addView(this@apply)
                    } else {
                        activity.let {
                            if (BuildConfig.DEBUG) {
                                Toast.makeText(activity,"Mintegral: BannerAd : Loaded()\n$adName", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    onAdLoaded?.invoke()
                }

                override fun onLogImpression(p0: MBridgeIds?) {
                    Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : onLogImpression()")
                    bannerCache[adName] = null
                    adLoadingState[adName] = false
                }

                override fun onLoadFailed(ids: MBridgeIds?, error: String?) {
                    Log.e("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : onLoadFailed() : $error")
                    bannerCache[adName] = null
                    adLoadingState[adName] = false
                    onAdFailed?.invoke()
                }

                override fun onClick(ids: MBridgeIds?) {
                    Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : onClick()")
                    bannerCache[adName] = null
                    adLoadingState[adName] = false
                    onAdClicked?.invoke()
                }

                override fun onLeaveApp(p0: MBridgeIds?) {
                    Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : onLeaveApp()")
                }

                override fun showFullScreen(p0: MBridgeIds?) {
                    Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : showFullScreen()")
                }

                override fun closeFullScreen(p0: MBridgeIds?) {
                    Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : closeFullScreen()")
                }

                override fun onCloseBanner(ids: MBridgeIds?) {
                    Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : onCloseBanner()")
                    bannerCache[adName] = null
                    adLoadingState[adName] = false
                }
            })
        }

        bannerView.load()
    }

//    private fun showCachedAd(adName: String, bannerContainer: FrameLayout?, onAdLoaded: (() -> Unit)? = null) {
//        bannerCache[adName]?.let { cachedBanner ->
//            onAdLoaded?.invoke()
//            bannerContainer?.removeAllViews()
//            bannerContainer?.addView(cachedBanner)
//            Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : Showing cached banner")
//        } ?: Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : No cached banner to show")
//    }

    private fun showCachedAd(adName: String, bannerContainer: FrameLayout?, onAdLoaded: (() -> Unit)? = null) {
        bannerCache[adName]?.let { cachedBanner ->
            // Remove the view from its current parent if it has one
            (cachedBanner.parent as? ViewGroup)?.removeView(cachedBanner)

            bannerContainer?.removeAllViews()
            bannerContainer?.addView(cachedBanner)
            onAdLoaded?.invoke()
            Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : Showing cached banner")
        } ?: Log.i("SOT_ADS_TAG", "Mintegral: BannerAd : $adName : No cached banner to show")
    }
}