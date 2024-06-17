package com.example.jaamebaade_client.datamanager


import android.util.Log
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import com.example.jaamebaade_client.utility.DownloadStatus
import com.example.jaamebaade_client.utility.DownloadStatusManager
import java.util.zip.ZipInputStream
import javax.inject.Inject

class PoetDataManager @Inject constructor(
    private val downloadStatusManager: DownloadStatusManager
) {


    fun saveFile(body: ResponseBody, file: File) {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        try {
            inputStream = body.byteStream()
            outputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.flush()
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    fun extractZipFile(zipFile: File, extractDir: File) {
        if (!extractDir.exists()) {
            extractDir.mkdirs()
        }
        ZipInputStream(zipFile.inputStream()).use { zipInputStream ->
            var entry = zipInputStream.nextEntry
            while (entry != null) {
                Log.d("File", entry.name)
                val file = File(extractDir, entry.name)
                if (entry.isDirectory) {
                    file.mkdirs()
                } else {
                    file.outputStream().use { output ->
                        zipInputStream.copyTo(output)
                    }
                }
                zipInputStream.closeEntry()
                entry = zipInputStream.nextEntry
            }
        }
    }

    fun saveDownloadStatus(poetId: String, status: DownloadStatus) {
        downloadStatusManager.saveDownloadStatus(poetId, status)
    }

    fun getDownloadStatus(poetId: String): DownloadStatus {
        return downloadStatusManager.getDownloadStatus(poetId)
    }

    fun getAllDownloadStatuses(): Map<String, DownloadStatus> {
        return downloadStatusManager.getAllDownloadStatuses()
    }
}