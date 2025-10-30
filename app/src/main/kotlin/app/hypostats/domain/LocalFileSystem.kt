package app.hypostats.domain

import java.io.File
import javax.inject.Inject

class LocalFileSystem
    @Inject
    constructor() : FileSystem {
        override fun readText(path: String): String = File(path).readText()

        override fun writeText(
            path: String,
            content: String,
        ) {
            File(path).writeText(content)
        }
    }
