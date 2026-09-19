package com.example.data.model

import java.util.Locale

data class TimeConfig(
    val hours: Int = 0,
    val minutes: Int = 24,
    val seconds: Int = 0
) {
    val totalSeconds: Int get() = (hours * 3600) + (minutes * 60) + seconds

    fun formatDuration(): String {
        return String.format(Locale.getDefault(), "%02dh %02dm %02ds", hours, minutes, seconds)
    }

    companion object {
        fun fromTotalSeconds(totalSec: Int): TimeConfig {
            val safeSec = if (totalSec < 0) 0 else totalSec
            val h = safeSec / 3600
            val m = (safeSec % 3600) / 60
            val s = safeSec % 60
            return TimeConfig(h, m, s)
        }
    }
}

data class SeriesTimeCalculation(
    val episodeTime: TimeConfig,
    val seasons: Int,
    val episodesPerSeason: Int
) {
    val seasonTotalSeconds: Int get() = episodeTime.totalSeconds * episodesPerSeason
    val seriesTotalSeconds: Int get() = seasonTotalSeconds * seasons

    fun formattedSeasonTotal(): String {
        return TimeConfig.fromTotalSeconds(seasonTotalSeconds).formatDuration()
    }

    fun formattedSeriesTotal(): String {
        return TimeConfig.fromTotalSeconds(seriesTotalSeconds).formatDuration()
    }

    // Budget: R$ 0.50 per minute
    val seasonCostReais: Double get() = (seasonTotalSeconds / 60.0) * 0.50
    val seriesCostReais: Double get() = (seriesTotalSeconds / 60.0) * 0.50
}

data class MovieActInfo(
    val actNumber: Int,
    val actName: String,
    val roleDescription: String,
    val percentage: Int,
    val startSeconds: Int,
    val endSeconds: Int
) {
    val formattedRange: String
        get() {
            val start = TimeConfig.fromTotalSeconds(startSeconds).formatDuration()
            val end = TimeConfig.fromTotalSeconds(endSeconds).formatDuration()
            return "$start ➔ $end"
        }
}

data class MovieTimeCalculation(
    val movieTime: TimeConfig
) {
    val totalSeconds: Int get() = movieTime.totalSeconds

    fun formattedTotal(): String = movieTime.formatDuration()

    // 3 Acts breakdown:
    // Act 1: 25% (Setup, inciting incident)
    // Act 2: 50% (Rising action, midpoint twist, dark night of the soul)
    // Act 3: 25% (Climax confrontation, resolution)
    fun getActs(): List<MovieActInfo> {
        val total = totalSeconds
        val act1Duration = (total * 0.25).toInt()
        val act2Duration = (total * 0.50).toInt()
        val act3Duration = total - act1Duration - act2Duration

        val act1End = act1Duration
        val act2End = act1End + act2Duration
        val act3End = total

        return listOf(
            MovieActInfo(
                actNumber = 1,
                actName = "Ato 1: Apresentação & Incidente Incitante",
                roleDescription = "Estabelecimento da DARKCOM, rotina da Terra e primeira fissura dimensional com invasão de demônios.",
                percentage = 25,
                startSeconds = 0,
                endSeconds = act1End
            ),
            MovieActInfo(
                actNumber = 2,
                actName = "Ato 2: Confronto & Crise Maior",
                roleDescription = "Avanço dos generais demônios, perdas dramáticas, descobrimento do plano do Rei Demônio e contra-ataque.",
                percentage = 50,
                startSeconds = act1End,
                endSeconds = act2End
            ),
            MovieActInfo(
                actNumber = 3,
                actName = "Ato 3: Batalha Decisiva & Resolução",
                roleDescription = "Despertar da forma cósmica, duelo final entre os mundos e fechamento épico do portal interdimensional.",
                percentage = 25,
                startSeconds = act2End,
                endSeconds = act3End
            )
        )
    }

    val costReais: Double get() = (totalSeconds / 60.0) * 0.50
}
