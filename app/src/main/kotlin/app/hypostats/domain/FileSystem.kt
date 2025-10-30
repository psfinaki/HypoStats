package app.hypostats.domain

interface FileSystem {
    fun readText(path: String): String

    fun writeText(
        path: String,
        content: String,
    )
}
