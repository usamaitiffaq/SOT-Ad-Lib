package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import com.manual.mediation.library.sotadlib.databinding.FragmentWTTwoBinding
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralBannerAdManager
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WTTwoFragment : Fragment() {
    private lateinit var binding: FragmentWTTwoBinding
    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    private lateinit var item: WalkThroughItem

    companion object {
        private const val ARG_ITEM = "walkThroughItem"

        fun newInstance(item: WalkThroughItem): WTTwoFragment {
            return WTTwoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ITEM, item)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments?.getParcelable(ARG_ITEM) ?: throw IllegalStateException("WalkThroughItem must be provided")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWTTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        Log.i("SOTStartTestActivity", "walkthrough2_scr")
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Glide.with(requireActivity())
                    .asDrawable()
                    .load(item.drawableResId)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(true)
                    .into(binding.main)
            }
        }
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Glide.with(requireActivity())
                    .asDrawable()
                    .load(item.drawableBubbleResId)
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

        if (!NetworkCheck.isNetworkAvailable(context)) {
            binding.glOne.setGuidelinePercent(0.8f)
        }
        binding.btnNext.setOnClickListener {
            Log.i("SOTStartTestActivity", "walkthrough2_scr_tap_next")

            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 2
        }

        val nativeWalkThrough3Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_3") as? Boolean ?: false
        if (nativeWalkThrough3Enabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_3_MED")) {
                "ADMOB" -> loadAdmobWTThreeNatives()
                "META" -> loadMetaWTThreeNatives()
                "MINTEGRAL" -> loadMintegralWTThreeBanner()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!NetworkCheck.isNetworkAvailable(context)) {
            binding.glOne.setGuidelinePercent(0.8f)
            binding.nativeAdContainerAd.visibility = View.GONE
        }

        val nativeWalkThrough1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_2") as? Boolean ?: false
        if (nativeWalkThrough1Enabled) {
            when (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_2_MED")) {
                "ADMOB" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showAdmobWTTwoNatives()
                }
                "META" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showMetaWTTwoNatives()
                }
                "MINTEGRAL" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showMintegralWTTwoBanner()
                }
            }
        } else {
            binding.nativeAdContainerAd.visibility = View.GONE
        }
    }

    private fun loadAdmobWTThreeNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_3")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_3",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } ?: Log.i("WTTwoFragment","ADMOB_NATIVE_WALKTHROUGH_3 ad ID is missing.")
    }
    private fun loadMetaWTThreeNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("META_NATIVE_WALKTHROUGH_3")?.let { adId ->
            MetaNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_3",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } ?: Log.i("WTTwoFragment","META_NATIVE_WALKTHROUGH_3 ad ID is missing.")
    }
    private fun loadMintegralWTThreeBanner() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_WALKTHROUGH_3")?.split("-")?.size == 2) {
            MintegralBannerAdManager.requestBannerAd(
                activity = requireActivity(),
                placementId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_3").split("-")[0],
                unitId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_3").split("-")[1],
                adName = "WALKTHROUGH_3",
                populateView = false)
        } else {
            Log.e("SOT_ADS_TAG","BANNER : Mintegral : MAY WALKTHROUGH_2 Incorrect ID Format (placementID-unitID)")
        }
    }

    private fun showAdmobWTTwoNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_2")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_2",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_2").toString().toBoolean(),
                populateView = true,
                adContainer = binding.nativeAdContainerAd,
                onAdFailed = {
                    binding.nativeAdContainerAd.visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_2: Admob: onAdFailed()")
                },
                onAdLoaded = {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_2: Admob: onAdLoaded()")
                }
            )
        } ?: Log.w("WTOneFragment", "ADMOB_NATIVE_WALKTHROUGH_2 ad ID is missing.")
    }
    private fun showMetaWTTwoNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("META_NATIVE_WALKTHROUGH_2")?.let { adId ->
            MetaNativeAdManager.requestAd(
                mContext = requireActivity(),
                adId = adId,
                adName = "WALKTHROUGH_2",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_2").toString().toBoolean(),
                populateView = true,
                nativeAdLayout = binding.nativeAdContainerAd,
                onAdFailed = {
                    binding.nativeAdContainerAd.visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_2: Meta: onAdFailed()")
                },
                onAdLoaded = {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_2: Meta: onAdLoaded()")
                }
            )
        } ?: Log.w("WTOneFragment", "META_NATIVE_WALKTHROUGH_2 ad ID is missing.")
    }
    private fun showMintegralWTTwoBanner() {
        if (sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("MINTEGRAL_BANNER_WALKTHROUGH_2")?.split("-")?.size == 2) {
            MintegralBannerAdManager.requestBannerAd(
                activity = requireActivity(),
                placementId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_2").split("-")[0],
                unitId = sotAdsConfigurations!!.firstOpenFlowAdIds.getValue("MINTEGRAL_BANNER_WALKTHROUGH_2").split("-")[1],
                adName = "WALKTHROUGH_2",
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_2").toString().toBoolean(),
                populateView = true,
                bannerContainer = binding.bannerAdMint,
                shimmerContainer = binding.shimmerLayout,
                onAdFailed = {
//                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_2: MINTEGRAL: onAdFailed()")
                },
                onAdLoaded = {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.INVISIBLE
                    binding.bannerAdMint.visibility = View.VISIBLE
                    Log.i("SOT_ADS_TAG", "WALKTHROUGH_2: MINTEGRAL: onAdLoaded()")
                }
            )
        } else {
            Log.i("SOT_ADS_TAG", "BANNER : Mintegral : MAY WALKTHROUGH_2 Incorrect ID Format (placementID-unitID)")
        }
    }
}