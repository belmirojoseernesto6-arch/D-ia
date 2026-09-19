package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CharacterEntity
import com.example.data.model.CharacterType
import com.example.data.model.VoiceProfile
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PortalRed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterCard(
    character: CharacterEntity,
    onCopyPrompt: () -> Unit,
    onChangeOutfit: () -> Unit,
    onPlayVoice: () -> Unit,
    onEvolveVillain: () -> Unit,
    onRemove: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShareCharacter: (String) -> Unit,
    onCopyCharacterData: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isVillain = character.type == CharacterType.VILAO.name
    val accentColor = if (isVillain) PortalRed else NeonCyan
    var showPromptDetails by remember { mutableStateOf(false) }

    val formattedData = """
        Nome: ${character.name}
        Tipo: ${if (isVillain) "Vilão" else "Herói"}
        Idade: ${character.age} anos
        Roupa: ${character.clothing}
        Voz: ${VoiceProfile.fromKey(character.voiceProfile).title}
        Prompt: ${character.prompt}
    """.trimIndent()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (character.evolutionStage > 0) 1.5.dp else 1.dp,
                color = if (character.evolutionStage > 0) ElectricGold else accentColor.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("character_card_${character.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Top Header: Badge, Name, Favorite & Quick actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    // Type Badge
                    Surface(
                        color = accentColor.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (isVillain) "VILÃO" else "HERÓI",
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    if (character.evolutionStage > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = ElectricGold.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricGold),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "LVL ${character.evolutionStage + 1} EVOLUÍDO",
                                color = ElectricGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Quick Card Action Bar: Copiar, Partilhar, Favorito, Remover
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onCopyCharacterData(formattedData) },
                        modifier = Modifier.size(34.dp).testTag("quick_copy_char_${character.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar dados do personagem",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { onShareCharacter(formattedData) },
                        modifier = Modifier.size(34.dp).testTag("quick_share_char_${character.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Partilhar no WhatsApp/Redes",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(34.dp).testTag("quick_fav_char_${character.id}")
                    ) {
                        Icon(
                            imageVector = if (character.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favoritar",
                            tint = if (character.isFavorite) ElectricGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(34.dp).testTag("quick_delete_char_${character.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remover personagem",
                            tint = PortalRed.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Selectable Name to satisfy: "Todos os TextViews de títulos devem ter android:textIsSelectable='true' para permitir copiar/partilhar igual WhatsApp"
            SelectionContainer {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Character specs grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Idade: ${character.age} anos",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Voz: ${VoiceProfile.fromKey(character.voiceProfile).title}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = accentColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Roupa: ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = character.clothing,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (character.customVoicePhrase.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Frase: \"${character.customVoicePhrase}\"",
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Prompt preview expandable
            AnimatedVisibility(visible = showPromptDetails) {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "PROMPT DE IMAGEM 8K:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SelectionContainer {
                        Text(
                            text = character.prompt,
                            fontSize = 12.sp,
                            color = Color(0xFFE0E0E0),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons:
            // [📋 Copiar Prompt] [👔 Trocar Roupa] [🎙️ Ouvir Voz] [😈 Evoluir (se for vilão)] [🗑️ Remover]
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // [📋 Copiar Prompt]
                FilledTonalButton(
                    onClick = onCopyPrompt,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = NeonCyan.copy(alpha = 0.15f),
                        contentColor = NeonCyan
                    ),
                    modifier = Modifier.testTag("btn_copy_prompt_${character.id}")
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copiar Prompt", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                // [👔 Trocar Roupa]
                FilledTonalButton(
                    onClick = onChangeOutfit,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.testTag("btn_change_outfit_${character.id}")
                ) {
                    Icon(imageVector = Icons.Outlined.Checkroom, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Trocar Roupa", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                // [🎙️ Ouvir Voz]
                ElevatedButton(
                    onClick = onPlayVoice,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (isVillain) PortalRed else NeonCyan,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.testTag("btn_listen_voice_${character.id}")
                ) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ouvir Voz", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // [😈 Evoluir (se for vilão)]
                if (isVillain) {
                    ElevatedButton(
                        onClick = onEvolveVillain,
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = PortalRed,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("btn_evolve_villain_${character.id}")
                    ) {
                        Icon(imageVector = Icons.Default.Upgrade, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("😈 Evoluir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Toggle Prompt Preview
                OutlinedButton(
                    onClick = { showPromptDetails = !showPromptDetails },
                    modifier = Modifier.testTag("btn_toggle_prompt_${character.id}")
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showPromptDetails) "Ocultar Prompt" else "Ver Prompt", fontSize = 12.sp)
                }
            }
        }
    }
}
