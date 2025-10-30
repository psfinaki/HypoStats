package app.hypostats.domain

import java.io.File

interface BackupService {
    suspend fun exportToFile(file: File): Result<File>

    suspend fun importFromFile(file: File): Result<Unit>
}
