package com.manual.mediation.library.sotadlib.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
            setContentView(config.view)
        }

        if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_2") == true) {
            when {
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_2_MED") == "ADMOB" -> {
                    loadAdmobSurveyDupNatives()
                }
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_2_MED") == "META" -> {
                    loadMetaSurveyDupNatives()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_1") == true) {
            when {
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_1_MED") == "ADMOB" -> {
                    showAdmobLanguageScreenOneNatives()
                }
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_1_MED") == "META" -> {
                    showMetaLanguageScreenOneNatives()
                }
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
            MetaNativeAdManager.requestAd(
                mContext = this,
                adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_SURVEY_1"),
                adName = "NATIVE_SURVEY_1",
                isMedia = true,
                isMediumAd = true,
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
        }
    }

    private fun showAdmobLanguageScreenOneNatives() {
        myView?.let {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_SURVEY_1"),
                adName = "NATIVE_SURVEY_1",
                isMedia = true,
                isMediumAd = true,
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
        }
    }


    private fun loadMetaSurveyDupNatives() {
        MetaNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_SURVEY_2"),
            adName = "NATIVE_SURVEY_2",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }

    private fun loadAdmobSurveyDupNatives() {
        AdmobNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_SURVEY_2"),
            adName = "NATIVE_SURVEY_2",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }
}