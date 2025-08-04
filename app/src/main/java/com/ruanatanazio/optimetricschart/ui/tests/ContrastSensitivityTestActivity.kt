package com.ruanatanazio.optimetricschart.ui.tests

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruanatanazio.optimetricschart.R
import com.ruanatanazio.optimetricschart.ui.theme.OptimetricsChartTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

class ContrastSensitivityTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptimetricsChartTheme {
                ContrastSensitivityTestScreen {
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContrastSensitivityTestScreen(onBack: () -> Unit) {
    var isFullscreen by remember { mutableStateOf(false) }
    var currentRow by remember { mutableStateOf(0) }
    
    val contrastGratings = remember { getContrastGratings() }
    
    if (isFullscreen) {
        FullscreenContrastTestView(
            gratings = contrastGratings,
            currentRow = currentRow,
            onRowChanged = { currentRow = it },
            onExitFullscreen = { isFullscreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.test_contrast_sensitivity)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isFullscreen = true }) {
                            Icon(Icons.Default.Fullscreen, contentDescription = "Fullscreen")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Instructions
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Teste de Sensibilidade ao Contraste",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Identifique a orientação das listras (vertical, horizontal, diagonal)\n• Mantenha distância de 3 metros\n• O contraste diminui da esquerda para direita\n• Pare quando não conseguir mais distinguir as listras",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Row selection
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Linha do Teste: ${currentRow + 1}/${contrastGratings.size}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Frequência espacial: ${contrastGratings[currentRow].spatialFrequency} cpd",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = currentRow.toFloat(),
                            onValueChange = { currentRow = it.toInt() },
                            valueRange = 0f..(contrastGratings.size - 1).toFloat(),
                            steps = contrastGratings.size - 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Test preview
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Prévia - Linha ${currentRow + 1}",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(listOf(contrastGratings[currentRow])) { grating ->
                                ContrastGratingPreview(grating = grating)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Start fullscreen button
                Button(
                    onClick = { isFullscreen = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Fullscreen, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iniciar Teste em Tela Cheia")
                }
            }
        }
    }
}

@Composable
fun ContrastGratingPreview(
    grating: ContrastGrating
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Frequência: ${grating.spatialFrequency} cpd",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            grating.patches.forEach { patch ->
                Canvas(
                    modifier = Modifier.size(60.dp)
                ) {
                    drawContrastPatch(
                        patch = patch,
                        size = this.size.minDimension
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullscreenContrastTestView(
    gratings: List<ContrastGrating>,
    currentRow: Int,
    onRowChanged: (Int) -> Unit,
    onExitFullscreen: () -> Unit
) {
    val currentGrating = gratings[currentRow]
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF808080)) // Neutral gray background
    ) {
        // Exit button
        IconButton(
            onClick = onExitFullscreen,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Exit",
                tint = Color.White
            )
        }
        
        // Test display
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Linha ${currentRow + 1} - Frequência ${currentGrating.spatialFrequency} cpd",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "Identifique a orientação das listras em cada círculo",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Display patches in a row
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                currentGrating.patches.forEachIndexed { index, patch ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Canvas(
                            modifier = Modifier.size(120.dp)
                        ) {
                            drawContrastPatch(
                                patch = patch,
                                size = this.size.minDimension
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${index + 1}",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
        
        // Navigation controls
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { 
                    if (currentRow > 0) onRowChanged(currentRow - 1)
                },
                enabled = currentRow > 0
            ) {
                Text("Anterior")
            }
            
            Text(
                text = "${currentRow + 1}/${gratings.size}",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.White
            )
            
            Button(
                onClick = { 
                    if (currentRow < gratings.size - 1) onRowChanged(currentRow + 1)
                },
                enabled = currentRow < gratings.size - 1
            ) {
                Text("Próximo")
            }
        }
    }
}

fun DrawScope.drawContrastPatch(
    patch: ContrastPatch,
    size: Float
) {
    val center = Offset(size / 2f, size / 2f)
    val radius = size / 2f
    
    // Draw background circle
    drawCircle(
        color = Color(0xFF808080),
        radius = radius,
        center = center
    )
    
    if (patch.hasGrating) {
        // Draw grating pattern
        val stripeWidth = size / (patch.spatialFrequency * 2)
        val numStripes = (size / stripeWidth).toInt()
        
        for (i in 0 until numStripes) {
            val stripePosition = i * stripeWidth
            val brightness = if (i % 2 == 0) {
                0.5f + patch.contrast / 2f
            } else {
                0.5f - patch.contrast / 2f
            }
            
            val color = Color(brightness, brightness, brightness)
            
            when (patch.orientation) {
                GratingOrientation.VERTICAL -> {
                    if (stripePosition < size) {
                        drawLine(
                            color = color,
                            start = Offset(stripePosition, 0f),
                            end = Offset(stripePosition, size),
                            strokeWidth = stripeWidth
                        )
                    }
                }
                GratingOrientation.HORIZONTAL -> {
                    if (stripePosition < size) {
                        drawLine(
                            color = color,
                            start = Offset(0f, stripePosition),
                            end = Offset(size, stripePosition),
                            strokeWidth = stripeWidth
                        )
                    }
                }
                GratingOrientation.DIAGONAL_RIGHT -> {
                    // Simplified diagonal implementation
                    if (stripePosition < size) {
                        drawLine(
                            color = color,
                            start = Offset(0f, stripePosition),
                            end = Offset(size - stripePosition, size),
                            strokeWidth = stripeWidth
                        )
                    }
                }
                GratingOrientation.DIAGONAL_LEFT -> {
                    // Simplified diagonal implementation
                    if (stripePosition < size) {
                        drawLine(
                            color = color,
                            start = Offset(stripePosition, 0f),
                            end = Offset(size, size - stripePosition),
                            strokeWidth = stripeWidth
                        )
                    }
                }
            }
        }
    }
}

data class ContrastGrating(
    val spatialFrequency: Double, // cycles per degree
    val patches: List<ContrastPatch>
)

data class ContrastPatch(
    val contrast: Float, // 0.0 to 1.0
    val orientation: GratingOrientation,
    val spatialFrequency: Double,
    val hasGrating: Boolean = true
)

enum class GratingOrientation {
    VERTICAL,
    HORIZONTAL,
    DIAGONAL_RIGHT,
    DIAGONAL_LEFT
}

fun getContrastGratings(): List<ContrastGrating> {
    val orientations = listOf(
        GratingOrientation.VERTICAL,
        GratingOrientation.HORIZONTAL,
        GratingOrientation.DIAGONAL_RIGHT,
        GratingOrientation.DIAGONAL_LEFT
    )
    
    return listOf(
        // Row 1 - Low spatial frequency, high contrast
        ContrastGrating(
            spatialFrequency = 1.5,
            patches = listOf(
                ContrastPatch(0.8f, orientations[0], 1.5),
                ContrastPatch(0.4f, orientations[1], 1.5),
                ContrastPatch(0.2f, orientations[2], 1.5),
                ContrastPatch(0.1f, orientations[3], 1.5),
                ContrastPatch(0.05f, orientations[0], 1.5),
                ContrastPatch(0.0f, orientations[1], 1.5, false) // Blank
            )
        ),
        // Row 2 - Medium spatial frequency
        ContrastGrating(
            spatialFrequency = 3.0,
            patches = listOf(
                ContrastPatch(0.6f, orientations[1], 3.0),
                ContrastPatch(0.3f, orientations[2], 3.0),
                ContrastPatch(0.15f, orientations[3], 3.0),
                ContrastPatch(0.075f, orientations[0], 3.0),
                ContrastPatch(0.04f, orientations[1], 3.0),
                ContrastPatch(0.0f, orientations[2], 3.0, false) // Blank
            )
        ),
        // Row 3 - Higher spatial frequency
        ContrastGrating(
            spatialFrequency = 6.0,
            patches = listOf(
                ContrastPatch(0.4f, orientations[2], 6.0),
                ContrastPatch(0.2f, orientations[3], 6.0),
                ContrastPatch(0.1f, orientations[0], 6.0),
                ContrastPatch(0.05f, orientations[1], 6.0),
                ContrastPatch(0.025f, orientations[2], 6.0),
                ContrastPatch(0.0f, orientations[3], 6.0, false) // Blank
            )
        ),
        // Row 4 - High spatial frequency
        ContrastGrating(
            spatialFrequency = 12.0,
            patches = listOf(
                ContrastPatch(0.3f, orientations[3], 12.0),
                ContrastPatch(0.15f, orientations[0], 12.0),
                ContrastPatch(0.075f, orientations[1], 12.0),
                ContrastPatch(0.04f, orientations[2], 12.0),
                ContrastPatch(0.02f, orientations[3], 12.0),
                ContrastPatch(0.0f, orientations[0], 12.0, false) // Blank
            )
        ),
        // Row 5 - Very high spatial frequency
        ContrastGrating(
            spatialFrequency = 18.0,
            patches = listOf(
                ContrastPatch(0.25f, orientations[0], 18.0),
                ContrastPatch(0.125f, orientations[1], 18.0),
                ContrastPatch(0.06f, orientations[2], 18.0),
                ContrastPatch(0.03f, orientations[3], 18.0),
                ContrastPatch(0.015f, orientations[0], 18.0),
                ContrastPatch(0.0f, orientations[1], 18.0, false) // Blank
            )
        )
    )
}
