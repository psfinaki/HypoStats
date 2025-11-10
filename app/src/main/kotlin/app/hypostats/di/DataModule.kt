package app.hypostats.di

import android.content.Context
import androidx.room.Room
import app.hypostats.data.Repository
import app.hypostats.data.RoomRepository
import app.hypostats.data.local.AppDataStore
import app.hypostats.data.local.HypoStatsDatabase
import app.hypostats.data.local.PreferencesAppDataStore
import app.hypostats.data.local.TreatmentDao
import app.hypostats.domain.AppCompatLanguageManager
import app.hypostats.domain.BackupService
import app.hypostats.domain.FileSystem
import app.hypostats.domain.JsonBackupService
import app.hypostats.domain.LanguageManager
import app.hypostats.domain.LocalFileSystem
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindRepository(roomRepository: RoomRepository): Repository

    @Binds
    abstract fun bindBackupService(jsonBackupService: JsonBackupService): BackupService

    @Binds
    abstract fun bindFileSystem(localFileSystem: LocalFileSystem): FileSystem

    @Binds
    abstract fun bindLanguageManager(appCompatLanguageManager: AppCompatLanguageManager): LanguageManager

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context,
        ): HypoStatsDatabase =
            Room
                .databaseBuilder(
                    context,
                    HypoStatsDatabase::class.java,
                    "hypostats_database",
                ).build()

        @Provides
        fun provideTreatmentDao(database: HypoStatsDatabase): TreatmentDao = database.treatmentDao()

        @Provides
        @Singleton
        fun provideAppDataStore(
            @ApplicationContext context: Context,
        ): AppDataStore = PreferencesAppDataStore(context)

        @Provides
        @Singleton
        fun provideJson(): Json =
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
    }
}
