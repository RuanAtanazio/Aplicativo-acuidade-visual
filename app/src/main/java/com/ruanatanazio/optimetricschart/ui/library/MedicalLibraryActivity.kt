package com.ruanatanazio.optimetricschart.ui.library

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

class MedicalLibraryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptimetricsChartTheme {
                MedicalLibraryScreen {
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalLibraryScreen(onBack: () -> Unit) {
    val libraryCategories = remember { getLibraryCategories() }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.library_title)) },
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
                        Icons.Default.LibraryBooks,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.library_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.library_desc),
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
                items(libraryCategories) { category ->
                    LibraryCategoryCard(
                        category = category,
                        onClick = {
                            // TODO: Open detailed information for this category
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryCategoryCard(
    category: LibraryCategory,
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
                Row {
                    AssistChip(
                        onClick = { },
                        label = { 
                            Text(
                                "${category.articles} artigos",
                                style = MaterialTheme.typography.labelSmall
                            ) 
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    AssistChip(
                        onClick = { },
                        label = { 
                            Text(
                                "${category.ageGroups} faixas etárias",
                                style = MaterialTheme.typography.labelSmall
                            ) 
                        }
                    )
                }
            }
            
            Text(
                text = stringResource(R.string.click_to_explore),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

data class LibraryCategory(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val articles: Int,
    val ageGroups: Int,
    val content: List<MedicalArticle> = emptyList()
)

data class MedicalArticle(
    val title: String,
    val description: String,
    val ageGroup: String,
    val content: String,
    val references: List<String> = emptyList()
)

@Composable
fun getLibraryCategories(): List<LibraryCategory> {
    return listOf(
        LibraryCategory(
            title = stringResource(R.string.library_visual_acuity),
            description = "Desenvolvimento da acuidade visual por idade, padrões normativos e métodos de avaliação",
            icon = Icons.Default.RemoveRedEye,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFE3F2FD),
            articles = 15,
            ageGroups = 6,
            content = listOf(
                MedicalArticle(
                    title = "Acuidade Visual em Recém-nascidos",
                    description = "Desenvolvimento visual nos primeiros meses de vida",
                    ageGroup = "0-6 meses",
                    content = "A acuidade visual de recém-nascidos é limitada..."
                ),
                MedicalArticle(
                    title = "Acuidade Visual em Crianças",
                    description = "Padrões normativos para idades pediátricas",
                    ageGroup = "1-12 anos",
                    content = "Durante a infância, a acuidade visual desenvolve-se gradualmente..."
                )
            )
        ),
        LibraryCategory(
            title = stringResource(R.string.library_color_vision),
            description = "Deficiências de visão de cores, genética e métodos diagnósticos",
            icon = Icons.Default.Palette,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFE8F5E8),
            articles = 12,
            ageGroups = 4,
            content = listOf(
                MedicalArticle(
                    title = "Daltonismo Congênito",
                    description = "Deficiências congênitas de visão de cores",
                    ageGroup = "Todas as idades",
                    content = "O daltonismo congênito afeta aproximadamente 8% dos homens..."
                )
            )
        ),
        LibraryCategory(
            title = stringResource(R.string.library_astigmatism),
            description = "Tipos de astigmatismo, correção e tratamento por faixa etária",
            icon = Icons.Default.RadioButtonUnchecked,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFF3E5F5),
            articles = 10,
            ageGroups = 5,
            content = listOf(
                MedicalArticle(
                    title = "Astigmatismo Infantil",
                    description = "Diagnóstico e manejo em crianças",
                    ageGroup = "0-18 anos",
                    content = "O astigmatismo em crianças pode afetar o desenvolvimento visual..."
                )
            )
        ),
        LibraryCategory(
            title = stringResource(R.string.library_glaucoma),
            description = "Tipos de glaucoma, fatores de risco e prevenção",
            icon = Icons.Default.Warning,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFFFF3E0),
            articles = 18,
            ageGroups = 4,
            content = listOf(
                MedicalArticle(
                    title = "Glaucoma de Ângulo Aberto",
                    description = "Forma mais comum de glaucoma",
                    ageGroup = "Adultos > 40 anos",
                    content = "O glaucoma de ângulo aberto é a forma mais comum..."
                )
            )
        ),
        LibraryCategory(
            title = stringResource(R.string.library_cataract),
            description = "Desenvolvimento de catarata, tipos e tratamentos cirúrgicos",
            icon = Icons.Default.Opacity,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFFFEBEE),
            articles = 14,
            ageGroups = 5,
            content = listOf(
                MedicalArticle(
                    title = "Catarata Congênita",
                    description = "Catarata presente ao nascimento",
                    ageGroup = "Neonatos e lactentes",
                    content = "A catarata congênita pode causar ambliopia..."
                )
            )
        ),
        LibraryCategory(
            title = stringResource(R.string.library_retinal_diseases),
            description = "Doenças retinianas, degeneração macular e retinopatias",
            icon = Icons.Default.Visibility,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFE0F2F1),
            articles = 20,
            ageGroups = 6,
            content = listOf(
                MedicalArticle(
                    title = "Degeneração Macular Relacionada à Idade",
                    description = "Principal causa de cegueira em idosos",
                    ageGroup = "Idosos > 65 anos",
                    content = "A DMRI é a principal causa de perda visual..."
                )
            )
        ),
        LibraryCategory(
            title = stringResource(R.string.library_pediatric_ophthalmology),
            description = "Oftalmologia pediátrica, desenvolvimento visual e ambliopia",
            icon = Icons.Default.ChildCare,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFFCE4EC),
            articles = 16,
            ageGroups = 4,
            content = listOf(
                MedicalArticle(
                    title = "Ambliopia - Olho Preguiçoso",
                    description = "Diagnóstico e tratamento precoce",
                    ageGroup = "0-8 anos",
                    content = "A ambliopia é uma condição tratável quando detectada precocemente..."
                )
            )
        ),
        LibraryCategory(
            title = stringResource(R.string.library_neuro_ophthalmology),
            description = "Patologias neuro-oftalmológicas e distúrbios da visão binocular",
            icon = Icons.Default.Psychology,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFE1F5FE),
            articles = 13,
            ageGroups = 5,
            content = listOf(
                MedicalArticle(
                    title = "Paralisia do Nervo Oculomotor",
                    description = "Causas e manifestações clínicas",
                    ageGroup = "Todas as idades",
                    content = "A paralisia do III nervo craniano pode ser causada..."
                )
            )
        )
    )
}
