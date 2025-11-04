package app.hypostats.ui.model

data class MainUiState(
    val selectedTab: AppTab = AppTab.HYPO,
    val selectedDrawerDestination: DrawerDestination = DrawerDestination.HOME,
    val appTheme: AppTheme = AppTheme.SYSTEM,
)
