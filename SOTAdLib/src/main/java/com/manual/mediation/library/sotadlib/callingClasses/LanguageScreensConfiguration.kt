package com.manual.mediation.library.sotadlib.callingClasses

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import com.manual.mediation.library.sotadlib.activities.LanguageScreenOne
import com.manual.mediation.library.sotadlib.data.Language
import com.manual.mediation.library.sotadlib.interfaces.LanguageInterface

class LanguageScreensConfiguration private constructor() {

    private lateinit var activityContext: Activity
    private var languageInterface: LanguageInterface? = null
    var languageList: ArrayList<Language>? = null

    var selectedDrawable: Drawable? = null
    var unSelectedDrawable: Drawable? = null

    var selectedRadio: Drawable? = null
    var unSelectedRadio: Drawable? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        var languageInstance: LanguageScreensConfiguration? = null
    }

    fun languageInitializationSetup() {
        Log.i("LanguageScreensConfiguration", "Language: languageInitializationSetup()")
        activityContext.startActivity(Intent(activityContext, LanguageScreenOne::class.java))
        activityContext.finish()
    }

    fun setLanguageInterface(languageInterface: LanguageInterface?) {
        this.languageInterface = languageInterface
    }

    fun showLanguageTwoScreen() {
        Log.i("SOTStartTestActivity", "language1_scr_tap_language")
        languageInterface?.showLanguageTwoScreen()
    }

    class Builder {
        private lateinit var activity: Activity
        private var languageList: ArrayList<Language>? = null

        private var selectedDrawable: Drawable? = null
        private var unSelectedDrawable: Drawable? = null
        private var selectedRadio: Drawable? = null
        private var unSelectedRadio: Drawable? = null

        fun setActivityContext(myActivity: Activity) = apply {
            this.activity = myActivity
        }

        fun setDrawableColors(selectedDrawable: Drawable, unSelectedDrawable: Drawable, selectedRadio: Drawable, unSelectedRadio: Drawable) = apply {
            this.selectedDrawable = selectedDrawable
            this.unSelectedDrawable = unSelectedDrawable

            this.selectedRadio = selectedRadio
            this.unSelectedRadio = unSelectedRadio
        }

        fun setLanguages(languageList: ArrayList<Language>) = apply {
            this.languageList = languageList
        }

        fun build(): LanguageScreensConfiguration {
            if (!::activity.isInitialized) {
                throw IllegalStateException("Activity context must be provided")
            }
            val languageScreensConfiguration = LanguageScreensConfiguration()
            languageScreensConfiguration.activityContext = activity
            languageScreensConfiguration.languageList = languageList!!

            languageScreensConfiguration.selectedDrawable = selectedDrawable!!
            languageScreensConfiguration.unSelectedDrawable = unSelectedDrawable!!
            languageScreensConfiguration.selectedRadio = selectedRadio!!
            languageScreensConfiguration.unSelectedRadio = unSelectedRadio!!
            languageInstance = languageScreensConfiguration
            return languageScreensConfiguration
        }
    }
}