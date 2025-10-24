package app.hypostats.domain

import java.io.File

interface BackupService {
    suspend fun exportToFile(file: File)

    suspend fun importFromFile(file: File)
}
