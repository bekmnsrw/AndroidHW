package com.example.androidhw.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.androidhw.data.entities.Todo
import java.util.*

@Dao
interface TodoDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(todo: Todo)

    @Query("DELETE FROM todo WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM todo ORDER BY date")
    suspend fun getAll(): List<Todo>

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getById(id: Int): Todo

    @Query("UPDATE todo SET title = :title, description = :description, date = :date, longitude = :longitude, latitude = :latitude WHERE id = :id")
    suspend fun update(id: Int, title: String, description: String, date: Date?, longitude: Double?, latitude: Double?)

    @Query("DELETE FROM todo")
    suspend fun clearTable()
}
