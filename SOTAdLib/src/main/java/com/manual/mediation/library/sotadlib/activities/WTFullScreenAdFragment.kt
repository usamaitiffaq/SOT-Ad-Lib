package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdFullScreen
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.databinding.FragmentWalkThroughFullScreenAdmobBinding
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralBannerFullScreen

class WTFullScreenAdFragment : Fragment() {

    private lateinit var binding: FragmentWalkThroughFullScreenAdmobBinding
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    private var handler: Handler? = null
    private lateinit var showCloseButtonRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalkThroughFullScreenAdmobBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        Log.i("SOTStartTestActivity", "walkthrough_fullscr")
        showCloseButtonRunnable = Runnable {
            binding.ivClose.visibility = View.VISIBLE
        }

        binding.ivClose.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 3
            binding.ivClose.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        binding.ivClose.visibility = View.GONE
        handler?.removeCallbacks(showCloseButtonRunnable)
        handler = null
    }

    override fun onResume() {
        super.onResume()
        binding.ivClose.visibility = View.GONE
        handler = Handler(Looper.getMainLooper())
        val myTime = 1000 * (sotAdsConfigurations?.getRemoteConfigData()?.get("TIMER_NATIVE_F_SRC")
            .toString().toLong() as? Long ?: 3)
        if (myTime in 1000..10000) {
            handler?.postDelayed(showCloseButtonRunnable, myTime)
        } else {
            binding.ivClose.visibility = View.GONE
        }

        val nativeWalkThroughFullScrEnabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_FULLSCR") as? Boolean ?: false
        if (nativeWalkThroughFullScrEnabled) {
            binding.shimmerLayoutF.root.visibility = View.VISIBLE
            when (sotAdsConfigurations?.getRemoteConfigData()
                ?.get("NATIVE_WALKTHROUGH_FULLSCR_MED")) {
                "ADMOB" -> {
                    showAdmobWTFullNatives()
                }

                "MINTEGRAL" -> {
                    showMintegralWTFullBanner()
                }
            }
        } else {
            binding.nativeAdContainer.visibility = View.GONE
        }
    }

    private fun showAdmobWTFullNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_FULLSCR")
            ?.let { adId ->
                AdmobNativeAdFullScreen.requestAd(
                    mContext = requireActivity(),
                    adId = adId,
                    adName = "WALKTHROUGH_FULL_SCREEN",
                    remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_FULLSCR").toString().toBoolean(),
                    populateView = true,
                    adContainer = binding.nativeAdContainer,
                    onAdFailed = {
                        binding.nativeAdViewAdmob.visibility = View.GONE
                        binding.ivClose.performClick()
                        Log.i("SOT_ADS_TAG", "WALKTHROUGH_FULL_SCREEN: Admob: onAdFailed()")
                    },
                    onAdLoaded = {
                        binding.shimmerLayoutF.root.visibility = View.GONE
                        binding.nativeAdViewAdmob.visibility = View.VISIBLE
                        Log.i("SOT_ADS_TAG", "WALKTHROUGH_FULL_SCREEN: Admob: onAdLoaded()")
                    }
                )
            } ?: Log.w("WTOneFragment", "ADMOB_NATIVE_WALKTHROUGH_FULL_SCREEN ad ID is missing.")
    }

    private fun showMintegralWTFullBanner() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_WALKTHROUGH_FULLSCR")?.split("-")?.size == 2) {
            MintegralBannerFullScreen.requestBannerAd(
                activity = requireActivity(),
                placementId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_FULLSCR").split("-")[0],
                unitId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_FULLSCR").split("-")[1],
                adName = "WALKTHROUGH_FULL_SCREEN",
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_FULLSCR").toString().toBoolean(),
                populateView = true,
                bannerContainer = binding.bannerAdF,
                shimmerContainer = binding.shimmerLayoutF.root,
                onAdFailed = {
                    binding.bannerAdF.visibility = View.GONE
                    binding.ivClose.performClick()
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_FULL_SCREEN: MINTEGRAL: onAdFailed()")
                },
                onAdLoaded = {
                    binding.shimmerLayoutF.root.visibility = View.GONE
                    binding.bannerAdF.visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_FULL_SCREEN: MINTEGRAL: onAdLoaded()")
                }
            )
        } else {
            Log.i("SOT_ADS_TAG", "BANNER : Mintegral : MAY WT_FULL_ Incorrect ID Format (placementID-unitID)")
        }
    }
}