package app.hypostats.ui.model

data class HypoUiState(
    val sugarAmount: Int = 0,
    val offsetMinutes: Int = 0,
) {
    companion object {
        val Empty =
            HypoUiState(
                sugarAmount = 0,
                offsetMinutes = 0,
            )
    }
}
