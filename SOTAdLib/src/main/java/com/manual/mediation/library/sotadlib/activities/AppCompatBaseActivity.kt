package com.manual.mediation.library.sotadlib.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.manual.mediation.library.sotadlib.utils.MyLocaleHelper

open class AppCompatBaseActivity : AppCompatActivity() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(MyLocaleHelper.onAttach(base, "en"))
        val config = applicationContext.resources.configuration
        applicationContext.resources.updateConfiguration(config, applicationContext.resources.displayMetrics)
    }
}