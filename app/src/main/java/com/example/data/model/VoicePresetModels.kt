package com.example.data.model

data class VozPersonagem(
    val personagem: String, // "Dr. Fischer"
    val estilo: String, // "cientista_inteligente"
    val pitch: Float, // 0.3 grave até 1.5 agudo
    val velocidade: Float, // 0.8 lento até 1.3 rápido
    val efeito: String = "normal" // "normal", "eco_demonio", "radio_tv"
)

// VOZES PRONTAS - IGUAL AO SEU VÍDEO
val vozes = mapOf(
    "presidente" to VozPersonagem("Presidente", "grave_autoridade", 0.7f, 0.85f, "normal"),
    "cientista" to VozPersonagem("Dr Fischer", "inteligente_calmo", 1.0f, 0.9f, "normal"),
    "apresentador" to VozPersonagem("Apresentador", "gritando_sensacionalista", 1.1f, 1.2f, "radio_tv"),
    "dante" to VozPersonagem("Dante", "heroi_debochado", 1.0f, 1.1f, "normal"),
    "demonio" to VozPersonagem("White Rabbit", "demonio_terror", 0.5f, 0.8f, "eco_demonio"),
    "dona_diner" to VozPersonagem("Dona do Diner", "mulher_fofa", 1.3f, 1.0f, "normal")
)
