package com.manual.mediation.library.sotadlib.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.adapters.LanguageAdapter
import com.manual.mediation.library.sotadlib.callingClasses.LanguageScreensConfiguration
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.interfaces.LanguageInterface
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.manual.mediation.library.sotadlib.utils.hideSystemUI

class LanguageScreenOne : AppCompatBaseActivity(), LanguageInterface {

    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var recyclerView: RecyclerView
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        supportActionBar?.hide()
        hideSystemUI()
        setContentView(R.layout.language_screen_one)
        recyclerView = findViewById(R.id.recyclerViewLanguage)
        recyclerView.layoutManager = LinearLayoutManager(this)
        Log.i("LanguageScreenOne", "Language: META_NATIVE_LANGUAGE_1"+sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_LANGUAGE_1"))

        LanguageScreensConfiguration.languageInstance?.let { config ->
            config.setLanguageInterface(this)

            config.languageList?.let {
                if (config.selectedDrawable != null && config.unSelectedDrawable != null) {
                    if (config.selectedRadio != null && config.unSelectedRadio != null) {
                        languageAdapter = LanguageAdapter(
                            ctx = this,
                            languages = config.languageList!!,
                            selectedDrawable = config.selectedDrawable!!,
                            unSelectedDrawable = config.unSelectedDrawable!!,
                            selectedRadio = config.selectedRadio!!,
                            unSelectedRadio = config.unSelectedRadio!!) {
                            config.showLanguageTwoScreen()
                        }
                        recyclerView.adapter = languageAdapter
                    }
                }
            }
        }
        if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_2") == true) {
            when {
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_2_MED") == "ADMOB" -> {
                    loadAdmobLanguageScreenDupNatives()
                }
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_2_MED") == "META" -> {
                    loadMetaLanguageScreenDupNatives()
                }
            }
        }
    }

    private fun loadMetaLanguageScreenDupNatives() {
        MetaNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_LANGUAGE_2"),
            adName = "NATIVE_LANGUAGE_2",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }

    private fun loadAdmobLanguageScreenDupNatives() {
        AdmobNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_LANGUAGE_2"),
            adName = "NATIVE_LANGUAGE_2",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }

    override fun showLanguageTwoScreen() {
        Log.i("LanguageScreenOne", "Language: showLanguageTwoScreen()")
        startActivity(Intent(this, LanguageScreenDup::class.java), ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
        finish()
    }

    override fun onResume() {
        super.onResume()
//            if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_1") == true) {
                when {
                    sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_1_MED") == "ADMOB" -> {
                        findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.VISIBLE
                        showAdmobLanguageScreenOneNatives()
                    }
                    sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_1_MED") == "META" -> {
                        findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.VISIBLE
                        showMetaLanguageScreenOneNatives()
                    }
                }
//            }
    }

    private fun showMetaLanguageScreenOneNatives() {
        MetaNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_LANGUAGE_1"),
            adName = "NATIVE_LANGUAGE_1",
            isMedia = true,
            isMediumAd = true,
            remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_1").toString().toBoolean(),
            populateView = true,
            nativeAdLayout = findViewById(R.id.nativeAdContainerAd),
            onAdFailed = {
                findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                Log.i("LanguageScreenOne", "Language: onAdFailed()")
            },
            onAdLoaded = {
                Log.i("LanguageScreenOne", "Language: onAdLoaded()")
            }
        )
    }

    private fun showAdmobLanguageScreenOneNatives() {
        AdmobNativeAdManager.requestAd(
            mContext = this,
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_LANGUAGE_1"),
            adName = "NATIVE_LANGUAGE_1",
            isMedia = true,
            isMediumAd = true,
            remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_1").toString().toBoolean(),
            populateView = true,
            adContainer = findViewById(R.id.nativeAdContainerAd),
            onAdFailed = {
                Handler().postDelayed({
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("LanguageScreenOne", "Language: onAdFailed()")
                },300)
            },
            onAdLoaded = {
                Log.i("LanguageScreenOne", "Language: onAdLoaded()")
            }
        )
    }
}