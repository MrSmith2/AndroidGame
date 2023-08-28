package com.example.birdiegame

import android.content.Context
import android.content.SharedPreferences

class ScoreManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Game", Context.MODE_PRIVATE)

    fun getBestScore(): Int {
        return sharedPreferences.getInt("best_score", 0)
    }

    fun setBestScore(score: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("best_score", score)
        editor.apply()
    }

    fun resetBestScore() {
        setBestScore(0)
    }
}