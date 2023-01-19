package com.example.aracua.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aracua.data.Note

@Database(entities = [Note::class], version = 1)
abstract class AracuaDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var Instance: AracuaDatabase? = null

        fun getDatabase(context: Context): AracuaDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AracuaDatabase::class.java, "aracua_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}