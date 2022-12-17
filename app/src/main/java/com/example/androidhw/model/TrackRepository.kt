package com.example.androidhw.model

import com.example.androidhw.R

object TrackRepository {
    val tracks = arrayListOf(
        Track(
            id = 0,
            name = "Infinite Tsukuyomi",
            cover = R.drawable.infinite_tsukuyomi,
            raw = R.raw.infinite_tsukuyomi,
            author = "ROCKET",
            genre = "Russian Rap"
        ),
        Track(
            id = 1,
            name = "ORANGE SODA",
            cover = R.drawable.orange_soda,
            raw = R.raw.orange_soda,
            author = "Baby Keem",
            genre = "Rap & Hip-Hop"
        ),
        Track(
            id = 2,
            name = "Close Eyes",
            cover = R.drawable.close_eyes,
            raw = R.raw.close_eyes,
            author = "DVRST",
            genre = "Phonk"
        ),
        Track(
            id = 3,
            name = "Поменять",
            cover = R.drawable.pomenyat,
            raw = R.raw.pomenyat,
            author = "LILDRUGHILL",
            genre = "Russian Rap"
        ),
        Track(
            id = 4,
            name = "black sheep",
            cover = R.drawable.black_sheep,
            raw = R.raw.black_sheep,
            author = "Convolk",
            genre = "Rap & Hip-Hop"
        )
    )

    val tracksUi: MutableList<TrackUiModel>
        get() = tracks.map {
            TrackUiModel(
                id = it.id,
                name = it.name,
                cover = it.cover,
                raw = it.raw,
                author = it.author,
                genre = it.genre,
                isPlaying = it.isPlaying
            )
        }.toMutableList()

    fun getTrack(id: Int): Track {
        return tracks.single() {it.id == id}
    }
}