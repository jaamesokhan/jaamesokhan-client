package ir.jaamebaade.jaamebaade_client.utility

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.IOException

/**
 * Stores font files imported by the user in the app's private storage.
 */
class UserFontStorage(
    private val context: Context,
) {
    companion object {
        private const val FONTS_DIR = "user_fonts"
        private const val MAX_FONT_SIZE_BYTES = 20L * 1024 * 1024
        private val SUPPORTED_EXTENSIONS = setOf("ttf", "otf")
    }

    sealed interface ImportResult {
        data class Success(val file: File) : ImportResult
        data object InvalidFile : ImportResult
        data object TooLarge : ImportResult
    }

    private val fontsDir: File
        get() = File(context.filesDir, FONTS_DIR).apply { mkdirs() }

    fun getFontFiles(): List<File> =
        fontsDir.listFiles { file -> file.extension.lowercase() in SUPPORTED_EXTENSIONS }
            ?.sortedBy { it.lastModified() }
            .orEmpty()

    fun importFont(uri: Uri): ImportResult {
        val originalName = queryDisplayName(uri) ?: "font.ttf"
        val extension = originalName.substringAfterLast('.', "").lowercase()
            .takeIf { it in SUPPORTED_EXTENSIONS } ?: "ttf"
        val baseName = originalName.substringBeforeLast('.')
            .replace(Regex("[^\\p{L}\\p{N} _-]"), "")
            .trim()
            .ifEmpty { "font" }

        val tempFile = File(fontsDir, ".import.tmp")
        try {
            val input = context.contentResolver.openInputStream(uri) ?: return ImportResult.InvalidFile
            var tooLarge = false
            input.use { stream ->
                tempFile.outputStream().use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var total = 0L
                    while (true) {
                        val read = stream.read(buffer)
                        if (read < 0) break
                        total += read
                        if (total > MAX_FONT_SIZE_BYTES) {
                            tooLarge = true
                            break
                        }
                        output.write(buffer, 0, read)
                    }
                }
            }
            if (tooLarge) return ImportResult.TooLarge
            if (!isValidFont(tempFile)) return ImportResult.InvalidFile

            val target = uniqueFile(baseName, extension)
            if (!tempFile.renameTo(target)) return ImportResult.InvalidFile
            return ImportResult.Success(target)
        } catch (e: IOException) {
            return ImportResult.InvalidFile
        } catch (e: SecurityException) {
            return ImportResult.InvalidFile
        } finally {
            tempFile.delete()
        }
    }

    fun deleteFont(fileName: String): Boolean {
        val file = File(fontsDir, fileName)
        return file.parentFile == fontsDir && file.delete()
    }

    private fun isValidFont(file: File): Boolean =
        try {
            Typeface.Builder(file).build() != null
        } catch (e: Exception) {
            false
        }

    private fun uniqueFile(baseName: String, extension: String): File {
        var candidate = File(fontsDir, "$baseName.$extension")
        var index = 2
        while (candidate.exists()) {
            candidate = File(fontsDir, "$baseName ($index).$extension")
            index++
        }
        return candidate
    }

    private fun queryDisplayName(uri: Uri): String? =
        try {
            context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor ->
                    if (cursor.moveToFirst()) cursor.getString(0) else null
                }
        } catch (e: Exception) {
            null
        }
}
