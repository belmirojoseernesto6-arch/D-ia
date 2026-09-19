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
import androidx.compose.material.icons.filled.Movie
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
import com.example.data.model.MovieActInfo
import com.example.data.model.MovieTimeCalculation
import com.example.data.model.TimeConfig
import com.example.ui.components.CharacterCard
import com.example.ui.components.QuickStoryEditor
import com.example.ui.components.TimePickerRow
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PortalRed
import com.example.ui.viewmodel.AnimeStudioUiState
import com.example.ui.viewmodel.AnimeStudioViewModel

@Composable
fun MovieCreationScreen(
    viewModel: AnimeStudioViewModel,
    uiState: AnimeStudioUiState,
    modifier: Modifier = Modifier
) {
    val movieCalculation = MovieTimeCalculation(
        movieTime = TimeConfig(uiState.movieHours, uiState.movieMinutes, uiState.movieSeconds)
    )
    val acts = movieCalculation.getActs()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // 1. TELA DE CRIAÇÃO DE HISTÓRIA DO FILME
            QuickStoryEditor(
                storyText = uiState.movieStoryText,
                onStoryChange = { viewModel.updateMovieStory(it) },
                onGenerateCharacters = { viewModel.generateCharactersForMovie() },
                onShareStory = { viewModel.shareText("História Filme DARKCOM", it) },
                isGenerating = uiState.isGenerating
            )
        }

        item {
            // 2. CONTROLE DE TEMPO & DIVISÃO EM 3 ATOS (Para FILME)
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
                            imageVector = Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = PortalRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CONTROLE DE TEMPO DO FILME",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Duração Total do Filme (ex: 01h 30m 00s):",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TimePickerRow(
                        hours = uiState.movieHours,
                        minutes = uiState.movieMinutes,
                        seconds = uiState.movieSeconds,
                        onTimeChanged = { h, m, s -> viewModel.updateMovieTime(h, m, s) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Mostrador da duração total
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Duração Total:",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = movieCalculation.formattedTotal(),
                                fontWeight = FontWeight.ExtraBold,
                                color = PortalRed,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // DIVISÃO EM 3 ATOS AUTOMATICAMENTE
                    Text(
                        text = "DIVISÃO AUTOMÁTICA EM 3 ATOS:",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    acts.forEach { act ->
                        MovieActCard(act = act)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        item {
            // 8. EXPORTAÇÃO DO FILME
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
                            imageVector = Icons.Default.MovieCreation,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ESTÚDIO DE EXPORTAÇÃO DO FILME",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Junta cenas + vozes + trilha orquestral cósmica em um filme cinematográfico.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botão [GERAR VÍDEO DO EPISÓDIO / FILME]
                    Button(
                        onClick = { viewModel.generateSingleEpisode(isSeries = false) },
                        enabled = !uiState.isGenerating,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PortalRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_generate_movie_episode")
                    ) {
                        if (uiState.isGenerating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Renderizando Cenas + Vozes...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("GERAR VÍDEO DO FILME (ATO 1)", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Botão [GERAR TRAILER DE 30s]
                        Button(
                            onClick = { viewModel.generateTrailer(isSeries = false) },
                            enabled = !uiState.isGenerating,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_generate_trailer_movie")
                        ) {
                            Icon(imageVector = Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("TRAILER DE 30s", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        // Botão [EXPORTAR PARA YOUTUBE SHORTS]
                        Button(
                            onClick = { viewModel.generateTrailer(isSeries = false) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_export_shorts_movie")
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
            // Header Personagens do Filme
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.People, contentDescription = null, tint = PortalRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ELENCO DO FILME (${uiState.movieCharacters.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = PortalRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Darkcom vs Abismo",
                        color = PortalRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        items(uiState.movieCharacters, key = { it.name + it.id }) { character ->
            CharacterCard(
                character = character,
                onCopyPrompt = { viewModel.copyToClipboard("Prompt Filme", character.prompt) },
                onChangeOutfit = { viewModel.cycleOutfit(character, isSeries = false) },
                onPlayVoice = { viewModel.playVoice(character) },
                onEvolveVillain = { viewModel.evolveCharacter(character, isSeries = false) },
                onRemove = { viewModel.removeCharacter(character, isSeries = false) },
                onToggleFavorite = { viewModel.toggleFavorite(character, isSeries = false) },
                onShareCharacter = { text -> viewModel.shareText("Personagem Filme", text) },
                onCopyCharacterData = { text -> viewModel.copyToClipboard("Personagem", text) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MovieActCard(act: MovieActInfo) {
    val actColor = when (act.actNumber) {
        1 -> NeonCyan
        2 -> ElectricGold
        else -> PortalRed
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .border(1.dp, actColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = act.actName,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = actColor
            )
            Surface(
                color = actColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "${act.percentage}%",
                    color = actColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Timestamp: ${act.formattedRange}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = act.roleDescription,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 15.sp
        )
    }
}
