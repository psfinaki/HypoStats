package app.hypostats.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.hypostats.R
import app.hypostats.ui.hypo.HypoScreen
import app.hypostats.ui.log.LogScreen
import app.hypostats.ui.model.MainUiState
import app.hypostats.ui.model.AppTab
import app.hypostats.ui.model.DrawerDestination
import app.hypostats.ui.settings.SettingsScreen
import app.hypostats.ui.stats.StatsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                selectedDestination = uiState.selectedDrawerDestination,
                onDestinationSelected = viewModel::selectDrawerDestination,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AppTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            bottomBar = {
                if (uiState.selectedDrawerDestination == DrawerDestination.HOME) {
                    BottomNavigationBar(
                        selectedTab = uiState.selectedTab,
                        onTabSelected = viewModel::selectTab
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MainContent(
                    uiState = uiState,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}

@Composable
private fun AppTopBar(
    onMenuClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.open_menu)
                )
            }
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun DrawerContent(
    selectedDestination: DrawerDestination,
    onDestinationSelected: (DrawerDestination) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Column {
            DrawerNavigationItem(
                icon = Icons.Default.Home,
                labelRes = R.string.nav_home,
                destination = DrawerDestination.HOME,
                selectedDestination = selectedDestination,
                onDestinationSelected = onDestinationSelected,
                onCloseDrawer = onCloseDrawer
            )
            DrawerNavigationItem(
                icon = Icons.AutoMirrored.Filled.List,
                labelRes = R.string.nav_log,
                destination = DrawerDestination.LOG,
                selectedDestination = selectedDestination,
                onDestinationSelected = onDestinationSelected,
                onCloseDrawer = onCloseDrawer
            )
            DrawerNavigationItem(
                icon = Icons.Default.Settings,
                labelRes = R.string.nav_settings,
                destination = DrawerDestination.SETTINGS,
                selectedDestination = selectedDestination,
                onDestinationSelected = onDestinationSelected,
                onCloseDrawer = onCloseDrawer
            )
        }
    }
}

@Composable
private fun DrawerNavigationItem(
    icon: ImageVector,
    labelRes: Int,
    destination: DrawerDestination,
    selectedDestination: DrawerDestination,
    onDestinationSelected: (DrawerDestination) -> Unit,
    onCloseDrawer: () -> Unit
) {
    NavigationDrawerItem(
        icon = { 
            Icon(
                imageVector = icon,
                contentDescription = stringResource(labelRes)
            ) 
        },
        label = { Text(stringResource(labelRes)) },
        selected = selectedDestination == destination,
        onClick = {
            onDestinationSelected(destination)
            onCloseDrawer()
        }
    )
}

@Composable
private fun BottomNavigationBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.submit_hypo)
            ) },
            label = { Text(stringResource(R.string.nav_hypo)) },
            selected = selectedTab == AppTab.HYPO,
            onClick = { onTabSelected(AppTab.HYPO) }
        )

        NavigationBarItem(
            icon = { Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = stringResource(R.string.see_stats)
            ) },
            label = { Text(stringResource(R.string.nav_stats)) },
            selected = selectedTab == AppTab.STATS,
            onClick = { onTabSelected(AppTab.STATS) }
        )
    }
}

@Composable
private fun MainContent(
    uiState: MainUiState,
    snackbarHostState: SnackbarHostState
) {
    when (uiState.selectedDrawerDestination) {
        DrawerDestination.LOG -> {
            LogScreen()
        }
        DrawerDestination.SETTINGS -> {
            SettingsScreen()
        }
        DrawerDestination.HOME -> {
            when (uiState.selectedTab) {
                AppTab.HYPO -> HypoScreen(snackbarHostState = snackbarHostState)
                AppTab.STATS -> StatsScreen()
            }
        }
    }
}
