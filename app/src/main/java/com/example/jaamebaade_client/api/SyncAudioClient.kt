package com.example.jaamebaade_client.api

import DesktopGanjoorPoemAudioList
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
        successCallBack: () -> Unit,
        failCallBack: () -> Unit
    ): DesktopGanjoorPoemAudioList? {
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
                        DesktopGanjoorPoemAudioList::class.java,
                        StringReader(xmlResponse)
                    )
                    successCallBack()
                    result
                } else {
                    failCallBack()
                    null
                }
            }
        } catch (e: Exception) {
            failCallBack()
            e.printStackTrace()
            return null
        }
    }
}