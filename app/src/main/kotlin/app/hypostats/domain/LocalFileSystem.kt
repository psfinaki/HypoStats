package app.hypostats.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class LocalFileSystem
    @Inject
    constructor() : FileSystem {
        override suspend fun readText(path: String): String =
            withContext(Dispatchers.IO) {
                File(path).readText()
            }

        override suspend fun writeText(
            path: String,
            content: String,
        ) {
            withContext(Dispatchers.IO) {
                File(path).writeText(content)
            }
        }
    }
