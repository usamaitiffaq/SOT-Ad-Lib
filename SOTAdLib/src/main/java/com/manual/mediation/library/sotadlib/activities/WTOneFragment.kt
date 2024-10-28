package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import com.manual.mediation.library.sotadlib.databinding.FragmentWTOneBinding
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WTOneFragment(val item: WalkThroughItem) : Fragment() {

    lateinit var binding: FragmentWTOneBinding
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWTOneBinding.inflate(inflater, container, false)
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
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 1
        }

        binding.btnNextDup.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 1
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

        if (sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_1") == true) {
            when {
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_1_MED") == "ADMOB" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showAdmobWTOneNatives()
                }
                sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_WALKTHROUGH_1_MED") == "META" -> {
                    binding.nativeAdContainerAd.visibility = View.VISIBLE
                    showMetaWTOneNatives()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.e("reloadAdAfterClickOrResume", "onStop: ")
    }

    private fun showMetaWTOneNatives() {
        MetaNativeAdManager.requestAd(
            mContext = requireActivity(),
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("META_NATIVE_WALKTHROUGH_1"),
            adName = "WALKTHROUGH_1",
            isMedia = true,
            isMediumAd = true,
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
    }

    private fun showAdmobWTOneNatives() {
        AdmobNativeAdManager.requestAd(
            mContext = requireActivity(),
            adId = sotAdsConfigurations?.firstOpenFlowAdIds!!.getValue("ADMOB_NATIVE_WALKTHROUGH_1"),
            adName = "WALKTHROUGH_1",
            isMedia = true,
            isMediumAd = true,
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
    }
}