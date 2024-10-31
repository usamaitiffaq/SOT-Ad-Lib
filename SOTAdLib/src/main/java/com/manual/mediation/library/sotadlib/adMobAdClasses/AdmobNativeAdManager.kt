package com.manual.mediation.library.sotadlib.adMobAdClasses

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Guideline
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.manual.mediation.library.sotadlib.BuildConfig
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.utils.NetworkCheck

object AdmobNativeAdManager {
    private val nativeAdCache = HashMap<String, NativeAd?>()
    private val adLoadingState = HashMap<String, Boolean>()

    fun requestAd(
        mContext: Activity?,
        adId: String,
        adName: String = "",
        isMedia: Boolean,
        isMediumAd: Boolean = false,
        populateView: Boolean = false,
        adContainer: CardView? = null,
        onAdFailed: (() -> Unit)? = null,
        onAdLoaded: (() -> Unit)? = null
    ) {

        if (!NetworkCheck.isNetworkAvailable(mContext)) {
            Log.e("SOT_ADS_TAG", "Network is not available, unable to load ad.")
            onAdFailed?.invoke()
            return
        }

        if (adLoadingState[adName] == true && nativeAdCache[adName] != null) {
            Log.i("SOT_ADS_TAG", "Admob: Native : $adName : showCachedAd()")
            showCachedAd(adName, isMedia, adContainer, isMediumAd)
            return
        }

        adLoadingState[adName] = true

        val adView: NativeAdView = if (isMedia) {
            if (isMediumAd) {
                mContext?.layoutInflater?.inflate(
                    R.layout.admob_native_mediaview_large,
                    null
                ) as NativeAdView
            } else {
                mContext?.layoutInflater?.inflate(
                    R.layout.admob_native_mediaview_medium,
                    null
                ) as NativeAdView
            }
        } else {
            if (isMediumAd) {
                mContext?.layoutInflater?.inflate(
                    R.layout.admob_native_simple_large,
                    null
                ) as NativeAdView
            } else {
                mContext?.layoutInflater?.inflate(
                    R.layout.admob_native_simple_small,
                    null
                ) as NativeAdView
            }
        }

        if (NetworkCheck.isNetworkAvailable(mContext)) {
            val adLoader = AdLoader.Builder(mContext, adId)
                .forNativeAd { nativeAd ->
                    nativeAdCache[adName] = nativeAd
                    adLoadingState[adName] = true
                    if (populateView) {
                        adContainer.let {
                            if (isMedia) {
                                Log.i("SOT_ADS_TAG", "Admob: Native : $adName : populateWithMediaViewAdmob()")
                                populateWithMediaViewAdmob(isMediumAd, nativeAd, adView)
                            } else {
                                Log.i("SOT_ADS_TAG", "Admob: Native : $adName : populateSimpleNativeAdmob()")
                                populateSimpleNativeAdmob(isMediumAd, nativeAd, adView)
                            }
                            adContainer?.removeAllViews()
                            adContainer?.addView(adView)
                        }
                    } else {
                        mContext.let {
                            if (BuildConfig.DEBUG) {
                                Toast.makeText(mContext,"Admob: Native : Loaded()\n$adName", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    onAdLoaded?.invoke()
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: LoadAdError) {
                        nativeAdCache[adName] = null
                        adLoadingState[adName] = false
                        onAdFailed?.invoke()
                        mContext.let {
                            if (BuildConfig.DEBUG) {
                                Toast.makeText(mContext,"Admob: Native : onAdFailedToLoad() $adName \n$errorCode", Toast.LENGTH_SHORT).show()
                            }
                        }
                        Log.i("SOT_ADS_TAG", "Admob: Native : $adName : onAdFailedToLoad()\n$errorCode")
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Log.i("SOT_ADS_TAG", "Admob: Native : $adName : onAdLoaded()")
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        Log.i("SOT_ADS_TAG", "Admob: Native : $adName : onAdClicked()")
                        nativeAdCache[adName] = null
                        adLoadingState[adName] = false
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        Log.i("SOT_ADS_TAG", "Admob: Native : $adName : onAdImpression()")
                        nativeAdCache[adName] = null
                        adLoadingState[adName] = false
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        Log.i("SOT_ADS_TAG", "Admob: Native : $adName : onAdOpened()")
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        Log.i("SOT_ADS_TAG", "Admob: Native : $adName : onAdClosed()")
                    }
                })
                .build()

            adLoader.loadAd(AdRequest.Builder().build())
        } else {
            onAdFailed?.invoke()
        }
    }

    fun showCachedAd(
        adName: String,
        isMedia: Boolean,
        adContainer: CardView?,
        isMediumAd: Boolean
    ) {
        val cachedAd = nativeAdCache[adName]
        if (cachedAd != null) {
            val adView: NativeAdView = if (isMedia) {
                if (isMediumAd) {
                    LayoutInflater.from(adContainer?.context)
                        .inflate(R.layout.admob_native_mediaview_large, null) as NativeAdView
                } else {
                    LayoutInflater.from(adContainer?.context)
                        .inflate(R.layout.admob_native_mediaview_medium, null) as NativeAdView
                }
            } else {
                if (isMediumAd) {
                    LayoutInflater.from(adContainer?.context)
                        .inflate(R.layout.admob_native_simple_large, null) as NativeAdView
                } else {
                    LayoutInflater.from(adContainer?.context)
                        .inflate(R.layout.admob_native_simple_small, null) as NativeAdView
                }
            }
            if (isMedia) {
                Log.i("SOT_ADS_TAG", "Admob: Native : $adName : populateWithMediaViewAdmob()")
                populateWithMediaViewAdmob(isMediumAd, cachedAd, adView)
            } else {
                Log.i("SOT_ADS_TAG", "Admob: Native : $adName : populateSimpleNativeAdmob()")
                populateSimpleNativeAdmob(isMediumAd, cachedAd, adView)
            }
            adContainer?.removeAllViews()
            adContainer?.addView(adView)
        }
    }

    private fun populateWithMediaViewAdmob(
        isMediumAd: Boolean,
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        adView.headlineView = adView.findViewById(R.id.adHeadline)
        adView.bodyView = adView.findViewById(R.id.adBody)
        adView.callToActionView = adView.findViewById(R.id.adCallToAction)
        adView.iconView = adView.findViewById(R.id.adAppIcon)
        adView.mediaView = adView.findViewById<View>(R.id.adMedia) as MediaView
        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.findViewById<View>(R.id.adMedia) as MediaView).setOnHierarchyChangeListener(object :
            ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View, child: View) {
                if (child is ImageView) {
                    child.scaleType = ImageView.ScaleType.FIT_XY
                }
            }

            override fun onChildViewRemoved(view: View, view1: View) {}
        })

        adView.mediaView!!.mediaContent = nativeAd.mediaContent!!

        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            if (isMediumAd) {
                adView.findViewById<Guideline>(R.id.glNativeAdmobMedium1).setGuidelinePercent(0f)
                adView.findViewById<CardView>(R.id.adIconCard).visibility = View.GONE
            } else {
                adView.findViewById<CardView>(R.id.adIconCard).visibility = View.GONE
            }
        } else {
            if (isMediumAd) {
                adView.findViewById<Guideline>(R.id.glNativeAdmobMedium1).setGuidelinePercent(0.17f)
                adView.findViewById<CardView>(R.id.adIconCard).visibility = View.VISIBLE
            } else {
                adView.findViewById<CardView>(R.id.adIconCard).visibility = View.VISIBLE
            }
            (adView.iconView as ImageView).setImageDrawable(nativeAd.icon!!.drawable)
            adView.iconView!!.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)
        adView.visibility = View.VISIBLE
        val vc: VideoController = nativeAd.mediaContent!!.videoController
        if (vc.hasVideoContent()) {
            vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {}
        }
    }

    private fun populateSimpleNativeAdmob(
        isMediumAd: Boolean,
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        adView.headlineView = adView.findViewById(R.id.adHeadline)
        adView.bodyView = adView.findViewById(R.id.adBody)
        adView.callToActionView = adView.findViewById(R.id.adCallToAction)
        adView.iconView = adView.findViewById(R.id.adAppIcon)
        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            if (isMediumAd) {
                adView.findViewById<Guideline>(R.id.glNativeAdmobSmall1).setGuidelinePercent(0f)
                adView.findViewById<CardView>(R.id.adIconCard).visibility = View.GONE
            } else {
                adView.findViewById<Guideline>(R.id.glNativeAdmobBannerNormal1)
                    .setGuidelinePercent(0f)
                adView.findViewById<CardView>(R.id.adIconCard).visibility = View.GONE
            }
        } else {
            if (isMediumAd) {
                adView.findViewById<Guideline>(R.id.glNativeAdmobSmall1).setGuidelinePercent(0.17f)
                adView.findViewById<CardView>(R.id.adIconCard).visibility = View.VISIBLE
            } else {
                adView.findViewById<Guideline>(R.id.glNativeAdmobBannerNormal1)
                    .setGuidelinePercent(0.15f)
                adView.findViewById<CardView>(R.id.adIconCard).visibility = View.VISIBLE
            }
            (adView.iconView as ImageView).setImageDrawable(nativeAd.icon!!.drawable)
            adView.iconView!!.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
        adView.visibility = View.VISIBLE
    }
}