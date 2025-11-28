package app.hypostats.domain

import android.net.Uri

interface BackupService {
    suspend fun exportToFile(uri: Uri): Result<Unit>

    suspend fun importFromFile(uri: Uri): Result<Unit>
}
