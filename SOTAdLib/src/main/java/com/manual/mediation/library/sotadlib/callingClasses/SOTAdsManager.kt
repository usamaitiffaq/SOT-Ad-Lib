package com.manual.mediation.library.sotadlib.callingClasses

import android.view.View
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import java.util.ArrayList

object SOTAdsManager {

    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    private var onFinish: (() -> Unit)? = null
    private var reConfigureBuilders: (() -> Unit)? = null

    fun startFlow(sotAdsConfigurations: SOTAdsConfigurations) {
        if (this.sotAdsConfigurations == null) {
            this.sotAdsConfigurations = sotAdsConfigurations
        }
    }

    fun showWelcomeScreen() {
        sotAdsConfigurations?.startWelcomeScreenConfiguration()
    }

    fun showWelcomeDupScreen() {
        sotAdsConfigurations?.welcomeScreensConfiguration?.showWelcomeTwoScreen()
    }

    fun completeWelcomeScreens() {
        sotAdsConfigurations?.welcomeScreensConfiguration?.endWelcomeTwoScreen()
        sotAdsConfigurations?.startWalkThroughConfiguration()
    }

    fun getConfigurations(): SOTAdsConfigurations? {
        return sotAdsConfigurations
    }

    fun setOnFlowStateListener(reConfigureBuilders: () -> Unit, onFinish: () -> Unit) {
        this.onFinish = onFinish
        this.reConfigureBuilders = reConfigureBuilders
    }

    fun notifyFlowFinished() {
        onFinish?.invoke()
    }

    fun notifyReConfigureBuilders() {
        reConfigureBuilders?.invoke()
    }

    fun refreshStrings(upWelcomeScreen: View, walkThroughList: ArrayList<WalkThroughItem>) {
        sotAdsConfigurations?.welcomeScreensConfiguration?.view = upWelcomeScreen
        sotAdsConfigurations?.walkThroughScreensConfiguration?.walkThroughList?.clear()
        sotAdsConfigurations?.walkThroughScreensConfiguration?.walkThroughList = walkThroughList
    }
}
