package com.example.data.model

enum class CharacterType(val label: String, val badgeColor: Long) {
    HEROI("Herói", 0xFF00E5FF),
    VILAO("Vilão", 0xFFFF1744)
}

enum class AgeCategory(
    val key: String,
    val title: String,
    val ageRange: String,
    val defaultAge: Int,
    val isVillain: Boolean,
    val basePrompt: String
) {
    CRIANCA(
        key = "CRIANCA",
        title = "Criança (8-12 anos)",
        ageRange = "8-12",
        defaultAge = 10,
        isVillain = false,
        basePrompt = "anime boy 10 years old, short, innocent face, big eyes, wearing DARKCOM junior uniform, chibi style but cinematic, dark meeting room background, 8k"
    ),
    ADOLESCENTE(
        key = "ADOLESCENTE",
        title = "Adolescente (13-17 anos)",
        ageRange = "13-17",
        defaultAge = 16,
        isVillain = false,
        basePrompt = "anime teenager 16 years old, hero trainee, spiky hair, wearing DARKCOM training suit, determined expression, aura energy, cinematic lighting"
    ),
    JOVEM_ADULTO(
        key = "JOVEM_ADULTO",
        title = "Jovem Adulto (18-25 anos)",
        ageRange = "18-25",
        defaultAge = 22,
        isVillain = false,
        basePrompt = "anime young adult 22 years old, main hero, muscular, wearing futuristic battle armor, cosmic aura, dramatic pose, ultra detailed"
    ),
    ADULTO(
        key = "ADULTO",
        title = "Adulto (26-45 anos)",
        ageRange = "26-45",
        defaultAge = 35,
        isVillain = false,
        basePrompt = "anime adult 35 years old, DARKCOM president, blue suit, white hat, serious leader, sitting at dark conference table, cinematic"
    ),
    IDOSO(
        key = "IDOSO",
        title = "Idoso (50-70 anos)",
        ageRange = "50-70",
        defaultAge = 65,
        isVillain = false,
        basePrompt = "anime elder 65 years old, wise general, military uniform with medals, white beard, wise face, commanding presence, dark room"
    ),
    VILAO_CRIANCA(
        key = "VILAO_CRIANCA",
        title = "Vilão Criança",
        ageRange = "8-14",
        defaultAge = 12,
        isVillain = true,
        basePrompt = "anime demon child 12 years old, evil smile, red eyes, small horns, dark energy, creepy"
    ),
    VILAO_ADULTO(
        key = "VILAO_ADULTO",
        title = "Vilão Adulto",
        ageRange = "25-50",
        defaultAge = 38,
        isVillain = true,
        basePrompt = "anime demon general, tall muscular monster, large horns, red skin, wearing dark armor, Demon Realm portal background, terrifying"
    ),
    VILAO_REI_FINAL(
        key = "VILAO_REI_FINAL",
        title = "Vilão Rei Final",
        ageRange = "Ancestral",
        defaultAge = 999,
        isVillain = true,
        basePrompt = "anime demon king final form, gigantic demon, 4 meters tall, wings, cosmic destruction aura, Earth Realm vs Demon Realm background, epic"
    );

    companion object {
        fun fromKey(key: String): AgeCategory {
            return entries.find { it.key == key } ?: JOVEM_ADULTO
        }
    }
}

enum class VoiceProfile(
    val key: String,
    val title: String,
    val description: String,
    val defaultPitch: Float,
    val defaultRate: Float,
    val sampleText: String
) {
    HEROI_JOVEM(
        key = "HEROI_JOVEM",
        title = "Herói Jovem",
        description = "Aguda energética, determinação inabalável",
        defaultPitch = 1.35f,
        defaultRate = 1.15f,
        sampleText = "Eu juro pela DARKCOM: nenhum demônio tocará no nosso planeta!"
    ),
    VILAO_MONSTRO(
        key = "VILAO_MONSTRO",
        title = "Vilão Monstro",
        description = "Grave distorcida, ressonância demoníaca",
        defaultPitch = 0.55f,
        defaultRate = 0.85f,
        sampleText = "Humanos insignificantes... O Reino Demoníaco consumirá este mundo!"
    ),
    PRESIDENTE(
        key = "PRESIDENTE",
        title = "Presidente",
        description = "Grave séria, autoridade e liderança militar",
        defaultPitch = 0.80f,
        defaultRate = 0.95f,
        sampleText = "Atenção todas as divisões DARKCOM: ativar Protocolo Ômega imediatamente."
    ),
    CIENTISTA(
        key = "CIENTISTA",
        title = "Cientista",
        description = "Inteligente calma, lógica e analítica",
        defaultPitch = 1.05f,
        defaultRate = 1.00f,
        sampleText = "Analisando a assinatura quântica da fenda dimensional. A barreira está colapsando!"
    );

    companion object {
        fun fromKey(key: String): VoiceProfile {
            return entries.find { it.key == key } ?: HEROI_JOVEM
        }
    }
}

val AVAILABLE_OUTFITS = listOf(
    "terno azul presidente com insígnia",
    "farda militar verde com medalhas",
    "jaleco branco cientista quântico",
    "armadura demônio com espinhos escuros",
    "armadura de batalha futurista DARKCOM",
    "manto cósmico com runas dimensionais",
    "traje de treinamento hero trainee"
)
