package com.example.kot_trip.base

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView

class Utils {
    companion object {
        fun getBitmapFromImageView(imageView: ImageView): Bitmap? {
            return if (imageView.drawable is BitmapDrawable) {
                (imageView.drawable as BitmapDrawable).bitmap
            } else {
                Log.d("Utils", "getBitmapFromImageView: imageView is not a BitmapDrawable")
                null
            }
        }
    }
}