package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CharacterEntity::class,
        ProjectEntity::class,
        GeneratedEpisodeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AnimeStudioDatabase : RoomDatabase() {
    abstract fun animeStudioDao(): AnimeStudioDao

    companion object {
        @Volatile
        private var INSTANCE: AnimeStudioDatabase? = null

        fun getInstance(context: Context): AnimeStudioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimeStudioDatabase::class.java,
                    "anime_studio_pro.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
