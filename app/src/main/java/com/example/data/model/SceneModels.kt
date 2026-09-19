package com.example.data.model

import org.json.JSONArray
import org.json.JSONObject

data class AnimeScene(
    val sceneNumber: Int,
    val title: String,
    val backgroundType: String, // "DARKCOM_MEETING_ROOM", "DEMON_PORTAL", "NEON_CITY_RUINS", "QUANTUM_LAB", "COSMIC_VOID"
    val visualPrompt: String,
    val speakerName: String,
    val speakerType: String, // HEROI or VILAO
    val dialogueText: String,
    val voiceProfile: String,
    val durationSeconds: Int = 6,
    val effectType: String = "NEON_PULSE" // "NEON_PULSE", "PORTAL_VORTEX", "LIGHTNING", "QUANTUM_GRID"
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("sceneNumber", sceneNumber)
            put("title", title)
            put("backgroundType", backgroundType)
            put("visualPrompt", visualPrompt)
            put("speakerName", speakerName)
            put("speakerType", speakerType)
            put("dialogueText", dialogueText)
            put("voiceProfile", voiceProfile)
            put("durationSeconds", durationSeconds)
            put("effectType", effectType)
        }
    }

    companion object {
        fun fromJson(obj: JSONObject): AnimeScene {
            return AnimeScene(
                sceneNumber = obj.optInt("sceneNumber", 1),
                title = obj.optString("title", "Cena"),
                backgroundType = obj.optString("backgroundType", "DARKCOM_MEETING_ROOM"),
                visualPrompt = obj.optString("visualPrompt", ""),
                speakerName = obj.optString("speakerName", "DARKCOM"),
                speakerType = obj.optString("speakerType", "HEROI"),
                dialogueText = obj.optString("dialogueText", "..."),
                voiceProfile = obj.optString("voiceProfile", "HEROI_JOVEM"),
                durationSeconds = obj.optInt("durationSeconds", 6),
                effectType = obj.optString("effectType", "NEON_PULSE")
            )
        }

        fun parseList(jsonString: String): List<AnimeScene> {
            if (jsonString.isBlank()) return emptyList()
            val list = mutableListOf<AnimeScene>()
            try {
                val array = JSONArray(jsonString)
                for (i in 0 until array.length()) {
                    list.add(fromJson(array.getJSONObject(i)))
                }
            } catch (_: Exception) {
                // Return empty if parsing failed
            }
            return list
        }

        fun serializeList(scenes: List<AnimeScene>): String {
            val array = JSONArray()
            scenes.forEach { array.put(it.toJson()) }
            return array.toString()
        }
    }
}
