package com.manual.mediation.library.sotadlib.adMobAdClasses

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.manual.mediation.library.sotadlib.BuildConfig
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.utils.AdLoadingDialog
import com.manual.mediation.library.sotadlib.utils.NetworkCheck

class AdmobInterstitialAdSplash(
    activity: Activity? = null,
    val adId: String,
    onAdDismissed: (() -> Unit)? = null,
    onAdFailed: (() -> Unit)? = null,
    onAdTimeout: (() -> Unit)? = null,
    onAdShowed: (() -> Unit)? = null
) {
    private var interstitialAd: InterstitialAd? = null
    private var currentActivity: Activity? = activity
    private var isShowingAd = false
    private var isShowDialog = true
    private var isShowingDialog = false
    private val timeoutHandler = Handler(Looper.getMainLooper())

    private val timeoutRunnable = Runnable {
        if (interstitialAd == null) {
            onAdTimeout?.invoke()
            dismissWaitDialog()
            Log.i("SOT_ADS_TAG", "Admob: Interstitial : Timeout()")
            currentActivity?.let {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(it, "Admob: Interstitial : Timeout", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    init {
        currentActivity?.let {
            fetchAd(onAdDismissed, onAdFailed, onAdShowed)
        }
    }

    private fun fetchAd(
        onAdDismissed: (() -> Unit)? = null,
        onAdFailed: (() -> Unit)? = null,
        onAdShowed: (() -> Unit)? = null
    ) {
        if (isAdAvailable()) return

        if (!NetworkCheck.isNetworkAvailable(currentActivity)) {
            return
        }

        currentActivity?.let {
            if (BuildConfig.DEBUG) {
                Toast.makeText(currentActivity, "Admob: Interstitial : Request", Toast.LENGTH_SHORT).show()
            }
        }
        val adLoadCallback = object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                Log.i("SOT_ADS_TAG", "Admob: Interstitial : onAdLoaded()")
                interstitialAd = ad
                showAdIfAvailable(onAdDismissed, onAdFailed, onAdShowed)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.i("SOT_ADS_TAG", "Admob: Interstitial : onAdFailedToLoad()")
                timeoutHandler.removeCallbacks(timeoutRunnable)
                dismissWaitDialog()
                onAdFailed?.invoke()
                currentActivity?.let {
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(currentActivity, "Admob: Interstitial : Failed To Load", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(currentActivity!!, adId, adRequest, adLoadCallback)
        timeoutHandler.postDelayed(timeoutRunnable, 20000)
    }

    private fun showAdIfAvailable(
        onAdDismissed: (() -> Unit)? = null,
        onAdFailed: (() -> Unit)? = null,
        onAdShowed: (() -> Unit)? = null
    ) {
        if (!isShowingAd && isAdAvailable()) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.i("SOT_ADS_TAG", "Admob: Interstitial : onAdDismissed()")
                    isShowingAd = false
                    timeoutHandler.removeCallbacks(timeoutRunnable)
                    dismissWaitDialog()
                    onAdDismissed?.invoke()
                    interstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.i("SOT_ADS_TAG", "Admob: Interstitial : onAdFailedToShow()")
                    isShowingAd = false
                    timeoutHandler.removeCallbacks(timeoutRunnable)
                    dismissWaitDialog()
                    onAdFailed?.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.i("SOT_ADS_TAG", "Admob: Interstitial : onAdShowed()")
                    isShowingAd = true
                    timeoutHandler.removeCallbacks(timeoutRunnable)
                    dismissWaitDialog()
                    onAdShowed?.invoke()
                }
            }

            showWaitDialog()

            Handler(Looper.getMainLooper()).postDelayed({
                interstitialAd?.show(currentActivity!!)
            }, 1500)
        }
    }

    private fun isAdAvailable(): Boolean {
        return interstitialAd != null
    }

    private fun showWaitDialog() {
        if (isShowDialog) {
            currentActivity?.let {
                val view = it.layoutInflater.inflate(R.layout.dialog_adloading, null, false)
                isShowingDialog = true
                AdLoadingDialog.setContentView(it, view = view, isCancelable = false).showDialogInterstitial()
            }
        }
    }

    private fun dismissWaitDialog() {
        if (isShowingDialog) {
            currentActivity?.let {
                AdLoadingDialog.dismissDialog(it)
            }
            isShowingDialog = false
        }
    }
}