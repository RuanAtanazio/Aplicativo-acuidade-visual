package com.ruanatanazio.optimetricschart.ui.tests

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlin.random.Random

class ColorVisionTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptimetricsChartTheme {
                ColorVisionTestScreen {
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorVisionTestScreen(onBack: () -> Unit) {
    var isFullscreen by remember { mutableStateOf(false) }
    var currentPlateIndex by remember { mutableStateOf(0) }
    
    val ishiharaPlates = remember { getIshiharaPlates() }
    
    if (isFullscreen) {
        FullscreenColorTestView(
            plates = ishiharaPlates,
            currentPlateIndex = currentPlateIndex,
            onPlateChanged = { currentPlateIndex = it },
            onExitFullscreen = { isFullscreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.test_color_vision)) },
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
                            text = "Teste de Visão de Cores - Ishihara",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.instruction_color_plates),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Mantenha distância de 75cm da tela\n• Boa iluminação é essencial\n• Responda rapidamente (3-5 segundos)",
                            style = MaterialTheme.typography.bodySmall
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
                            text = "Prévia das Placas de Ishihara",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(ishiharaPlates.take(3)) { plate ->
                                IshiharaPlatePreview(plate = plate)
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
fun IshiharaPlatePreview(
    plate: IshiharaPlate,
    size: Int = 150
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Placa ${plate.number}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(CircleShape)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawIshiharaPlate(
                    plate = plate,
                    size = this.size.minDimension
                )
            }
        }
        
        Text(
            text = "Número esperado: ${plate.expectedNumber}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullscreenColorTestView(
    plates: List<IshiharaPlate>,
    currentPlateIndex: Int,
    onPlateChanged: (Int) -> Unit,
    onExitFullscreen: () -> Unit
) {
    val currentPlate = plates[currentPlateIndex]
    val configuration = LocalConfiguration.current
    val screenSize = minOf(configuration.screenWidthDp, configuration.screenHeightDp)
    val plateSize = (screenSize * 0.7f).dp
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
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
        
        // Plate display
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Placa ${currentPlate.number}",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "Que número você vê?",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            Box(
                modifier = Modifier
                    .size(plateSize)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawIshiharaPlate(
                        plate = currentPlate,
                        size = this.size.minDimension
                    )
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
                    if (currentPlateIndex > 0) onPlateChanged(currentPlateIndex - 1)
                },
                enabled = currentPlateIndex > 0
            ) {
                Text("Anterior")
            }
            
            Text(
                text = "${currentPlateIndex + 1}/${plates.size}",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.White
            )
            
            Button(
                onClick = { 
                    if (currentPlateIndex < plates.size - 1) onPlateChanged(currentPlateIndex + 1)
                },
                enabled = currentPlateIndex < plates.size - 1
            ) {
                Text("Próximo")
            }
        }
    }
}

fun DrawScope.drawIshiharaPlate(
    plate: IshiharaPlate,
    size: Float
) {
    val radius = size / 2f
    val center = Offset(radius, radius)
    val dotRadius = size / 80f
    
    // Generate random dots for background
    val random = Random(plate.number)
    val numDots = 400
    
    // Draw background dots
    repeat(numDots) {
        val angle = random.nextFloat() * 2 * Math.PI
        val distance = random.nextFloat() * (radius - dotRadius)
        val x = center.x + (distance * cos(angle)).toFloat()
        val y = center.y + (distance * sin(angle)).toFloat()
        
        val color = if (isInNumberArea(x, y, center, plate.expectedNumber, radius)) {
            plate.numberColor
        } else {
            plate.backgroundColor
        }
        
        drawCircle(
            color = color,
            radius = dotRadius,
            center = Offset(x, y)
        )
    }
}

// Simplified function to determine if a point is in the number area
fun isInNumberArea(x: Float, y: Float, center: Offset, number: String, radius: Float): Boolean {
    val relativeX = (x - center.x) / radius
    val relativeY = (y - center.y) / radius
    
    // Simple pattern for number detection (simplified for demo)
    return when (number) {
        "8" -> {
            val distance = kotlin.math.sqrt((relativeX * relativeX + relativeY * relativeY).toDouble())
            (distance > 0.2 && distance < 0.4) || (distance > 0.5 && distance < 0.7)
        }
        "3" -> {
            relativeX > -0.3 && relativeX < 0.3 && kotlin.math.abs(relativeY) < 0.6
        }
        "5" -> {
            (relativeY > 0.2 && relativeY < 0.6 && relativeX > -0.3 && relativeX < 0.1) ||
            (relativeY > -0.2 && relativeY < 0.2 && relativeX > -0.3 && relativeX < 0.3) ||
            (relativeY > -0.6 && relativeY < -0.2 && relativeX > -0.1 && relativeX < 0.3)
        }
        else -> false
    }
}

data class IshiharaPlate(
    val number: Int,
    val expectedNumber: String,
    val numberColor: Color,
    val backgroundColor: Color,
    val description: String
)

fun getIshiharaPlates(): List<IshiharaPlate> {
    return listOf(
        IshiharaPlate(
            number = 1,
            expectedNumber = "8",
            numberColor = Color(0xFF8B4513),
            backgroundColor = Color(0xFF90EE90),
            description = "Teste de controle - Todos devem ver o número 8"
        ),
        IshiharaPlate(
            number = 2,
            expectedNumber = "3",
            numberColor = Color(0xFFFF6347),
            backgroundColor = Color(0xFF90EE90),
            description = "Protanopia/Deuteranopia podem ter dificuldade"
        ),
        IshiharaPlate(
            number = 3,
            expectedNumber = "5",
            numberColor = Color(0xFF32CD32),
            backgroundColor = Color(0xFFFF6347),
            description = "Teste de deficiência vermelho-verde"
        ),
        IshiharaPlate(
            number = 4,
            expectedNumber = "2",
            numberColor = Color(0xFF9370DB),
            backgroundColor = Color(0xFF90EE90),
            description = "Teste de deficiência vermelho-verde"
        ),
        IshiharaPlate(
            number = 5,
            expectedNumber = "6",
            numberColor = Color(0xFFFF1493),
            backgroundColor = Color(0xFF90EE90),
            description = "Teste específico para protanopia"
        ),
        IshiharaPlate(
            number = 6,
            expectedNumber = "9",
            numberColor = Color(0xFF4169E1),
            backgroundColor = Color(0xFFFF6347),
            description = "Teste específico para deuteranopia"
        )
    )
}
