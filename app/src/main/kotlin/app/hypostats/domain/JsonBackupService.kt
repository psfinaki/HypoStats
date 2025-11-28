package app.hypostats.domain

import android.net.Uri
import app.hypostats.domain.model.Backup
import app.hypostats.domain.model.BackupTreatment
import app.hypostats.domain.model.Treatment
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.io.IOException
import java.time.Instant
import javax.inject.Inject

class JsonBackupService
    @Inject
    constructor(
        private val repository: Repository,
        private val json: Json,
        private val fileSystem: FileSystem,
    ) : BackupService {
        override suspend fun exportToFile(uri: Uri): Result<Unit> {
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
            return try {
                fileSystem.writeText(uri, jsonString)
                Result.success(Unit)
            } catch (e: IOException) {
                Result.failure(e)
            }
        }

        override suspend fun importFromFile(uri: Uri): Result<Unit> {
            val jsonString =
                try {
                    fileSystem.readText(uri)
                } catch (e: IOException) {
                    return Result.failure(e)
                }
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
            return Result.success(Unit)
        }
    }
