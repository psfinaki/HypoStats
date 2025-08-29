package app.hypostats.ui.model

enum class AppTab {
    HYPO,
    STATS
}

enum class DrawerDestination {
    HOME,
    LOG,
    SETTINGS
}

data class MainUiState(
    val selectedTab: AppTab = AppTab.HYPO,
    val selectedDrawerDestination: DrawerDestination = DrawerDestination.HOME
)
