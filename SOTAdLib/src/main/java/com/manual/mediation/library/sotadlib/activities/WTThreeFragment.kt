package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import com.manual.mediation.library.sotadlib.databinding.FragmentWTThreeBinding
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.manual.mediation.library.sotadlib.utils.PrefHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WTThreeFragment(val item: WalkThroughItem) : Fragment() {

    lateinit var binding: FragmentWTThreeBinding
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWTThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()

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
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Glide.with(requireActivity())
                    .asDrawable()
                    .load(item.drawableBubble)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(true)
                    .into(binding.bubbleDup)
            }
        }

        binding.txtHeading.text = item.heading
        binding.txtDescription.text = item.description

        binding.btnNext.setOnClickListener {
            PrefHelper(requireContext()).putBoolean("StartScreens", value = true)
            SOTAdsManager.notifyFlowFinished()
        }

        binding.btnNextDup.setOnClickListener {
            PrefHelper(requireContext()).putBoolean("StartScreens", value = true)
            SOTAdsManager.notifyFlowFinished()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (NetworkCheck.isNetworkAvailable(context)) {
            binding.glOne.setGuidelinePercent(0.35f)
            binding.glTwo.setGuidelinePercent(0.5f)
            binding.cl2.visibility = View.VISIBLE
            binding.cl2Dup.visibility = View.GONE
        } else {
            binding.glOne.setGuidelinePercent(0.45f)
            binding.glTwo.setGuidelinePercent(0.8f)
            binding.cl2Dup.visibility = View.VISIBLE
            binding.cl2.visibility = View.GONE
        }

//        if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_3") == true) {
            when {
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_3_MED") == "ADMOB" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showAdmobWTThreeNatives()
                }
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_3_MED") == "META" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showMetaWTThreeNatives()
                }
            }
//        }
    }

    override fun onStop() {
        super.onStop()
        Log.e("reloadAdAfterClickOrResume", "onStop: ")
    }

    private fun showMetaWTThreeNatives() {
        MetaNativeAdManager.requestAd(
            mContext = requireActivity(),
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_WALKTHROUGH_3"),
            adName = "WALKTHROUGH_3",
            isMedia = true,
            isMediumAd = true,
            remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_3").toString().toBoolean(),
            populateView = true,
            nativeAdLayout = binding.nativeAdContainerAd,
            onAdFailed = {
                binding.nativeAdContainerAd.visibility = View.GONE
                Log.i("SOT_ADS_TAG", "WALKTHROUGH_3: Meta: onAdFailed()")
            },
            onAdLoaded = {
                binding.nativeAdContainerAd.visibility = View.VISIBLE
                Log.i("SOT_ADS_TAG", "WALKTHROUGH_3: Meta: onAdLoaded()")
            }
        )
    }

    private fun showAdmobWTThreeNatives() {
        AdmobNativeAdManager.requestAd(
            mContext = requireActivity(),
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_WALKTHROUGH_3"),
            adName = "WALKTHROUGH_3",
            isMedia = true,
            isMediumAd = true,
            remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_3").toString().toBoolean(),
            populateView = true,
            adContainer = binding.nativeAdContainerAd,
            onAdFailed = {
                binding.nativeAdContainerAd.visibility = View.GONE
                Log.i("SOT_ADS_TAG", "WALKTHROUGH_3: Admob: onAdFailed()")
            },
            onAdLoaded = {
                binding.nativeAdContainerAd.visibility = View.VISIBLE
                Log.i("SOT_ADS_TAG", "WALKTHROUGH_3: Admob: onAdLoaded()")
            }
        )
    }
}