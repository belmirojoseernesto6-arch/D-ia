package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episodes")
data class GeneratedEpisodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val projectId: Long = 0L,
    val projectTitle: String,
    val projectMode: String, // "SERIE" or "FILME"
    val episodeNumber: Int = 1,
    val seasonNumber: Int = 1,
    val title: String,
    val synopsis: String,
    val durationSeconds: Int,
    val formattedDuration: String,
    val scenesDataJson: String, // serialized scenes with dialogues, speakers, backgrounds
    val isTrailer: Boolean = false,
    val viralYoutubeTitle: String,
    val viralDescription: String,
    val viralTags: String,
    val status: String = "PRONTO", // "PRONTO" ou "NA_FILA"
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getDuracaoFormatada(): String = formattedDuration
}

typealias Episodio = GeneratedEpisodeEntity
