package com.ruanatanazio.optimetricschart.ui.tests

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruanatanazio.optimetricschart.R
import com.ruanatanazio.optimetricschart.ui.theme.OptimetricsChartTheme
import kotlin.math.pow

class VisualAcuityTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptimetricsChartTheme {
                VisualAcuityTestScreen {
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualAcuityTestScreen(onBack: () -> Unit) {
    var isFullscreen by remember { mutableStateOf(false) }
    var selectedLine by remember { mutableStateOf(0) }
    var testDistance by remember { mutableStateOf(6.0) } // 6 meters default
    
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    
    // Snellen chart lines with 20/xx values
    val snellenLines = listOf(
        SnellenLine("20/200", "E", 200, 88.0),
        SnellenLine("20/100", "F P", 100, 44.0),
        SnellenLine("20/70", "T O Z", 70, 30.8),
        SnellenLine("20/50", "L P E D", 50, 22.0),
        SnellenLine("20/40", "F E D F C", 40, 17.6),
        SnellenLine("20/30", "F P T O Z", 30, 13.2),
        SnellenLine("20/25", "D F P O T E C", 25, 11.0),
        SnellenLine("20/20", "F E L O P Z D", 20, 8.8),
        SnellenLine("20/15", "D F P O T E C L D", 15, 6.6),
        SnellenLine("20/10", "P E T O L C Z D F H", 10, 4.4)
    )
    
    if (isFullscreen) {
        FullscreenTestView(
            snellenLines = snellenLines,
            selectedLine = selectedLine,
            testDistance = testDistance,
            screenWidthDp = screenWidthDp,
            onLineSelected = { selectedLine = it },
            onExitFullscreen = { isFullscreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.test_visual_acuity)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isFullscreen = true }) {
                            Icon(Icons.Default.Fullscreen, contentDescription = "Fullscreen")
                        }
                        IconButton(onClick = { /* Open settings */ }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
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
                            text = "Instruções do Teste",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.instruction_cover_eye),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(R.string.instruction_distance, testDistance.toString()),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Distance setting
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Distância do Teste: ${testDistance}m",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Slider(
                            value = testDistance.toFloat(),
                            onValueChange = { testDistance = it.toDouble() },
                            valueRange = 1f..10f,
                            steps = 17,
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
                            text = "Prévia do Teste - Clique em Tela Cheia",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(snellenLines.take(6)) { line ->
                                SnellenLinePreview(
                                    line = line,
                                    isSelected = snellenLines.indexOf(line) == selectedLine,
                                    onClick = { selectedLine = snellenLines.indexOf(line) }
                                )
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
fun SnellenLinePreview(
    line: SnellenLine,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = line.fraction,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = line.letters,
                fontSize = (line.angularSize / 4).sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullscreenTestView(
    snellenLines: List<SnellenLine>,
    selectedLine: Int,
    testDistance: Double,
    screenWidthDp: Int,
    onLineSelected: (Int) -> Unit,
    onExitFullscreen: () -> Unit
) {
    val currentLine = snellenLines[selectedLine]
    
    // Calculate letter size based on angular size and viewing distance
    val angularSizeArcMin = currentLine.angularSize
    val letterSizeMm = testDistance * 1000 * kotlin.math.tan(Math.toRadians(angularSizeArcMin / 60.0))
    val letterSizeDp = (letterSizeMm / 0.264583).dp // Convert mm to dp (96 DPI)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                tint = Color.Black
            )
        }
        
        // Test content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Line indicator
            Text(
                text = currentLine.fraction,
                color = Color.Gray,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Letters
            Text(
                text = currentLine.letters,
                color = Color.Black,
                fontSize = letterSizeDp.value.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = letterSizeDp.value.sp * 1.2f
            )
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
                    if (selectedLine > 0) onLineSelected(selectedLine - 1)
                },
                enabled = selectedLine > 0
            ) {
                Text("Anterior")
            }
            
            Text(
                text = "${selectedLine + 1}/${snellenLines.size}",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.Gray
            )
            
            Button(
                onClick = { 
                    if (selectedLine < snellenLines.size - 1) onLineSelected(selectedLine + 1)
                },
                enabled = selectedLine < snellenLines.size - 1
            ) {
                Text("Próximo")
            }
        }
    }
}

data class SnellenLine(
    val fraction: String,
    val letters: String,
    val denominator: Int,
    val angularSize: Double // in arc minutes
)
