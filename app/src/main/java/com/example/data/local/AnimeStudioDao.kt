package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeStudioDao {

    // Characters
    @Query("SELECT * FROM characters ORDER BY id DESC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY id DESC")
    fun getFavoriteCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE projectId = :projectId ORDER BY id ASC")
    fun getCharactersForProject(projectId: Long): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Update
    suspend fun updateCharacter(character: CharacterEntity)

    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteCharacterById(id: Long)

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    // Projects
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Long): Flow<ProjectEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)

    // Episodes & Generated Videos
    @Query("SELECT * FROM episodes ORDER BY createdAt DESC")
    fun getAllEpisodes(): Flow<List<GeneratedEpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE status = 'NA_FILA' ORDER BY createdAt ASC")
    fun getQueuedEpisodes(): Flow<List<GeneratedEpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE status != 'NA_FILA' ORDER BY createdAt DESC")
    fun getCompletedEpisodes(): Flow<List<GeneratedEpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE projectId = :projectId ORDER BY episodeNumber ASC")
    fun getEpisodesForProject(projectId: Long): Flow<List<GeneratedEpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE id = :id")
    fun getEpisodeById(id: Long): Flow<GeneratedEpisodeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: GeneratedEpisodeEntity): Long

    @Query("UPDATE episodes SET status = :status WHERE id = :id")
    suspend fun updateEpisodeStatus(id: Long, status: String)

    @Query("DELETE FROM episodes WHERE id = :id")
    suspend fun deleteEpisodeById(id: Long)
}
