package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.VoiceStudioEngine
import com.example.data.local.AnimeStudioDatabase
import com.example.data.local.CharacterEntity
import com.example.data.local.GeneratedEpisodeEntity
import com.example.data.local.ProjectEntity
import com.example.data.model.AVAILABLE_OUTFITS
import com.example.data.model.CharacterGeneratorLogic
import com.example.data.model.CharacterType
import com.example.data.model.EpisodeGeneratorLogic
import com.example.data.model.MovieTimeCalculation
import com.example.data.model.SeriesTimeCalculation
import com.example.data.model.TimeConfig
import com.example.data.model.VoiceProfile
import com.example.data.repository.AnimeStudioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AnimeStudioUiState(
    val selectedTab: Int = 0, // 0 = Série, 1 = Filme, 2 = Galeria / Biblioteca
    // Série
    val seriesStoryText: String = "",
    val seriesTitle: String = "DARKCOM: DEFESA TERRESTRE",
    val seriesEpisodeHours: Int = 0,
    val seriesEpisodeMinutes: Int = 24,
    val seriesEpisodeSeconds: Int = 0,
    val seriesSeasons: Int = 1,
    val seriesEpisodesPerSeason: Int = 10,
    val nextEpisodeToGenerate: Int = 1,
    val seriesCharacters: List<CharacterEntity> = emptyList(),

    // Filme
    val movieStoryText: String = "",
    val movieTitle: String = "DARKCOM: O DIA DO JUÍZO CÓSMICO",
    val movieHours: Int = 1,
    val movieMinutes: Int = 30,
    val movieSeconds: Int = 0,
    val movieCharacters: List<CharacterEntity> = emptyList(),

    // Modals & Overlays
    val isBudgetDialogOpen: Boolean = false,
    val isElevenLabsDialogOpen: Boolean = false,
    val activeVideoPlayerEpisode: GeneratedEpisodeEntity? = null,
    val activeYoutubeExportEpisode: GeneratedEpisodeEntity? = null,
    val isGenerating: Boolean = false,
    val statusMessage: String? = null
)

class AnimeStudioViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AnimeStudioDatabase.getInstance(application)
    val repository = AnimeStudioRepository(db.animeStudioDao())
    val voiceEngine = VoiceStudioEngine(application)

    private val _uiState = MutableStateFlow(
        AnimeStudioUiState(
            seriesStoryText = "Empresa DARKCOM protege a Terra de demônios de universo paralelo. Em uma noite de tempestade cósmica, a fenda Ômega se abre na baía de Neo-Tokyo.",
            movieStoryText = "Um antigo portal demoníaco desperta sob a base secreta da DARKCOM. O Rei Demônio Abaddon tenta fundir a dimensão das trevas com a Terra antes que o eclipse termine."
        )
    )
    val uiState: StateFlow<AnimeStudioUiState> = _uiState.asStateFlow()

    // Room Flows
    val savedCharacters: StateFlow<List<CharacterEntity>> = repository.allCharacters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteCharacters: StateFlow<List<CharacterEntity>> = repository.favoriteCharacters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedEpisodes: StateFlow<List<GeneratedEpisodeEntity>> = repository.allEpisodes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Generate initial characters for default story
        generateCharactersForSeries()
        generateCharactersForMovie()
    }

    override fun onCleared() {
        super.onCleared()
        voiceEngine.release()
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    // Series controls
    fun updateSeriesStory(text: String) {
        _uiState.update { it.copy(seriesStoryText = text) }
    }

    fun updateSeriesTime(hours: Int, minutes: Int, seconds: Int) {
        _uiState.update {
            it.copy(
                seriesEpisodeHours = hours.coerceIn(0, 23),
                seriesEpisodeMinutes = minutes.coerceIn(0, 59),
                seriesEpisodeSeconds = seconds.coerceIn(0, 59)
            )
        }
    }

    fun updateSeriesCounts(seasons: Int, episodesPerSeason: Int) {
        _uiState.update {
            it.copy(
                seriesSeasons = seasons.coerceIn(1, 20),
                seriesEpisodesPerSeason = episodesPerSeason.coerceIn(1, 50)
            )
        }
    }

    // Movie controls
    fun updateMovieStory(text: String) {
        _uiState.update { it.copy(movieStoryText = text) }
    }

    fun updateMovieTime(hours: Int, minutes: Int, seconds: Int) {
        _uiState.update {
            it.copy(
                movieHours = hours.coerceIn(0, 23),
                movieMinutes = minutes.coerceIn(0, 59),
                movieSeconds = seconds.coerceIn(0, 59)
            )
        }
    }

    // Story Character Generation
    fun generateCharactersForSeries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true) }
            val chars = CharacterGeneratorLogic.generateCharactersFromStory(_uiState.value.seriesStoryText)
            _uiState.update { it.copy(seriesCharacters = chars, isGenerating = false) }
            // Auto persist to Room
            repository.saveCharacters(chars)
            showToast("Personagens gerados com sucesso para a Série! ⚔️")
        }
    }

    fun generateCharactersForMovie() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true) }
            val chars = CharacterGeneratorLogic.generateCharactersFromStory(_uiState.value.movieStoryText)
            _uiState.update { it.copy(movieCharacters = chars, isGenerating = false) }
            // Auto persist to Room
            repository.saveCharacters(chars)
            showToast("Personagens gerados com sucesso para o Filme! 🎬")
        }
    }

    // Character Card Actions
    fun cycleOutfit(character: CharacterEntity, isSeries: Boolean) {
        val currentIndex = AVAILABLE_OUTFITS.indexOf(character.clothing)
        val nextIndex = if (currentIndex == -1 || currentIndex == AVAILABLE_OUTFITS.lastIndex) 0 else currentIndex + 1
        val newOutfit = AVAILABLE_OUTFITS[nextIndex]
        val updated = character.copy(clothing = newOutfit)

        updateCharacterInState(updated, isSeries)
        viewModelScope.launch { repository.updateCharacter(updated) }
        showToast("Roupa trocada: $newOutfit 👔")
    }

    fun evolveCharacter(character: CharacterEntity, isSeries: Boolean) {
        if (character.type != CharacterType.VILAO.name) {
            showToast("Apenas vilões podem evoluir! 😈")
            return
        }
        val evolved = CharacterGeneratorLogic.evolveVillain(character)
        updateCharacterInState(evolved, isSeries)
        viewModelScope.launch { repository.updateCharacter(evolved) }
        showToast("Vilão evoluído para ${evolved.name}! 😈🔥")
        playVoice(evolved)
    }

    fun removeCharacter(character: CharacterEntity, isSeries: Boolean) {
        if (isSeries) {
            _uiState.update { state ->
                state.copy(seriesCharacters = state.seriesCharacters.filter { it.id != character.id && it.name != character.name })
            }
        } else {
            _uiState.update { state ->
                state.copy(movieCharacters = state.movieCharacters.filter { it.id != character.id && it.name != character.name })
            }
        }
        viewModelScope.launch { repository.deleteCharacter(character) }
        showToast("Personagem removido 🗑️")
    }

    fun toggleFavorite(character: CharacterEntity, isSeries: Boolean) {
        val newFav = !character.isFavorite
        val updated = character.copy(isFavorite = newFav)
        updateCharacterInState(updated, isSeries)
        viewModelScope.launch {
            repository.updateCharacter(updated)
            repository.toggleFavorite(character.id, newFav)
        }
        showToast(if (newFav) "Salvo na Biblioteca de Favoritos! ⭐" else "Removido dos Favoritos")
    }

    private fun updateCharacterInState(updated: CharacterEntity, isSeries: Boolean) {
        if (isSeries) {
            _uiState.update { state ->
                state.copy(seriesCharacters = state.seriesCharacters.map {
                    if ((it.id != 0L && it.id == updated.id) || it.name == updated.name) updated else it
                })
            }
        } else {
            _uiState.update { state ->
                state.copy(movieCharacters = state.movieCharacters.map {
                    if ((it.id != 0L && it.id == updated.id) || it.name == updated.name) updated else it
                })
            }
        }
    }

    // Audio & Voice Studio
    fun playVoice(character: CharacterEntity) {
        val profile = VoiceProfile.fromKey(character.voiceProfile)
        val textToSpeak = if (character.customVoicePhrase.isNotBlank()) character.customVoicePhrase else profile.sampleText
        voiceEngine.speakVoice(textToSpeak, profile)
    }

    fun playCustomVoice(text: String, voiceProfileKey: String) {
        val profile = VoiceProfile.fromKey(voiceProfileKey)
        voiceEngine.speakVoice(text, profile)
    }

    // Clipboard & Share actions as requested
    fun copyToClipboard(label: String, text: String) {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        showToast("Copiado! ✅")
    }

    fun shareText(title: String, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, text)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chooser = Intent.createChooser(intent, "Partilhar: $title").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        getApplication<Application>().startActivity(chooser)
    }

    // Episode Generation (1 episode at a time, saved offline in Room)
    fun generateSingleEpisode(isSeries: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true) }
            val state = _uiState.value
            val characters = if (isSeries) state.seriesCharacters else state.movieCharacters
            val projectTitle = if (isSeries) state.seriesTitle else state.movieTitle
            val projectMode = if (isSeries) "SERIE" else "FILME"
            val epNum = if (isSeries) state.nextEpisodeToGenerate else 1
            val durationSec = if (isSeries) {
                TimeConfig(state.seriesEpisodeHours, state.seriesEpisodeMinutes, state.seriesEpisodeSeconds).totalSeconds
            } else {
                TimeConfig(state.movieHours, state.movieMinutes, state.movieSeconds).totalSeconds
            }

            val newEpisode = EpisodeGeneratorLogic.generateEpisode(
                projectId = 1L,
                projectTitle = projectTitle,
                projectMode = projectMode,
                episodeNum = epNum,
                seasonNum = 1,
                storyText = if (isSeries) state.seriesStoryText else state.movieStoryText,
                characters = characters,
                durationSeconds = if (durationSec > 0) durationSec else 1440
            )

            val savedId = repository.saveEpisode(newEpisode)
            val fullEpisode = newEpisode.copy(id = savedId)

            if (isSeries) {
                _uiState.update {
                    it.copy(
                        nextEpisodeToGenerate = it.nextEpisodeToGenerate + 1,
                        isGenerating = false,
                        activeVideoPlayerEpisode = fullEpisode
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        activeVideoPlayerEpisode = fullEpisode
                    )
                }
            }
            showToast("Episódio gerado com sucesso e salvo offline! 🎬✨")
        }
    }

    fun generateTrailer(isSeries: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true) }
            val state = _uiState.value
            val characters = if (isSeries) state.seriesCharacters else state.movieCharacters
            val projectTitle = if (isSeries) state.seriesTitle else state.movieTitle
            val projectMode = if (isSeries) "SERIE" else "FILME"

            val trailer = EpisodeGeneratorLogic.generateTrailer30s(
                projectId = 1L,
                projectTitle = projectTitle,
                projectMode = projectMode,
                storyText = if (isSeries) state.seriesStoryText else state.movieStoryText,
                characters = characters
            )

            val savedId = repository.saveEpisode(trailer)
            val fullTrailer = trailer.copy(id = savedId)

            _uiState.update {
                it.copy(
                    isGenerating = false,
                    activeVideoPlayerEpisode = fullTrailer
                )
            }
            showToast("Trailer de 30s gerado com sucesso! 🚀🔥")
        }
    }

    fun deleteEpisode(episode: GeneratedEpisodeEntity) {
        viewModelScope.launch {
            repository.deleteEpisodeById(episode.id)
            showToast("Episódio excluído da galeria 🗑️")
        }
    }

    // Video Player & YouTube Modal
    fun openVideoPlayer(episode: GeneratedEpisodeEntity) {
        _uiState.update { it.copy(activeVideoPlayerEpisode = episode) }
    }

    fun closeVideoPlayer() {
        voiceEngine.stop()
        _uiState.update { it.copy(activeVideoPlayerEpisode = null) }
    }

    fun openYoutubeExport(episode: GeneratedEpisodeEntity) {
        _uiState.update { it.copy(activeYoutubeExportEpisode = episode) }
    }

    fun closeYoutubeExport() {
        _uiState.update { it.copy(activeYoutubeExportEpisode = null) }
    }

    fun toggleBudgetDialog(open: Boolean) {
        _uiState.update { it.copy(isBudgetDialogOpen = open) }
    }

    fun toggleElevenLabsDialog(open: Boolean) {
        _uiState.update { it.copy(isElevenLabsDialogOpen = open) }
    }

    private fun showToast(msg: String) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}
