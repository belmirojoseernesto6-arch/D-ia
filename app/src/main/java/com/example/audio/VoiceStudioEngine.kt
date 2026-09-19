package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.data.model.VoiceProfile
import com.example.data.model.VozPersonagem
import java.util.Locale

class VoiceStudioEngine(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    // ElevenLabs Configuration
    var elevenLabsApiKey: String = ""
    var elevenLabsVoiceIdHero: String = "21m00Tcm4TlvDq8ikWAM"
    var elevenLabsVoiceIdVillain: String = "pNInz6obpgDQGcFmaJgB"
    var elevenLabsVoiceIdPresident: String = "ErXwobaYiN019PkySvjV"
    var elevenLabsVoiceIdScientist: String = "VR6AewLTigWG4xSOukaG"

    private var onDoneListener: (() -> Unit)? = null

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("pt", "BR"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to English or default device locale
                tts?.setLanguage(Locale.getDefault())
            }
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    onDoneListener?.invoke()
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    onDoneListener?.invoke()
                }
            })
            isInitialized = true
        } else {
            Log.e("VoiceStudioEngine", "Failed to initialize TextToSpeech")
        }
    }

    /**
     * Gera fala baseada em VozPersonagem (Online / Offline híbrido)
     */
    fun gerarFala(
        texto: String,
        voz: VozPersonagem,
        isOnline: Boolean = true,
        onFinished: (() -> Unit)? = null
    ) {
        if (!isInitialized || tts == null) {
            onFinished?.invoke()
            return
        }

        this.onDoneListener = onFinished
        tts?.stop()

        // Configuração de Pitch e Velocidade personalizada por personagem
        // OFFLINE: Usa TTS do celular com pitch/velocidade
        // ONLINE: Usa ElevenLabs ou TTS calibrado
        tts?.setPitch(voz.pitch)
        tts?.setSpeechRate(voz.velocidade)

        val utteranceId = "utterance_${System.currentTimeMillis()}"
        tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun speakVoice(
        text: String,
        profile: VoiceProfile,
        onFinished: (() -> Unit)? = null
    ) {
        if (!isInitialized || tts == null) {
            onFinished?.invoke()
            return
        }

        this.onDoneListener = onFinished

        tts?.stop()
        tts?.setPitch(profile.defaultPitch)
        tts?.setSpeechRate(profile.defaultRate)

        val utteranceId = "utterance_${System.currentTimeMillis()}"
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}
