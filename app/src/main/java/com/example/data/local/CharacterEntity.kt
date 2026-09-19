package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val type: String, // "HEROI" or "VILAO"
    val clothing: String,
    val age: Int,
    val ageCategory: String, // "CRIANCA", "ADOLESCENTE", "JOVEM_ADULTO", "ADULTO", "IDOSO", "VILAO_CRIANCA", "VILAO_ADULTO", "VILAO_REI_FINAL"
    val voiceProfile: String, // "HEROI_JOVEM", "VILAO_MONSTRO", "PRESIDENTE", "CIENTISTA"
    val prompt: String,
    val isFavorite: Boolean = false,
    val evolutionStage: Int = 0,
    val projectId: Long = 0L,
    val customVoicePhrase: String = ""
)
