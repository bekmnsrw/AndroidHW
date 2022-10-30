package com.example.androidhw.repositories

sealed class MyUiModel(val id: Int) {
    data class Character(
        val headline: String,
        val supportingText: String,
        val imageUrl: String
        ): MyUiModel(0)

    data class Advertisement(
        val supportingText: String,
        val imageUrl: String
    ): MyUiModel(0)
}
