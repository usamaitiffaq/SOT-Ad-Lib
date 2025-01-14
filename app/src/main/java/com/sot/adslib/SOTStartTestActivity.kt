package com.sot.adslib

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import com.manual.mediation.library.sotadlib.activities.AppCompatBaseActivity
import com.manual.mediation.library.sotadlib.adMobAdClasses.AdMobBannerAdSplash
import com.manual.mediation.library.sotadlib.callingClasses.LanguageScreensConfiguration
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager
import com.manual.mediation.library.sotadlib.callingClasses.WalkThroughScreensConfiguration
import com.manual.mediation.library.sotadlib.callingClasses.WelcomeScreensConfiguration
import com.manual.mediation.library.sotadlib.data.Language
import com.manual.mediation.library.sotadlib.data.WalkThroughItem
import com.manual.mediation.library.sotadlib.metaAdClasses.MetaBannerAdSplash
import com.manual.mediation.library.sotadlib.mintegralAdClasses.MintegralBannerAdSplash
import com.manual.mediation.library.sotadlib.unityAdClasses.UnityBannerAdSplash
import com.manual.mediation.library.sotadlib.utils.MyLocaleHelper
import com.manual.mediation.library.sotadlib.utils.NetworkCheck
import com.manual.mediation.library.sotadlib.utils.PrefHelper
import com.manual.mediation.library.sotadlib.utils.hideSystemUIUpdated
import com.manual.mediation.library.sotadlib.utilsGoogleAdsConsent.ConsentConfigurations
import com.sot.adslib.databinding.ActivitySotstartTestBinding
import com.urdu_keyboard.utilityClasses.RemoteConfigConstTest

class SOTStartTestActivity : AppCompatBaseActivity() {

    private lateinit var binding: ActivitySotstartTestBinding
    private lateinit var sotAdsConfigurations: SOTAdsConfigurations
    private var firstOpenFlowAdIds: HashMap<String, String> = HashMap()
    private var isDuplicateScreenStarted = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySotstartTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUIUpdated()
        startFirstOpenFlow()
    }

    private fun startFirstOpenFlow() {
        Log.i("SOTStartTestActivity", "sot_adlib_start_scr")
        firstOpenFlowAdIds.apply {
            this["ADMOB_SPLASH_INTERSTITIAL"] = "ca-app-pub-3940256099942544/1033173712"
            this["ADMOB_SPLASH_RESUME"] = "ca-app-pub-3940256099942544/9257395921"
            this["ADMOB_BANNER_SPLASH"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_LANGUAGE_1"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_LANGUAGE_2"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_SURVEY_1"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_SURVEY_2"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_WALKTHROUGH_1"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_WALKTHROUGH_2"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_WALKTHROUGH_FULLSCR"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_WALKTHROUGH_3"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_INTERSTITIAL_LETS_START"] = "ca-app-pub-3940256099942544/1033173712"

            this["META_SPLASH_INTERSTITIAL"] = ""
            this["META_SPLASH_RESUME"] = ""
            this["META_BANNER_SPLASH"] = ""
            this["META_NATIVE_LANGUAGE_1"] = ""
            this["META_NATIVE_LANGUAGE_2"] = ""
            this["META_NATIVE_SURVEY_1"] = ""
            this["META_NATIVE_SURVEY_2"] = ""
            this["META_NATIVE_WALKTHROUGH_1"] = ""
            this["META_NATIVE_WALKTHROUGH_2"] = ""
            this["META_NATIVE_WALKTHROUGH_FULLSCR"] = ""
            this["META_NATIVE_WALKTHROUGH_3"] = ""
            this["META_INTERSTITIAL_LETS_START"] = ""

            // Ad PlacementID-UnitID
            this["MINTEGRAL_SPLASH_INTERSTITIAL"] = "290653-462374"
            this["MINTEGRAL_SPLASH_RESUME"] = "328916-1542060"
            this["MINTEGRAL_BANNER_SPLASH"] = "1010694-2677210"
            this["MINTEGRAL_BANNER_LANGUAGE_1"] = "1010694-2677210"
            this["MINTEGRAL_BANNER_LANGUAGE_2"] = "1010694-2677210"
            this["MINTEGRAL_BANNER_SURVEY_1"] = "1010694-2677210"
            this["MINTEGRAL_BANNER_SURVEY_2"] = "1010694-2677210"
            this["MINTEGRAL_BANNER_WALKTHROUGH_1"] = "1010694-2677210"
            this["MINTEGRAL_BANNER_WALKTHROUGH_2"] = "1010694-2677210"
            this["MINTEGRAL_BANNER_WALKTHROUGH_FULLSCR"] = "1010694-2677210"
            this["MINTEGRAL_BANNER_WALKTHROUGH_3"] = "1010694-2677210"
            this["MINTEGRAL_INTERSTITIAL_LETS_START"] = "290653-462374"
        }

        SOTAdsManager.setOnFlowStateListener(
            reConfigureBuilders = {
                SOTAdsManager.refreshStrings(setUpWelcomeScreen(this), getWalkThroughList(this))
            },
            onFinish = {
                Log.i("SOTStartTestActivity", "sot_adlib_end_scr")
                gotoMainActivity()
            }
        )

        val consentConfig = ConsentConfigurations.Builder()
            /*.setVungleInitializationId("66dff077655237cdd139d49a")*/
            .setApplicationContext(application)
            .setMintegralInitializationId(appId = "144002", appKey = "7c22942b749fe6a6e361b675e96b3ee9")
            .setUnityInitializationId(gameId = "1234567", testMode = true)
            .setActivityContext(this)
            .setTestDeviceHashedIdList(
                arrayListOf(
                    "09DD12A6DB3BBF9B55D65FAA9FD7D8E0",
                    "3F8FB4EE64D851EDBA704E705EC63A62",
                    "84C3994693FB491110A5A4AEF8C5561B",
                    "CB2F3812ACAA2A3D8C0B31682E1473EB",
                    "F02B044F22C917805C3DF6E99D3B8800"
                )
            )
            .setOnConsentGatheredCallback {
                Log.i("ConsentMessage", "SOTStartActivity: setOnConsentGatheredCallback")
                fetchAdIDS(
                    remoteConfigOperationsCompleted = {
                        sotAdsConfigurations.setRemoteConfigData(
                            activityContext = this@SOTStartTestActivity,
                            myRemoteConfigData = it
                        )

                        if (NetworkCheck.isNetworkAvailable(this)) {
                            if (it.getValue(RemoteConfigConstTest.BANNER_SPLASH) == true) {
                                binding.bannerAd.visibility = View.VISIBLE
                                when {
                                    it.getValue(RemoteConfigConstTest.BANNER_SPLASH_MED) == "ADMOB" -> {
                                        loadAdmobBannerAd()
                                    }
                                    it.getValue(RemoteConfigConstTest.BANNER_SPLASH_MED) == "META" -> {
                                        loadMetaBannerAd()
                                    }
                                    it.getValue(RemoteConfigConstTest.BANNER_SPLASH_MED) == "MINTEGRAL" -> {
                                        loadMintegralBannerAd()
                                    }
                                    it.getValue(RemoteConfigConstTest.BANNER_SPLASH_MED) == "UNITY" -> {
                                        loadUnityBannerAd()
                                    }
                                }
                            }
                        }
                    }
                )
            }
            .build()

        val welcomeScreensConfiguration = WelcomeScreensConfiguration.Builder()
            .setActivityContext(this)
            .setXMLLayout(setUpWelcomeScreen(this))
            .build()

        val languageScreensConfiguration = LanguageScreensConfiguration.Builder()
            .setActivityContext(this)
            .setDrawableColors(
                selectedDrawable = AppCompatResources.getDrawable(this,
                    com.manual.mediation.library.sotadlib.R.drawable.ad_att_bg)!!,
                unSelectedDrawable = AppCompatResources.getDrawable(
                    this,
                    com.manual.mediation.library.sotadlib.R.drawable.ad_att_bg)!!,
                selectedRadio = AppCompatResources.getDrawable(
                    this,
                    com.manual.mediation.library.sotadlib.R.drawable.ic_done)!!,
                unSelectedRadio = AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_launcher_foreground)!!
            )
            .setLanguages(arrayListOf(Language.Urdu, Language.English, Language.Hindi, Language.French, Language.Dutch, Language.Arabic, Language.German))
            .build()

        val walkThroughScreensConfiguration = WalkThroughScreensConfiguration.Builder()
            .setActivityContext(this)
            .setWalkThroughContent(getWalkThroughList(this))
            .build()

        sotAdsConfigurations = SOTAdsConfigurations.Builder()
            .setFirstOpenFlowAdIds(firstOpenFlowAdIds)
            .setConsentConfig(consentConfig)
            .setLanguageScreenConfiguration(languageScreensConfiguration)
            .setWelcomeScreenConfiguration(welcomeScreensConfiguration)
            .setWalkThroughScreenConfiguration(walkThroughScreensConfiguration)
            .build()

        SOTAdsManager.startFlow(sotAdsConfigurations)
    }

    private fun loadUnityBannerAd() {
        UnityBannerAdSplash.showBannerAds(
            activity = this,
            bannerContainer = binding.bannerAd,
            placementId = "banner")
    }

    private fun loadMintegralBannerAd() {
        MintegralBannerAdSplash(
            activity = this@SOTStartTestActivity,
            placementID = "1010694",
            unitID = "2677210",
            bannerContainer = binding.bannerAd,
            shimmerContainer = binding.bannerShimmerLayout.root,
            onAdFailed = {
                binding.bannerAd.visibility = View.GONE
            },
            onAdLoaded = {
            },
            onAdClicked = {}
        )
    }

    private fun loadAdmobBannerAd() {
        AdMobBannerAdSplash(
            activity = this@SOTStartTestActivity,
            placementID = "ca-app-pub-3940256099942544/9214589741",
            bannerContainer = binding.bannerAd,
            shimmerContainer = binding.bannerShimmerLayout.root,
            onAdFailed = {
                binding.bannerAd.visibility = View.GONE
            },
            onAdLoaded = {
            },
            onAdClicked = {}
        )
    }

    private fun loadMetaBannerAd() {
        MetaBannerAdSplash(this@SOTStartTestActivity,
            placementID = "",
            bannerContainer = binding.bannerAd,
            shimmerContainer = binding.bannerShimmerLayout.root,
            onAdFailed = {
                binding.bannerAd.visibility = View.GONE
            },
            onAdLoaded = {
            },
            onAdClicked = {}
        )
    }

    private fun gotoMainActivity() {
        val time = if (PrefHelper(this).getBooleanDefault("StartScreens", default = false)) {
            0
        } else {
            if (NetworkCheck.isNetworkAvailable(this)) {
                0
            } else {
                3000
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, FinalActivity::class.java)
            startActivity(intent)
            finish()
        }, time.toLong())
    }

    private fun setUpWelcomeScreen(context: Context): View {
        val localizedConfig = resources.configuration.apply { MyLocaleHelper.onAttach(context, "en") }
        val localizedContext = ContextWrapper(this).createConfigurationContext(localizedConfig)

        val welcomeScreenView = LayoutInflater.from(localizedContext).inflate(R.layout.layout_welcome_scr_test, null, false)

        val txtWallpapers = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtWallpapers)
        val txtEditor = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtEditor)
        val txtLiveThemes = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtLiveThemes)
        val txtPhotoOnKeyboard = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtPhotoOnKeyboard)
        val txtPhotoTranslator = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtPhotoTranslator)
        val txtInstantSticker = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtInstantSticker)
        val txtLiveTranslator = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtLiveTranslator)
        val txtUrduSticker = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtUrduSticker)

        var txtWallpapersBool = false
        var txtEditorBool = false
        var txtLiveThemesBool = false
        var txtPhotoOnKeyboardBool = false
        var txtPhotoTranslatorBool = false
        var txtInstantStickerBool = false
        var txtLiveTranslatorBool = false
        var txtUrduStickerBool = false

        val nextButton = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtNext)

        txtWallpapers.setOnClickListener {
            Log.i("SOTStartTestActivity", "survey_scr_check_wallpaper")
            if (isDuplicateScreenStarted) {
                SOTAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtWallpapersBool) {
                txtWallpapersBool = false
            } else {
                txtWallpapersBool = true
            }
        }
        txtEditor.setOnClickListener {
            Log.i("SOTStartTestActivity", "survey_scr_check_urdu_editor")
            if (isDuplicateScreenStarted) {
                SOTAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtEditorBool) {
                txtEditorBool = false
            } else {
                txtEditorBool = true
            }
        }
        txtLiveThemes.setOnClickListener {
            Log.i("SOTStartTestActivity", "survey_scr_check_live_themes")
            if (isDuplicateScreenStarted) {
                SOTAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtLiveThemesBool) {
                txtLiveThemesBool = false
            } else {
                txtLiveThemesBool = true
            }
        }
        txtPhotoOnKeyboard.setOnClickListener {
            Log.i("SOTStartTestActivity", "survey_scr_check_photo_on_keyboard")
            if (isDuplicateScreenStarted) {
                SOTAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtPhotoOnKeyboardBool) {
                txtPhotoOnKeyboardBool = false
            } else {
                txtPhotoOnKeyboardBool = true
            }
        }
        txtPhotoTranslator.setOnClickListener {
            Log.i("SOTStartTestActivity", "survey_scr_check_photo_translator")
            if (isDuplicateScreenStarted) {
                SOTAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtPhotoTranslatorBool) {
                txtPhotoTranslatorBool = false
            } else {
                txtPhotoTranslatorBool = true
            }
        }
        txtInstantSticker.setOnClickListener {
            Log.i("SOTStartTestActivity", "survey_scr_check_instant_stickers")
            if (isDuplicateScreenStarted) {
                SOTAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtInstantStickerBool) {
                txtInstantStickerBool = false
            } else {
                txtInstantStickerBool = true
            }
        }
        txtLiveTranslator.setOnClickListener {
            Log.i("SOTStartTestActivity", "survey_scr_check_live_translator")
            if (isDuplicateScreenStarted) {
                SOTAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtLiveTranslatorBool) {
                txtLiveTranslatorBool = false
            } else {
                txtLiveTranslatorBool = true
            }
        }
        txtUrduSticker.setOnClickListener {
            Log.i("SOTStartTestActivity", "survey_scr_check_urdu_stickers")
            if (isDuplicateScreenStarted) {
                SOTAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtUrduStickerBool) {
                txtUrduStickerBool = false
            } else {
                txtUrduStickerBool = true
            }
        }

        nextButton.setOnClickListener {
            if (txtWallpapersBool || txtEditorBool ||
                txtLiveThemesBool || txtPhotoOnKeyboardBool ||
                txtPhotoTranslatorBool || txtInstantStickerBool ||
                txtLiveTranslatorBool || txtUrduStickerBool
            ) {
                Log.i("SOTStartTestActivity", "survey2_scr")
                Log.i("SOTStartTestActivity", "survey2_scr_tap_continue")
                SOTAdsManager.completeWelcomeScreens()
            } else {
                Log.i("SOTStartTestActivity", "survey1_scr")
                Log.i("SOTStartTestActivity", "survey1_scr_tap_continue")
                if (isDuplicateScreenStarted) {
                    SOTAdsManager.showWelcomeDupScreen()
                }
                isDuplicateScreenStarted = false
                val toast = Toast.makeText(this, "Please check the checkbox", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
        return welcomeScreenView
    }

    private fun getWalkThroughList(context: Context): ArrayList<WalkThroughItem> {
        val localizedContext = ContextWrapper(this).createConfigurationContext(
            resources.configuration.apply { MyLocaleHelper.onAttach(context, "en") }
        )
        return arrayListOf(
            WalkThroughItem(
                heading = "Screen 1",
                description = "This is screen one",
                headingColor = com.manual.mediation.library.sotadlib.R.color.redLib,
                descriptionColor = com.manual.mediation.library.sotadlib.R.color.yellowLib,
                nextColor = com.manual.mediation.library.sotadlib.R.color.orangeLib,
                drawable = AppCompatResources.getDrawable(
                    context,
                    com.manual.mediation.library.sotadlib.R.drawable.pakistan
                ),
                drawableBubble = AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_launcher_foreground
                )
            ),
            WalkThroughItem(
                heading = "Screen 2",
                description = "This is screen two",
                headingColor = com.manual.mediation.library.sotadlib.R.color.redLib,
                descriptionColor = com.manual.mediation.library.sotadlib.R.color.yellowLib,
                nextColor = com.manual.mediation.library.sotadlib.R.color.orangeLib,
                drawable = AppCompatResources.getDrawable(
                    context,
                    com.manual.mediation.library.sotadlib.R.drawable.pakistan
                ),
                drawableBubble = AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_launcher_foreground
                )
            ),
            WalkThroughItem(
                heading = "Screen 3",
                description = "This is screen three",
                headingColor = com.manual.mediation.library.sotadlib.R.color.redLib,
                descriptionColor = com.manual.mediation.library.sotadlib.R.color.yellowLib,
                nextColor = com.manual.mediation.library.sotadlib.R.color.orangeLib,
                drawable = AppCompatResources.getDrawable(
                    context,
                    com.manual.mediation.library.sotadlib.R.drawable.pakistan
                ),
                drawableBubble = AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_launcher_foreground
                )
            )
        )
    }

    private fun fetchAdIDS(remoteConfigOperationsCompleted: (HashMap<String, Any>) -> Unit) {
        if (NetworkCheck.isNetworkAvailable(this@SOTStartTestActivity)) {
            saveAllValues()
            remoteConfigOperationsCompleted.invoke(getSharedPreferencesValues())
        } else {
            remoteConfigOperationsCompleted.invoke(getSharedPreferencesValues())
        }
    }

    private fun saveAllValues() {
        val editor = getSharedPreferences("RemoteConfig", MODE_PRIVATE).edit()
        // SOT-Ads-Visibility-Config
        editor.putString(RemoteConfigConstTest.RESUME_INTER_SPLASH, "INTERSTITIAL")
        editor.putBoolean(RemoteConfigConstTest.BANNER_SPLASH, true)
        editor.putBoolean(RemoteConfigConstTest.RESUME_OVERALL, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_LANGUAGE_1, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_LANGUAGE_2, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_SURVEY_1, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_SURVEY_2, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_1, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_2, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_FULLSCR, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_3, true)
        editor.putBoolean(RemoteConfigConstTest.INTERSTITIAL_LETS_START, true)

        // SOT-Ads-Mediation-Config
        editor.putString(RemoteConfigConstTest.RESUME_INTER_SPLASH_MED, "ADMOB")
        editor.putString(RemoteConfigConstTest.RESUME_OVERALL_MED, "ADMOB")
        editor.putString(RemoteConfigConstTest.BANNER_SPLASH_MED, "ADMOB")
        editor.putString(RemoteConfigConstTest.NATIVE_LANGUAGE_1_MED, "ADMOB")
        editor.putString(RemoteConfigConstTest.NATIVE_LANGUAGE_2_MED, "ADMOB")
        editor.putString(RemoteConfigConstTest.NATIVE_SURVEY_1_MED, "ADMOB")
        editor.putString(RemoteConfigConstTest.NATIVE_SURVEY_2_MED, "MINTEGRAL")
        editor.putString(RemoteConfigConstTest.NATIVE_WALKTHROUGH_1_MED, "MINTEGRAL")
        editor.putString(RemoteConfigConstTest.NATIVE_WALKTHROUGH_2_MED, "ADMOB")
        editor.putString(RemoteConfigConstTest.NATIVE_WALKTHROUGH_FULLSCR_MED, "MINTEGRAL")
        editor.putString(RemoteConfigConstTest.NATIVE_WALKTHROUGH_3_MED, "MINTEGRAL")
        editor.putString(RemoteConfigConstTest.INTERSTITIAL_LETS_START_MED, "MINTEGRAL")

        editor.putString(RemoteConfigConstTest.TIMER_NATIVE_F_SRC, "5")

        editor.apply()
    }

    private fun getSharedPreferencesValues(): HashMap<String, Any> {
        val remoteConfigHashMap: HashMap<String, Any> = HashMap()
        val prefs: SharedPreferences = getSharedPreferences("RemoteConfig", Context.MODE_PRIVATE)

        remoteConfigHashMap.apply {
            this["RESUME_INTER_SPLASH"] = "${prefs.getString(RemoteConfigConstTest.RESUME_INTER_SPLASH, "Empty")}"
            this["BANNER_SPLASH"] = prefs.getBoolean(RemoteConfigConstTest.BANNER_SPLASH, false)
            this["RESUME_OVERALL"] = prefs.getBoolean(RemoteConfigConstTest.RESUME_OVERALL, false)
            this["NATIVE_LANGUAGE_1"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_LANGUAGE_1, false)
            this["NATIVE_LANGUAGE_2"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_LANGUAGE_2, false)
            this["NATIVE_SURVEY_1"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_SURVEY_1, false)
            this["NATIVE_SURVEY_2"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_SURVEY_2, false)
            this["NATIVE_WALKTHROUGH_1"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_1, false)
            this["NATIVE_WALKTHROUGH_2"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_2, false)
            this["NATIVE_WALKTHROUGH_FULLSCR"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_FULLSCR, false)
            this["NATIVE_WALKTHROUGH_3"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_3, false)
            this["INTERSTITIAL_LETS_START"] = prefs.getBoolean(RemoteConfigConstTest.INTERSTITIAL_LETS_START, false)

            this["RESUME_INTER_SPLASH_MED"] = "${prefs.getString(RemoteConfigConstTest.RESUME_INTER_SPLASH_MED, "Empty")}"
            this["RESUME_OVERALL_MED"] = "${prefs.getString(RemoteConfigConstTest.RESUME_OVERALL_MED, "Empty")}"
            this["BANNER_SPLASH_MED"] = "${prefs.getString(RemoteConfigConstTest.BANNER_SPLASH_MED, "Empty")}"
            this["NATIVE_LANGUAGE_1_MED"] = "${prefs.getString(RemoteConfigConstTest.NATIVE_LANGUAGE_1_MED, "Empty")}"
            this["NATIVE_LANGUAGE_2_MED"] = "${prefs.getString(RemoteConfigConstTest.NATIVE_LANGUAGE_2_MED, "Empty")}"
            this["NATIVE_SURVEY_1_MED"] = "${prefs.getString(RemoteConfigConstTest.NATIVE_SURVEY_1_MED, "Empty")}"
            this["NATIVE_SURVEY_2_MED"] = "${prefs.getString(RemoteConfigConstTest.NATIVE_SURVEY_2_MED, "Empty")}"
            this["NATIVE_WALKTHROUGH_1_MED"] = "${prefs.getString(RemoteConfigConstTest.NATIVE_WALKTHROUGH_1_MED, "Empty")}"
            this["NATIVE_WALKTHROUGH_2_MED"] = "${prefs.getString(RemoteConfigConstTest.NATIVE_WALKTHROUGH_2_MED, "Empty")}"
            this["NATIVE_WALKTHROUGH_FULLSCR_MED"] = "${prefs.getString(RemoteConfigConstTest.NATIVE_WALKTHROUGH_FULLSCR_MED, "Empty")}"
            this["NATIVE_WALKTHROUGH_3_MED"] = "${prefs.getString(RemoteConfigConstTest.NATIVE_WALKTHROUGH_3_MED, "Empty")}"
            this["INTERSTITIAL_LETS_START_MED"] = "${prefs.getString(RemoteConfigConstTest.INTERSTITIAL_LETS_START_MED, "Empty")}"

            this["TIMER_NATIVE_F_SRC"] = "${prefs.getString(RemoteConfigConstTest.TIMER_NATIVE_F_SRC, "Empty")}"
        }
        return remoteConfigHashMap
    }
}