package com.ruanatanazio.optimetricschart.ui.tests

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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

class AstigmatismTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptimetricsChartTheme {
                AstigmatismTestScreen {
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AstigmatismTestScreen(onBack: () -> Unit) {
    var isFullscreen by remember { mutableStateOf(false) }
    var selectedTest by remember { mutableStateOf(0) }
    
    val astigmatismTests = remember { getAstigmatismTests() }
    
    if (isFullscreen) {
        FullscreenAstigmatismTestView(
            tests = astigmatismTests,
            currentTestIndex = selectedTest,
            onTestChanged = { selectedTest = it },
            onExitFullscreen = { isFullscreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.test_astigmatism)) },
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
                            text = "Teste de Astigmatismo - Gráfico Radial",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Cubra um olho de cada vez\n• Mantenha distância de 3-6 metros\n• Observe se todas as linhas aparecem igualmente nítidas\n• Linhas mais escuras ou borradas podem indicar astigmatismo",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Test selection
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tipo de Teste",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        astigmatismTests.forEachIndexed { index, test ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedTest == index,
                                    onClick = { selectedTest = index }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = test.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Prévia - ${astigmatismTests[selectedTest].name}",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        AstigmatismTestPreview(
                            test = astigmatismTests[selectedTest],
                            size = 200
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = astigmatismTests[selectedTest].description,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
fun AstigmatismTestPreview(
    test: AstigmatismTest,
    size: Int
) {
    Canvas(
        modifier = Modifier.size(size.dp)
    ) {
        drawAstigmatismPattern(
            test = test,
            size = this.size.minDimension
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullscreenAstigmatismTestView(
    tests: List<AstigmatismTest>,
    currentTestIndex: Int,
    onTestChanged: (Int) -> Unit,
    onExitFullscreen: () -> Unit
) {
    val currentTest = tests[currentTestIndex]
    val configuration = LocalConfiguration.current
    val screenSize = minOf(configuration.screenWidthDp, configuration.screenHeightDp)
    val testSize = (screenSize * 0.8f).dp
    
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
        
        // Test display
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentTest.name,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "Cubra um olho. Todas as linhas aparecem igualmente nítidas?",
                color = Color.DarkGray,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            Canvas(
                modifier = Modifier.size(testSize)
            ) {
                drawAstigmatismPattern(
                    test = currentTest,
                    size = this.size.minDimension
                )
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
                    if (currentTestIndex > 0) onTestChanged(currentTestIndex - 1)
                },
                enabled = currentTestIndex > 0
            ) {
                Text("Anterior")
            }
            
            Text(
                text = "${currentTestIndex + 1}/${tests.size}",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.Gray
            )
            
            Button(
                onClick = { 
                    if (currentTestIndex < tests.size - 1) onTestChanged(currentTestIndex + 1)
                },
                enabled = currentTestIndex < tests.size - 1
            ) {
                Text("Próximo")
            }
        }
    }
}

fun DrawScope.drawAstigmatismPattern(
    test: AstigmatismTest,
    size: Float
) {
    val center = Offset(size / 2f, size / 2f)
    val radius = size / 2f * 0.9f
    
    when (test.type) {
        AstigmatismTestType.RADIAL_LINES -> {
            // Draw radial lines
            val numLines = 24
            val angleStep = 2 * PI / numLines
            
            repeat(numLines) { i ->
                val angle = i * angleStep
                val startX = center.x + (radius * 0.2f * cos(angle)).toFloat()
                val startY = center.y + (radius * 0.2f * sin(angle)).toFloat()
                val endX = center.x + (radius * cos(angle)).toFloat()
                val endY = center.y + (radius * sin(angle)).toFloat()
                
                drawLine(
                    color = Color.Black,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        
        AstigmatismTestType.CLOCK_DIAL -> {
            // Draw clock-like pattern
            val numLines = 12
            val angleStep = 2 * PI / numLines
            
            repeat(numLines) { i ->
                val angle = i * angleStep - PI / 2 // Start from 12 o'clock
                val startX = center.x + (radius * 0.3f * cos(angle)).toFloat()
                val startY = center.y + (radius * 0.3f * sin(angle)).toFloat()
                val endX = center.x + (radius * cos(angle)).toFloat()
                val endY = center.y + (radius * sin(angle)).toFloat()
                
                val strokeWidth = if (i % 3 == 0) 5.dp.toPx() else 3.dp.toPx()
                
                drawLine(
                    color = Color.Black,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                
                // Add hour numbers
                if (i % 3 == 0) {
                    val hour = if (i == 0) 12 else i
                    val textX = center.x + (radius * 1.1f * cos(angle)).toFloat()
                    val textY = center.y + (radius * 1.1f * sin(angle)).toFloat()
                    // Note: Text drawing in Canvas would need more complex implementation
                }
            }
        }
        
        AstigmatismTestType.FAN_PATTERN -> {
            // Draw fan pattern with different line densities
            val sectors = 8
            val angleStep = 2 * PI / sectors
            
            repeat(sectors) { sector ->
                val sectorAngle = sector * angleStep
                val linesInSector = if (sector % 2 == 0) 8 else 4
                
                repeat(linesInSector) { line ->
                    val lineAngle = sectorAngle + (line * angleStep / linesInSector)
                    val startX = center.x + (radius * 0.2f * cos(lineAngle)).toFloat()
                    val startY = center.y + (radius * 0.2f * sin(lineAngle)).toFloat()
                    val endX = center.x + (radius * cos(lineAngle)).toFloat()
                    val endY = center.y + (radius * sin(lineAngle)).toFloat()
                    
                    drawLine(
                        color = Color.Black,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
    
    // Draw center circle
    drawCircle(
        color = Color.Black,
        radius = 4.dp.toPx(),
        center = center
    )
}

data class AstigmatismTest(
    val name: String,
    val type: AstigmatismTestType,
    val description: String
)

enum class AstigmatismTestType {
    RADIAL_LINES,
    CLOCK_DIAL,
    FAN_PATTERN
}

fun getAstigmatismTests(): List<AstigmatismTest> {
    return listOf(
        AstigmatismTest(
            name = "Linhas Radiais",
            type = AstigmatismTestType.RADIAL_LINES,
            description = "Padrão clássico com 24 linhas radiais. Se você tem astigmatismo, algumas linhas podem aparecer mais escuras ou nítidas que outras."
        ),
        AstigmatismTest(
            name = "Mostrador de Relógio",
            type = AstigmatismTestType.CLOCK_DIAL,
            description = "Padrão similar a um relógio. Pessoas com astigmatismo podem ver algumas 'horas' mais nítidas que outras."
        ),
        AstigmatismTest(
            name = "Padrão em Leque",
            type = AstigmatismTestType.FAN_PATTERN,
            description = "Padrão com diferentes densidades de linhas. Útil para detectar diferentes tipos de astigmatismo."
        )
    )
}
