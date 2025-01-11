package com.manual.mediation.library.sotadlib.mintegralAdClasses

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.mbridge.msdk.nativex.view.MBMediaView
import com.mbridge.msdk.out.Campaign
import com.mbridge.msdk.out.Frame
import com.mbridge.msdk.out.MBNativeHandler
import com.mbridge.msdk.out.NativeListener
import com.mbridge.msdk.widget.MBAdChoice

object MintegralNativeAdFullScreen {

    private val nativeAdCache = HashMap<String, Campaign?>()
    private val adLoadingState = HashMap<String, Boolean>()
    private val mbNativeHandlerState = HashMap<String, MBNativeHandler>()

    fun requestAd(
        mContext: Activity?,
        placementId: String,
        unitId: String,
        adName: String = "",
        remoteConfig: Boolean = true,
        populateView: Boolean = false,
        adContainer: CardView? = null,
        onAdFailed: (() -> Unit)? = null,
        onAdLoaded: (() -> Unit)? = null
    ) {
        if (mContext == null) {
            Log.i("SOT_ADS_TAG", "Context is null; cannot load ad.")
            onAdFailed?.invoke()
            return
        }

        if (populateView) {
            if (!NetworkCheck.isNetworkAvailable(mContext) || !remoteConfig) {
                adContainer?.visibility = View.GONE
                Log.i("SOT_ADS_TAG", "Mintegral: Native: View is gone")
                onAdFailed?.invoke()
                return
            } else {
                adContainer?.visibility = View.VISIBLE
                Log.i("SOT_ADS_TAG", "Mintegral: Native: View is visible")
            }
        }

        if (adLoadingState[adName] == true && nativeAdCache[adName] != null && mbNativeHandlerState[adName] != null) {
            Log.i("SOT_ADS_TAG", "Mintegral: Native: $adName: showCachedAd()")
            showCachedAd(adName, adContainer, mbNativeHandlerState[adName]!!)
            return
        }

        adLoadingState[adName] = true

        val properties = MBNativeHandler.getNativeProperties(placementId, unitId)
        val mbNativeHandler = MBNativeHandler(properties, mContext)

        mbNativeHandler.setAdListener(object : NativeListener.NativeAdListener {
            override fun onAdLoaded(list: List<Campaign?>?, i: Int) {
                if (!list.isNullOrEmpty()) {
                    val campaign = list[0]!!
                    nativeAdCache[adName] = campaign
                    adLoadingState[adName] = true
                    mbNativeHandlerState[adName] = mbNativeHandler
                    if (populateView) {
                        Log.i("SOT_ADS_TAG", "Mintegral: Native: $adName: populateNativeAd()")
                        populateNativeAd(campaign, adContainer, mbNativeHandler)
                    }
                    onAdLoaded?.invoke()
                } else {
                    onAdFailed?.invoke()
                }
            }

            override fun onAdLoadError(error: String) {
                nativeAdCache[adName] = null
                adLoadingState[adName] = false
                onAdFailed?.invoke()
                Log.e("SOT_ADS_TAG", "Mintegral: Native: $adName: onAdLoadError(): $error")
            }

            override fun onAdClick(campaign: Campaign?) {
                Log.i("SOT_ADS_TAG", "Mintegral: Native: $adName: onAdClick()")
                nativeAdCache[adName] = null
                adLoadingState[adName] = false
            }

            override fun onAdFramesLoaded(p0: MutableList<Frame>?) {
                Log.i("SOT_ADS_TAG", "Mintegral: Native: $adName: onAdFramesLoaded()")
            }

            override fun onLoggingImpression(i: Int) {
                Log.i("SOT_ADS_TAG", "Mintegral: Native: $adName: onLoggingImpression()")
            }
        })
        mbNativeHandler.trackingListener = object : NativeListener.NativeTrackingListener {
            override fun onInterceptDefaultLoadingDialog(): Boolean {
                return false
            }

            override fun onShowLoading(campaign: Campaign) {
                Log.e("SOT_ADS_TAG", "onShowLoading(): $campaign")
            }

            override fun onDismissLoading(campaign: Campaign) {
                Log.e("SOT_ADS_TAG", "onDismissLoading(): $campaign")
            }

            override fun onDownloadStart(campaign: Campaign) {
                Log.e("SOT_ADS_TAG", "onDownloadStart(): $campaign")
            }

            override fun onDownloadFinish(campaign: Campaign) {
                Log.e("SOT_ADS_TAG","onDownloadFinish(): ")
            }

            override fun onDownloadProgress(i: Int) {
                Log.e("SOT_ADS_TAG", "onDownloadProgress(): $i")
            }

            override fun onStartRedirection(campaign: Campaign, s: String) {

                Log.e("SOT_ADS_TAG", "onStartRedirection(): $campaign :: $s")
            }

            override fun onFinishRedirection(campaign: Campaign, s: String) {
                Log.e("SOT_ADS_TAG", "onFinishRedirection(): $campaign :: $s")
            }

            override fun onRedirectionFailed(campaign: Campaign, s: String) {
                Log.e("SOT_ADS_TAG", "onRedirectionFailed(): $campaign :: $s")
            }
        }
        mbNativeHandler.load()
    }

    private fun showCachedAd(
        adName: String,
        adContainer: CardView?,
        mbNativeHandler: MBNativeHandler) {
        adContainer?.context?.let {
            nativeAdCache[adName]?.let { cachedAd ->
                populateNativeAd(cachedAd, adContainer, mbNativeHandler)
            } ?: run {
                Log.i("SOT_ADS_TAG", "Ad is not available in cache for adName: $adName")
            }
        } ?: Log.i("SOT_ADS_TAG", "Ad container or context is null; cannot load ad.")
    }

    private fun populateNativeAd(
        campaign: Campaign,
        adContainer: CardView?,
        mbNativeHandler: MBNativeHandler
    ) {
        val ivIcon = adContainer?.findViewById<ImageView>(R.id.custom_icon)
        val mbMediaView = adContainer?.findViewById<MBMediaView>(R.id.custom_media)
        val mbAdChoice = adContainer?.findViewById<MBAdChoice>(R.id.custom_choice)
        val tvTitle = adContainer?.findViewById<TextView>(R.id.custom_title)
        val tvDescription = adContainer?.findViewById<TextView>(R.id.custom_desc)

        ivIcon?.let { imageView ->
            if (!TextUtils.isEmpty(campaign.imageUrl)) {
                Glide.with(imageView.context)
                    .load(campaign.imageUrl)
                    .into(imageView)
                mbNativeHandler.registerView(imageView, campaign)
            }
        }

        tvTitle?.text = campaign.appName
        tvDescription?.text = campaign.appDesc

        mbAdChoice?.setCampaign(campaign)
        mbMediaView?.setNativeAd(campaign)

        adContainer?.let {
            mbNativeHandler.registerView(it, campaign)
        }
    }
}