package com.manual.mediation.library.sotadlib.data

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//data class WalkThroughItem(
//    val heading: String,
//    val description: String,
//    val headingColor: Int,
//    val descriptionColor: Int,
//    val nextColor: Int,
//    val drawable: Drawable?,
//    val drawableBubble: Drawable?
//)


@Parcelize
data class WalkThroughItem(
    val heading: String,
    val description: String,
    val headingColor: Int,
    val descriptionColor: Int,
    val nextColor: Int,
    val drawableResId: Int,
    val drawableBubbleResId: Int
) : Parcelable