package com.example.androidhw.repositories

object Repository {
    var dataList: List<MyUiModel> = listOf()

    private val characterItems: List<MyUiModel> = listOf(
        MyUiModel.Character(
            "Naruto Uzumaki",
            "Hero of the Hidden Leaf",
            "https://i.pinimg.com/564x/41/9f/f5/419ff508b894f934b4139035ffd04d41.jpg"
        ),
        MyUiModel.Character(
            "Sasuke Uchiha",
            "Supporting Shadow",
            "https://i.pinimg.com/564x/41/29/59/412959a850619ef5ed2f38bca39ba5ac.jpg"
        ),
        MyUiModel.Character(
            "Sakura Haruno",
            "Tsunade Number Two",
            "https://i.pinimg.com/564x/dc/67/1a/dc671ab046712d59685a1479cb2ef053.jpg"
        ),
        MyUiModel.Character(
            "Kakashi Hatake",
            "Copy Ninja Kakashi",
            "https://i.pinimg.com/564x/66/52/ba/6652bab65936a2a8bc51b9a0549c2370.jpg"
        )
    )

    private val advertisementItems: List<MyUiModel> = listOf(
        MyUiModel.Advertisement(
            "Watch 'Naruto' on Kinopoisk HD",
            "https://s3.afisha.ru/mediastorage/21/bc/04bd46c04e9640a7ad3a9f67bc21.jpg"
        ),
        MyUiModel.Advertisement(
            "Play 'Naruto: Ultimate Ninja Storm 4'",
            "https://www.jvfrance.com/wp-content/uploads/2016/03/naruto-shippuden-ultimate-ninja-storm-4.jpg"
        )
    )

    fun generateList(size: Int) {
        val list = mutableListOf<MyUiModel>()
        for (i in 0 until size) {
            if (i % 6 == 0) {
                val item = advertisementItems[(advertisementItems.indices).random()] as MyUiModel.Advertisement
                list.add(item.copy())
            } else {
                val item = (characterItems[(characterItems.indices).random()] as MyUiModel.Character)
                list.add(item.copy())
            }
        }
        dataList = list
    }

    fun addItem(position: Int, item: MyUiModel.Character) {
        val list = dataList.toMutableList()

        if (position >= dataList.size - 1) {
            list.add(item)
        } else {
            list.add(position, item)
            for (i in position until dataList.size) {
                if (list[i] is MyUiModel.Advertisement && i % 6 != 0) {
                    list[i] = list[i - 1].also {
                        list[i - 1] = list[i]
                    }
                }
            }
        }

        dataList = list.toList()
    }

    fun deleteItem(position: Int) {
        val list = dataList.toMutableList()

        list.removeAt(position)

        for (i in position until dataList.size - 2) {
            if (list[i] is MyUiModel.Advertisement && i % 6 != 0) {
                list[i] = list[i + 1].also {
                    list[i + 1] = list[i]
                }
            }
            if (list[list.size - 1] is MyUiModel.Advertisement) {
                list.removeAt(list.size - 1)
            }
        }

        dataList = list.toList()
    }
}