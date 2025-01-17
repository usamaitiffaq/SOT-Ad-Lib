package com.manual.mediation.library.sotadlib.callingClasses

import android.app.Activity
import android.util.Log
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobInterstitialAdSplash
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobResumeAdSplash
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaInterstitialAdSplash
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralBannerAdManager
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralInterstitialAdSplash
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralResumeAdSplash
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.manual.mediation.library.sotadlib.utils.PrefHelper
import com.manual.mediation.library.sotadlib.utilsGoogleAdsConsent.ConsentConfigurations

class SOTAdsConfigurations private constructor() {

    var languageScreensConfiguration: LanguageScreensConfiguration? = null
    var welcomeScreensConfiguration: WelcomeScreensConfiguration? = null
    var walkThroughScreensConfiguration: WalkThroughScreensConfiguration? = null
    private var remoteConfigData: HashMap<String, Any>? = null
    var firstOpenFlowAdIds: HashMap<String, String> = HashMap()
    private lateinit var admobResumeAdSplash: AdmobResumeAdSplash
    private lateinit var mintegralResumeAdSplash: MintegralResumeAdSplash
    private lateinit var admobInterstitialAdSplash: AdmobInterstitialAdSplash
    private lateinit var mintegralInterstitialAdSplash: MintegralInterstitialAdSplash
    private lateinit var metaInterstitialAdSplash: MetaInterstitialAdSplash

    fun setRemoteConfigData(activityContext: Activity, myRemoteConfigData: HashMap<String, Any>) {
        this.remoteConfigData = myRemoteConfigData
        this.remoteConfigData?.forEach { value ->
            Log.i("RemoteConfigFetches","SOTAdsConfigurations : setRemoteConfigData() "+value.key + " : " + value.value)
        }

        if (NetworkCheck.isNetworkAvailable(activityContext)) {
            when {
                myRemoteConfigData.getValue("RESUME_INTER_SPLASH") == "RESUME" -> {
                    when {
                        myRemoteConfigData.getValue("RESUME_INTER_SPLASH_MED") == "ADMOB" -> {
                            showAdMobResumeAdSplash(activityContext)
                        }
                        myRemoteConfigData.getValue("RESUME_INTER_SPLASH_MED") == "MINTEGRAL" -> {
                            showMintegralResumeAdSplash(activityContext)
                        }
                    }
                }
                myRemoteConfigData.getValue("RESUME_INTER_SPLASH") == "INTERSTITIAL" -> {
                    when {
                        myRemoteConfigData.getValue("RESUME_INTER_SPLASH_MED") == "ADMOB" -> {
                            showAdMobInterstitialAdSplash(activityContext)
                        }
                        myRemoteConfigData.getValue("RESUME_INTER_SPLASH_MED") == "META" -> {
                            showMetaInterstitialAdSplash(activityContext)
                        }
                        myRemoteConfigData.getValue("RESUME_INTER_SPLASH_MED") == "MINTEGRAL" -> {
                            showMintegralInterstitialAdSplash(activityContext)
                        }
                    }
                }
                myRemoteConfigData.getValue("RESUME_INTER_SPLASH") == "OFF" -> {
                    proceedNext(activityContext)
                }
            }

            if (!PrefHelper(activityContext).getBooleanDefault("StartScreens", default = false)) {
                if (myRemoteConfigData.getValue("NATIVE_LANGUAGE_1") == true) {
                    when {
                        myRemoteConfigData.getValue("NATIVE_LANGUAGE_1_MED") == "ADMOB" -> {
                            loadAdmobLanguageScreenOneNatives(activityContext)
                        }
                        myRemoteConfigData.getValue("NATIVE_LANGUAGE_1_MED") == "META" -> {
                            loadMetaLanguageScreenOneNatives(activityContext)
                        }
                        myRemoteConfigData.getValue("NATIVE_LANGUAGE_1_MED") == "MINTEGRAL" -> {
                            loadMintegralLanguageScreenOneBanner(activityContext)
                        }
                    }
                }

                if (myRemoteConfigData.getValue("NATIVE_LANGUAGE_2") == true) {
                    when {
                        myRemoteConfigData.getValue("NATIVE_LANGUAGE_2_MED") == "ADMOB" -> {
                            loadAdmobLanguageScreenDupNatives(activityContext)
                        }
                        myRemoteConfigData.getValue("NATIVE_LANGUAGE_2_MED") == "META" -> {
                            loadMetaLanguageScreenDupNatives(activityContext)
                        }
                        myRemoteConfigData.getValue("NATIVE_LANGUAGE_2_MED") == "MINTEGRAL" -> {
                            loadMintegralLanguageScreenDupBanner(activityContext)
                        }
                    }
                }
            }
        } else {
            proceedNext(activityContext)
        }
    }

    private fun loadAdmobLanguageScreenOneNatives(mContext: Activity) {
         AdmobNativeAdManager.requestAd(
             mContext = mContext,
             adId = firstOpenFlowAdIds.getValue("ADMOB_NATIVE_LANGUAGE_1"),
             adName = "NATIVE_LANGUAGE_1",
             isMedia = true,
             isMediumAd = true,
             populateView = false
         )
    }
    private fun loadMetaLanguageScreenOneNatives(mContext: Activity) {
        MetaNativeAdManager.requestAd(
            mContext = mContext,
            adId = firstOpenFlowAdIds.getValue("META_NATIVE_LANGUAGE_1"),
            adName = "NATIVE_LANGUAGE_1",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }
    private fun loadMintegralLanguageScreenOneBanner(activityContext: Activity) {
        if (firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_LANGUAGE_1").split("-").size == 2) {
            MintegralBannerAdManager.requestBannerAd(
                activity = activityContext,
                placementId = firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_LANGUAGE_1").split("-")[0],
                unitId = firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_LANGUAGE_1").split("-")[1],
                adName = "NATIVE_LANGUAGE_1",
                populateView = false)
        } else {
            Log.e("SOT_ADS_TAG","BANNER : Mintegral : MAY LANGUAGE_1 Incorrect ID Format (placementID-unitID)")
        }
    }

    private fun loadAdmobLanguageScreenDupNatives(mContext: Activity) {
        AdmobNativeAdManager.requestAd(
            mContext = mContext,
            adId = firstOpenFlowAdIds.getValue("ADMOB_NATIVE_LANGUAGE_2"),
            adName = "NATIVE_LANGUAGE_2",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }
    private fun loadMetaLanguageScreenDupNatives(mContext: Activity) {
        MetaNativeAdManager.requestAd(
            mContext = mContext,
            adId = firstOpenFlowAdIds.getValue("META_NATIVE_LANGUAGE_2"),
            adName = "NATIVE_LANGUAGE_2",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }
    private fun loadMintegralLanguageScreenDupBanner(activityContext: Activity) {
        if (firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_LANGUAGE_2").split("-").size == 2) {
            MintegralBannerAdManager.requestBannerAd(
                activity = activityContext,
                placementId = firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_LANGUAGE_2").split("-")[0],
                unitId = firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_LANGUAGE_2").split("-")[1],
                adName = "NATIVE_LANGUAGE_2",
                populateView = false)
        } else {
            Log.e("SOT_ADS_TAG","BANNER : Mintegral : MAY LANGUAGE_2 Incorrect ID Format (placementID-unitID)")
        }
    }

    private fun showMetaInterstitialAdSplash(activityContext: Activity) {
        activityContext.let {
            metaInterstitialAdSplash = MetaInterstitialAdSplash(activityContext, firstOpenFlowAdIds.getValue("META_SPLASH_INTERSTITIAL"),
                onAdDismissed = {
                    proceedNext(activityContext)
                },
                onAdFailed = {
                    proceedNext(activityContext)
                },
                onAdTimeout = {
                    proceedNext(activityContext)
                },
                onAdShowed = {}
            )
        }
    }

    private fun showAdMobResumeAdSplash(activityContext: Activity) {
        activityContext.let {
            admobResumeAdSplash = AdmobResumeAdSplash(activityContext, firstOpenFlowAdIds.getValue("ADMOB_SPLASH_RESUME"),
                onAdDismissed = {
                    proceedNext(activityContext)
                },
                onAdFailed = {
                    proceedNext(activityContext)
                },
                onAdTimeout = {
                    proceedNext(activityContext)
                },
                onAdShowed = {}
            )
        }
    }

    private fun showMintegralResumeAdSplash(activityContext: Activity) {
        activityContext.let {
            if (firstOpenFlowAdIds.getValue("MINTEGRAL_SPLASH_RESUME").split("-").size == 2) {
                mintegralResumeAdSplash = MintegralResumeAdSplash(
                    activity = activityContext,
                    placementId = firstOpenFlowAdIds.getValue("MINTEGRAL_SPLASH_RESUME").split("-")[0],
                    unitId = firstOpenFlowAdIds.getValue("MINTEGRAL_SPLASH_RESUME").split("-")[1],
                    canSkip = true,
                    timeoutSkip = 5,
                    onAdDismissed = { proceedNext(activityContext) },
                    onAdFailed = { proceedNext(activityContext) },
                    onAdTimeout = { proceedNext(activityContext) },
                    onAdShowed = { }
                )
            } else {
                Log.i("SOT_ADS_TAG","Interstitial : Mintegral : May SPLASH_Incorrect ID Format (placementID-unitID)")
            }
        }
    }

    private fun showAdMobInterstitialAdSplash(activityContext: Activity) {
        activityContext.let {
            admobInterstitialAdSplash = AdmobInterstitialAdSplash(activityContext, firstOpenFlowAdIds.getValue("ADMOB_SPLASH_INTERSTITIAL"),
                onAdDismissed = {
                    proceedNext(activityContext)
                },
                onAdFailed = {
                    proceedNext(activityContext)
                },
                onAdTimeout = {
                    proceedNext(activityContext)
                },
                onAdShowed = {}
            )
        }
    }

    private fun showMintegralInterstitialAdSplash(activityContext: Activity) {
        activityContext.let {
            if (firstOpenFlowAdIds.getValue("MINTEGRAL_SPLASH_INTERSTITIAL").split("-").size == 2) {
                mintegralInterstitialAdSplash = MintegralInterstitialAdSplash(
                    activityContext,
                    placementId = firstOpenFlowAdIds.getValue("MINTEGRAL_SPLASH_INTERSTITIAL").split("-")[0],
                    unitId = firstOpenFlowAdIds.getValue("MINTEGRAL_SPLASH_INTERSTITIAL").split("-")[1],
                    onAdDismissed = {
                        proceedNext(activityContext)
                    },
                    onAdFailed = {
                        proceedNext(activityContext)
                    },
                    onAdTimeout = {
                        proceedNext(activityContext)
                    },
                    onAdShowed = {}
                )
            } else {
                Log.i("SOT_ADS_TAG","Interstitial : Mintegral : May SPLASH_Incorrect ID Format (placementID-unitID)")
            }
        }
    }

    private fun proceedNext(activityContext: Activity) {
        if (PrefHelper(activityContext).getBooleanDefault("StartScreens", default = false)) {
            SOTAdsManager.notifyFlowFinished()
        } else {
            startLanguageScreenConfiguration()
        }
    }

    fun getRemoteConfigData() : HashMap<String, Any>? {
        return this.remoteConfigData
    }

    private fun startLanguageScreenConfiguration() {
        Log.i("LanguageScreens","SOTAdsConfigurations : startLanguageScreenConfiguration() ")
        this.languageScreensConfiguration?.languageInitializationSetup() ?: run { }
    }

    fun startWelcomeScreenConfiguration() {
        Log.i("WelcomeScreens","SOTAdsConfigurations : startWelcomeScreenConfiguration() ")
        SOTAdsManager.notifyReConfigureBuilders()
        this.welcomeScreensConfiguration?.welcomeInitializationSetup() ?: run { }
    }

    fun startWalkThroughConfiguration() {
        Log.i("WalkThroughScreens","SOTAdsConfigurations : startWalkThroughConfiguration() ")
        this.walkThroughScreensConfiguration?.walkThroughInitializationSetup() ?: run { }
    }

    class Builder {
        private lateinit var consentConfigurations: ConsentConfigurations
        private var languageScreensConfiguration: LanguageScreensConfiguration? = null
        private var welcomeScreensConfiguration: WelcomeScreensConfiguration? = null
        private var walkThroughScreensConfiguration: WalkThroughScreensConfiguration? = null
        private var firstOpenFlowAdIds: HashMap<String, String> = HashMap()

        fun setFirstOpenFlowAdIds(firstOpenFlowAdIds: HashMap<String, String>) = apply {
            this.firstOpenFlowAdIds = firstOpenFlowAdIds
            this.firstOpenFlowAdIds.forEach { value ->
                Log.i("SOT_ADS_TAG","setFirstOpenFlowAdIds : "+value.key + " : " + value.value)
            }
        }

        fun setConsentConfig(consentConfig: ConsentConfigurations) = apply {
            this.consentConfigurations = consentConfig
        }

        fun setLanguageScreenConfiguration(languageScreensConfiguration: LanguageScreensConfiguration) = apply {
            this.languageScreensConfiguration = languageScreensConfiguration
        }

        fun setWelcomeScreenConfiguration(welcomeScreensConfiguration: WelcomeScreensConfiguration) = apply {
            this.welcomeScreensConfiguration = welcomeScreensConfiguration
        }

        fun setWalkThroughScreenConfiguration(walkThroughScreensConfiguration: WalkThroughScreensConfiguration) = apply {
            this.walkThroughScreensConfiguration = walkThroughScreensConfiguration
        }

        fun build(): SOTAdsConfigurations {
            if (!::consentConfigurations.isInitialized) {
                throw IllegalStateException("ConsentConfigurations must be provided")
            }
            val sotAdsConfigurations = SOTAdsConfigurations()
            sotAdsConfigurations.firstOpenFlowAdIds = firstOpenFlowAdIds
            sotAdsConfigurations.welcomeScreensConfiguration = welcomeScreensConfiguration
            sotAdsConfigurations.languageScreensConfiguration = languageScreensConfiguration
            sotAdsConfigurations.walkThroughScreensConfiguration = walkThroughScreensConfiguration
            return sotAdsConfigurations
        }
    }
}
