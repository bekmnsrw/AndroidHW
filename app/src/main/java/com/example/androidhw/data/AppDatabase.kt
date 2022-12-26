package com.example.androidhw.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidhw.data.dao.TodoDao
import com.example.androidhw.data.entities.Todo

@Database(entities = [Todo::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTodoDao(): TodoDao
}
