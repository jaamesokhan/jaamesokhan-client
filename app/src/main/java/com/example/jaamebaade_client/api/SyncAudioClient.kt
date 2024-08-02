package com.example.jaamebaade_client.api

import AudioSyncResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.simpleframework.xml.core.Persister
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL

class SyncAudioClient {
    suspend fun getAudioSyncInfo(
        urlPath: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ): AudioSyncResponse? {
        try {
            val url = URL(urlPath)
            return withContext(Dispatchers.IO) {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val xmlResponse = reader.use { it.readText() }
                        .removeSurrounding("\"", "\"")
                        .replace("\\n", "")
                        .replace("\\r", "")

                    val serializer = Persister()
                    val result = serializer.read(
                        AudioSyncResponse::class.java,
                        StringReader(xmlResponse)
                    )
                    onSuccess()
                    result
                } else {
                    onFailure()
                    null
                }
            }
        } catch (e: Exception) {
            onFailure()
            e.printStackTrace()
            return null
        }
    }
}