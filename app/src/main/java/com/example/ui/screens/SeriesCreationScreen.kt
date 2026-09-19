package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SeriesTimeCalculation
import com.example.data.model.TimeConfig
import com.example.ui.components.CharacterCard
import com.example.ui.components.CounterStepper
import com.example.ui.components.QuickStoryEditor
import com.example.ui.components.TimePickerRow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PortalRed
import com.example.ui.viewmodel.AnimeStudioUiState
import com.example.ui.viewmodel.AnimeStudioViewModel

@Composable
fun SeriesCreationScreen(
    viewModel: AnimeStudioViewModel,
    uiState: AnimeStudioUiState,
    modifier: Modifier = Modifier
) {
    val timeCalculation = SeriesTimeCalculation(
        episodeTime = TimeConfig(uiState.seriesEpisodeHours, uiState.seriesEpisodeMinutes, uiState.seriesEpisodeSeconds),
        seasons = uiState.seriesSeasons,
        episodesPerSeason = uiState.seriesEpisodesPerSeason
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // 1. TELA DE CRIAÇÃO DE HISTÓRIA
            QuickStoryEditor(
                storyText = uiState.seriesStoryText,
                onStoryChange = { viewModel.updateSeriesStory(it) },
                onGenerateCharacters = { viewModel.generateCharactersForSeries() },
                onShareStory = { viewModel.shareText("História DARKCOM", it) },
                isGenerating = uiState.isGenerating
            )
        }

        item {
            // 2. CONTROLE DE TEMPO (Dentro de Série)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonCyan.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CONTROLE DE TEMPO DA SÉRIE",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Duração de CADA Episódio:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TimePickerRow(
                        hours = uiState.seriesEpisodeHours,
                        minutes = uiState.seriesEpisodeMinutes,
                        seconds = uiState.seriesEpisodeSeconds,
                        onTimeChanged = { h, m, s -> viewModel.updateSeriesTime(h, m, s) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Counter steppers for Temporadas e Episódios por Temporada
                    CounterStepper(
                        title = "Quantas Temporadas",
                        value = uiState.seriesSeasons,
                        range = 1..10,
                        onValueChange = { viewModel.updateSeriesCounts(it, uiState.seriesEpisodesPerSeason) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CounterStepper(
                        title = "Episódios por Temporada",
                        value = uiState.seriesEpisodesPerSeason,
                        range = 1..26,
                        onValueChange = { viewModel.updateSeriesCounts(uiState.seriesSeasons, it) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Mostrar cálculo automático: "Total da Temporada: 2h 00m 00s"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total da Temporada:",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = timeCalculation.formattedSeasonTotal(),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = NeonCyan
                                )
                            }
                            if (uiState.seriesSeasons > 1) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Total da Série (${uiState.seriesSeasons} Temporadas):",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = timeCalculation.formattedSeriesTotal(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            // 8. EXPORTAÇÃO & GERADOR (gera 1 episódio por vez, junta cenas + vozes + música)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PortalRed.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MovieCreation,
                            contentDescription = null,
                            tint = PortalRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ESTÚDIO DE EXPORTAÇÃO",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gera 1 episódio por vez e salva no banco offline para reprodução nativa imediata.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botão [GERAR VÍDEO DO EPISÓDIO]
                    Button(
                        onClick = { viewModel.generateSingleEpisode(isSeries = true) },
                        enabled = !uiState.isGenerating,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_generate_episode_series")
                    ) {
                        if (uiState.isGenerating) {
                            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sintetizando Cenas + Vozes + Trilha...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("GERAR VÍDEO DO EPISÓDIO ${uiState.nextEpisodeToGenerate}", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Botão [GERAR TRAILER DE 30s]
                        Button(
                            onClick = { viewModel.generateTrailer(isSeries = true) },
                            enabled = !uiState.isGenerating,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PortalRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_generate_trailer_series")
                        ) {
                            Icon(imageVector = Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("TRAILER DE 30s", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        // Botão [EXPORTAR PARA YOUTUBE SHORTS]
                        Button(
                            onClick = {
                                viewModel.generateTrailer(isSeries = true)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_export_shorts_series")
                        ) {
                            Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("YOUTUBE SHORTS", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        item {
            // 3. PERSONAGENS GERADOS HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.People, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PERSONAGENS (${uiState.seriesCharacters.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = NeonCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Heróis & Vilões",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // List of Character Cards
        items(uiState.seriesCharacters, key = { it.name + it.id }) { character ->
            CharacterCard(
                character = character,
                onCopyPrompt = { viewModel.copyToClipboard("Prompt Anime", character.prompt) },
                onChangeOutfit = { viewModel.cycleOutfit(character, isSeries = true) },
                onPlayVoice = { viewModel.playVoice(character) },
                onEvolveVillain = { viewModel.evolveCharacter(character, isSeries = true) },
                onRemove = { viewModel.removeCharacter(character, isSeries = true) },
                onToggleFavorite = { viewModel.toggleFavorite(character, isSeries = true) },
                onShareCharacter = { text -> viewModel.shareText("Personagem DARKCOM", text) },
                onCopyCharacterData = { text -> viewModel.copyToClipboard("Personagem", text) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
