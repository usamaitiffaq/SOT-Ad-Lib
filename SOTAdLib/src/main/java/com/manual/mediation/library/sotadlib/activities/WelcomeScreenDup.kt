package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import com.facebook.shimmer.ShimmerFrameLayout
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.callingClasses.WelcomeScreensConfiguration
import com.manual.mediation.library.sotadlib.interfaces.WelcomeDupInterface
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralBannerAdManager
import com.manual.mediation.library.sotadlib.utils.hideSystemUIUpdated

class WelcomeScreenDup: AppCompatBaseActivity(), WelcomeDupInterface {

    var myView: View? = null
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        supportActionBar?.hide()
        hideSystemUIUpdated()
        sotAdsConfigurations = SOTAdsManager.getConfigurations()

        WelcomeScreensConfiguration.welcomeInstance?.let { config ->
            config.setWelcomeDupInterface(this)
            myView = config.view
            myView?.parent?.let { parent ->
                if (parent is ViewGroup) {
                    parent.removeView(myView)
                }
            }
            setContentView(myView)
        }

        val nativeWalkThrough1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_1") as? Boolean ?: false
        if (nativeWalkThrough1Enabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_1_MED")) {
                "ADMOB" -> loadAdmobWTOneNatives()
                "META" -> loadMetaWTOneNatives()
                "MINTEGRAL" -> loadMintegralWTOneBanner()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val nativeSurvey1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_2") as? Boolean ?: false
        if (nativeSurvey1Enabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_2_MED")) {
                "ADMOB" -> showAdmobLanguageScreenOneNatives()
                "META" -> showMetaLanguageScreenOneNatives()
                "MINTEGRAL" -> showMintegralSurveyDupBanner()
            }
        } else {
            myView?.let {
                myView?.findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
            }
        }
    }

    private fun showMetaLanguageScreenOneNatives() {
        myView?.let {
            sotAdsConfigurations?.firstOpenFlowAdIds?.get("META_NATIVE_SURVEY_2")?.let { adId ->
                MetaNativeAdManager.requestAd(
                    mContext = this,
                    adId = adId,
                    adName = "NATIVE_SURVEY_2",
                    isMedia = true,
                    isMediumAd = true,
                    remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_2").toString().toBoolean(),
                    populateView = true,
                    nativeAdLayout = myView?.findViewById(R.id.nativeAdContainerAd),
                    onAdFailed = {
                        myView?.findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
                        Log.i("SOT_ADS_TAG","WelcomeScreenDup: Meta: onAdFailed()")
                    },
                    onAdLoaded = {
                        Log.i("SOT_ADS_TAG","WelcomeScreenDup: Meta: onAdLoaded()")
                    }
                )
            } ?: Log.w("WelcomeScreenDup", "META_NATIVE_SURVEY_2 ad ID is missing.")
        }
    }
    private fun showAdmobLanguageScreenOneNatives() {
        myView?.let {
            sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_SURVEY_2")?.let { adId ->
                AdmobNativeAdManager.requestAd(
                    mContext = this,
                    adId = adId,
                    adName = "NATIVE_SURVEY_2",
                    isMedia = true,
                    isMediumAd = true,
                    remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_2").toString().toBoolean(),
                    populateView = true,
                    adContainer = myView?.findViewById(R.id.nativeAdContainerAd),
                    onAdFailed = {
                        myView?.findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
                        Log.i("SOT_ADS_TAG","WelcomeScreenDup: Admob: onAdFailed()")
                    },
                    onAdLoaded = {
                        Log.i("SOT_ADS_TAG","WelcomeScreenDup: Admob: onAdLoaded()")
                    }
                )
            } ?: Log.w("WelcomeScreenDup", "ADMOB_NATIVE_SURVEY_2 ad ID is missing.")
        }
    }
    private fun showMintegralSurveyDupBanner() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_SURVEY_2")?.split("-")?.size == 2) {
            MintegralBannerAdManager.requestBannerAd(
                activity = this,
                placementId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_SURVEY_2")!!.split("-")[0],
                unitId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_SURVEY_2")!!.split("-")[1],
                adName = "NATIVE_SURVEY_2",
                populateView = true,
                bannerContainer = findViewById(R.id.bannerAdMint),
                shimmerContainer = findViewById(R.id.shimmerLayout),
                onAdFailed = {
//                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "SURVEY_2: MINTEGRAL: onAdFailed()")
                },
                onAdLoaded = {
                    findViewById<ShimmerFrameLayout>(R.id.shimmerLayout).stopShimmer()
                    findViewById<ShimmerFrameLayout>(R.id.shimmerLayout).visibility = View.INVISIBLE
                    findViewById<FrameLayout>(R.id.bannerAdMint).visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "SURVEY_2: MINTEGRAL: onAdLoaded()")
                }
            )
        } else {
            Log.i("SOT_ADS_TAG", "BANNER : Mintegral : MAY SURVEY_2 Incorrect ID Format (placementID-unitID)")
        }
    }

    private fun loadMetaWTOneNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("META_NATIVE_WALKTHROUGH_1")
        if (adId != null) {
            MetaNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "WALKTHROUGH_1",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG","Meta ad ID not found for WALKTHROUGH_1")
        }
    }
    private fun loadAdmobWTOneNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_1")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "WALKTHROUGH_1",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG","Admob ad ID not found for WALKTHROUGH_1")
        }
    }
    private fun loadMintegralWTOneBanner() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_WALKTHROUGH_1")?.split("-")?.size == 2) {
            MintegralBannerAdManager.requestBannerAd(
                activity = this,
                placementId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_WALKTHROUGH_1")!!.split("-")[0],
                unitId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_WALKTHROUGH_1")!!.split("-")[1],
                adName = "WALKTHROUGH_1",
                populateView = false)
        } else {
            Log.e("SOT_ADS_TAG","BANNER : Mintegral : MAY SURVEY_1 Incorrect ID Format (placementID-unitID)")
        }
    }

    override fun endWelcomeTwoScreen() {
        finish()
    }
}