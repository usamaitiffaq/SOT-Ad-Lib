package com.manual.mediation.library.sotadlib.activities

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
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.utils.hideSystemUI

class WelcomeScreenDup: AppCompatBaseActivity() {

    var myView: View? = null
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        hideSystemUI()
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        WelcomeScreensConfiguration.welcomeInstance?.let { config ->
            myView = config.view
            (myView!!.parent as? ViewGroup)?.removeView(myView)
            setContentView(myView)
        }

        if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_1") == true) {
            when {
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_1_MED") == "ADMOB" -> {
                    loadAdmobWTOneNatives()
                }
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_1_MED") == "META" -> {
                    loadMetaWTOneNatives()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_2") == true) {
            when {
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_2_MED") == "ADMOB" -> {
                    showAdmobLanguageScreenOneNatives()
                }
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_2_MED") == "META" -> {
                    showMetaLanguageScreenOneNatives()
                }
            }
        }
    }

    private fun showMetaLanguageScreenOneNatives() {
        myView?.let {
            MetaNativeAdManager.requestAd(
                mContext = this,
                adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_SURVEY_2"),
                adName = "NATIVE_SURVEY_2",
                isMedia = true,
                isMediumAd = true,
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
        }
    }

    private fun showAdmobLanguageScreenOneNatives() {
        myView?.let {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_SURVEY_2"),
                adName = "NATIVE_SURVEY_2",
                isMedia = true,
                isMediumAd = true,
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
        }
    }

    private fun loadMetaWTOneNatives() {
        MetaNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_WALKTHROUGH_1"),
            adName = "WALKTHROUGH_1",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }

    private fun loadAdmobWTOneNatives() {
        AdmobNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_WALKTHROUGH_1"),
            adName = "WALKTHROUGH_1",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }
}