package com.example.jaamebaade_client.repository


import android.content.Context
import android.util.Log
import com.example.jaamebaade_client.api.PoetApiService
import com.example.jaamebaade_client.api.RetrofitInstance
import com.example.jaamebaade_client.model.Poet
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import com.example.jaamebaade_client.utility.DownloadStatus
import com.example.jaamebaade_client.utility.DownloadStatusManager
import java.util.zip.ZipInputStream
import javax.inject.Inject

class PoetRepository @Inject constructor(
    private val poetApiService: PoetApiService,
    private val context: Context,
    private val downloadStatusManager: DownloadStatusManager
) {

    suspend fun getPoets(): List<Poet> {
        val poets = poetApiService.getPoets()
        return poets.content
    }

    fun downloadPoet(id: String): Response<ResponseBody> {
        return poetApiService.downloadPoet(id).execute() // .awaitResponse() for coroutines
    }

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
                Log.e("File", entry.name)
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