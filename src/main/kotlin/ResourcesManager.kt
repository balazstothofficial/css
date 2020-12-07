import java.io.File
import java.io.InputStream

object ResourcesManager {

    private val classLoader by lazy {
        this.javaClass.classLoader
    }

    fun list(path: String): List<File> {
        val url = requireNotNull(classLoader.getResource(path)) {
            "Resource folder $path not found"
        }

        val folder = File(url.path)
        require(folder.isDirectory) { "Not a folder: ${url.path}" }

        return folder.listFilePaths().map { File(it) }
    }

    fun open(path: String): InputStream {
        return requireNotNull(classLoader.getResourceAsStream(path)) {
            "Resource $path not found"
        }
    }

    private fun File.listFilePaths() = listFiles()?.map { it.path } ?: emptyList()
}


