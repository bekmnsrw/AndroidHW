package com.example.androidhw.data

import android.content.Context
import androidx.room.Room
import com.example.androidhw.data.entities.Todo
import java.util.*

class TodoRepository(context: Context) {

    private val db by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .build()
    }

    private val todoDao by lazy {
        db.getTodoDao()
    }

    suspend fun saveTodo(todo: Todo) {
        todoDao.save(todo)
    }

    suspend fun deleteTodo(id: Int) {
        todoDao.deleteById(id)
    }

    suspend fun getAllTodo(): List<Todo> {
        return todoDao.getAll()
    }

    suspend fun getTodoById(id: Int): Todo {
        return todoDao.getById(id)
    }

    suspend fun updateTodo(id: Int, title: String, description: String, date: Date?, longitude: Double?, latitude: Double?) {
        todoDao.update(id, title, description, date, longitude, latitude)
    }

    suspend fun deleteAllTodo() {
        todoDao.clearTable()
    }

    companion object {
        private const val DATABASE_NAME = "todo.db"
    }
}
