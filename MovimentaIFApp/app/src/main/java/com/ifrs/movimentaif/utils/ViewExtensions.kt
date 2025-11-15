package com.ifrs.movimentaif.utils

import android.view.View
import com.google.android.material.button.MaterialButton

fun View.setOnClickListenerWithSound(onClick: (View) -> Unit) {
    this.setOnClickListener { view ->
        SoundManager.playClickSound()
        onClick(view)
    }
}

fun MaterialButton.setOnClickListenerWithSound(onClick: (View) -> Unit) {
    this.setOnClickListener { view ->
        SoundManager.playClickSound()
        onClick(view)
    }
}
