package com.manual.mediation.library.sotadlib.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.callingClasses.WelcomeScreensConfiguration
import com.manual.mediation.library.sotadlib.interfaces.WelcomeInterface
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.utils.hideSystemUI

class WelcomeScreenOne : AppCompatBaseActivity(), WelcomeInterface {

    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    var myView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        hideSystemUI()
        sotAdsConfigurations = SOTAdsManager.getConfigurations()

        WelcomeScreensConfiguration.welcomeInstance?.let { config ->
            config.setWelcomeInterface(this)
            myView = config.view
            myView?.parent?.let { parent ->
                if (parent is ViewGroup) {
                    parent.removeView(myView)
                }
            }
            setContentView(myView)
        }

        val nativeSurvey2Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_2") as? Boolean ?: false
        if (nativeSurvey2Enabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_2_MED")) {
                "ADMOB" -> loadAdmobSurveyDupNatives()
                "META" -> loadMetaSurveyDupNatives()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val nativeSurvey1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_1") as? Boolean ?: false
        if (nativeSurvey1Enabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_1_MED")) {
                "ADMOB" -> showAdmobLanguageScreenOneNatives()
                "META" -> showMetaLanguageScreenOneNatives()
            }
        } else {
            myView?.let {
                myView?.findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
            }
        }
    }

    override fun showWelcomeTwoScreen() {
        WelcomeScreensConfiguration.welcomeInstance?.setWelcomeInterface(null)
        startActivity(Intent(this, WelcomeScreenDup::class.java), ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        finish()
    }

    private fun showMetaLanguageScreenOneNatives() {
        myView?.let {
            sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("META_NATIVE_SURVEY_1")?.let { adId ->
                MetaNativeAdManager.requestAd(
                    mContext = this,
                    adId = adId,
                    adName = "NATIVE_SURVEY_1",
                    isMedia = true,
                    isMediumAd = true,
                    remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_1").toString().toBoolean(),
                    populateView = true,
                    nativeAdLayout = myView?.findViewById(R.id.nativeAdContainerAd),
                    onAdFailed = {
                        myView?.findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
                        Log.i("SOT_ADS_TAG","WelcomeScreenOne: Meta: onAdFailed()")
                    },
                    onAdLoaded = {
                        Log.i("SOT_ADS_TAG","WelcomeScreenOne: Meta: onAdLoaded()")
                    }
                )
            } ?: Log.w("WelcomeScreenOne", "META_NATIVE_SURVEY_1 ad ID is missing.")
        }
    }

    private fun showAdmobLanguageScreenOneNatives() {
        myView?.let {
            sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_SURVEY_1")?.let { adId ->
                AdmobNativeAdManager.requestAd(
                    mContext = this,
                    adId = adId,
                    adName = "NATIVE_SURVEY_1",
                    isMedia = true,
                    isMediumAd = true,
                    remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_1").toString().toBoolean(),
                    populateView = true,
                    adContainer = myView?.findViewById(R.id.nativeAdContainerAd),
                    onAdFailed = {
                        myView?.findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
                        Log.i("SOT_ADS_TAG","WelcomeScreenOne: Admob: onAdFailed()")
                    },
                    onAdLoaded = {
                        Log.i("SOT_ADS_TAG","WelcomeScreenOne: Admob: onAdLoaded()")
                    }
                )
            } ?: Log.w("WelcomeScreenOne", "ADMOB_NATIVE_SURVEY_1 ad ID is missing.")
        }
    }

    private fun loadMetaSurveyDupNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("META_NATIVE_SURVEY_2")
        if (adId != null) {
            MetaNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_SURVEY_2",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG", "Meta ad ID not found for NATIVE_SURVEY_2")
        }
    }

    private fun loadAdmobSurveyDupNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_SURVEY_2")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_SURVEY_2",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG", "Admob ad ID not found for NATIVE_SURVEY_2")
        }
    }
}