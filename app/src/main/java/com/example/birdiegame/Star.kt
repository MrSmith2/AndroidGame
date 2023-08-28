package com.example.birdiegame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Star(context: Context, screenX: Int, screenHeight: Int) {
    private val starBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.star)
    private val width: Int = starBitmap.width
    private val height: Int = starBitmap.height
    private var x: Float = screenX.toFloat()
    private var y: Float = (0..screenHeight - height).random().toFloat()
    private val speed: Float = 8f

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(starBitmap, x, y, null)
    }

    fun update() {
        x -= speed
    }

    fun isOutOfScreen(): Boolean {
        return x + width < 0
    }

    fun getBoundingBox(): Rect {
        return Rect(x.toInt(), y.toInt(), x.toInt() + width, y.toInt() + height)
    }
}
