package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.NeonCyan
import java.util.Locale

@Composable
fun BudgetDialog(
    isSeries: Boolean,
    totalMinutes: Double,
    formattedDuration: String,
    onClose: () -> Unit,
    onCopyBudget: (String) -> Unit,
    onShareBudget: (String) -> Unit
) {
    val ratePerMinute = 0.50
    val totalCost = totalMinutes * ratePerMinute

    val scriptCost = totalCost * 0.20
    val animCost = totalCost * 0.50
    val voiceCost = totalCost * 0.20
    val vfxCost = totalCost * 0.10

    val budgetReport = """
        📊 ORÇAMENTO DE PRODUÇÃO - ANIME STUDIO PRO
        Projeto: ${if (isSeries) "Série de Anime Completa" else "Filme Longa-Metragem"}
        Duração Total: $formattedDuration (${String.format(Locale.getDefault(), "%.1f", totalMinutes)} min)
        Taxa Base: R$ 0,50 / minuto
        
        DETALHAMENTO:
        - Roteiro & Prompts DARKCOM: R$ ${String.format(Locale.getDefault(), "%.2f", scriptCost)}
        - Geração de Cenas e Animação: R$ ${String.format(Locale.getDefault(), "%.2f", animCost)}
        - Estúdio de Vozes & ElevenLabs: R$ ${String.format(Locale.getDefault(), "%.2f", voiceCost)}
        - Efeitos Visuais & Portais VFX: R$ ${String.format(Locale.getDefault(), "%.2f", vfxCost)}
        
        💰 CUSTO TOTAL ESTIMADO: R$ ${String.format(Locale.getDefault(), "%.2f", totalCost)}
    """.trimIndent()

    Dialog(onDismissRequest = onClose) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, ElectricGold.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .testTag("budget_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = ElectricGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = ElectricGold,
                                modifier = Modifier.padding(6.dp).size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CALCULADORA DE ORÇAMENTO",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Base: R$ 0,50 por minuto de animação",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Big Total Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        .border(1.dp, ElectricGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TOTAL ESTIMADO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "R$ ${String.format(Locale.getDefault(), "%.2f", totalCost)}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ElectricGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$formattedDuration de produção",
                            fontSize = 12.sp,
                            color = NeonCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Breakdown list
                BudgetRow(label = "Roteiro & Prompts DARKCOM (20%)", value = scriptCost)
                BudgetRow(label = "Animação & Storyboard AI (50%)", value = animCost)
                BudgetRow(label = "Estúdio de Vozes (ElevenLabs) (20%)", value = voiceCost)
                BudgetRow(label = "VFX & Portais Interdimensionais (10%)", value = vfxCost)

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(14.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onCopyBudget(budgetReport) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copiar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onShareBudget(budgetReport) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricGold,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
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

@Composable
private fun BudgetRow(label: String, value: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "R$ ${String.format(Locale.getDefault(), "%.2f", value)}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
