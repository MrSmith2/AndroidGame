package com.example.birdiegame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), Runnable {
    private var prefs: ScoreManager
    private var gameStarted: Boolean = false
    private var star: Star
    private val backgroundBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.background)
    private var score: Int = 0
    private var gameOver: Boolean = false
    private var isPlaying: Boolean = false
    private var gameThread: Thread? = null
    private val surfaceHolder: SurfaceHolder = holder
    private var bird: Bird
    private var obstacle: Obstacle
    private val screenX: Int = resources.displayMetrics.widthPixels
    private val screenY: Int = resources.displayMetrics.heightPixels
    private val scorePaint = Paint().apply {
        color = Color.BLACK
        textSize = 100f
    }

    init {
        prefs = ScoreManager(context)
        bird = Bird(context, screenY)
        obstacle = Obstacle(context, screenX, screenY)
        star = Star(context, screenX, screenY)
    }
    private var bestScore: Int = prefs.getBestScore()

    private fun gameStart(canvas: Canvas) {
        bird.update()
        star.update()
        obstacle.update()

        if (obstacle.isOutOfScreen()) {
            if (star.isOutOfScreen()) {
                star = Star(context, screenX, screenY)
            }
            obstacle = Obstacle(context, screenX, screenY)
            score++
        }

        if (bird.getBoundingBox().intersect(obstacle.getBoundingBox()) || bird.y > screenY || bird.y + bird.height  < 0) {
            gameOver = true
            isPlaying = false
        }

        if (bird.getBoundingBox().intersect(star.getBoundingBox())) {
            star = Star(context, screenX, screenY)
            score += 5
        }

        canvas.drawBitmap(backgroundBitmap, null, Rect(0, 0, width, height), null)
        star.draw(canvas)
        bird.draw(canvas)
        obstacle.draw(canvas)

        canvas.drawText("Score: $score", (screenX / 3).toFloat(), (screenY / 20).toFloat(), scorePaint)
    }

    private fun gameOverLogic(canvas: Canvas) {
        bird.velocity = 50f
        bird.update()
        obstacle.update()
        star.update()
        canvas.drawBitmap(backgroundBitmap, null, Rect(0, 0, width, height), null)
        bird.draw(canvas)
        obstacle.draw(canvas)
        if (score > bestScore) {
            bestScore = score
            prefs.setBestScore(bestScore)

            canvas.drawText(
                "Score: $score",
                (screenX / 3).toFloat(),
                (screenY / 20).toFloat(),
                scorePaint
            )
            canvas.drawText(
                "New best score!",
                (screenX / 4).toFloat(),
                ((screenY / 2) - 300).toFloat(),
                scorePaint
            )
            canvas.drawText(
                "Score: $score",
                (screenX / 4).toFloat(),
                ((screenY / 2) - 200).toFloat(),
                scorePaint
            )
            canvas.drawText(
                "Best: $bestScore",
                (screenX / 4).toFloat(),
                ((screenY / 2) - 100).toFloat(),
                scorePaint
            )

            val buttonWidth = 200
            val buttonHeight = 100
            val buttonX = (screenX - buttonWidth) / 2
            val buttonY = (screenY - buttonHeight) / 2
            canvas.drawRect(
                buttonX.toFloat(),
                buttonY.toFloat(),
                (buttonX + buttonWidth).toFloat(),
                (buttonY + buttonHeight).toFloat(),
                scorePaint
            )
            scorePaint.color = Color.WHITE
            canvas.drawText("Restart", buttonX + 40f, buttonY + 60f, scorePaint)
        } else {
            canvas.drawText(
                "Score: $score",
                (screenX / 3).toFloat(),
                (screenY / 20).toFloat(),
                scorePaint
            )
            canvas.drawText(
                "Game over",
                (screenX / 4).toFloat(),
                ((screenY / 2) - 350).toFloat(),
                scorePaint
            )
            canvas.drawText(
                "Score: $score",
                (screenX / 4).toFloat(),
                ((screenY / 2) - 250).toFloat(),
                scorePaint
            )
            canvas.drawText(
                "Best: $bestScore",
                (screenX / 4).toFloat(),
                ((screenY / 2) - 150).toFloat(),
                scorePaint
            )

            val buttonWidth1 = screenX / 3
            val buttonHeight1 = screenY / 15
            val buttonX1 = (screenX - buttonWidth1) / 2
            val buttonY1 = (screenY - buttonHeight1) / 10
            scorePaint.color = Color.GREEN
            canvas.drawRect(
                buttonX1.toFloat(),
                buttonY1.toFloat(),
                (buttonX1 + buttonWidth1).toFloat(),
                (buttonY1 + buttonHeight1).toFloat(),
                scorePaint
            )
            scorePaint.color = Color.BLACK
            canvas.drawText("Restart", buttonX1 + 15f, buttonY1 + 100f, scorePaint)

            val buttonWidth2 = screenX / 2
            val buttonHeight2 = screenY / 15
            val buttonX2 = (screenX - buttonWidth2) / 2
            val buttonY2 = (screenY - buttonHeight2 * 2)
            scorePaint.color = Color.YELLOW
            canvas.drawRect(
                buttonX2.toFloat(),
                buttonY2.toFloat(),
                (buttonX2 + buttonWidth2).toFloat(),
                (buttonY2 + buttonHeight2).toFloat(),
                scorePaint
            )
            scorePaint.color = Color.BLACK
            canvas.drawText("Reset Best", buttonX2 + 25f, buttonY2 + 100f, scorePaint)
        }
    }

    override fun run() {
        while (true) {
            if (!surfaceHolder.surface.isValid) {
                continue
            }

            val canvas: Canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.WHITE)

            if (!gameOver) gameStart(canvas)
            else gameOverLogic(canvas)

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    fun resume() {
        isPlaying = true
        gameThread = Thread(this)
        gameThread?.start()
    }

    fun pause() {
        isPlaying = false
        try {
            gameThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun gameStart() {
        gameOver = false
        isPlaying = true
        bird = Bird(context, screenY)
        obstacle = Obstacle(context, screenX, screenY)
        score = 0
        bird.velocity = 0f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!gameStarted) {
                    gameStarted = true
                    return true
                }
                if (gameOver && event.y < screenY / 5){
                    gameStart()
                } else if (gameOver && event.y > screenY - 350) {
                    prefs.resetBestScore()
                } else bird.jump()
            }
        }
        return true
    }
}