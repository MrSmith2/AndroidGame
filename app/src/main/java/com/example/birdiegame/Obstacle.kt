package com.example.birdiegame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Obstacle(context: Context, screenX: Int, screenHeight: Int) {
    private val obstacleBitmaps: List<Bitmap> = listOf(
        BitmapFactory.decodeResource(context.resources, R.drawable.obstacle_1),
        BitmapFactory.decodeResource(context.resources, R.drawable.obstacle_2),
        BitmapFactory.decodeResource(context.resources, R.drawable.obstacle_3),
    )
    private val randomObstacle = when ((0..10).random()) {
        in 0..5 -> 0
        in 6..8 -> 1
        else -> 2
    }
    private val obstacleBitmap: Bitmap = obstacleBitmaps[randomObstacle]
    private val width: Int = obstacleBitmap.width
    private val height: Int = obstacleBitmap.height
    private var x: Float = screenX.toFloat()
    private var y: Float = (screenHeight - height).toFloat()
    private var speed: Float = 10f

    init {
        val randomHeight = (Math.random() * (screenHeight * 0.6)).toInt()
        y = (screenHeight - randomHeight - height).toFloat()
        speed = when (obstacleBitmaps.indexOf(obstacleBitmap)) {
            0 -> speed * ((20..25).random().toFloat() / 10f)
            1 -> speed * ((15..20).random().toFloat() / 10f)
            2 -> speed * ((10..15).random().toFloat() / 10f)
            else -> speed
        }
    }

    fun draw(canvas: Canvas) {
/*
        val boundingBox = getBoundingBox()
        val paint = Paint().apply {
            color = Color.RED
            alpha = 100
        }
        canvas.drawRect(boundingBox, paint)
*/

        canvas.drawBitmap(obstacleBitmap, x, y, null)
    }

    fun update() {
        x -= speed
    }

    fun isOutOfScreen(): Boolean {
        return x + width < 0
    }

    fun getBoundingBox(): Rect {
        return Rect(
            x.toInt() + width / 18, y.toInt() + height / 2,
            x.toInt() + width - width / 10, y.toInt() + height - height / 3
        )
    }
}