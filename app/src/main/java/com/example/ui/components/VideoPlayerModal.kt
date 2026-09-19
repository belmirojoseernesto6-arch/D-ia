package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.GeneratedEpisodeEntity
import com.example.data.model.AnimeScene
import com.example.data.model.VoiceProfile
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PortalRed
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerModal(
    episode: GeneratedEpisodeEntity,
    onClose: () -> Unit,
    onPlaySceneVoice: (text: String, voiceProfileKey: String) -> Unit,
    onOpenYoutubeShorts: (GeneratedEpisodeEntity) -> Unit,
    onShareVideo: (String) -> Unit
) {
    val scenes = remember(episode.scenesDataJson) {
        val parsed = AnimeScene.parseList(episode.scenesDataJson)
        if (parsed.isEmpty()) {
            listOf(
                AnimeScene(
                    sceneNumber = 1,
                    title = "Batalha DARKCOM",
                    backgroundType = "DARKCOM_MEETING_ROOM",
                    visualPrompt = "anime scene",
                    speakerName = "Comandante DARKCOM",
                    speakerType = "HEROI",
                    dialogueText = "Iniciando defesa orbital!",
                    voiceProfile = VoiceProfile.PRESIDENTE.key
                )
            )
        } else parsed
    }

    var currentSceneIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(true) }
    var sceneProgress by remember { mutableFloatStateOf(0f) }

    val currentScene = scenes.getOrElse(currentSceneIndex) { scenes.first() }

    // Play voice whenever scene changes and player is active
    LaunchedEffect(currentSceneIndex, isPlaying) {
        if (isPlaying) {
            onPlaySceneVoice(currentScene.dialogueText, currentScene.voiceProfile)
        }
    }

    // Auto-advance scenes during playback
    LaunchedEffect(currentSceneIndex, isPlaying) {
        if (!isPlaying) return@LaunchedEffect
        sceneProgress = 0f
        val steps = 60
        val stepDelay = (currentScene.durationSeconds * 1000L) / steps
        for (i in 1..steps) {
            if (!isPlaying) break
            delay(stepDelay)
            sceneProgress = i / steps.toFloat()
        }
        if (isPlaying) {
            if (currentSceneIndex < scenes.lastIndex) {
                currentSceneIndex++
            } else {
                // End of episode
                isPlaying = false
            }
        }
    }

    // Animated effects for anime canvas
    val infiniteTransition = rememberInfiniteTransition(label = "anime_vfx")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = androidx.compose.animation.core.LinearEasing)
        ),
        label = "rotation"
    )

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.94f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.5.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .testTag("video_player_modal"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0A0E17))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Top Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = if (episode.isTrailer) PortalRed else NeonCyan,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (episode.isTrailer) "TRAILER 30S" else "EPISÓDIO NATIVO",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "OFFLINE PLAYER",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                            Text(
                                text = episode.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1
                            )
                        }

                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar Player",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Animated Anime Screen Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF0D1B2A),
                                        Color(0xFF04060A)
                                    )
                                )
                            )
                            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    ) {
                        // Canvas visual graphics: portal vortex / cyber grid
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2f, size.height / 2f)

                            // Background grid lines
                            val gridSpacing = 40.dp.toPx()
                            var x = 0f
                            while (x < size.width) {
                                drawLine(
                                    color = NeonCyan.copy(alpha = 0.08f),
                                    start = Offset(x, 0f),
                                    end = Offset(x, size.height),
                                    strokeWidth = 1f
                                )
                                x += gridSpacing
                            }

                            if (currentScene.backgroundType == "DEMON_PORTAL") {
                                // Demonic Red Portal Vortex
                                drawCircle(
                                    color = PortalRed.copy(alpha = 0.25f * pulseAlpha),
                                    radius = size.height * 0.42f,
                                    center = center
                                )
                                drawCircle(
                                    color = PortalRed,
                                    radius = size.height * 0.35f,
                                    center = center,
                                    style = Stroke(width = 6f)
                                )
                                drawCircle(
                                    color = ElectricGold,
                                    radius = size.height * 0.22f,
                                    center = center,
                                    style = Stroke(width = 3f)
                                )
                            } else {
                                // DARKCOM Cyber Matrix Cyan Shield
                                drawCircle(
                                    color = NeonCyan.copy(alpha = 0.2f * pulseAlpha),
                                    radius = size.height * 0.4f,
                                    center = center
                                )
                                drawCircle(
                                    color = NeonCyan,
                                    radius = size.height * 0.32f,
                                    center = center,
                                    style = Stroke(width = 5f)
                                )
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.7f),
                                    radius = size.height * 0.18f,
                                    center = center,
                                    style = Stroke(width = 2f)
                                )
                            }
                        }

                        // Scene Title Overlay on Screen
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp)
                                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "CENA ${currentScene.sceneNumber}: ${currentScene.title.uppercase()}",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        // Subtitle & Dialogue Box
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                                    )
                                )
                                .padding(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Surface(
                                    color = if (currentScene.speakerType == "VILAO") PortalRed else NeonCyan,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = currentScene.speakerName,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "\"${currentScene.dialogueText}\"",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { sceneProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = NeonCyan,
                        trackColor = Color(0xFF1F293D)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Playback Controls (Rewind, Play/Pause, FastForward, Scene Selector)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cena ${currentSceneIndex + 1}/${scenes.size}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Previous scene
                            IconButton(
                                onClick = {
                                    if (currentSceneIndex > 0) currentSceneIndex--
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FastRewind,
                                    contentDescription = "Cena Anterior",
                                    tint = if (currentSceneIndex > 0) NeonCyan else Color.Gray
                                )
                            }

                            // Play / Pause
                            Surface(
                                shape = CircleShape,
                                color = NeonCyan,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clickable { isPlaying = !isPlaying }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "Pausar" else "Reproduzir",
                                        tint = Color.Black,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            // Next scene
                            IconButton(
                                onClick = {
                                    if (currentSceneIndex < scenes.lastIndex) currentSceneIndex++
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FastForward,
                                    contentDescription = "Próxima Cena",
                                    tint = if (currentSceneIndex < scenes.lastIndex) NeonCyan else Color.Gray
                                )
                            }

                            // Replay
                            IconButton(
                                onClick = {
                                    currentSceneIndex = 0
                                    isPlaying = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay,
                                    contentDescription = "Reiniciar",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = episode.formattedDuration,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bottom Export Buttons: [Exportar para YouTube Shorts] & [Partilhar Vídeo]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onOpenYoutubeShorts(episode) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PortalRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("btn_export_youtube_shorts_modal")
                        ) {
                            Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("YouTube Shorts", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val shareText = "🎥 Assista ao anime ${episode.title} gerado no ANIME STUDIO PRO!\n\n${episode.synopsis}\n\nDuração: ${episode.formattedDuration}\n#AnimeStudioPro"
                                onShareVideo(shareText)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("btn_share_video_modal")
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Partilhar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
