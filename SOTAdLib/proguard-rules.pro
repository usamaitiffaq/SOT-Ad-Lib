-keep class android.webkit.** { *; }
-dontwarn android.webkit.**
-dontwarn com.facebook.infer.annotation.Nullsafe$Mode
-dontwarn com.facebook.infer.annotation.Nullsafe

-keep class com.facebook.infer.annotation.** { *; }
-dontwarn com.facebook.infer.annotation.**


-keep class com.manual.mediation.library.sotadlib.activities.AppCompatBaseActivity.** { *; }
-keep class com.manual.mediation.library.sotadlib.activities.LanguageScreenOne.** { *; }
-keep class com.manual.mediation.library.sotadlib.activities.LanguageScreenDup.** { *; }
-keep class com.manual.mediation.library.sotadlib.activities.WalkThroughConfigActivity.** { *; }
-keep class com.manual.mediation.library.sotadlib.activities.WelcomeScreenOne.** { *; }
-keep class com.manual.mediation.library.sotadlib.activities.WelcomeScreenDup.** { *; }
-keep class com.manual.mediation.library.sotadlib.activities.WTFullScreenAdFragment.** { *; }
-keep class com.manual.mediation.library.sotadlib.activities.WTOneFragment.** { *; }
-keep class com.manual.mediation.library.sotadlib.activities.WTThreeFragment.** { *; }
-keep class com.manual.mediation.library.sotadlib.activities.WTTwoFragment.** { *; }

-keep class com.manual.mediation.library.sotadlib.adapters.LanguageAdapter.** { *; }
-keep class com.manual.mediation.library.sotadlib.adapters.WalkThroughAdapter.** { *; }

-keep class com.manual.mediation.library.sotadlib.adMobAdClasses.AdMobBannerAdSplash.** { *; }
-keep class com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobInterstitialAdSplash.** { *; }
-keep class com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobNativeAdManager.** { *; }
-keep class com.manual.mediation.library.sotadlib.adMobAdClasses.AdmobResumeAdSplash.** { *; }

-keep class com.manual.mediation.library.sotadlib.callingClasses.LanguageScreensConfiguration.** { *; }
-keep class com.manual.mediation.library.sotadlib.callingClasses.SOTAdsConfigurations.** { *; }
-keep class com.manual.mediation.library.sotadlib.callingClasses.SOTAdsManager.** { *; }
-keep class com.manual.mediation.library.sotadlib.callingClasses.WalkThroughScreensConfiguration.** { *; }
-keep class com.manual.mediation.library.sotadlib.callingClasses.WelcomeScreensConfiguration.** { *; }

-keep class com.manual.mediation.library.sotadlib.data.Language.** { *; }
-keep class com.manual.mediation.library.sotadlib.data.WalkThroughItem.** { *; }

-keep interface com.manual.mediation.library.sotadlib.interfaces.LanguageInterface.** { *; }
-keep interface com.manual.mediation.library.sotadlib.interfaces.WelcomeInterface.** { *; }
-keep interface com.manual.mediation.library.sotadlib.interfaces.OnNextButtonClickListener.** { *; }

-keep class com.manual.mediation.library.sotadlib.metaAdClasses.MetaBannerAdSplash.** { *; }
-keep class com.manual.mediation.library.sotadlib.metaAdClasses.MetaInterstitialAdSplash.** { *; }
-keep class com.manual.mediation.library.sotadlib.metaAdClasses.MetaNativeAdManager.** { *; }

-keep class com.manual.mediation.library.sotadlib.utils.AdLoadingDialog.** { *; }
-keep class com.manual.mediation.library.sotadlib.utils.ExFunKt.** { *; }
-keep class com.manual.mediation.library.sotadlib.utils.MyLocaleHelper.** { *; }
-keep class com.manual.mediation.library.sotadlib.utils.NetworkCheck.** { *; }
-keep class com.manual.mediation.library.sotadlib.utils.PrefHelper.** { *; }

-keep class com.manual.mediation.library.sotadlib.utilsGoogleAdsConsent.ConsentConfigurations.** { *; }
-keep class com.manual.mediation.library.sotadlib.utilsGoogleAdsConsent.GoogleMobileAdsConsentManager.** { *; }

-keep class com.manual.mediation.library.sotadlib.vungleAdClasses.VungleResumeAdSplash.** { *; }
-keep class com.manual.mediation.library.sotadlib.vungleAdClasses.VungleInterstitialAdSplash.** { *; }
-keep class com.manual.mediation.library.sotadlib.vungleAdClasses.VungleBannerAdSplash.** { *; }