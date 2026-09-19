package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val storyText: String,
    val projectMode: String, // "SERIE" or "FILME"
    val episodeHours: Int = 0,
    val episodeMinutes: Int = 24,
    val episodeSeconds: Int = 0,
    val seasonsCount: Int = 1,
    val episodesPerSeason: Int = 10,
    val movieHours: Int = 1,
    val movieMinutes: Int = 30,
    val movieSeconds: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
