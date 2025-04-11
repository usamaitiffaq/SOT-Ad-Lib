package com.manual.mediation.library.sotadlib.callingClasses

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.manual.mediation.library.sotadlib.activities.WalkThroughConfigActivity
import com.manual.mediation.library.sotadlib.data.WalkThroughItem

class WalkThroughScreensConfiguration private constructor() {

    private lateinit var activityContext: Activity
    lateinit var walkThroughList: ArrayList<WalkThroughItem>

    object SotAdsConfigurationHolder {
        var walkThroughConfig: WalkThroughScreensConfiguration? = null
    }

    fun walkThroughInitializationSetup() {
        Log.i("WalkThroughScreensConfiguration","WalkThrough: walkThroughInitializationSetup()")
        activityContext.startActivity(Intent(activityContext, WalkThroughConfigActivity::class.java))
        activityContext.finish()
        activityContext.overridePendingTransition(0,0)
    }

    class Builder {
        private lateinit var activity: Activity
        private var walkThroughList: ArrayList<WalkThroughItem>? = null

        fun setActivityContext(myActivity: Activity) = apply {
            this.activity = myActivity
        }

        fun setWalkThroughContent(walkThroughList: ArrayList<WalkThroughItem>) = apply {
            this.walkThroughList = walkThroughList
        }

        fun build(): WalkThroughScreensConfiguration {
            if (!::activity.isInitialized) {
                throw IllegalStateException("Activity context must be provided")
            }
            if (walkThroughList == null) {
                throw IllegalStateException("View must be initialized")
            }
            val walkThroughScreenAdsConfig = WalkThroughScreensConfiguration()
            walkThroughScreenAdsConfig.activityContext = activity
            walkThroughScreenAdsConfig.walkThroughList = ArrayList()
            walkThroughScreenAdsConfig.walkThroughList.addAll(walkThroughList!!)
            return walkThroughScreenAdsConfig
        }
    }
}