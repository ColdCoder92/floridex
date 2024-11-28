package com.example.floridex

// To parse the JSON, install Klaxon and do:
//
//   val welcome5 = Welcome5.fromJson(jsonString)

import com.beust.klaxon.*

private val klaxon = Klaxon()

data class Welcome5 (
    @Json(name = "RelativeLayout")
    val relativeLayout: RelativeLayout
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Welcome5>(json)
    }
}

data class RelativeLayout (
    @Json(name = "LinearLayout")
    val linearLayout: LinearLayout,

    @Json(name = "_xmlns:android")
    val xmlnsAndroid: String,

    @Json(name = "_xmlns:app")
    val xmlnsApp: String,

    @Json(name = "_xmlns:tools")
    val xmlnsTools: String,

    @Json(name = "_android:id")
    val androidID: String,

    @Json(name = "_android:layout_width")
    val androidLayoutWidth: String,

    @Json(name = "_android:layout_height")
    val androidLayoutHeight: String,

    @Json(name = "_android:background")
    val androidBackground: String,

    @Json(name = "_android:gravity")
    val androidGravity: String,

    @Json(name = "_android:padding")
    val androidPadding: String,

    @Json(name = "_tools:context")
    val toolsContext: String
)

data class LinearLayout (
    @Json(name = "ImageView")
    val imageView: ImageView,

    @Json(name = "EditText")
    val editText: List<EditText>,

    @Json(name = "Button")
    val button: List<Button>,

    @Json(name = "TextView")
    val textView: TextView,

    @Json(name = "_android:id")
    val androidID: String,

    @Json(name = "_android:layout_width")
    val androidLayoutWidth: String,

    @Json(name = "_android:layout_height")
    val androidLayoutHeight: String,

    @Json(name = "_android:gravity")
    val androidGravity: String,

    @Json(name = "_android:layout_marginTop")
    val androidLayoutMarginTop: String,

    @Json(name = "_android:orientation")
    val androidOrientation: String
)

data class Button (
    @Json(name = "_android:layout_width")
    val androidLayoutWidth: String,

    @Json(name = "_android:layout_height")
    val androidLayoutHeight: String,

    @Json(name = "_android:layout_marginTop")
    val androidLayoutMarginTop: String? = null,

    @Json(name = "_android:background")
    val androidBackground: String,

    @Json(name = "_android:backgroundTint")
    val androidBackgroundTint: String,

    @Json(name = "_android:padding")
    val androidPadding: String? = null,

    @Json(name = "_android:text")
    val androidText: String,

    @Json(name = "_android:textSize")
    val androidTextSize: String
)

data class EditText (
    @Json(name = "_android:id")
    val androidID: String,

    @Json(name = "_android:layout_width")
    val androidLayoutWidth: String,

    @Json(name = "_android:layout_height")
    val androidLayoutHeight: String,

    @Json(name = "_android:layout_marginTop")
    val androidLayoutMarginTop: String,

    @Json(name = "_android:background")
    val androidBackground: String,

    @Json(name = "_android:hint")
    val androidHint: String,

    @Json(name = "_android:inputType")
    val androidInputType: String,

    @Json(name = "_android:padding")
    val androidPadding: String,

    @Json(name = "_android:textSize")
    val androidTextSize: String
)

data class ImageView (
    @Json(name = "_android:layout_width")
    val androidLayoutWidth: String,

    @Json(name = "_android:layout_height")
    val androidLayoutHeight: String,

    @Json(name = "_android:src")
    val androidSrc: String
)

data class TextView (
    @Json(name = "_android:layout_width")
    val androidLayoutWidth: String,

    @Json(name = "_android:layout_height")
    val androidLayoutHeight: String,

    @Json(name = "_android:layout_marginTop")
    val androidLayoutMarginTop: String,

    @Json(name = "_android:fontFamily")
    val androidFontFamily: String,

    @Json(name = "_android:gravity")
    val androidGravity: String,

    @Json(name = "_android:text")
    val androidText: String,

    @Json(name = "_android:textColor")
    val androidTextColor: String
)
