package com.example.data.model

import com.example.data.local.CharacterEntity

object CharacterGeneratorLogic {

    fun generateCharactersFromStory(storyText: String, projectId: Long = 0L): List<CharacterEntity> {
        val lower = storyText.lowercase()

        val characters = mutableListOf<CharacterEntity>()

        // 1. Main Hero (Jovem Adulto or Adolescente)
        val heroName = when {
            lower.contains("kaito") -> "Kaito"
            lower.contains("ren") -> "Ren"
            lower.contains("shin") -> "Shin"
            lower.contains("lucas") || lower.contains("luka") -> "Luka Kurogane"
            else -> "Kaito Arashi"
        }
        characters.add(
            CharacterEntity(
                name = heroName,
                type = CharacterType.HEROI.name,
                clothing = "armadura de batalha futurista DARKCOM",
                age = 22,
                ageCategory = AgeCategory.JOVEM_ADULTO.key,
                voiceProfile = VoiceProfile.HEROI_JOVEM.key,
                prompt = AgeCategory.JOVEM_ADULTO.basePrompt,
                projectId = projectId,
                customVoicePhrase = "Minha lâmina de fótons não recuará diante do abismo!"
            )
        )

        // 2. Leader / President DARKCOM (Adulto)
        val presidentName = when {
            lower.contains("presidente") || lower.contains("darkcom") -> "Presidente Victor Vance"
            lower.contains("diretor") -> "Diretor Kenneth"
            else -> "Presidente Victor Vance"
        }
        characters.add(
            CharacterEntity(
                name = presidentName,
                type = CharacterType.HEROI.name,
                clothing = "terno azul presidente com insígnia",
                age = 35,
                ageCategory = AgeCategory.ADULTO.key,
                voiceProfile = VoiceProfile.PRESIDENTE.key,
                prompt = AgeCategory.ADULTO.basePrompt,
                projectId = projectId,
                customVoicePhrase = "A DARKCOM é a última muralha da Terra. Mantenham a linha!"
            )
        )

        // 3. Scientist or Wise General (Cientista / Idoso)
        if (lower.contains("portal") || lower.contains("paralelo") || lower.contains("cientista") || lower.contains("dimens")) {
            characters.add(
                CharacterEntity(
                    name = "Dra. Elena Shirogane",
                    type = CharacterType.HEROI.name,
                    clothing = "jaleco branco cientista quântico",
                    age = 29,
                    ageCategory = AgeCategory.ADULTO.key,
                    voiceProfile = VoiceProfile.CIENTISTA.key,
                    prompt = "anime female scientist 29 years old, intelligent expression, glasses, wearing white quantum lab coat over DARKCOM tech suit, glowing holographic screens, dark laboratory background, highly detailed",
                    projectId = projectId,
                    customVoicePhrase = "A oscilação dimensional está em 98.4%. Se o núcleo ceder, os universos colidem!"
                )
            )
        } else {
            characters.add(
                CharacterEntity(
                    name = "General Kazuki",
                    type = CharacterType.HEROI.name,
                    clothing = "farda militar verde com medalhas",
                    age = 65,
                    ageCategory = AgeCategory.IDOSO.key,
                    voiceProfile = VoiceProfile.PRESIDENTE.key,
                    prompt = AgeCategory.IDOSO.basePrompt,
                    projectId = projectId,
                    customVoicePhrase = "Eu vi as guerras do passado, soldados. Lutaremos com honra até o último segundo!"
                )
            )
        }

        // 4. Villain Demon General (Adulto / Monstro)
        val demonGeneralName = when {
            lower.contains("malakor") -> "Lorde Malakor"
            lower.contains("azazel") -> "General Azazel"
            lower.contains("baal") -> "Baal-Zul"
            else -> "General Demônio Malakor"
        }
        characters.add(
            CharacterEntity(
                name = demonGeneralName,
                type = CharacterType.VILAO.name,
                clothing = "armadura demônio com espinhos escuros",
                age = 38,
                ageCategory = AgeCategory.VILAO_ADULTO.key,
                voiceProfile = VoiceProfile.VILAO_MONSTRO.key,
                prompt = AgeCategory.VILAO_ADULTO.basePrompt,
                projectId = projectId,
                customVoicePhrase = "A carne dos mortais será o banquete do nosso imperador!"
            )
        )

        // 5. Final Demon King or Villain Child
        if (lower.contains("rei") || lower.contains("imperador") || lower.contains("paralelo") || lower.contains("destru")) {
            characters.add(
                CharacterEntity(
                    name = "Rei Demônio Abaddon (Forma Final)",
                    type = CharacterType.VILAO.name,
                    clothing = "manto cósmico com runas dimensionais",
                    age = 999,
                    ageCategory = AgeCategory.VILAO_REI_FINAL.key,
                    voiceProfile = VoiceProfile.VILAO_MONSTRO.key,
                    prompt = AgeCategory.VILAO_REI_FINAL.basePrompt,
                    projectId = projectId,
                    evolutionStage = 2,
                    customVoicePhrase = "Eu sou o fim do cosmos. Nenhum universo pode conter minha fúria!"
                )
            )
        } else {
            characters.add(
                CharacterEntity(
                    name = "Vesper, A Criança das Sombras",
                    type = CharacterType.VILAO.name,
                    clothing = "manto cósmico com runas dimensionais",
                    age = 12,
                    ageCategory = AgeCategory.VILAO_CRIANCA.key,
                    voiceProfile = VoiceProfile.HEROI_JOVEM.key,
                    prompt = AgeCategory.VILAO_CRIANCA.basePrompt,
                    projectId = projectId,
                    customVoicePhrase = "Vocês acham que podem fugir de mim? O portal já foi aberto, hehehe..."
                )
            )
        }

        // 6. Junior hero / Trainee if story hints at defense or learning
        characters.add(
            CharacterEntity(
                name = "Yuki (Recruta DARKCOM)",
                type = CharacterType.HEROI.name,
                clothing = "traje de treinamento hero trainee",
                age = 16,
                ageCategory = AgeCategory.ADOLESCENTE.key,
                voiceProfile = VoiceProfile.HEROI_JOVEM.key,
                prompt = AgeCategory.ADOLESCENTE.basePrompt,
                projectId = projectId,
                customVoicePhrase = "Eu posso ser apenas um recruta, mas darei minha vida por esta cidade!"
            )
        )

        return characters
    }

    fun evolveVillain(villain: CharacterEntity): CharacterEntity {
        return when (villain.ageCategory) {
            AgeCategory.VILAO_CRIANCA.key -> {
                villain.copy(
                    name = "${villain.name} (Despertar Demônio Adulto)",
                    age = 35,
                    ageCategory = AgeCategory.VILAO_ADULTO.key,
                    clothing = "armadura demônio com espinhos escuros",
                    voiceProfile = VoiceProfile.VILAO_MONSTRO.key,
                    prompt = AgeCategory.VILAO_ADULTO.basePrompt,
                    evolutionStage = 1,
                    customVoicePhrase = "Minha verdadeira forma emergiu das profundezas do abismo!"
                )
            }
            AgeCategory.VILAO_ADULTO.key -> {
                villain.copy(
                    name = "${villain.name} (REI DEMÔNIO SUPREMO)",
                    age = 999,
                    ageCategory = AgeCategory.VILAO_REI_FINAL.key,
                    clothing = "armadura demoníaca colossal de 4 metros e asas cósmicas",
                    voiceProfile = VoiceProfile.VILAO_MONSTRO.key,
                    prompt = AgeCategory.VILAO_REI_FINAL.basePrompt,
                    evolutionStage = 2,
                    customVoicePhrase = "Testemunhem a extinção dos mortais! O Reino Demoníaco reina supremo!"
                )
            }
            else -> {
                villain.copy(
                    name = "${villain.name} [TRANSCENDÊNCIA CÓSMICA FINAL]",
                    evolutionStage = villain.evolutionStage + 1,
                    clothing = "manto dimensional estelar com asas de destruição",
                    prompt = "${villain.prompt}, ascended ultimate demon form, planetary collision, hyper realistic anime masterpiece, 8k render",
                    customVoicePhrase = "Nem o espaço nem o tempo podem me aprisionar agora!"
                )
            }
        }
    }
}
