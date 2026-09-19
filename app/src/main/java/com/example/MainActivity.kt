package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.MovieTimeCalculation
import com.example.data.model.SeriesTimeCalculation
import com.example.data.model.TimeConfig
import com.example.ui.components.BudgetDialog
import com.example.ui.components.ElevenLabsConfigDialog
import com.example.ui.components.VideoPlayerModal
import com.example.ui.components.YouTubeShortsExportModal
import com.example.ui.screens.LibraryGalleryScreen
import com.example.ui.screens.MovieCreationScreen
import com.example.ui.screens.SeriesCreationScreen
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PortalRed
import com.example.ui.viewmodel.AnimeStudioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AnimeStudioApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeStudioApp(
    viewModel: AnimeStudioViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SelectionContainer {
                                Text(
                                    text = "ANIME STUDIO PRO",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NeonCyan,
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = PortalRed,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "DARKCOM",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    },
                    actions = {
                        // Orçamento
                        IconButton(
                            onClick = { viewModel.toggleBudgetDialog(true) },
                            modifier = Modifier.testTag("top_bar_btn_budget")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = "Orçamento",
                                tint = ElectricGold
                            )
                        }

                        // Estúdio de Vozes / ElevenLabs
                        IconButton(
                            onClick = { viewModel.toggleElevenLabsDialog(true) },
                            modifier = Modifier.testTag("top_bar_btn_voices")
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "Estúdio de Vozes",
                                tint = NeonCyan
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = NeonCyan
                    )
                )

                // 2 ABAS PRINCIPAIS no topo (mais Galeria):
                // ABA 1: CRIAR SÉRIE
                // ABA 2: CRIAR FILME
                // ABA 3: GALERIA
                TabRow(
                    selectedTabIndex = uiState.selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = NeonCyan,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                            color = if (uiState.selectedTab == 1) PortalRed else NeonCyan
                        )
                    }
                ) {
                    Tab(
                        selected = uiState.selectedTab == 0,
                        onClick = { viewModel.selectTab(0) },
                        modifier = Modifier.testTag("tab_create_series"),
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Tv,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (uiState.selectedTab == 0) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CRIAR SÉRIE",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    color = if (uiState.selectedTab == 0) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )

                    Tab(
                        selected = uiState.selectedTab == 1,
                        onClick = { viewModel.selectTab(1) },
                        modifier = Modifier.testTag("tab_create_movie"),
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Movie,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (uiState.selectedTab == 1) PortalRed else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CRIAR FILME",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    color = if (uiState.selectedTab == 1) PortalRed else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )

                    Tab(
                        selected = uiState.selectedTab == 2,
                        onClick = { viewModel.selectTab(2) },
                        modifier = Modifier.testTag("tab_gallery"),
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VideoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (uiState.selectedTab == 2) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "GALERIA",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (uiState.selectedTab == 2) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.selectedTab) {
                0 -> SeriesCreationScreen(viewModel = viewModel, uiState = uiState)
                1 -> MovieCreationScreen(viewModel = viewModel, uiState = uiState)
                2 -> LibraryGalleryScreen(viewModel = viewModel)
            }

            // MODALS & OVERLAYS:
            // 1. Native Offline Video Player Modal
            uiState.activeVideoPlayerEpisode?.let { episode ->
                VideoPlayerModal(
                    episode = episode,
                    onClose = { viewModel.closeVideoPlayer() },
                    onPlaySceneVoice = { text, profileKey ->
                        viewModel.playCustomVoice(text, profileKey)
                    },
                    onOpenYoutubeShorts = { viewModel.openYoutubeExport(it) },
                    onShareVideo = { shareText ->
                        viewModel.shareText(episode.title, shareText)
                    }
                )
            }

            // 2. YouTube Shorts Export Modal
            uiState.activeYoutubeExportEpisode?.let { episode ->
                YouTubeShortsExportModal(
                    episode = episode,
                    onClose = { viewModel.closeYoutubeExport() },
                    onCopyText = { label, text -> viewModel.copyToClipboard(label, text) },
                    onShareContent = { title, text -> viewModel.shareText(title, text) }
                )
            }

            // 3. Budget Calculator Dialog
            if (uiState.isBudgetDialogOpen) {
                val isSeries = uiState.selectedTab == 0
                val totalMinutes = if (isSeries) {
                    SeriesTimeCalculation(
                        episodeTime = TimeConfig(uiState.seriesEpisodeHours, uiState.seriesEpisodeMinutes, uiState.seriesEpisodeSeconds),
                        seasons = uiState.seriesSeasons,
                        episodesPerSeason = uiState.seriesEpisodesPerSeason
                    ).seriesTotalSeconds / 60.0
                } else {
                    MovieTimeCalculation(
                        movieTime = TimeConfig(uiState.movieHours, uiState.movieMinutes, uiState.movieSeconds)
                    ).totalSeconds / 60.0
                }

                val formattedDuration = if (isSeries) {
                    SeriesTimeCalculation(
                        episodeTime = TimeConfig(uiState.seriesEpisodeHours, uiState.seriesEpisodeMinutes, uiState.seriesEpisodeSeconds),
                        seasons = uiState.seriesSeasons,
                        episodesPerSeason = uiState.seriesEpisodesPerSeason
                    ).formattedSeriesTotal()
                } else {
                    MovieTimeCalculation(
                        movieTime = TimeConfig(uiState.movieHours, uiState.movieMinutes, uiState.movieSeconds)
                    ).formattedTotal()
                }

                BudgetDialog(
                    isSeries = isSeries,
                    totalMinutes = totalMinutes,
                    formattedDuration = formattedDuration,
                    onClose = { viewModel.toggleBudgetDialog(false) },
                    onCopyBudget = { viewModel.copyToClipboard("Orçamento Anime Studio", it) },
                    onShareBudget = { viewModel.shareText("Orçamento Anime Studio", it) }
                )
            }

            // 4. ElevenLabs Voices Dialog
            if (uiState.isElevenLabsDialogOpen) {
                ElevenLabsConfigDialog(
                    voiceEngine = viewModel.voiceEngine,
                    onClose = { viewModel.toggleElevenLabsDialog(false) },
                    onTestVoice = { text, profileKey ->
                        viewModel.playCustomVoice(text, profileKey)
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
