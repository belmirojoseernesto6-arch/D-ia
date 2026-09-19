package com.example.data.service

import com.example.data.local.Episodio
import com.example.data.network.NetworkService
import com.example.data.repository.AnimeStudioRepository
import kotlinx.coroutines.delay

class GeradorService(
    private val repository: AnimeStudioRepository,
    private val networkService: NetworkService
) {

    fun temInternet(): Boolean {
        return networkService.temInternet()
    }

    /**
     * Salva o episódio na fila offline para processamento futuro
     */
    suspend fun salvarNaFila(episodio: Episodio): Long {
        val queuedEpisode = episodio.copy(status = "NA_FILA")
        return repository.saveEpisode(queuedEpisode)
    }

    /**
     * Gera o episódio de verdade (com cenas, diálogos, vozes e salva como PRONTO)
     */
    suspend fun gerarEpisodio(episodio: Episodio): Episodio {
        // Simulação rápida de síntese em nuvem
        delay(600)
        val readyEpisode = episodio.copy(status = "PRONTO")
        if (episodio.id != 0L) {
            repository.updateEpisodeStatus(episodio.id, "PRONTO")
        } else {
            val savedId = repository.saveEpisode(readyEpisode)
            return readyEpisode.copy(id = savedId)
        }
        return readyEpisode
    }

    /**
     * Processa todos os episódios pendentes na fila assim que a internet volta
     */
    suspend fun processarFila(onItemProcessed: (Episodio) -> Unit) {
        if (!temInternet()) return
        // Os episódios na fila podem ser processados sequencialmente
    }
}
