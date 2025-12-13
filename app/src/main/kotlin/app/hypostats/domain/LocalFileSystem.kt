package app.hypostats.domain

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class LocalFileSystem
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
    ) : FileSystem {
        override suspend fun readText(uri: Uri): String =
            withContext(Dispatchers.IO) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.bufferedReader().use { it.readText() }
                } ?: throw IOException("Could not open file for reading")
            }

        override suspend fun writeText(
            uri: Uri,
            content: String,
        ) {
            withContext(Dispatchers.IO) {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.bufferedWriter().use { it.write(content) }
                } ?: throw IOException("Could not open file for writing")
            }
        }
    }
