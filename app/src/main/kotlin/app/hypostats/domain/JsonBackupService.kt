package app.hypostats.domain

import app.hypostats.data.Repository
import app.hypostats.domain.model.Backup
import app.hypostats.domain.model.BackupTreatment
import app.hypostats.domain.model.Treatment
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.io.File
import java.time.Instant
import javax.inject.Inject

class JsonBackupService
    @Inject
    constructor(
        private val repository: Repository,
        private val json: Json,
        private val fileSystem: FileSystem,
    ) : BackupService {
        override suspend fun exportToFile(file: File) {
            val treatments = repository.getAllTreatments().first()
            val trackingStartDate = repository.getTrackingStartDate().first()

            val backup =
                Backup(
                    trackingStartDate = trackingStartDate.toString(),
                    treatments =
                        treatments.map { treatment ->
                            BackupTreatment(
                                timestamp = treatment.timestamp.toString(),
                                sugarAmount = treatment.sugarAmount,
                            )
                        },
                )

            val jsonString = json.encodeToString(backup)
            fileSystem.writeText(file.absolutePath, jsonString)
        }

        override suspend fun importFromFile(file: File) {
            if (!file.exists()) {
                throw IllegalStateException("No backup file found")
            }

            val jsonString = fileSystem.readText(file.absolutePath)
            val backup = json.decodeFromString<Backup>(jsonString)

            val treatments =
                backup.treatments.map { backupTreatment ->
                    Treatment(
                        timestamp = Instant.parse(backupTreatment.timestamp),
                        sugarAmount = backupTreatment.sugarAmount,
                    )
                }

            val trackingStartDate = Instant.parse(backup.trackingStartDate)

            repository.deleteAllTreatments()
            repository.addTreatments(treatments)
            repository.setTrackingStartDate(trackingStartDate)
        }
    }
