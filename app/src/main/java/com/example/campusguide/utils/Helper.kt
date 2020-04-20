package com.example.campusguide.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

class Helper {

    companion object {
        fun capitalizeWords(location: String): String {
            val words = location.split(" ").toMutableList()
            var output = ""
            for (word in words) {
                output += word.capitalize() + " "
            }
            output = output.trim()
            return output
        }

        fun textAsBitmap(text: String, textSize: Float, textColor: Int): Bitmap? {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.setTextSize(textSize)
            paint.setColor(textColor)
            paint.setTextAlign(Paint.Align.LEFT)
            val baseline: Float = -paint.ascent() // ascent() is negative
            val width = (paint.measureText(text) + 0.5f).toInt() // round
            val height = (baseline + paint.descent() + 0.5f).toInt()
            val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawText(text, 0f, baseline, paint)
            return image
        }
    }
}
