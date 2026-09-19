package com.example.data.model

import com.example.data.local.CharacterEntity
import com.example.data.local.GeneratedEpisodeEntity
import java.util.Locale

object EpisodeGeneratorLogic {

    fun generateEpisode(
        projectId: Long,
        projectTitle: String,
        projectMode: String,
        episodeNum: Int,
        seasonNum: Int,
        storyText: String,
        characters: List<CharacterEntity>,
        durationSeconds: Int
    ): GeneratedEpisodeEntity {
        val heroes = characters.filter { it.type == CharacterType.HEROI.name }
        val villains = characters.filter { it.type == CharacterType.VILAO.name }

        val leadHero = heroes.firstOrNull()
        val leader = heroes.find { it.ageCategory == AgeCategory.ADULTO.key || it.name.contains("Presidente") } ?: leadHero
        val leadVillain = villains.firstOrNull()

        val episodeTitle = when (episodeNum) {
            1 -> "Episódio 1: A Fenda de Ômega e o Primeiro Alerta"
            2 -> "Episódio 2: Incursão Noturna em Neo-Tokyo"
            3 -> "Episódio 3: O Laboratório sob Cerco Demoníaco"
            4 -> "Episódio 4: Sombras do Universo Paralelo"
            5 -> "Episódio 5: O Sacrifício dos Veteranos"
            6 -> "Episódio 6: Lâmina de Fótons Desperta"
            7 -> "Episódio 7: O Colapso das Linhas de Defesa"
            8 -> "Episódio 8: A Marcha do General do Abismo"
            9 -> "Episódio 9: Véspera do Juízo Cósmico"
            else -> "Episódio $episodeNum: Confronto no Limiar das Dimensões"
        }

        val scenes = mutableListOf<AnimeScene>()

        // Scene 1: Darkcom Headquarters Briefing
        scenes.add(
            AnimeScene(
                sceneNumber = 1,
                title = "Alerta Vermelho na DARKCOM",
                backgroundType = "DARKCOM_MEETING_ROOM",
                visualPrompt = "anime dark conference room, glowing blue holographic tactical screens, DARKCOM high command assembling, cinematic mood",
                speakerName = leader?.name ?: "Presidente Vance",
                speakerType = "HEROI",
                dialogueText = "Senhores, os sensores detectaram ruptura na malha dimensional. Demônios de classe S emergiram!",
                voiceProfile = leader?.voiceProfile ?: VoiceProfile.PRESIDENTE.key,
                durationSeconds = 6,
                effectType = "NEON_PULSE"
            )
        )

        // Scene 2: Villain portal emergence
        scenes.add(
            AnimeScene(
                sceneNumber = 2,
                title = "Abertura do Portal Escarlate",
                backgroundType = "DEMON_PORTAL",
                visualPrompt = "massive demonic red vortex opening in the sky over skyline, lightning strikes, sinister shadows pouring out, dark fantasy anime",
                speakerName = leadVillain?.name ?: "General Malakor",
                speakerType = "VILAO",
                dialogueText = "Tremam, humanos! Este mundo patético agora pertence aos lordes do Abismo!",
                voiceProfile = leadVillain?.voiceProfile ?: VoiceProfile.VILAO_MONSTRO.key,
                durationSeconds = 7,
                effectType = "PORTAL_VORTEX"
            )
        )

        // Scene 3: Hero battle response
        scenes.add(
            AnimeScene(
                sceneNumber = 3,
                title = "Entrada dos Defensores DARKCOM",
                backgroundType = "NEON_CITY_RUINS",
                visualPrompt = "anime hero in glowing cyber armor holding energy blade, dramatic counter-attack pose, neon sparks flying, cinematic lighting",
                speakerName = leadHero?.name ?: "Kaito Arashi",
                speakerType = "HEROI",
                dialogueText = "Não enquanto eu respirar! Protocolo de Defesa Quântica: ATIVAR!",
                voiceProfile = leadHero?.voiceProfile ?: VoiceProfile.HEROI_JOVEM.key,
                durationSeconds = 6,
                effectType = "LIGHTNING"
            )
        )

        // Scene 4: Climax clash of episode
        scenes.add(
            AnimeScene(
                sceneNumber = 4,
                title = "Impacto Dimensional Decisivo",
                backgroundType = "COSMIC_VOID",
                visualPrompt = "epic collision between cosmic blue aura and demonic crimson fire, shockwave tearing space, anime climax shot 8k",
                speakerName = leadVillain?.name ?: "General Malakor",
                speakerType = "VILAO",
                dialogueText = "Seu poder é impressionante... Mas o verdadeiro pesadelo apenas começou!",
                voiceProfile = leadVillain?.voiceProfile ?: VoiceProfile.VILAO_MONSTRO.key,
                durationSeconds = 7,
                effectType = "QUANTUM_GRID"
            )
        )

        val synopsis = "A DARKCOM mobiliza suas forças de elite após a abertura de uma fenda no universo paralelo. ${leadHero?.name ?: "O herói"} enfrenta ${leadVillain?.name ?: "as hordas do abismo"} para salvar a metrópole."
        val formattedDuration = TimeConfig.fromTotalSeconds(durationSeconds).formatDuration()

        val viralTitle = "DEMÔNIOS INVADEM A TERRA E DARKCOM REAGE! 😱🔥 #Anime #Shorts"
        val viralDesc = "Episódio completo gerado no ANIME STUDIO PRO.\nHistória: $storyText\nAssista à batalha épica entre ${leadHero?.name} e ${leadVillain?.name}!\n\n#AnimeStudioPro #Darkcom #AnimeSeries #ViralShorts"
        val viralTags = "#Anime, #DARKCOM, #Shorts, #Otaku, #AnimeEdit, #AnimeFight, #AIStudio"

        return GeneratedEpisodeEntity(
            projectId = projectId,
            projectTitle = projectTitle,
            projectMode = projectMode,
            episodeNumber = episodeNum,
            seasonNumber = seasonNum,
            title = episodeTitle,
            synopsis = synopsis,
            durationSeconds = durationSeconds,
            formattedDuration = formattedDuration,
            scenesDataJson = AnimeScene.serializeList(scenes),
            isTrailer = false,
            viralYoutubeTitle = viralTitle,
            viralDescription = viralDesc,
            viralTags = viralTags
        )
    }

    fun generateTrailer30s(
        projectId: Long,
        projectTitle: String,
        projectMode: String,
        storyText: String,
        characters: List<CharacterEntity>
    ): GeneratedEpisodeEntity {
        val heroes = characters.filter { it.type == CharacterType.HEROI.name }
        val villains = characters.filter { it.type == CharacterType.VILAO.name }
        val leadHero = heroes.firstOrNull()
        val leadVillain = villains.firstOrNull()

        val trailerScenes = listOf(
            AnimeScene(
                sceneNumber = 1,
                title = "Trailer - O Sussurro do Abismo",
                backgroundType = "DEMON_PORTAL",
                visualPrompt = "sinister red vortex in the clouds, terrifying demon silhouettes emerging, horror anime cinematic aesthetic",
                speakerName = leadVillain?.name ?: "Rei Demônio",
                speakerType = "VILAO",
                dialogueText = "Quando o portal se abrir, nenhuma alma sobreviverá...",
                voiceProfile = VoiceProfile.VILAO_MONSTRO.key,
                durationSeconds = 7,
                effectType = "PORTAL_VORTEX"
            ),
            AnimeScene(
                sceneNumber = 2,
                title = "Trailer - O Juramento da DARKCOM",
                backgroundType = "DARKCOM_MEETING_ROOM",
                visualPrompt = "DARKCOM command bridge, neon monitors flashing warning sirens, heroes arming battle weapons",
                speakerName = leadHero?.name ?: "Herói DARKCOM",
                speakerType = "HEROI",
                dialogueText = "Eles não sabem com quem estão lidando. Defenderemos a Terra até o fim!",
                voiceProfile = VoiceProfile.HEROI_JOVEM.key,
                durationSeconds = 8,
                effectType = "NEON_PULSE"
            ),
            AnimeScene(
                sceneNumber = 3,
                title = "Trailer - Batalha Cósmica Sem Precedentes",
                backgroundType = "COSMIC_VOID",
                visualPrompt = "ultra fast paced anime duel, neon swords against demon claws, explosions, celestial shockwaves, dramatic title card",
                speakerName = "Narrador Épico",
                speakerType = "HEROI",
                dialogueText = "ANIME STUDIO PRO APRESENTA: O CONFRONTO DEFINITIVO!",
                voiceProfile = VoiceProfile.PRESIDENTE.key,
                durationSeconds = 15,
                effectType = "LIGHTNING"
            )
        )

        return GeneratedEpisodeEntity(
            projectId = projectId,
            projectTitle = projectTitle,
            projectMode = projectMode,
            episodeNumber = 0,
            seasonNumber = 1,
            title = "TRAILER OFICIAL: $projectTitle (30s)",
            synopsis = "Trailer cinematográfico de 30 segundos com cortes dinâmicos da invasão demoníaca e da defesa da DARKCOM.",
            durationSeconds = 30,
            formattedDuration = "00h 00m 30s",
            scenesDataJson = AnimeScene.serializeList(trailerScenes),
            isTrailer = true,
            viralYoutubeTitle = "TRAILER OFICIAL 4K: $projectTitle! 🔥😱 #Trailer #Anime",
            viralDescription = "Confira o trailer eletrizante de 30 segundos de $projectTitle, gerado pelo ANIME STUDIO PRO!\n\n#AnimeTrailer #Shorts #AnimeStudioPro",
            viralTags = "#Trailer, #AnimeTrailer, #DARKCOM, #Shorts, #AnimeFight"
        )
    }
}
