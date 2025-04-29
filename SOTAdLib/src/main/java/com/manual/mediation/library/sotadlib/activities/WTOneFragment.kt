package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdMobInterstitialInside
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdFullScreen
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import com.manual.mediation.library.sotadlib.databinding.FragmentWTOneBinding
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralBannerAdManager
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralBannerFullScreen
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralInterstitialInside
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WTOneFragment(val item: WalkThroughItem) : Fragment() {


    lateinit var binding: FragmentWTOneBinding
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWTOneBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()

        val nativeWalkThroughTwoEnabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_2") as? Boolean ?: false
        if (nativeWalkThroughTwoEnabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_2_MED")) {
                "ADMOB" -> loadAdmobWTTwoNatives()
                "META" -> loadMetaWTTwoNatives()
                "MINTEGRAL" -> loadMintegralWTTwoBanner()
            }
        }

        val nativeWalkThroughFullEnabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_FULLSCR") as? Boolean ?: false
        if (nativeWalkThroughFullEnabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_FULLSCR_MED")) {
                "ADMOB" -> loadAdmobWTFullNatives()
                "MINTEGRAL" -> loadMintegralWTFullBanner()
            }
        }

        val interstitialLetsStartEnabled = sotAdsConfigurations?.getRemoteConfigData()?.get("INTERSTITIAL_LETS_START") as? Boolean ?: false
        if (interstitialLetsStartEnabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("INTERSTITIAL_LETS_START_MED")) {
                "ADMOB" -> {
                    loadAdmobWTThreeInterstitial()
                }
                "MINTEGRAL" -> {
                    loadMintegralWTThreeInterstitial()
                }
            }
        }

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Glide.with(requireActivity())
                    .asDrawable()
                    .load(item.drawable)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(true)
                    .into(binding.main)
            }
        }
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Glide.with(requireActivity())
                    .asDrawable()
                    .load(item.drawableBubble)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(true)
                    .into(binding.bubble)
            }
        }

        binding.txtHeading.setTextColor(ContextCompat.getColor(requireActivity(), item.headingColor))
        binding.txtDescription.setTextColor(ContextCompat.getColor(requireActivity(), item.descriptionColor))
        binding.btnNext.setTextColor(ContextCompat.getColor(requireActivity(), item.nextColor))

        binding.txtHeading.text = item.heading
        binding.txtDescription.text = item.description

        binding.btnNext.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 1
        }

        /*binding.btnNextDup.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 1
        }*/
    }

    private fun loadAdmobWTThreeInterstitial() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_INTERSTITIAL_LETS_START")
        if (adId != null) {
            AdMobInterstitialInside.checkAndLoadAdMobInterstitial(
                context = requireActivity(),
                nameFragment = "WALKTHROUGH_3",
                adId = adId,
                onAdLoadedCallAdmob = {
                    Log.i("SOT_ADS_TAG","Admob: Interstitial : WALKTHROUGH_3 : adLoaded()")
                }
            )
        } else {
            Log.e("SOT_ADS_TAG","Admob: Interstitial ad ID not found for WALKTHROUGH_3")
        }
    }
    private fun loadMintegralWTThreeInterstitial() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_INTERSTITIAL_LETS_START")?.split("-")?.size == 2) {
            MintegralInterstitialInside.checkAndLoadMintegralInterstitial(
                context = requireActivity(),
                nameFragment = "WALKTHROUGH_3",
                placementId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_INTERSTITIAL_LETS_START").split("-")[0],
                unitId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_INTERSTITIAL_LETS_START").split("-")[1],
                onAdLoadedCallback = {
                    Log.i("SOT_ADS_TAG","Mintegral: Interstitial : WALKTHROUGH_3 : adLoaded()")
                }
            )
        } else {
            Log.e("SOT_ADS_TAG","Mintegral: Interstitial ad ID not found for WALKTHROUGH_3")
        }
    }

    private fun loadAdmobWTTwoNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_2")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_2",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG","Admob ad ID not found for ADMOB_NATIVE_WALKTHROUGH_2")
        }
    }
    private fun loadMetaWTTwoNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("META_NATIVE_WALKTHROUGH_2")
        if (adId != null) {
            MetaNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_2",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG","Meta ad ID not found for WALKTHROUGH_2")
        }
    }
    private fun loadMintegralWTTwoBanner() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_WALKTHROUGH_2")?.split("-")?.size == 2) {
            MintegralBannerAdManager.requestBannerAd(
                activity = requireActivity(),
                placementId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_2").split("-")[0],
                unitId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_2").split("-")[1],
                adName = "WALKTHROUGH_2",
                populateView = false)
        } else {
            Log.e("SOT_ADS_TAG","BANNER : Mintegral : MAY WALKTHROUGH_2 Incorrect ID Format (placementID-unitID)")
        }
    }

    private fun loadMintegralWTFullBanner() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_WALKTHROUGH_FULLSCR")?.split("-")?.size == 2) {
            MintegralBannerFullScreen.requestBannerAd(
                activity = requireActivity(),
                placementId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_FULLSCR").split("-")[0],
                unitId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_FULLSCR").split("-")[1],
                adName = "WALKTHROUGH_FULL_SCREEN",
                populateView = false)
        } else {
            Log.e("SOT_ADS_TAG","BANNER : Mintegral : MAY WT_FULL_Incorrect ID Format (placementID-unitID)")
        }
    }
    private fun loadAdmobWTFullNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_FULLSCR")
        if (adId != null) {
            AdmobNativeAdFullScreen.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_FULL_SCREEN",
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG","Admob ad ID not found for WALKTHROUGH_FULL_SCREEN")
        }
    }

    override fun onResume() {
        super.onResume()
        if (!NetworkCheck.isNetworkAvailable(context)) {
            binding.glOne.setGuidelinePercent(0.8f)
            binding.nativeAdContainerAd.visibility = View.GONE
        }
        /*if (NetworkCheck.isNetworkAvailable(context)) {
            binding.glOne.setGuidelinePercent(0.35f)
            binding.glTwo.setGuidelinePercent(0.5f)
            binding.cl2.visibility = View.VISIBLE
            binding.cl2Dup.visibility = View.GONE
        } else {
            binding.glOne.setGuidelinePercent(0.45f)
            binding.glTwo.setGuidelinePercent(0.8f)
            binding.cl2Dup.visibility = View.VISIBLE
            binding.cl2.visibility = View.GONE
        }*/

        val nativeWalkThrough1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_1") as? Boolean ?: false
        if (nativeWalkThrough1Enabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_1_MED")) {
                "ADMOB" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showAdmobWTOneNatives()
                }
                "META" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showMetaWTOneNatives()
                }
                "MINTEGRAL" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showMintegralWTOneBanner()
                }
            }
        } else {
            binding.nativeAdContainerAd.visibility = View.GONE
        }
    }

    private fun showMetaWTOneNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("META_NATIVE_WALKTHROUGH_1")?.let { adId ->
            MetaNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_1",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_1").toString().toBoolean(),
                populateView = true,
                nativeAdLayout = binding.nativeAdContainerAd,
                onAdFailed = {
                    binding.nativeAdContainerAd.visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_1: Meta: onAdFailed()")
                },
                onAdLoaded = {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_1: Meta: onAdLoaded()")
                }
            )
        } ?: Log.w("WTOneFragment", "META_NATIVE_WALKTHROUGH_1 ad ID is missing.")
    }
    private fun showAdmobWTOneNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_1")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_1",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_1").toString().toBoolean(),
                populateView = true,
                adContainer = binding.nativeAdContainerAd,
                onAdFailed = {
                    binding.nativeAdContainerAd.visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_1: Admob: onAdFailed()")
                },
                onAdLoaded = {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_1: Admob: onAdLoaded()")
                }
            )
        } ?: Log.w("WTOneFragment", "ADMOB_NATIVE_WALKTHROUGH_1 ad ID is missing.")
    }
    private fun showMintegralWTOneBanner() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_WALKTHROUGH_1")?.split("-")?.size == 2) {
            MintegralBannerAdManager.requestBannerAd(
                activity = requireActivity(),
                placementId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_1").split("-")[0],
                unitId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_1").split("-")[1],
                adName = "WALKTHROUGH_1",
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_1").toString().toBoolean(),
                populateView = true,
                bannerContainer = binding.bannerAdMint,
                shimmerContainer = binding.shimmerLayout,
                onAdFailed = {
//                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_1: MINTEGRAL: onAdFailed()")
                },
                onAdLoaded = {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.INVISIBLE
                    binding.bannerAdMint.visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_1: MINTEGRAL: onAdLoaded()")
                }
            )
        } else {
            Log.i("SOT_ADS_TAG", "BANNER : Mintegral : MAY WALKTHROUGH_1 Incorrect ID Format (placementID-unitID)")
        }
    }
}