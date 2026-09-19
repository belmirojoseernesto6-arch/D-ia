package com.example.data.repository

import com.example.data.local.AnimeStudioDao
import com.example.data.local.CharacterEntity
import com.example.data.local.GeneratedEpisodeEntity
import com.example.data.local.ProjectEntity
import kotlinx.coroutines.flow.Flow

class AnimeStudioRepository(private val dao: AnimeStudioDao) {

    val allCharacters: Flow<List<CharacterEntity>> = dao.getAllCharacters()
    val favoriteCharacters: Flow<List<CharacterEntity>> = dao.getFavoriteCharacters()
    val allProjects: Flow<List<ProjectEntity>> = dao.getAllProjects()
    val allEpisodes: Flow<List<GeneratedEpisodeEntity>> = dao.getAllEpisodes()
    val queuedEpisodes: Flow<List<GeneratedEpisodeEntity>> = dao.getQueuedEpisodes()
    val completedEpisodes: Flow<List<GeneratedEpisodeEntity>> = dao.getCompletedEpisodes()

    fun getCharactersForProject(projectId: Long): Flow<List<CharacterEntity>> =
        dao.getCharactersForProject(projectId)

    suspend fun saveCharacter(character: CharacterEntity): Long =
        dao.insertCharacter(character)

    suspend fun saveCharacters(characters: List<CharacterEntity>) =
        dao.insertCharacters(characters)

    suspend fun updateCharacter(character: CharacterEntity) =
        dao.updateCharacter(character)

    suspend fun deleteCharacterById(id: Long) =
        dao.deleteCharacterById(id)

    suspend fun deleteCharacter(character: CharacterEntity) =
        dao.deleteCharacterById(character.id)

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) =
        dao.updateFavorite(id, isFavorite)

    suspend fun saveProject(project: ProjectEntity): Long =
        dao.insertProject(project)

    suspend fun updateProject(project: ProjectEntity) =
        dao.updateProject(project)

    suspend fun deleteProjectById(id: Long) =
        dao.deleteProjectById(id)

    suspend fun saveEpisode(episode: GeneratedEpisodeEntity): Long =
        dao.insertEpisode(episode)

    suspend fun updateEpisodeStatus(id: Long, status: String) =
        dao.updateEpisodeStatus(id, status)

    suspend fun deleteEpisodeById(id: Long) =
        dao.deleteEpisodeById(id)
}
