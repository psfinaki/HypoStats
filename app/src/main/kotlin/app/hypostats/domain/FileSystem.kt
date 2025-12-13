package app.hypostats.domain

import android.net.Uri

interface FileSystem {
    suspend fun readText(uri: Uri): String

    suspend fun writeText(
        uri: Uri,
        content: String,
    )
}
