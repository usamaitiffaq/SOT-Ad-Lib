package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.adapters.LanguageAdapter
import com.manual.mediation.library.sotadlib.callingClasses.LanguageScreensConfiguration
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.manual.mediation.library.sotadlib.utils.hideSystemUI

class LanguageScreenDup: AppCompatBaseActivity() {

    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imvDone: AppCompatImageView
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        hideSystemUI()
        setContentView(R.layout.language_screen_dup)
        imvDone = findViewById(R.id.imvDone)
        recyclerView = findViewById(R.id.recyclerViewLanguage)
        recyclerView.layoutManager = LinearLayoutManager(this)

        LanguageScreensConfiguration.languageInstance?.let { config ->
            config.languageList?.let {
                if (config.selectedDrawable != null && config.unSelectedDrawable != null) {
                    if (config.selectedRadio != null && config.unSelectedRadio != null) {
                        languageAdapter = LanguageAdapter(
                            ctx = this,
                            languages = config.languageList!!,
                            selectedDrawable = config.selectedDrawable!!,
                            unSelectedDrawable = config.unSelectedDrawable!!,
                            selectedRadio = config.selectedRadio!!,
                            unSelectedRadio = config.unSelectedRadio!!) { }
                        recyclerView.adapter = languageAdapter
                    }
                }
            }
        }

        imvDone.setOnClickListener {
            SOTAdsManager.showWelcomeScreen()
            finish()
        }

        if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_1") == true) {
            when {
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_1_MED") == "ADMOB" -> {
                    loadAdmobSurveyNatives()
                }
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_1_MED") == "META" -> {
                    loadMetaSurveyNatives()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (NetworkCheck.isNetworkAvailable(this)) {
            if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_2") == true) {
                when {
                    sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_2_MED") == "ADMOB" -> {
                        findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.VISIBLE
                        showAdmobLanguageScreenOneNatives()
                    }
                    sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_2_MED") == "META" -> {
                        findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.VISIBLE
                        showMetaLanguageScreenOneNatives()
                    }
                }
            }
        }
    }

    private fun showMetaLanguageScreenOneNatives() {
        MetaNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_LANGUAGE_2"),
            adName = "NATIVE_LANGUAGE_2",
            isMedia = true,
            isMediumAd = true,
            populateView = true,
            nativeAdLayout = findViewById(R.id.nativeAdContainerAd),
            onAdFailed = {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("SOT_ADS_TAG","LanguageScreenDup: Meta: onAdFailed()")
                },300)
            },
            onAdLoaded = {
                Log.i("SOT_ADS_TAG","LanguageScreenDup: Meta: onAdLoaded()")
            }
        )
    }

    private fun showAdmobLanguageScreenOneNatives() {
        AdmobNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_LANGUAGE_2"),
            adName = "NATIVE_LANGUAGE_2",
            isMedia = true,
            isMediumAd = true,
            populateView = true,
            adContainer = findViewById(R.id.nativeAdContainerAd),
            onAdFailed = {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("SOT_ADS_TAG","LanguageScreenDup: Admob onAdFailed()")
                },300)
            },
            onAdLoaded = {
                Log.i("SOT_ADS_TAG","LanguageScreenDup: Admob onAdLoaded()")
            }
        )
    }

    private fun loadMetaSurveyNatives() {
        MetaNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_SURVEY_1"),
            adName = "NATIVE_SURVEY_1",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }

    private fun loadAdmobSurveyNatives() {
        AdmobNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_SURVEY_1"),
            adName = "NATIVE_SURVEY_1",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }
}