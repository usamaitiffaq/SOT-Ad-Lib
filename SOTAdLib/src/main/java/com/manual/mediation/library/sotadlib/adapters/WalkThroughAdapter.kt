package com.manual.mediation.library.sotadlib.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.manual.mediation.library.sotadlib.activities.WTFullScreenAdFragment
import com.manual.mediation.library.sotadlib.activities.WTOneFragment
import com.manual.mediation.library.sotadlib.activities.WTThreeFragment
import com.manual.mediation.library.sotadlib.activities.WTTwoFragment
import com.manual.mediation.library.sotadlib.data.WalkThroughItem

class WalkThroughAdapter(
    private val fragmentActivity: FragmentActivity,
    private val walkThroughItems: ArrayList<WalkThroughItem>,
    private val noOfFragments: Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return noOfFragments
    }

    override fun createFragment(position: Int): Fragment {
        val myReturnFragment: Fragment
        if (noOfFragments == 4) {
            myReturnFragment = when (position) {
                0 -> WTOneFragment(walkThroughItems[0])
                1 -> WTTwoFragment(walkThroughItems[1])
                2 -> WTFullScreenAdFragment()
                3 -> WTThreeFragment(walkThroughItems[2])
                else -> WTOneFragment(walkThroughItems[0])
            }
        } else {
            myReturnFragment = when (position) {
                0 -> WTOneFragment(walkThroughItems[0])
                1 -> WTTwoFragment(walkThroughItems[1])
                2 -> WTThreeFragment(walkThroughItems[2])
                else -> WTOneFragment(walkThroughItems[0])
            }
        }
        return myReturnFragment
    }
}