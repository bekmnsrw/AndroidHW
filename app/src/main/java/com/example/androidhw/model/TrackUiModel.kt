package com.example.androidhw.model

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class TrackUiModel(
    val id: Int,
    val name: String,
    @DrawableRes val cover: Int,
    @RawRes val raw: Int,
    val author: String,
    val genre: String,
    val isPlaying: Boolean
)
