package app.hypostats.domain

interface FileSystem {
    suspend fun readText(path: String): String

    suspend fun writeText(
        path: String,
        content: String,
    )
}
