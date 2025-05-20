package com.manual.mediation.library.sotadlib.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.adapters.LanguageAdapter
import com.manual.mediation.library.sotadlib.callingClasses.LanguageScreensConfiguration
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.interfaces.LanguageInterface
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralBannerAdManager
import com.manual.mediation.library.sotadlib.utils.hideSystemUIUpdated

class LanguageScreenOne : AppCompatBaseActivity(), LanguageInterface {

    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var recyclerView: RecyclerView
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        supportActionBar?.hide()
        hideSystemUIUpdated()
        setContentView(R.layout.language_screen_one)
        Log.i("SOTStartTestActivity", "language1_scr")

        recyclerView = findViewById(R.id.recyclerViewLanguage)
        recyclerView.layoutManager = LinearLayoutManager(this)

        Log.i("LanguageScreenOne", "Language: onCreate")

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

        /*val nativeLanguage2Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_2") as? Boolean ?: false
        if (nativeLanguage2Enabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_2_MED")) {
                "ADMOB" -> loadAdmobLanguageScreenDupNatives()
                "META" -> loadMetaLanguageScreenDupNatives()
            }
        }*/
    }

    private fun loadMetaLanguageScreenDupNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.get("META_NATIVE_LANGUAGE_2")
        if (adId != null) {
            MetaNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_LANGUAGE_2",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.w("LanguageScreenOne", "META_NATIVE_LANGUAGE_2 ad ID is missing in firstOpenFlowAdIds.")
        }
    }

    private fun loadAdmobLanguageScreenDupNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.get("ADMOB_NATIVE_LANGUAGE_2")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_LANGUAGE_2",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.w("LanguageScreenOne", "ADMOB_NATIVE_LANGUAGE_2 ad ID is missing in firstOpenFlowAdIds.")
        }
    }

//    override fun showLanguageTwoScreen() {
//        Log.i("LanguageScreenOne", "Language: showLanguageTwoScreen()")
//        startActivity(Intent(this, LanguageScreenDup::class.java), ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
//        finish()
//        overridePendingTransition(0, 0)
//    }

    override fun showLanguageTwoScreen() {
        if (!isFinishing && !isDestroyed) {
            try {
                val intent = Intent(this, LanguageScreenDup::class.java)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                    overridePendingTransition(0, 0)
                } else {
                    Log.e("LanguageScreenOne", "No activity found to handle intent")
                }
            } catch (e: Exception) {
                Log.e("LanguageScreenOne", "Error starting activity", e)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val nativeLanguage1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_1") as? Boolean ?: false
        if (nativeLanguage1Enabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_1_MED")) {
                "ADMOB" -> {
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.VISIBLE
                    showAdmobLanguageScreenOneNatives()
                }
                "META" -> {
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.VISIBLE
                    showMetaLanguageScreenOneNatives()
                }
                "MINTEGRAL" -> {
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.VISIBLE
                    showMintegralLanguageScreenOneBanner()
                }
            }
        } else {
            findViewById<CardView>(R.id.nativeAdContainerAd)?.let {
                findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
            }
        }
    }

    private fun showAdmobLanguageScreenOneNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_LANGUAGE_1")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_LANGUAGE_1",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_1").toString().toBoolean(),
                populateView = true,
                adContainer = findViewById(R.id.nativeAdContainerAd),
                onAdFailed = {
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("LanguageScreenOne", "Language: onAdFailed()")
                },
                onAdLoaded = {
                    Log.i("LanguageScreenOne", "Language: onAdLoaded()")
                }
            )
        } ?: Log.w("LanguageScreenOne", "ADMOB_NATIVE_LANGUAGE_1 ad ID is missing.")
    }

    private fun showMetaLanguageScreenOneNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("META_NATIVE_LANGUAGE_1")?.let { adId ->
            MetaNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
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
        } ?: Log.w("LanguageScreenOne", "META_NATIVE_LANGUAGE_1 ad ID is missing.")
    }

    private fun showMintegralLanguageScreenOneBanner() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_LANGUAGE_1")?.split("-")?.size == 2) {
            MintegralBannerAdManager.requestBannerAd(
                activity = this,
                placementId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_LANGUAGE_1").split("-")[0],
                unitId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_LANGUAGE_1").split("-")[1],
                adName = "NATIVE_LANGUAGE_1",
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_1").toString().toBoolean(),
                populateView = true,
                bannerContainer = findViewById(R.id.bannerAdMint),
                shimmerContainer = findViewById(R.id.shimmerLayout),
                onAdFailed = {
//                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "LANGUAGE_1: MINTEGRAL: onAdFailed()")
                },
                onAdLoaded = {
                    findViewById<ShimmerFrameLayout>(R.id.shimmerLayout).stopShimmer()
                    findViewById<ShimmerFrameLayout>(R.id.shimmerLayout).visibility = View.INVISIBLE
                    findViewById<FrameLayout>(R.id.bannerAdMint).visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "LANGUAGE_1: MINTEGRAL: onAdLoaded()")
                }
            )
        } else {
            Log.i("SOT_ADS_TAG", "BANNER : Mintegral : MAY LANGUAGE_1 Incorrect ID Format (placementID-unitID)")
        }
    }
}