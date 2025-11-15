package com.ifrs.movimentaif.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.ifrs.movimentaif.R

object SoundManager {
    private var soundPool: SoundPool? = null
    private var clickSoundId: Int = 0
    private var isInitialized = false
    private var isLoaded = false

    fun init(context: Context) {
        if (isInitialized) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool?.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                isLoaded = true
            }
        }

        clickSoundId = soundPool?.load(context, R.raw.som, 1) ?: 0
        isInitialized = true
    }

    fun playClickSound() {
        if (isInitialized && isLoaded && clickSoundId != 0) {
            soundPool?.play(clickSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        isInitialized = false
    }
}
