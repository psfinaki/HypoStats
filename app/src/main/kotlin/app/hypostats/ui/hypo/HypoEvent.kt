package app.hypostats.ui.hypo

sealed interface HypoEvent {
    data object AddGrams : HypoEvent

    data object AddOffset : HypoEvent

    data object Reset : HypoEvent

    data object Submit : HypoEvent
}
