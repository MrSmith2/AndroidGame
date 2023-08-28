package com.example.birdiegame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Bird(context: Context, screenHeight: Int) {
    private val birdBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bird)
    private val width: Int = birdBitmap.width
    val height: Int = birdBitmap.height
    private var x: Float = 25f
    var y: Float = screenHeight / 3f + 225
    var velocity: Float = 0f
    private val gravity: Float = 0.5f
    private val jumpStrength: Float = -13f

    fun draw(canvas: Canvas) {
/*      val boundingBox = getBoundingBox()
        val paint = Paint().apply {
            color = Color.RED
            alpha = 100
        }

        canvas.drawRect(boundingBox, paint)*/

        canvas.drawBitmap(birdBitmap, x, y, null)
    }

    fun update() {
        velocity += gravity
        y += velocity
    }

    fun jump() {
        velocity = jumpStrength
    }

    fun getBoundingBox(): Rect {
        return Rect(x.toInt() + 100, y.toInt() + 75, x.toInt() + width - 50, y.toInt() + height - 50)
    }
}