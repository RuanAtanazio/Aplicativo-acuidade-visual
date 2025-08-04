package com.ruanatanazio.optimetricschart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ruanatanazio.optimetricschart.ui.about.AboutActivity
import com.ruanatanazio.optimetricschart.ui.education.EducationActivity
import com.ruanatanazio.optimetricschart.ui.library.MedicalLibraryActivity
import com.ruanatanazio.optimetricschart.ui.settings.SettingsActivity
import com.ruanatanazio.optimetricschart.ui.tests.*
import com.ruanatanazio.optimetricschart.ui.theme.OptimetricsChartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptimetricsChartTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.app_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    Text(
                        text = stringResource(R.string.app_version),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Visibility, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_tests)) },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.School, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_education)) },
                    selected = false,
                    onClick = {
                        context.startActivity(Intent(context, EducationActivity::class.java))
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_library)) },
                    selected = false,
                    onClick = {
                        context.startActivity(Intent(context, MedicalLibraryActivity::class.java))
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_settings)) },
                    selected = false,
                    onClick = {
                        context.startActivity(Intent(context, SettingsActivity::class.java))
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_about)) },
                    selected = false,
                    onClick = {
                        context.startActivity(Intent(context, AboutActivity::class.java))
                    }
                )
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(getTestItems()) { test ->
                TestCard(
                    test = test,
                    onClick = {
                        val intent = Intent(context, test.activityClass)
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

data class TestItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val activityClass: Class<*>
)

@Composable
fun getTestItems(): List<TestItem> {
    return listOf(
        TestItem(
            title = stringResource(R.string.test_visual_acuity),
            description = stringResource(R.string.test_visual_acuity_desc),
            icon = Icons.Default.RemoveRedEye,
            backgroundColor = Color(0xFFE3F2FD),
            activityClass = VisualAcuityTestActivity::class.java
        ),
        TestItem(
            title = stringResource(R.string.test_color_vision),
            description = stringResource(R.string.test_color_vision_desc),
            icon = Icons.Default.ColorLens,
            backgroundColor = Color(0xFFE8F5E8),
            activityClass = ColorVisionTestActivity::class.java
        ),
        TestItem(
            title = stringResource(R.string.test_astigmatism),
            description = stringResource(R.string.test_astigmatism_desc),
            icon = Icons.Default.RadioButtonUnchecked,
            backgroundColor = Color(0xFFF3E5F5),
            activityClass = AstigmatismTestActivity::class.java
        ),
        TestItem(
            title = stringResource(R.string.test_contrast_sensitivity),
            description = stringResource(R.string.test_contrast_sensitivity_desc),
            icon = Icons.Default.Tune,
            backgroundColor = Color(0xFFFFF3E0),
            activityClass = ContrastSensitivityTestActivity::class.java
        ),
        TestItem(
            title = stringResource(R.string.test_depth_perception),
            description = stringResource(R.string.test_depth_perception_desc),
            icon = Icons.Default.View3D,
            backgroundColor = Color(0xFFFFEBEE),
            activityClass = DepthPerceptionTestActivity::class.java
        ),
        TestItem(
            title = stringResource(R.string.test_visual_field),
            description = stringResource(R.string.test_visual_field_desc),
            icon = Icons.Default.CenterFocusStrong,
            backgroundColor = Color(0xFFE0F2F1),
            activityClass = VisualFieldTestActivity::class.java
        ),
        TestItem(
            title = stringResource(R.string.test_binocular_vision),
            description = stringResource(R.string.test_binocular_vision_desc),
            icon = Icons.Default.Visibility,
            backgroundColor = Color(0xFFFCE4EC),
            activityClass = BinocularVisionTestActivity::class.java
        ),
        TestItem(
            title = stringResource(R.string.test_landolt_c),
            description = stringResource(R.string.test_landolt_c_desc),
            icon = Icons.Default.RadioButtonUnchecked,
            backgroundColor = Color(0xFFE1F5FE),
            activityClass = LandoltCTestActivity::class.java
        ),
        TestItem(
            title = stringResource(R.string.test_snellen),
            description = stringResource(R.string.test_snellen_desc),
            icon = Icons.Default.TextFormat,
            backgroundColor = Color(0xFFFFF8E1),
            activityClass = SnellenTestActivity::class.java
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestCard(
    test: TestItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = test.backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = test.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            
            Column {
                Text(
                    text = test.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = test.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    textAlign = TextAlign.Start
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(R.string.btn_start_test),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
