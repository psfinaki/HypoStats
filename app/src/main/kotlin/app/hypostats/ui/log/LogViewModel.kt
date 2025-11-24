package app.hypostats.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.domain.Repository
import app.hypostats.domain.model.Treatment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LogViewModel
    @Inject
    constructor(
        repository: Repository,
    ) : ViewModel() {
        val treatments: StateFlow<List<Treatment>> =
            repository
                .getAllTreatments()
                .map { treatments -> treatments.sortedByDescending { it.timestamp } }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Lazily,
                    initialValue = emptyList(),
                )
    }
