package com.manual.mediation.library.sotadlib.activities

import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.manual.mediation.library.sotadlib.R
import com.manual.mediation.library.sotadlib.adapters.WalkThroughAdapter
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.databinding.ActivityWalkThroughConfigBinding
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.manual.mediation.library.sotadlib.utils.hideSystemUI

class WalkThroughConfigActivity : AppCompatBaseActivity() {

    lateinit var binding: ActivityWalkThroughConfigBinding
    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    private lateinit var viewPager: ViewPager2
    private var previousPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        hideSystemUI()
        binding = ActivityWalkThroughConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        viewPager = findViewById(R.id.viewPager)

        val myNoOfFrag = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_FULLSCR")
        val noOfFragment = if (NetworkCheck.isNetworkAvailable(this) && myNoOfFrag == true) {
            4
        } else {
            3
        }

        viewPager.adapter = WalkThroughAdapter(fragmentActivity = this, sotAdsConfigurations?.walkThroughScreensConfiguration?.walkThroughList!!, noOfFragment)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (previousPosition != -1) {
                    when (previousPosition) {
                        0 -> if (position == 1) {
                            Log.i("WalkThroughConfigActivity","previousPosition: $previousPosition :: $position")
                        }
                        1 -> if (position == 2) {
                            Log.i("WalkThroughConfigActivity","previousPosition: $previousPosition :: $position")
                        } else if (position == 0) {
                            Log.i("WalkThroughConfigActivity","previousPosition: $previousPosition :: $position")
                        }
                        2 -> if (position == 1) {
                            Log.i("WalkThroughConfigActivity","previousPosition: $previousPosition :: $position")
                        }
                    }
                }
                previousPosition = position
            }
        })
    }
}