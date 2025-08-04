package com.ruanatanazio.optimetricschart.ui.education

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ruanatanazio.optimetricschart.R
import com.ruanatanazio.optimetricschart.ui.theme.OptimetricsChartTheme

class EducationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptimetricsChartTheme {
                EducationScreen {
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(onBack: () -> Unit) {
    val educationCategories = remember { getEducationCategories() }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.education_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            // Header description
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.VideoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.education_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.education_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Categories
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(educationCategories) { category ->
                    EducationCategoryCard(
                        category = category,
                        onClick = {
                            // TODO: Open video list for this category
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationCategoryCard(
    category: EducationCategory,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = category.backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${category.videoCount} vídeos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

data class EducationCategory(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val videoCount: Int,
    val videos: List<EducationVideo> = emptyList()
)

data class EducationVideo(
    val title: String,
    val description: String,
    val duration: String,
    val thumbnailUrl: String? = null,
    val videoUrl: String? = null
)

@Composable
fun getEducationCategories(): List<EducationCategory> {
    return listOf(
        EducationCategory(
            title = stringResource(R.string.education_ocular_physiotherapy),
            description = "Exercícios e técnicas de fisioterapia para saúde ocular",
            icon = Icons.Default.FitnessCenter,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFE8F5E8),
            videoCount = 12,
            videos = listOf(
                EducationVideo(
                    title = "Exercícios Básicos para os Olhos",
                    description = "Técnicas fundamentais de relaxamento ocular",
                    duration = "8:30"
                ),
                EducationVideo(
                    title = "Fisioterapia para Fadiga Visual",
                    description = "Exercícios específicos para síndrome do olho seco",
                    duration = "12:15"
                )
            )
        ),
        EducationCategory(
            title = stringResource(R.string.education_ophthalmic_surgery),
            description = "Procedimentos cirúrgicos em oftalmologia",
            icon = Icons.Default.LocalHospital,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFFFEBEE),
            videoCount = 8,
            videos = listOf(
                EducationVideo(
                    title = "Cirurgia de Catarata - Facoemulsificação",
                    description = "Técnica moderna de remoção de catarata",
                    duration = "15:45"
                ),
                EducationVideo(
                    title = "LASIK - Correção a Laser",
                    description = "Procedimento de correção refrativa",
                    duration = "10:20"
                )
            )
        ),
        EducationCategory(
            title = stringResource(R.string.education_interesting_videos),
            description = "Conteúdo educativo e curiosidades sobre oftalmologia",
            icon = Icons.Default.Psychology,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFE1F5FE),
            videoCount = 15,
            videos = listOf(
                EducationVideo(
                    title = "Como Funciona a Visão Humana",
                    description = "Anatomia e fisiologia do sistema visual",
                    duration = "11:30"
                ),
                EducationVideo(
                    title = "Daltonismo: Mitos e Verdades",
                    description = "Entendendo a deficiência de visão de cores",
                    duration = "9:45"
                )
            )
        )
    )
}
