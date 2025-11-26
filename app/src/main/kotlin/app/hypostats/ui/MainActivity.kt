package app.hypostats.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.hypostats.R
import app.hypostats.ui.about.AboutScreen
import app.hypostats.ui.hypo.HypoScreen
import app.hypostats.ui.log.LogScreen
import app.hypostats.ui.model.AppTab
import app.hypostats.ui.model.AppTheme
import app.hypostats.ui.model.DrawerDestination
import app.hypostats.ui.model.MainUiState
import app.hypostats.ui.settings.SettingsScreen
import app.hypostats.ui.stats.StatsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.state.collectAsStateWithLifecycle()
            val isDarkTheme =
                when (uiState.appTheme) {
                    AppTheme.LIGHT -> false
                    AppTheme.DARK -> true
                    AppTheme.SYSTEM -> isSystemInDarkTheme()
                }
            val colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()

            MaterialTheme(colorScheme = colorScheme) {
                MainApp(viewModel = viewModel, uiState = uiState)
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: MainViewModel,
    uiState: MainUiState,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                selectedDestination = uiState.selectedDrawerDestination,
                onDestinationSelected = viewModel::selectDrawerDestination,
                onCloseDrawer = { scope.launch { drawerState.close() } },
            )
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AppTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                )
            },
            bottomBar = {
                if (uiState.selectedDrawerDestination == DrawerDestination.HOME) {
                    BottomNavigationBar(
                        selectedTab = uiState.selectedTab,
                        onTabSelected = viewModel::selectTab,
                    )
                }
            },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MainContent(
                    uiState = uiState,
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }
}

@Composable
private fun AppTopBar(onMenuClick: () -> Unit) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(statusBarPadding)
                    .height(64.dp)
                    .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(R.drawable.menu_24),
                    contentDescription = stringResource(R.string.open_menu),
                )
            }
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun DrawerContent(
    selectedDestination: DrawerDestination,
    onDestinationSelected: (DrawerDestination) -> Unit,
    onCloseDrawer: () -> Unit,
) {
    ModalDrawerSheet {
        Column {
            NavigationDrawerItemWrapper(
                iconRes = R.drawable.home_24,
                labelRes = R.string.nav_home,
                isSelected = selectedDestination == DrawerDestination.HOME,
                onClick = {
                    onDestinationSelected(DrawerDestination.HOME)
                    onCloseDrawer()
                },
            )
            NavigationDrawerItemWrapper(
                iconRes = R.drawable.list_24,
                labelRes = R.string.nav_log,
                isSelected = selectedDestination == DrawerDestination.LOG,
                onClick = {
                    onDestinationSelected(DrawerDestination.LOG)
                    onCloseDrawer()
                },
            )
            NavigationDrawerItemWrapper(
                iconRes = R.drawable.settings_24,
                labelRes = R.string.nav_settings,
                isSelected = selectedDestination == DrawerDestination.SETTINGS,
                onClick = {
                    onDestinationSelected(DrawerDestination.SETTINGS)
                    onCloseDrawer()
                },
            )
            NavigationDrawerItemWrapper(
                iconRes = R.drawable.info_24,
                labelRes = R.string.nav_about,
                isSelected = selectedDestination == DrawerDestination.ABOUT,
                onClick = {
                    onDestinationSelected(DrawerDestination.ABOUT)
                    onCloseDrawer()
                },
            )
        }
    }
}

@Composable
private fun NavigationDrawerItemWrapper(
    @DrawableRes iconRes: Int,
    labelRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = stringResource(labelRes),
            )
        },
        label = { Text(stringResource(labelRes)) },
        selected = isSelected,
        onClick = onClick,
    )
}

@Composable
private fun BottomNavigationBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.cookie_24),
                    contentDescription = stringResource(R.string.submit_hypo),
                )
            },
            label = { Text(stringResource(R.string.nav_hypo)) },
            selected = selectedTab == AppTab.HYPO,
            onClick = { onTabSelected(AppTab.HYPO) },
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.analytics_24),
                    contentDescription = stringResource(R.string.see_stats),
                )
            },
            label = { Text(stringResource(R.string.nav_stats)) },
            selected = selectedTab == AppTab.STATS,
            onClick = { onTabSelected(AppTab.STATS) },
        )
    }
}

@Composable
private fun MainContent(
    uiState: MainUiState,
    snackbarHostState: SnackbarHostState,
) {
    when (uiState.selectedDrawerDestination) {
        DrawerDestination.HOME -> {
            when (uiState.selectedTab) {
                AppTab.HYPO -> HypoScreen(snackbarHostState = snackbarHostState)
                AppTab.STATS -> StatsScreen()
            }
        }
        DrawerDestination.LOG -> {
            LogScreen()
        }
        DrawerDestination.SETTINGS -> {
            SettingsScreen(snackbarHostState = snackbarHostState)
        }
        DrawerDestination.ABOUT -> {
            AboutScreen()
        }
    }
}
