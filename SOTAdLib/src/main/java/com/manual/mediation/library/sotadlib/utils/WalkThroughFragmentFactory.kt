package com.manual.mediation.library.sotadlib.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import com.manual.mediation.library.sotadlib.activities.WTFullScreenAdFragment
import com.manual.mediation.library.sotadlib.activities.WTOneFragment
import com.manual.mediation.library.sotadlib.activities.WTThreeFragment
import com.manual.mediation.library.sotadlib.activities.WTTwoFragment
import com.manual.mediation.library.sotadlib.data.WalkThroughItem

class WalkThroughFragmentFactory(
    private val walkThroughItems: ArrayList<WalkThroughItem>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            WTOneFragment::class.java.name -> WTOneFragment(walkThroughItems[0])
            WTTwoFragment::class.java.name -> WTTwoFragment(walkThroughItems[1])
            WTThreeFragment::class.java.name -> WTThreeFragment(walkThroughItems[2])
            WTFullScreenAdFragment::class.java.name -> WTFullScreenAdFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}